package ru.shadowsith.translationserver.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import ru.shadowsith.translationserver.client.GoogleApiClient
import ru.shadowsith.translationserver.dto.TranslateRq
import ru.shadowsith.translationserver.dto.TranslateRs
import java.net.URI

@SpringBootTest
class TranslateServiceImplTest {
    @Autowired
    private lateinit var googleApiClient: GoogleApiClient
    @Autowired
    private lateinit var restTemplate: RestTemplate
    @Autowired
    private lateinit var translateService: TranslateServiceImpl
    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun init() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    @DisplayName("Проверка запроса")
    fun translate() {
        val id = "someId"
        val translateResponse = "[\"Привет\"]"
        val soursLanguage = "en"
        val targetLanguage = "ru"
        val text = "Hello"
        val specification = googleApiClient.getRequestSpecification(soursLanguage, targetLanguage, text)
        val expectedRs: TranslateRs = translateService.prepareResponse(id, googleApiClient, translateResponse)
        mockServer.expect(
            ExpectedCount.once(),
            requestTo(URI(specification.url!!))
        )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(translateResponse)
            )
        val actualResult = translateService.translate(TranslateRq(id, soursLanguage, targetLanguage, text))

        mockServer.verify()
        Assertions.assertEquals(expectedRs, actualResult)
    }
}