package com.hvs.ws.back.infra.persistence.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropagandaGatewayConfig {

    @Bean
    public PropagandaDomainGateway propagandaDomainGatewayBean(PropagandaJpaRepository repository) {

        return new PropagandaDomainGatewayImpl(repository);
    }
}
