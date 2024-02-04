package ru.shadowsith.translationserver.controller

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.shadowsith.translationserver.dto.TranslateRq
import ru.shadowsith.translationserver.dto.TranslateRs
import ru.shadowsith.translationserver.services.TranslateService

@WebMvcTest(controllers = [TranslateController::class])
@AutoConfigureMockMvc
class TranslateControllerTest {
    @Autowired
    private val webApplicationContext: WebApplicationContext? = null
    var mockMvc: MockMvc? = null

    @MockBean
    @Qualifier("translateServiceImpl")
    var service: TranslateService? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }


    @Test
    @DisplayName("Положительный запрос на перевод.")
    fun translateText() {
        val translateRs = TranslateRs("42", "Hallo!")

        Mockito.`when`(service!!.translate(ArgumentMatchers.any(TranslateRq::class.java))).thenReturn(translateRs)
        val mvcResult = mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/api/v1/translate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    """{
                      "requestId": "42",
                      "originalText": "Hello!",
                      "targetLanguage": "de",
                      "sourceLanguage": "en",
                      "api": "google"
                    }"""
                )
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.responseId", Matchers.equalTo("42")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.translateText", Matchers.equalTo("Hallo!")))
            .andReturn()
        MatcherAssert.assertThat(
            "MediaType отличается от ожидаемого!",
            mvcResult.response.contentType, Matchers.`is`(MediaType.APPLICATION_JSON_VALUE)
        )
    }

}