package com.hvs.ws.back.domain.entity.propaganda;

import com.hvs.ws.back.domain.validation.ValidationHandler;
import com.hvs.ws.back.domain.validation.Validator;
import com.hvs.ws.back.domain.validation.notification.Erro;

public class PropagandaValidator extends Validator {

    private final Propaganda propaganda;

    public PropagandaValidator(ValidationHandler aHandler,
                               final Propaganda propaganda) {

        super(aHandler);
        this.propaganda = propaganda;
    }

    @Override
    public void validate() {

        validateBloco();
        validatePosicao();
        validateNome();
        validateDuracao();
        validateOrdem();
    }

    private void validateBloco() {

        if (this.propaganda.getBloco() == null) {
            this.validationHandler().append(new Erro("'block' cannot be null"));
        }
    }

    private void validatePosicao() {

        if (this.propaganda.getPosicao() == null) {
            this.validationHandler().append(new Erro("'position' cannot be null"));
        }
    }

    private void validateNome() {

        final var nome = this.propaganda.getNome();

        if (nome == null || nome.isBlank()) {
            this.validationHandler().append(new Erro("'name' cannot be blank"));
        }
    }

    private void validateDuracao() {

        final var duracaoSeg = this.propaganda.getDuracaoSeg();

        if (duracaoSeg == null || duracaoSeg <= 0) {
            this.validationHandler().append(new Erro("'duration in seconds' must be greater than zero"));
        }
    }

    private void validateOrdem() {

        final var ordem = this.propaganda.getOrdem();

        if (ordem == null || ordem <= 0) {
            this.validationHandler().append(new Erro("'order' must be greater than zero"));
        }
    }

    public Propaganda getPropaganda() { return propaganda; }
}
