package com.hvs.ws.back.infra.persistence.programa;

import com.hvs.ws.back.domain.entity.programa.Programa;
import com.hvs.ws.back.infra.persistence.BasicEntity;
import com.hvs.ws.back.infra.persistence.bloco.BlocoEntity;
import com.hvs.ws.back.infra.persistence.episodio.EpisodioEntity;
import com.hvs.ws.back.infra.persistence.genero.GeneroEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import java.util.List;

@Entity
@Table(name = "programa")
@BatchSize(size = 50)
public class ProgramaEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String statusDesc;
    private String nome;
    private Boolean emProducao;
    private String tipoDesc;
    private Long temporadas;
    private Long partes;

    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EpisodioEntity> episodios;
    private String lancamento;
    private String encerramento;

    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlocoEntity> blocos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "programa_genero",
            joinColumns = @JoinColumn(name = "programa_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private List<GeneroEntity> generos;

    @Column(columnDefinition = "TEXT")
    private String sinopse;
    private String classificacaoEtariaDesc;
    private String estudio;
    private String capaUrl;
    private String redeOriginal;
    private String tipoExibicaoDesc;
    private String tituloAlternativo;
    private String audioIdiomas;
    private String legendasDisponiveis;
    private String siteOficial;

    public ProgramaEntity() {

    }

    public ProgramaEntity(final Long id,
                          final String uuid,
                          final String statusDesc,
                          final String nome,
                          final Boolean emProducao,
                          final String tipoDesc,
                          final Long temporadas,
                          final Long partes,
                          final List<EpisodioEntity> episodios,
                          final String lancamento,
                          final String encerramento,
                          final List<BlocoEntity> blocos,
                          final String sinopse,
                          final String classificacaoEtariaDesc,
                          final String estudio,
                          final String capaUrl,
                          final String redeOriginal,
                          final String tipoExibicaoDesc,
                          final String tituloAlternativo,
                          final String audioIdiomas,
                          final String legendasDisponiveis,
                          final String siteOficial,
                          final List<GeneroEntity> generos) {

        this.id = id;
        this.uuid = uuid;
        this.statusDesc = statusDesc;
        this.nome = nome;
        this.emProducao = emProducao;
        this.tipoDesc = tipoDesc;
        this.temporadas = temporadas;
        this.partes = partes;
        this.episodios = episodios;
        this.lancamento = lancamento;
        this.encerramento = encerramento;
        this.blocos = blocos;
        this.sinopse = sinopse;
        this.classificacaoEtariaDesc = classificacaoEtariaDesc;
        this.estudio = estudio;
        this.capaUrl = capaUrl;
        this.redeOriginal = redeOriginal;
        this.tipoExibicaoDesc = tipoExibicaoDesc;
        this.tituloAlternativo = tituloAlternativo;
        this.audioIdiomas = audioIdiomas;
        this.legendasDisponiveis = legendasDisponiveis;
        this.siteOficial = siteOficial;
        this.generos = generos;
    }

    public static ProgramaEntity from(final Programa aPrograma) {

        return new ProgramaEntity(
                aPrograma.getId().getValue() < 0 ? null : aPrograma.getId().getValue(),
                aPrograma.getUuid().getValue(),
                aPrograma.getStatus().getDesc(),
                aPrograma.getNome(),
                aPrograma.getEmProducao(),
                aPrograma.getTipo().getDesc(),
                aPrograma.getTemporadas(),
                aPrograma.getPartes(),
                aPrograma.getEpisodios() != null && !aPrograma.getEpisodios().isEmpty() ?
                        aPrograma.getEpisodios().stream().map(episodio ->
                                EpisodioEntity.from(episodio.getId().getValue())).toList() : null,
                aPrograma.getLancamento() != null ? aPrograma.getLancamento().toString() : null,
                aPrograma.getEncerramento() != null ? aPrograma.getEncerramento().toString() : null,
                aPrograma.getBlocos() != null && !aPrograma.getBlocos().isEmpty() ?
                        aPrograma.getBlocos().stream().map(bloco ->
                                BlocoEntity.from(bloco.getId().getValue())).toList() : null,
                aPrograma.getSinopse(),
                aPrograma.getClassificacaoEtaria() != null ? aPrograma.getClassificacaoEtaria().getDesc() : null,
                aPrograma.getEstudio(),
                aPrograma.getCapaUrl(),
                aPrograma.getRedeOriginal(),
                aPrograma.getTipoExibicao() != null ? aPrograma.getTipoExibicao().getDesc() : null,
                aPrograma.getTituloAlternativo(),
                aPrograma.getAudioIdiomas(),
                aPrograma.getLegendasDisponiveis(),
                aPrograma.getSiteOficial(),
                aPrograma.getGeneros() != null && !aPrograma.getGeneros().isEmpty() ?
                        aPrograma.getGeneros().stream().map(GeneroEntity::from).toList() : null);
    }

    public static ProgramaEntity from(final Long aProgramaId) {

        final var programa = new ProgramaEntity();
        programa.setId(aProgramaId);

        return programa;
    }

    public Programa toDomain() {

        return Programa.from(getId(),
                             uuid,
                             statusDesc,
                             nome,
                             emProducao,
                             tipoDesc,
                             temporadas,
                             partes,
                             episodios != null && !episodios.isEmpty() ?
                                     episodios.stream().map(EpisodioEntity::toDomainChildren).toList() : null,
                             lancamento,
                             encerramento,
                             blocos != null && !blocos.isEmpty() ?
                                     blocos.stream().map(BlocoEntity::toDomainChildren).toList() : null,
                             sinopse,
                             classificacaoEtariaDesc,
                             estudio,
                             capaUrl,
                             redeOriginal,
                             tipoExibicaoDesc,
                             tituloAlternativo,
                             audioIdiomas,
                             legendasDisponiveis,
                             siteOficial,
                             generos != null && !generos.isEmpty() ?
                                     generos.stream().map(GeneroEntity::toDomainChildren).toList() : null);
    }

    public Programa toDomainChildren() {

        return Programa.from(getId(),
                             uuid,
                             statusDesc,
                             nome,
                             emProducao,
                             tipoDesc,
                             temporadas,
                             partes,
                             episodios != null && !episodios.isEmpty() ?
                                     episodios.stream().map(EpisodioEntity::toDomainSimple).toList() : null,
                             lancamento,
                             encerramento,
                             blocos != null && !blocos.isEmpty() ?
                                     blocos.stream().map(BlocoEntity::toDomainSimple).toList() : null,
                             sinopse,
                             classificacaoEtariaDesc,
                             estudio,
                             capaUrl,
                             redeOriginal,
                             tipoExibicaoDesc,
                             tituloAlternativo,
                             audioIdiomas,
                             legendasDisponiveis,
                             siteOficial,
                             generos != null && !generos.isEmpty() ?
                                     generos.stream().map(GeneroEntity::toDomainSimple).toList() : null);
    }

    public Programa toDomainSimple() {

        return Programa.from(getId(),
                             uuid,
                             statusDesc,
                             nome,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             capaUrl,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             null);
    }

    @Override
    public Long getId() {
        return this.id;
    }
    public String getCapaUrl() {
        return this.capaUrl;
    }
    public void setId(final Long aId) {
        this.id = aId;
    }
    public void setStatusDesc(final String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
