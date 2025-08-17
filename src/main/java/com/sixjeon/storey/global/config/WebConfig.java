package com.sixjeon.storey.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://storey-6jeon.vercel.app",
                        "http://localhost:5173") // 실제 프론트 주소 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);

    }
    // 외부 API 호출을 위한 WebClient Bean
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
