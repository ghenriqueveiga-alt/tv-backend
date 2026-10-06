package com.hvs.ws.back.infra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioPagamentoGateway;
import com.hvs.ws.back.domain.entity.anuncio.VerificacaoPagamento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Verificação automática do pagamento em blockchain.
 *
 * Consulta pública (sem chave de API):
 *  - BTC: mempool.space  {@code GET /api/tx/{txid}}
 *  - ETH: RPC público    {@code eth_getTransactionByHash} + receipt
 * Moedas sem adaptador (LN, etc.) retornam verificação manual — aí é o
 * moderador que decide na tela de moderação.
 *
 * As carteiras de recebimento vêm da configuração ({@code anuncio.wallet.*});
 * o endereço informado pelo anunciante é só contato, não é o destino.
 */
@Component
public class AnuncioPagamentoVerifier implements AnuncioPagamentoGateway {

    private static final BigDecimal SATS = new BigDecimal("100000000");
    private static final BigDecimal WEI = new BigDecimal("1000000000000000000");
    private static final String RPC_ETH = "https://ethereum.publicnode.com";

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Value("${anuncio.wallet.btc:}")
    private String walletBtc;

    @Value("${anuncio.wallet.eth:}")
    private String walletEth;

    @Override
    public VerificacaoPagamento verificar(final Anuncio aAnuncio) {

        try {
            return tryVerificar(aAnuncio);
        } catch (Exception e) {

            throw new IllegalStateException("Falha ao consultar a blockchain: " + e.getMessage(), e);
        }
    }

    private VerificacaoPagamento tryVerificar(final Anuncio aAnuncio) throws Exception {

        final var moeda = aAnuncio.getMoeda() == null
                ? ""
                : aAnuncio.getMoeda().trim().toUpperCase();
        final var valor = aAnuncio.getValorPago();

        if (valor == null) {
            return VerificacaoPagamento.pendente("Valor do plano ausente.");
        }

        if (aAnuncio.getTxHash() == null || aAnuncio.getTxHash().isBlank()) {
            return VerificacaoPagamento.pendente("O anuncio nao tem tx hash.");
        }

        return switch (moeda) {
            case "BTC" -> verificarBitcoin(aAnuncio, valor);
            case "ETH" -> verificarEthereum(aAnuncio, valor);
            case "LN", "LIGHTNING" -> VerificacaoPagamento.pendente(
                    "Verificacao manual: Lightning Network nao tem consulta publica por tx hash.");
            default -> VerificacaoPagamento.pendente(
                    "Verificacao manual: a moeda '" + moeda + "' nao tem adaptador automatico.");
        };
    }

    private VerificacaoPagamento verificarBitcoin(final Anuncio aAnuncio,
                                                  final BigDecimal aValor) throws Exception {

        if (walletBtc == null || walletBtc.isBlank()) {
            return VerificacaoPagamento.pendente("Carteira de recebimento BTC nao configurada no servidor.");
        }

        final var txid = aAnuncio.getTxHash().trim();
        final var response = get("https://mempool.space/api/tx/" + txid);

        if (response.statusCode() == 404) {
            return VerificacaoPagamento.pendente("Transacao BTC nao encontrada (txid invalido).");
        }

        if (response.statusCode() != 200) {
            throw new IllegalStateException("mempool.space respondeu " + response.statusCode());
        }

        final JsonNode tx = mapper.readTree(response.body());

        if (!tx.path("status").path("confirmed").asBoolean(false)) {
            return VerificacaoPagamento.pendente("Aguardando confirmacao na rede Bitcoin.");
        }

        long recebido = 0;
        for (final JsonNode vout : tx.path("vout")) {

            if (walletBtc.equals(vout.path("scriptpubkey_address").asText(null))) {
                recebido += vout.path("value").asLong(0);
            }
        }

        if (recebido == 0) {
            return VerificacaoPagamento.pendente(
                    "A transacao nao envia valor para a carteira de recebimento.");
        }

        final var minimo = aValor.multiply(SATS).toBigInteger();
        if (BigInteger.valueOf(recebido).compareTo(minimo) < 0) {
            return VerificacaoPagamento.pendente(
                    "Valor recebido (" + recebido + " sats) menor que o plano.");
        }

        return VerificacaoPagamento.confirmado(
                "Pagamento Bitcoin confirmado (" + recebido + " sats).");
    }

    private VerificacaoPagamento verificarEthereum(final Anuncio aAnuncio,
                                                   final BigDecimal aValor) throws Exception {

        if (walletEth == null || walletEth.isBlank()) {
            return VerificacaoPagamento.pendente("Carteira de recebimento ETH nao configurada no servidor.");
        }

        final var hash = normalizarHash(aAnuncio.getTxHash());
        final JsonNode tx = rpc(RPC_ETH, "eth_getTransactionByHash", hash);

        if (tx == null || tx.isNull()) {
            return VerificacaoPagamento.pendente("Transacao ETH nao encontrada (hash invalido).");
        }

        if (tx.path("blockNumber").isNull() || tx.path("blockNumber").asText("").isBlank()) {
            return VerificacaoPagamento.pendente("Aguardando inclusao da transacao em um bloco.");
        }

        if (!walletEth.equalsIgnoreCase(tx.path("to").asText(""))) {
            return VerificacaoPagamento.pendente(
                    "A transacao nao envia valor para a carteira de recebimento.");
        }

        final var recebido = new BigInteger(tx.path("value").asText("0x0").replaceFirst("^0x", ""), 16);
        final var minimo = aValor.multiply(WEI).toBigInteger();

        if (recebido.compareTo(minimo) < 0) {
            return VerificacaoPagamento.pendente("Valor recebido menor que o plano.");
        }

        final JsonNode receipt = rpc(RPC_ETH, "eth_getTransactionReceipt", hash);

        if (receipt == null || receipt.isNull()) {
            return VerificacaoPagamento.pendente("Recibo da transacao indisponivel.");
        }

        if (!"0x1".equals(receipt.path("status").asText(""))) {
            return VerificacaoPagamento.pendente("Transacao ETH revertida na rede.");
        }

        return VerificacaoPagamento.confirmado("Pagamento Ethereum confirmado.");
    }

    private String normalizarHash(final String aTxHash) {

        final var hash = aTxHash.trim();
        return hash.toLowerCase().startsWith("0x") ? hash : "0x" + hash;
    }

    private HttpResponse<String> get(final String aUrl) throws Exception {

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(aUrl))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode rpc(final String aUrl, final String aMethod, final String aParam) throws Exception {

        final var payload = Map.of(
                "jsonrpc", "2.0",
                "id", 1,
                "method", aMethod,
                "params", List.of(aParam));

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(aUrl))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

        final var response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("RPC respondeu " + response.statusCode());
        }

        final JsonNode json = mapper.readTree(response.body());

        if (json.hasNonNull("error")) {
            throw new IllegalStateException(json.get("error").toString());
        }

        return json.get("result");
    }
}
