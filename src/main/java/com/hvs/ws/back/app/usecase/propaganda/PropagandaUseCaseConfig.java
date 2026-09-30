package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropagandaUseCaseConfig {

    @Bean
    public CreatePropagandaUseCase createPropagandaUseCaseBean(PropagandaDomainGateway gateway) {

        return new CreatePropagandaUseCaseImpl(gateway);
    }
    @Bean
    public ReadAllPropagandaUseCase readAllPropagandaUseCaseBean(PropagandaDomainGateway gateway) {

        return new ReadAllPropagandaUseCaseImpl(gateway);
    }
    @Bean
    public PatchPropagandaUseCase patchPropagandaUseCaseBean(PropagandaDomainGateway gateway) {

        return new PatchPropagandaUseCaseImpl(gateway);
    }
    @Bean
    public DeletePropagandaUseCase deletePropagandaUseCaseBean(PropagandaDomainGateway gateway) {

        return new DeletePropagandaUseCaseImpl(gateway);
    }
}
