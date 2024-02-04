package ru.shadowsith.foreignlanguagetrainerfront.controllers

import org.hamcrest.core.StringContains
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.shadowsith.foreignlanguagetrainerfront.dto.Locale
import ru.shadowsith.foreignlanguagetrainerfront.services.LocaleService

@SpringBootTest
@AutoConfigureMockMvc
class LocaleControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var localeService: LocaleService

    @Test
    @DisplayName("Тест страницы локализаций")
    fun getLocales() {
        val localeRu = Locale(id = 1, caption = "Русский", code = "ru")
        val localeEn = Locale(id = 2, caption = "Английский", code = "en")
        val list = listOf(localeRu, localeEn)
        Mockito.`when`(localeService.getLocales()).thenReturn(list)

        mockMvc.perform(MockMvcRequestBuilders.get("/locales"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("locales"))
            .andExpect(MockMvcResultMatchers.model().attribute("locales", list))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Языки")))
    }

    @Test
    @DisplayName("Тест добавления локализации")
    fun addLocale() {
        val locale = Locale(caption = "Русский", code = "ru")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/locales")
                .flashAttr("locale", locale)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/locales"))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/locales"))
        Mockito.verify(localeService, Mockito.times(1))
            .addLocale(locale)
    }

    @Test
    @DisplayName("Тест удаления локализации")
    fun deleteLocale() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/locales/1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)

        Mockito.verify(localeService, Mockito.times(1))
            .deleteLocale(1)
    }


}