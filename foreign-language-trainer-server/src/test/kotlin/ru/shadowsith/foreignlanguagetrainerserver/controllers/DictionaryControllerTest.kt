package ru.shadowsith.foreignlanguagetrainerserver.controllers

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary
import ru.shadowsith.foreignlanguagetrainerserver.model.Locale
import ru.shadowsith.foreignlanguagetrainerserver.repositories.DictionaryRepository
import ru.shadowsith.foreignlanguagetrainerserver.repositories.LocaleRepository
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@ActiveProfiles("local-dev-h2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DictionaryControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var dictionaryRepository: DictionaryRepository
    @Autowired
    private lateinit var localeRepository: LocaleRepository

    @AfterEach
    fun after() {
        dictionaryRepository.deleteAll()
        localeRepository.deleteAll()
    }

    @Test
    @DisplayName("Добавление словаря.")
    fun addDictionary() {
        var sourceLocale = Locale(caption = "Русский", code = "ru")
        var targetLocale = Locale(caption = "Английский", code = "en")
        sourceLocale = localeRepository.save(sourceLocale)
        targetLocale = localeRepository.save(targetLocale)

        val dictionary = Dictionary(id = null, name = "Русско-Английский", sourceLocale = sourceLocale, targetLocale = targetLocale)

        val path = "/api/v1/dictionaries"

        val actualResponse = restTemplate.postForEntity(
            URI("http://localhost:${port}$path"),
            dictionary,
            Dictionary::class.java)

        val actualDictionary = actualResponse.body
        var expected = dictionaryRepository.findById(actualDictionary?.id!!).get()
        expected = expected.copy(translations = null)
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.CREATED, actualResponse.statusCode)
        assertEquals(expected, actualDictionary)
    }

    @Test
    @DisplayName("Удаление словаря.")
    fun deleteDictionary() {
        var sourceLocale = Locale(caption = "Русский", code = "ru")
        var targetLocale = Locale(caption = "Английский", code = "en")
        sourceLocale = localeRepository.save(sourceLocale)
        targetLocale = localeRepository.save(targetLocale)
        var dictionary = Dictionary(id = null, name = "Русско-Английский", sourceLocale = sourceLocale, targetLocale = targetLocale)
        dictionary =  dictionaryRepository.save(dictionary)

        val path = "/api/v1/dictionaries/${dictionary.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.DELETE,
            createHttpEntity(null),
            Dictionary::class.java)
        val actualDictionary = actualResponse.body!!


        val expectedResult = dictionaryRepository.findById(dictionary.id!!).getOrNull()
        assertNull(expectedResult)
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(dictionary, actualDictionary.copy(translations = null))
    }

    @Test
    @DisplayName("Получение словарей.")
    fun getDictionaries() {
        val list = mutableListOf<Dictionary>()

        var sourceLocale = Locale(caption = "Русский", code = "ru")
        var targetLocale = Locale(caption = "Английский", code = "en")
        sourceLocale = localeRepository.save(sourceLocale)
        targetLocale = localeRepository.save(targetLocale)

        var dictionary = Dictionary(id = null, name = "qwe-qwe", sourceLocale = sourceLocale, targetLocale = targetLocale)
        list.add(dictionaryRepository.save(dictionary).copy(translations = listOf()))
        dictionary = Dictionary(id = null, name = "eqw-eqw", sourceLocale = targetLocale, targetLocale = sourceLocale)
        list.add(dictionaryRepository.save(dictionary).copy(translations = listOf()))

        val path = "/api/v1/dictionaries"

        val actualResponse = restTemplate.getForEntity(
            "http://localhost:${port}$path",
            Array<Dictionary>::class.java
        )
        val actualDictionaries = actualResponse.body!!

        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(list, actualDictionaries.toList())
    }

    @Test
    @DisplayName("Получение словаря.")
    fun getDictionary() {
        var sourceLocale = Locale(caption = "sadf", code = "ru")
        var targetLocale = Locale(caption = "fsda", code = "en")
        sourceLocale = localeRepository.save(sourceLocale)
        targetLocale = localeRepository.save(targetLocale)

        var dictionary = Dictionary(id = null, name = "adsf-asdf", sourceLocale = sourceLocale, targetLocale = targetLocale)
        dictionary = dictionaryRepository.save(dictionary).copy(translations = listOf())


        val path = "/api/v1/dictionaries/${dictionary.id}"

        val actualResponse = restTemplate.getForEntity(
            "http://localhost:${port}$path",
            Dictionary::class.java
        )
        val actualDictionary = actualResponse.body!!

        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(dictionary, actualDictionary)
    }

    fun <T>createHttpEntity(body: T?) : HttpEntity<T> {
        val headers = HttpHeaders()
        return HttpEntity<T>(body, headers)
    }
}