package com.example.employeesystem.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a shared {@link ModelMapper} bean for Entity &lt;-&gt; DTO conversion.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates a ModelMapper configured with a strict matching strategy
     * so field names map predictably between DTOs and entities.
     *
     * @return configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        return modelMapper;
    }
}
