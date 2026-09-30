package com.hvs.ws.back.domain.entity.propaganda;

import com.hvs.ws.back.domain.entity.Entity;
import com.hvs.ws.back.domain.entity.arquivo.Arquivo;
import com.hvs.ws.back.domain.entity.bloco.Bloco;
import com.hvs.ws.back.domain.validation.ValidationHandler;

import java.util.Objects;

public class Propaganda extends Entity<PropagandaId> {

    private final PropagandaUuid uuid;
    private final PropagandaStatus status;
    private final Bloco bloco;
    private final PropagandaPosicao posicao;
    private final String nome;
    private final Integer duracaoSeg;
    private final Arquivo arquivo;
    private final Integer ordem;

    private Propaganda(final PropagandaId id,
                       final PropagandaUuid uuid,
                       final PropagandaStatus status,
                       final Bloco bloco,
                       final PropagandaPosicao posicao,
                       final String nome,
                       final Integer duracaoSeg,
                       final Arquivo arquivo,
                       final Integer ordem) {

        super(id);
        this.uuid = uuid;
        this.status = status;
        this.bloco = bloco;
        this.posicao = posicao;
        this.nome = nome;
        this.duracaoSeg = duracaoSeg;
        this.arquivo = arquivo;
        this.ordem = ordem;
    }

    public static Propaganda create(final Long aBlocoId,
                                    final String aPosicaoCode,
                                    final String aNome,
                                    final Integer aDuracaoSeg,
                                    final Long aArquivoId,
                                    final Integer aOrdem) {

        return new Propaganda(
                PropagandaId.from(-1L),
                PropagandaUuid.unique(),
                PropagandaStatus.ACTIVE,
                aBlocoId != null ? Bloco.from(aBlocoId) : null,
                aPosicaoCode != null ? PropagandaPosicao.findByCode(aPosicaoCode) : null,
                aNome,
                aDuracaoSeg,
                aArquivoId != null ? Arquivo.from(aArquivoId) : null,
                aOrdem);
    }

    public static Propaganda patch(final Long aId,
                                   final String aNome,
                                   final Integer aDuracaoSeg,
                                   final Integer aOrdem,
                                   final Propaganda aPropagandaDB) {

        return new Propaganda(
                aId != null ? PropagandaId.from(aId) : aPropagandaDB.getId(),
                aPropagandaDB.getUuid(),
                aPropagandaDB.getStatus(),
                aPropagandaDB.getBloco(),
                aPropagandaDB.getPosicao(),
                aNome != null ? aNome : aPropagandaDB.getNome(),
                aDuracaoSeg != null ? aDuracaoSeg : aPropagandaDB.getDuracaoSeg(),
                aPropagandaDB.getArquivo(),
                aOrdem != null ? aOrdem : aPropagandaDB.getOrdem());
    }

    public static Propaganda from(final Long aId,
                                  final String aUuid,
                                  final String aStatusDesc,
                                  final Bloco aBloco,
                                  final String aPosicaoDesc,
                                  final String aNome,
                                  final Integer aDuracaoSeg,
                                  final Arquivo aArquivo,
                                  final Integer aOrdem) {

        return new Propaganda(
                aId != null ? PropagandaId.from(aId) : null,
                aUuid != null ? PropagandaUuid.from(aUuid) : null,
                aStatusDesc != null ? PropagandaStatus.findByDesc(aStatusDesc) : null,
                aBloco,
                aPosicaoDesc != null ? PropagandaPosicao.findByDesc(aPosicaoDesc) : null,
                aNome,
                aDuracaoSeg,
                aArquivo,
                aOrdem);
    }

    public static Propaganda from(final Long aId) {

        return new Propaganda(
                aId != null ? PropagandaId.from(aId) : null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    public void validate(ValidationHandler aHandler) {

        new PropagandaValidator(aHandler, this).validate();
    }

    public PropagandaUuid getUuid() {
        return uuid;
    }
    public PropagandaStatus getStatus() {
        return status;
    }
    public Bloco getBloco() {
        return bloco;
    }
    public PropagandaPosicao getPosicao() {
        return posicao;
    }
    public String getNome() {
        return nome;
    }
    public Integer getDuracaoSeg() {
        return duracaoSeg;
    }
    public Arquivo getArquivo() {
        return arquivo;
    }
    public Integer getOrdem() {
        return ordem;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        if (!super.equals(o))
            return false;

        Propaganda propaganda = (Propaganda) o;

        return Objects.equals(uuid, propaganda.uuid) &&
                status == propaganda.status &&
                Objects.equals(bloco, propaganda.bloco) &&
                posicao == propaganda.posicao &&
                Objects.equals(nome, propaganda.nome) &&
                Objects.equals(duracaoSeg, propaganda.duracaoSeg) &&
                Objects.equals(arquivo, propaganda.arquivo) &&
                Objects.equals(ordem, propaganda.ordem);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                super.hashCode(),
                uuid,
                status,
                bloco,
                posicao,
                nome,
                duracaoSeg,
                arquivo,
                ordem);
    }
}
