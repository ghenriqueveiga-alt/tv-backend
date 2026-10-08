package com.hvs.ws.back.infra.persistence.grade;

import com.hvs.ws.back.domain.entity.grade.Grade;
import com.hvs.ws.back.infra.persistence.BasicEntity;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import com.hvs.ws.back.infra.persistence.bloco.BlocoEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Table(name = "grade")
@BatchSize(size = 50)
public class GradeEntity extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String statusDesc;
    private String nome;
    private String descricao;

    @OneToMany(mappedBy = "grade")
    private List<BlocoEntity> blocos;
    private String periodoInicio;
    private String periodoFim;
    private Boolean gradeAtiva;

    public GradeEntity() {

    }

    public GradeEntity(final Long id,
                       final String uuid,
                       final String statusDesc,
                       final String nome,
                       final String descricao,
                       final List<BlocoEntity> blocos,
                       final String periodoInicio,
                       final String periodoFim,
                       final Boolean gradeAtiva) {

        this.id = id;
        this.uuid = uuid;
        this.statusDesc = statusDesc;
        this.nome = nome;
        this.descricao = descricao;
        this.blocos = blocos;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.gradeAtiva = gradeAtiva;
    }

    public static GradeEntity from(final Grade aGrade) {

        return new GradeEntity(
                aGrade.getId().getValue() < 0 ? null : aGrade.getId().getValue(),
                aGrade.getUuid().getValue(),
                aGrade.getStatus().getDesc(),
                aGrade.getNome(),
                aGrade.getDescricao(),
                aGrade.getBlocos() != null && !aGrade.getBlocos().isEmpty() ?
                        aGrade.getBlocos().stream().map(bloco ->
                                BlocoEntity.from(bloco.getId().getValue())).toList() : null,
                aGrade.getPeriodoInicio() != null ? aGrade.getPeriodoInicio().toString() : null,
                aGrade.getPeriodoFim() != null ? aGrade.getPeriodoFim().toString() : null,
                aGrade.getGradeAtiva());

    }

    public static GradeEntity from(final Long aGradeId) {

        final var grade = new GradeEntity();
        grade.setId(aGradeId);

        return grade;
    }

    private LocalDateTime parseDate(String aDate) {
        if (aDate == null) return null;
        try {
            return LocalDateTime.parse(aDate);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(aDate).atStartOfDay();
        }
    }

    public Grade toDomain() {

        return Grade.from(
                getId(),
                uuid,
                statusDesc,
                nome,
                descricao,
                blocos != null && !blocos.isEmpty() ?
                        blocos.stream().map(BlocoEntity::toDomainChildren).toList() : null,
                parseDate(periodoInicio),
                parseDate(periodoFim),
                gradeAtiva);
    }

    public Grade toDomainChildren() {

        return Grade.from(
                getId(),
                uuid,
                statusDesc,
                nome,
                descricao,
                blocos != null && !blocos.isEmpty() ?
                        blocos.stream().map(BlocoEntity::toDomainSimple).toList() : null,
                parseDate(periodoInicio),
                parseDate(periodoFim),
                gradeAtiva);
    }

    public Grade toDomainSimple() {

        return Grade.from(
                getId(),
                uuid,
                null,
                nome,
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
    public void setId(final Long aId) {
        this.id = aId;
    }
    public void setStatusDesc(final String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
