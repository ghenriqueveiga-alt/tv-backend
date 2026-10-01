package com.hvs.ws.back.infra.persistence.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.infra.persistence.BasicEntity;
import com.hvs.ws.back.infra.persistence.arquivo.ArquivoEntity;
import com.hvs.ws.back.infra.persistence.bloco.BlocoEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "propaganda")
public class PropagandaEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String statusDesc;

    @ManyToOne
    @JoinColumn(name = "bloco_id", referencedColumnName = "id")
    private BlocoEntity bloco;
    private String posicaoDesc;
    private String nome;
    private Integer duracaoSeg;

    @ManyToOne
    @JoinColumn(name = "arquivo_id", referencedColumnName = "id")
    private ArquivoEntity arquivo;
    private Integer ordem;
    private Integer pagina;

    public PropagandaEntity() {

    }

    public PropagandaEntity(final Long id,
                            final String uuid,
                            final String statusDesc,
                            final BlocoEntity bloco,
                            final String posicaoDesc,
                            final String nome,
                            final Integer duracaoSeg,
                            final ArquivoEntity arquivo,
                            final Integer ordem,
                            final Integer pagina) {

        this.id = id;
        this.uuid = uuid;
        this.statusDesc = statusDesc;
        this.bloco = bloco;
        this.posicaoDesc = posicaoDesc;
        this.nome = nome;
        this.duracaoSeg = duracaoSeg;
        this.arquivo = arquivo;
        this.ordem = ordem;
        this.pagina = pagina;
    }

    public static PropagandaEntity from(final Propaganda aPropaganda) {

        return new PropagandaEntity(
                aPropaganda.getId().getValue() < 0 ? null : aPropaganda.getId().getValue(),
                aPropaganda.getUuid().getValue(),
                aPropaganda.getStatus() != null ? aPropaganda.getStatus().getDesc() : null,
                aPropaganda.getBloco() != null ? BlocoEntity.from(aPropaganda.getBloco().getId().getValue()) : null,
                aPropaganda.getPosicao() != null ? aPropaganda.getPosicao().getDesc() : null,
                aPropaganda.getNome(),
                aPropaganda.getDuracaoSeg(),
                aPropaganda.getArquivo() != null ? ArquivoEntity.from(aPropaganda.getArquivo().getId().getValue()) : null,
                aPropaganda.getOrdem(),
                aPropaganda.getPagina());
    }

    public static PropagandaEntity from(final Long aPropagandaId) {

        final var propaganda = new PropagandaEntity();
        propaganda.setId(aPropagandaId);

        return propaganda;
    }

    public Propaganda toDomain() {

        return Propaganda.from(
                getId(),
                uuid,
                statusDesc,
                bloco != null ? bloco.toDomain() : null,
                posicaoDesc,
                nome,
                duracaoSeg,
                arquivo != null ? arquivo.toDomain() : null,
                ordem,
                pagina);
    }

    @Override
    public Long getId() {
        return this.id;
    }
    public void setId(final Long aId) {
        this.id = aId;
    }
    public void setStatusDesc(final String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
