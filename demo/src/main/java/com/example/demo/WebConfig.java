package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir requisições do front-end na porta 5500
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500") // Adicione a origem do seu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowedHeaders("*") // Todos os cabeçalhos
                .allowCredentials(true); // Se necessário, permite envio de cookies
    }
}
