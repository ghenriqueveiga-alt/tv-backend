package com.hvs.ws.back.domain.entity.anuncio;

import com.hvs.ws.back.domain.entity.Entity;
import com.hvs.ws.back.domain.validation.ValidationHandler;

import java.math.BigDecimal;
import java.util.Objects;

public class Anuncio extends Entity<AnuncioId> {

    private final AnuncioUuid uuid;
    private final AnuncioStatus status;
    private final String titulo;
    private final String descricao;
    private final String imageUrl;
    private final String linkUrl;
    private final String moeda;
    private final String walletAddress;
    private final BigDecimal valorPago;
    private final String txHash;
    private final Integer posicao;
    private final Integer largura;
    private final Integer altura;

    private Anuncio(final AnuncioId id,
                    final AnuncioUuid uuid,
                    final AnuncioStatus status,
                    final String titulo,
                    final String descricao,
                    final String imageUrl,
                    final String linkUrl,
                    final String moeda,
                    final String walletAddress,
                    final BigDecimal valorPago,
                    final String txHash,
                    final Integer posicao,
                    final Integer largura,
                    final Integer altura) {

        super(id);
        this.uuid = uuid;
        this.status = status;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.moeda = moeda;
        this.walletAddress = walletAddress;
        this.valorPago = valorPago;
        this.txHash = txHash;
        this.posicao = posicao;
        this.largura = largura;
        this.altura = altura;
    }

    public static Anuncio create(final String aTitulo,
                                 final String aDescricao,
                                 final String aImageUrl,
                                 final String aLinkUrl,
                                 final String aMoeda,
                                 final String aWalletAddress,
                                 final String aTxHash,
                                 final Integer aPosicao,
                                 final Integer aLargura,
                                 final Integer aAltura,
                                 final BigDecimal aValorPago) {

        return new Anuncio(
                AnuncioId.from(-1L),
                AnuncioUuid.unique(),
                AnuncioStatus.PENDENTE,
                aTitulo,
                aDescricao,
                aImageUrl,
                aLinkUrl,
                aMoeda,
                aWalletAddress,
                aValorPago,
                aTxHash,
                aPosicao,
                aLargura,
                aAltura);
    }

    public static Anuncio patch(final Long aId,
                                final String aTitulo,
                                final String aDescricao,
                                final String aImageUrl,
                                final String aLinkUrl,
                                final String aMoeda,
                                final String aWalletAddress,
                                final String aTxHash,
                                final String aStatusCode,
                                final Integer aPosicao,
                                final Integer aLargura,
                                final Integer aAltura,
                                final BigDecimal aValorPago,
                                final Anuncio aAnuncioDB) {

        // Status: código informado troca o estado, ausência mantém o atual.
        final AnuncioStatus status = aStatusCode != null
                ? AnuncioStatus.findByCode(aStatusCode)
                : aAnuncioDB.getStatus();

        return new Anuncio(
                aId != null ? AnuncioId.from(aId) : aAnuncioDB.getId(),
                aAnuncioDB.getUuid(),
                status,
                aTitulo != null ? aTitulo : aAnuncioDB.getTitulo(),
                aDescricao != null ? aDescricao : aAnuncioDB.getDescricao(),
                aImageUrl != null ? aImageUrl : aAnuncioDB.getImageUrl(),
                aLinkUrl != null ? aLinkUrl : aAnuncioDB.getLinkUrl(),
                aMoeda != null ? aMoeda : aAnuncioDB.getMoeda(),
                aWalletAddress != null ? aWalletAddress : aAnuncioDB.getWalletAddress(),
                aValorPago != null ? aValorPago : aAnuncioDB.getValorPago(),
                aTxHash != null ? aTxHash : aAnuncioDB.getTxHash(),
                aPosicao != null ? aPosicao : aAnuncioDB.getPosicao(),
                aLargura != null ? aLargura : aAnuncioDB.getLargura(),
                aAltura != null ? aAltura : aAnuncioDB.getAltura());
    }

    public static Anuncio from(final Long aId,
                               final String aUuid,
                               final String aStatusDesc,
                               final String aTitulo,
                               final String aDescricao,
                               final String aImageUrl,
                               final String aLinkUrl,
                               final String aMoeda,
                               final String aWalletAddress,
                               final BigDecimal aValorPago,
                               final String aTxHash,
                               final Integer aPosicao,
                               final Integer aLargura,
                               final Integer aAltura) {

        return new Anuncio(
                aId != null ? AnuncioId.from(aId) : null,
                aUuid != null ? AnuncioUuid.from(aUuid) : null,
                aStatusDesc != null ? AnuncioStatus.findByDesc(aStatusDesc) : null,
                aTitulo,
                aDescricao,
                aImageUrl,
                aLinkUrl,
                aMoeda,
                aWalletAddress,
                aValorPago,
                aTxHash,
                aPosicao,
                aLargura,
                aAltura);
    }

    public static Anuncio from(final Long aId) {

        return new Anuncio(
                AnuncioId.from(aId),
                null, null, null, null, null, null, null,
                null, null, null, null, null, null);
    }

    @Override
    public void validate(ValidationHandler aHandler) {

        new AnuncioValidator(aHandler, this).validate();
    }

    public AnuncioUuid getUuid() {
        return uuid;
    }
    public AnuncioStatus getStatus() {
        return status;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public String getMoeda() {
        return moeda;
    }
    public String getWalletAddress() {
        return walletAddress;
    }
    public BigDecimal getValorPago() {
        return valorPago;
    }
    public String getTxHash() {
        return txHash;
    }
    public Integer getPosicao() {
        return posicao;
    }
    public Integer getLargura() {
        return largura;
    }
    public Integer getAltura() {
        return altura;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        if (!super.equals(o))
            return false;

        Anuncio anuncio = (Anuncio) o;

        return Objects.equals(uuid, anuncio.uuid) &&
                status == anuncio.status &&
                Objects.equals(titulo, anuncio.titulo) &&
                Objects.equals(descricao, anuncio.descricao) &&
                Objects.equals(imageUrl, anuncio.imageUrl) &&
                Objects.equals(linkUrl, anuncio.linkUrl) &&
                Objects.equals(moeda, anuncio.moeda) &&
                Objects.equals(walletAddress, anuncio.walletAddress) &&
                Objects.equals(valorPago, anuncio.valorPago) &&
                Objects.equals(txHash, anuncio.txHash) &&
                Objects.equals(posicao, anuncio.posicao) &&
                Objects.equals(largura, anuncio.largura) &&
                Objects.equals(altura, anuncio.altura);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                super.hashCode(),
                uuid,
                status,
                titulo,
                descricao,
                imageUrl,
                linkUrl,
                moeda,
                walletAddress,
                valorPago,
                txHash,
                posicao,
                largura,
                altura);
    }
}
