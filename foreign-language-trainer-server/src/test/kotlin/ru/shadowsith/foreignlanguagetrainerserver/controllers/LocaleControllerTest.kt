package ru.shadowsith.foreignlanguagetrainerserver.controllers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import ru.shadowsith.foreignlanguagetrainerserver.model.Locale
import ru.shadowsith.foreignlanguagetrainerserver.repositories.DictionaryRepository
import ru.shadowsith.foreignlanguagetrainerserver.repositories.LocaleRepository
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@ActiveProfiles("local-dev-h2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocaleControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var localeRepository: LocaleRepository

    @Autowired
    private lateinit var dictionaryRepository: DictionaryRepository

    @AfterEach
    fun after() {
        dictionaryRepository.deleteAll()
        localeRepository.deleteAll()
    }


    @Test
    @DisplayName("Добавление локализации.")
    fun addLocale() {
        val locale = Locale(caption = "Немецкий", code = "de")

        val path = "/api/v1/locales"

        val actualResponse = restTemplate.postForEntity(
            URI("http://localhost:${port}$path"),
            locale,
            Locale::class.java)

        val actualLocale = actualResponse.body
        val expected = localeRepository.findById(actualLocale?.id!!).get()
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.CREATED, actualResponse.statusCode)
        assertEquals(expected, actualLocale)
    }

    @Test
    @DisplayName("Удаление локализации.")
    fun deleteLocale() {
        var locale = Locale(caption = "Немецкий", code = "de")
        locale = localeRepository.save(locale)

        val path = "/api/v1/locales/${locale.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.DELETE,
            createHttpEntity(null),
            Locale::class.java)
        val actualLocale = actualResponse.body!!


        val expectedResult = localeRepository.findById(locale.id!!).getOrNull()
        assertNull(expectedResult)
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(locale, actualLocale)
    }

    @Test
    @DisplayName("Получение локализаций.")
    fun getLocales() {
        val list = mutableListOf<Locale>()
        var locale = Locale(caption = "Немецкий", code = "de")
        list.add(localeRepository.save(locale))
        locale = Locale(caption = "Русский", code = "ru")
        list.add(localeRepository.save(locale))

        val path = "/api/v1/locales"

        val actualResponse = restTemplate.getForEntity(
            "http://localhost:${port}$path",
            Array<Locale>::class.java
        )
        val actualLocale = actualResponse.body!!

        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(list, actualLocale.toList())
    }

    fun <T>createHttpEntity(body: T?) : HttpEntity<T> {
        val headers = HttpHeaders()
        return HttpEntity<T>(body, headers)
    }

}