package ru.shadowsith.foreignlanguagetrainerfront.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfiguration(
    env: Environment,
    private val builder: RestTemplateBuilder
) {
    private val serverUri = env.getRequiredProperty("foreign-language-trainer-server.uri")
    @Bean
    fun restTemplate(): RestTemplate {
        return builder.rootUri(serverUri).build();
    }

}