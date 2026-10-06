package com.hvs.ws.back.infra.persistence.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnuncioGatewayConfig {

    @Bean
    public AnuncioDomainGateway anuncioDomainGatewayBean(AnuncioJpaRepository repository) {

        return new AnuncioDomainGatewayImpl(repository);
    }
}
