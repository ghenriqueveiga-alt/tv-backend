package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioPagamentoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnuncioUseCaseConfig {

    @Bean
    public CreateAnuncioUseCase createAnuncioUseCaseBean(AnuncioDomainGateway gateway) {

        return new CreateAnuncioUseCaseImpl(gateway);
    }

    @Bean
    public ReadAllAnuncioUseCase readAllAnuncioUseCaseBean(AnuncioDomainGateway gateway) {

        return new ReadAllAnuncioUseCaseImpl(gateway);
    }

    @Bean
    public PatchAnuncioUseCase patchAnuncioUseCaseBean(AnuncioDomainGateway gateway) {

        return new PatchAnuncioUseCaseImpl(gateway);
    }

    @Bean
    public DeleteAnuncioUseCase deleteAnuncioUseCaseBean(AnuncioDomainGateway gateway) {

        return new DeleteAnuncioUseCaseImpl(gateway);
    }

    @Bean
    public VerificarAnuncioUseCase verificarAnuncioUseCaseBean(AnuncioDomainGateway gateway,
                                                               AnuncioPagamentoGateway pagamento) {

        return new VerificarAnuncioUseCaseImpl(gateway, pagamento);
    }
}
