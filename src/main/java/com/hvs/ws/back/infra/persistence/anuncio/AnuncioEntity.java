package com.hvs.ws.back.infra.persistence.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.infra.persistence.BasicEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "anuncio")
public class AnuncioEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String statusDesc;
    private String titulo;
    private String descricao;
    private String imageUrl;
    private String linkUrl;
    private String moeda;
    private String walletAddress;
    // 8 casas decimais: sem precisão explícita o JPA grava DECIMAL(?,2) e
    // 0.001 (BTC) viraria 0.00.
    @Column(precision = 18, scale = 8)
    private BigDecimal valorPago;
    private String txHash;
    private Integer posicao;
    private Integer largura;
    private Integer altura;

    public AnuncioEntity() {

    }

    public AnuncioEntity(final Long id,
                         final String uuid,
                         final String statusDesc,
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

        this.id = id;
        this.uuid = uuid;
        this.statusDesc = statusDesc;
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

    public static AnuncioEntity from(final Anuncio aAnuncio) {

        return new AnuncioEntity(
                aAnuncio.getId().getValue() < 0 ? null : aAnuncio.getId().getValue(),
                aAnuncio.getUuid().getValue(),
                aAnuncio.getStatus() != null ? aAnuncio.getStatus().getDesc() : null,
                aAnuncio.getTitulo(),
                aAnuncio.getDescricao(),
                aAnuncio.getImageUrl(),
                aAnuncio.getLinkUrl(),
                aAnuncio.getMoeda(),
                aAnuncio.getWalletAddress(),
                aAnuncio.getValorPago(),
                aAnuncio.getTxHash(),
                aAnuncio.getPosicao(),
                aAnuncio.getLargura(),
                aAnuncio.getAltura());
    }

    public Anuncio toDomain() {

        return Anuncio.from(
                getId(),
                uuid,
                statusDesc,
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

    @Override
    public Long getId() {
        return this.id;
    }
    public void setId(final Long aId) {
        this.id = aId;
    }
    public void setStatusDesc(final String aStatusDesc) {
        this.statusDesc = aStatusDesc;
    }
}
