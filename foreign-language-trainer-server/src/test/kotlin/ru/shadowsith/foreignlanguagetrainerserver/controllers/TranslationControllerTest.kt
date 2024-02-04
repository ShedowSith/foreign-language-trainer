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
import ru.shadowsith.foreignlanguagetrainerserver.model.Translation
import ru.shadowsith.foreignlanguagetrainerserver.repositories.DictionaryRepository
import ru.shadowsith.foreignlanguagetrainerserver.repositories.LocaleRepository
import ru.shadowsith.foreignlanguagetrainerserver.repositories.TranslationRepository
import java.net.URI
import kotlin.jvm.optionals.getOrNull

@ActiveProfiles("local-dev-h2")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TranslationControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var translationRepository: TranslationRepository
    @Autowired
    private lateinit var dictionaryRepository: DictionaryRepository
    @Autowired
    private lateinit var localeRepository: LocaleRepository

    private lateinit var globalDictionary: Dictionary
    @BeforeAll
    fun init() {
        var sourceLocale = Locale(caption = "Русский", code = "ru")
        var targetLocale = Locale(caption = "Английский", code = "en")
        sourceLocale = localeRepository.save(sourceLocale)
        targetLocale = localeRepository.save(targetLocale)
        val dictionary = Dictionary(id = null, name = "Русско-Английский", sourceLocale = sourceLocale, targetLocale = targetLocale)
        globalDictionary = dictionaryRepository.save(dictionary)
    }

    @AfterEach
    fun after() {
        translationRepository.deleteAll()
    }

    @Test
    @DisplayName("Добавление слова с переводом.")
    fun addTranslation() {

        val translation = Translation(id = null, word = "Привет", translation = "Hello", dictionary = globalDictionary)

        val path = "/api/v1/translations"

        val actualResponse = restTemplate.postForEntity(
            URI("http://localhost:${port}$path"),
            translation,
            Translation::class.java)

        val actualTranslation = actualResponse.body
        var expected = translationRepository.findById(actualTranslation?.id!!).get()
        expected = expected.copy(dictionary = expected.dictionary?.copy(translations = null))
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.CREATED, actualResponse.statusCode)
        assertEquals(expected, actualTranslation)
    }

    @Test
    @DisplayName("Удаление слова.")
    fun deleteTranslation() {
        var translation = Translation(id = null, word = "Привет", translation = "Hello", dictionary = globalDictionary)
        translation =  translationRepository.save(translation)

        val path = "/api/v1/translations/${translation.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.DELETE,
            createHttpEntity(null),
            Translation::class.java)
        val actualTranslation = actualResponse.body!!


        val expectedResult = translationRepository.findById(translation.id!!).getOrNull()
        assertNull(expectedResult)
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(translation, actualTranslation.copy(dictionary = actualTranslation.dictionary?.copy(translations = null)))
    }

    @Test
    @DisplayName("Изменение слова.")
    fun changeTranslation() {
        var translation = Translation(id = null, word = "Привет", translation = "Hello", dictionary = globalDictionary)
        translation =  translationRepository.save(translation)
        val translationChange = translation.copy(translation = "Hi")
        val path = "/api/v1/translations/${translation.id}"

        val actualResponse = restTemplate.exchange(
            URI("http://localhost:${port}$path"),
            HttpMethod.PUT,
            createHttpEntity(translationChange),
            Translation::class.java)

        var actualResult = translationRepository.findById(translation.id!!).get()
        actualResult = actualResult.copy(dictionary = actualResult.dictionary?.copy(translations = null))
        assertNotNull(actualResponse)
        assertEquals(HttpStatus.OK, actualResponse.statusCode)
        assertEquals(translationChange, actualResult)
    }

    fun <T>createHttpEntity(body: T?) : HttpEntity<T> {
        val headers = HttpHeaders()
        return HttpEntity<T>(body, headers)
    }

}