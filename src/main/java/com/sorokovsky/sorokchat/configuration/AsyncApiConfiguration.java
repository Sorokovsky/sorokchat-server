package com.sorokovsky.sorokchat.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.springwolf.asyncapi.v3.model.AsyncAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class AsyncApiConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public AsyncAPI asyncAPI() throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        final var resource = new ClassPathResource("static/asyncapi.yml");
        if (!resource.exists()) {
            throw new RuntimeException("Файл src/main/resources/static/asyncapi.yaml не знайдено!");
        }
        return yamlMapper.readValue(resource.getInputStream(), AsyncAPI.class);
    }
}
