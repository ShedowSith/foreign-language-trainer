package ru.shadowsith.foreignlanguagetrainerfront.controllers

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import ru.shadowsith.foreignlanguagetrainerfront.dto.AutoTranslate
import ru.shadowsith.foreignlanguagetrainerfront.dto.Translation
import ru.shadowsith.foreignlanguagetrainerfront.services.TranslationService
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TranslationControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @MockBean
    private lateinit var translationService: TranslationService

    @Test
    @DisplayName("Проверка апи обновления перевода")
    fun updateTranslation() {
        val translation = Translation(id = null, word = "привет", translation = "Hello", isStudied = false)

        val path = "/translations/1"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.PUT,
            createHttpEntity(translation),
            Unit::class.java
        )
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        Mockito.verify(translationService, Mockito.times(1))
            .updateTranslation(1, translation)
    }

    @Test
    @DisplayName("Проверка апи удаления перевода")
    fun deleteTranslation() {
        val path = "/translations/1"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.DELETE,
            createHttpEntity(null),
            Unit::class.java
        )

        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        Mockito.verify(translationService, Mockito.times(1))
            .deleteTranslation(1)
    }

    @Test
    @DisplayName("Проверка апи авто-перевода")
    fun autoTranslation() {
        val request = AutoTranslate(1, "word")
        Mockito.`when`(translationService.autoTranslate(request)).thenReturn("Мир")
        val path = "/translations/auto"
        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.POST,
            createHttpEntity(request),
            String::class.java
        )
        val actual = actualResponse.body!!
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals("Мир", actual)
    }

    fun <T>createHttpEntity(body: T?) : HttpEntity<T> {
        val headers = HttpHeaders()
        return HttpEntity<T>(body, headers)
    }
}