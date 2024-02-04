package ru.shadowsith.foreignlanguagetrainerfront.services

import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import ru.shadowsith.foreignlanguagetrainerfront.dto.Locale

@Service
class LocaleServiceImpl(
    private val restTemplate: RestTemplate
) : LocaleService {
    override fun getLocales(): List<Locale> {
        return restTemplate.getForObject("/api/v1/locales", object: ParameterizedTypeReference<List<Locale>>() {})
    }

    override fun addLocale(locale: Locale) {
        restTemplate.postForEntity("/api/v1/locales", locale, Locale::class.java)
    }

    override fun deleteLocale(id: Long) {
        restTemplate.delete("/api/v1/locales/$id")
    }
}