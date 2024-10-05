package ar.edu.utn.frc.tup.lciii.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GeneralBeanConfig {
    private static final int TIMEOUT_MILLISECONDS = 1000000;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                // Sets the connection timeout to 1000 milliseconds (1 second)
                .setConnectTimeout(Duration.ofMillis(TIMEOUT_MILLISECONDS))
                // Sets the read timeout to 1000 milliseconds (1 second)
                .setReadTimeout(Duration.ofMillis(TIMEOUT_MILLISECONDS))
                .build();
    }
}
