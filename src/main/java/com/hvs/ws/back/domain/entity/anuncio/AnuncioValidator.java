package com.hvs.ws.back.domain.entity.anuncio;

import com.hvs.ws.back.domain.validation.ValidationHandler;
import com.hvs.ws.back.domain.validation.Validator;
import com.hvs.ws.back.domain.validation.notification.Erro;

public class AnuncioValidator extends Validator {

    private final Anuncio anuncio;

    public AnuncioValidator(ValidationHandler aHandler,
                            final Anuncio anuncio) {

        super(aHandler);
        this.anuncio = anuncio;
    }

    @Override
    public void validate() {

        validateTitulo();
        validateLink();
        validatePosicao();
        validateDimensoes();
        validateValor();
        validateStatus();
    }

    private void validateTitulo() {

        final var titulo = this.anuncio.getTitulo();

        if (titulo == null || titulo.isBlank()) {
            this.validationHandler().append(new Erro("'titulo' cannot be blank"));
        }
    }

    private void validateLink() {

        final var link = this.anuncio.getLinkUrl();

        if (link == null || link.isBlank()) {
            this.validationHandler().append(new Erro("'linkUrl' cannot be blank"));
        }
    }

    private void validatePosicao() {

        final var posicao = this.anuncio.getPosicao();

        if (posicao == null || posicao < 0 || posicao > 2) {
            this.validationHandler().append(new Erro("'posicao' must be 0 (header), 1 (sidebar) or 2 (footer)"));
        }
    }

    private void validateDimensoes() {

        final var largura = this.anuncio.getLargura();
        final var altura = this.anuncio.getAltura();

        if (largura == null || largura <= 0) {
            this.validationHandler().append(new Erro("'largura' must be greater than zero"));
        }

        if (altura == null || altura <= 0) {
            this.validationHandler().append(new Erro("'altura' must be greater than zero"));
        }
    }

    private void validateValor() {

        final var valorPago = this.anuncio.getValorPago();

        if (valorPago == null || valorPago.signum() <= 0) {
            this.validationHandler().append(new Erro("'valorPago' must be greater than zero"));
        }
    }

    private void validateStatus() {

        if (this.anuncio.getStatus() == null) {
            this.validationHandler().append(new Erro("'status' cannot be null"));
        }
    }

    public Anuncio getAnuncio() { return anuncio; }
}
