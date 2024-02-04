package ru.shadowsith.translationserver.client

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.MultiValueMap
import ru.shadowsith.translationserver.dto.TranslateRequestSpecification

@SpringBootTest
class GoogleApiClientTest {
    @Autowired
    lateinit var googleApiClient: GoogleApiClient
    @Autowired
    lateinit var agents: List<String>

    private val soursLanguage = "ru"
    private val targetLanguage = "de"
    private val text = "Привет мир"

    @Test
    @DisplayName("Наличие User-Agent из списка.")
    fun getRequestSpecification() {
        val transRqSpec: TranslateRequestSpecification = googleApiClient.getRequestSpecification(
            soursLanguage,
            targetLanguage,
            text
        )
        val headers: MultiValueMap<String, String> = transRqSpec.headers!!
        val currentAgents = headers["User-Agent"]
        for (currentAgent in currentAgents!!) {
            MatcherAssert.assertThat(agents, Matchers.hasItem(currentAgent))
        }
    }

    @Test
    @DisplayName("Форматирование строки полученной от внешнего API")
    fun prepareTextByUsedApi() {
        val response = "[\"Hallo\"]"
        val preparedText: String = googleApiClient.prepareJsonByUsedApi(response)
        assertThat(preparedText).isEqualTo("Hallo")
    }

    @Test
    @DisplayName("Форматирование url template")
    fun setUrlTemplate() {
        val expectedUrlTemplate = "https://translate.google.com/translate_a/t?client=dict-chrome-ex&sl=ru&tl=de&q="
        googleApiClient.setUrlTemplate(soursLanguage, targetLanguage)
        val urlTemplate: String = googleApiClient.getUrlTemplate()!!
        assertThat(urlTemplate).isEqualTo(expectedUrlTemplate)
    }
}