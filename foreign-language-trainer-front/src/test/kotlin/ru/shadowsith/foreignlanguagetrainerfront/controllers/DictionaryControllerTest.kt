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
import ru.shadowsith.foreignlanguagetrainerfront.dto.*
import ru.shadowsith.foreignlanguagetrainerfront.services.DictionaryService
import ru.shadowsith.foreignlanguagetrainerfront.services.LocaleService
import ru.shadowsith.foreignlanguagetrainerfront.services.TranslationService


@SpringBootTest
@AutoConfigureMockMvc
class DictionaryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var dictionaryService: DictionaryService

    @MockBean
    private lateinit var translationService: TranslationService

    @MockBean
    private lateinit var localeService: LocaleService

    @Test
    @DisplayName("Тест страницы словарей")
    fun getDictionaries() {
        val localeRu = Locale(id = 1, caption = "Русский", code = "ru")
        val localeEn = Locale(id = 2, caption = "Английский", code = "en")
        val list = listOf(localeRu, localeEn)
        Mockito.`when`(localeService.getLocales()).thenReturn(list)
        val dictionary = Dictionary(id = 1, name = "Русско-Английский", sourceLocale = localeRu, targetLocale = localeEn)
        Mockito.`when`(dictionaryService.getDictionaries()).thenReturn(listOf(dictionary))

        mockMvc.perform(MockMvcRequestBuilders.get("/dictionaries"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("dictionaries"))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(dictionary.name)))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Словари")))
    }

    @Test
    @DisplayName("Тест получение страницы конкретного словаря")
    fun getDictionary() {
        val localeRu = Locale(id = 1, caption = "Русский", code = "ru")
        val localeEn = Locale(id = 2, caption = "Английский", code = "en")
        val dictionary = Dictionary(id = 1, name = "Русско-Английский", sourceLocale = localeRu, targetLocale = localeEn)
        Mockito.`when`(dictionaryService.getDictionary(1)).thenReturn(dictionary)

        mockMvc.perform(MockMvcRequestBuilders.get("/dictionaries/${dictionary.id}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("dictionary"))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(dictionary.name)))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Словарь")))
    }
    @Test
    @DisplayName("Тест добавления словаря")
    fun addDictionary() {
        val soursLocale = Locale(id = 1)
        val targetLocale = Locale(id = 2)
        val dictionary = Dictionary(id = null, name = "wqe", targetLocale = targetLocale, sourceLocale = soursLocale)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/dictionaries")
            .flashAttr("dictionary", dictionary)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/dictionaries"))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/dictionaries"))
        Mockito.verify(dictionaryService, Mockito.times(1))
            .addDictionary(dictionary)
    }

    @Test
    @DisplayName("Тест добавления перевода")
    fun addTranslation() {
        val translation = Translation(word = "привет", translation = "Hello")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/dictionaries/1/translations")
                .flashAttr("translation", translation)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/dictionaries/1"))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/dictionaries/1"))

        Mockito.verify(translationService, Mockito.times(1))
            .addTranslation(translation.copy(dictionary = Dictionary(1)))
        Mockito.verify(dictionaryService, Mockito.times(1))
            .getDictionary(1)
    }

    @Test
    @DisplayName("Тест удаления словаря")
    fun deleteDictionary() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/dictionaries/1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)

        Mockito.verify(dictionaryService, Mockito.times(1))
            .deleteDictionary(1)
    }

    @Test
    @DisplayName("Тест страницы тренировки номер 1")
    fun getTrainingOne() {
        val localeRu = Locale(id = 1, caption = "Русский", code = "ru")
        val localeEn = Locale(id = 2, caption = "Английский", code = "en")
        val translation = Translation(id = 3, word = "привет", translation = "Hello", isStudied = false)
        val dictionary = Dictionary(
            id = 1,
            name = "Русско-Английский",
            sourceLocale = localeRu,
            targetLocale = localeEn,
            translations = listOf(translation)
        )
        Mockito.`when`(dictionaryService.getDictionary(1)).thenReturn(dictionary)

        mockMvc.perform(MockMvcRequestBuilders.get("/dictionaries/1/training?testId=1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("training"))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(dictionary.name)))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Тренировка по словарю:")))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(translation.word)))
            .andExpect(MockMvcResultMatchers.model().attribute("dictionary", dictionary.copy(translations = dictionary.translations.map { it.copy(translation = null) })))
    }

    @Test
    @DisplayName("Тест страницы тренировки номер 2")
    fun getTrainingTwo() {
        val localeRu = Locale(id = 1, caption = "Русский", code = "ru")
        val localeEn = Locale(id = 2, caption = "Английский", code = "en")
        val translation = Translation(id = 3, word = "привет", translation = "Hello", isStudied = false)
        val dictionary = Dictionary(
            id = 1,
            name = "Русско-Английский",
            sourceLocale = localeRu,
            targetLocale = localeEn,
            translations = listOf(translation)
        )
        Mockito.`when`(dictionaryService.getDictionary(1)).thenReturn(dictionary)

        mockMvc.perform(MockMvcRequestBuilders.get("/dictionaries/1/training?testId=2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("training-two"))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(dictionary.name)))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString("Тренировка по словарю:")))
            .andExpect(MockMvcResultMatchers.content().string(StringContains.containsString(translation.translation)))
            .andExpect(MockMvcResultMatchers.model().attribute("dictionary", dictionary.copy(translations = dictionary.translations.map { it.copy(word = null) })))
    }
    @Test
    @DisplayName("Тест страницы результатов тренировки")
    fun checkTraining() {
        val resultChecking = ResultChecking(total = 1, correctAnswerCount = 1)
        val answer = Answer()
        answer.id = 1
        answer.word = "привет"
        answer.translation = "Hello"
        val formTraining =  FormTraining()
        formTraining.addTranslation(answer)
        Mockito.`when`(dictionaryService.checkTraining(1, formTraining, 1)).thenReturn(resultChecking)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/dictionaries/1/training")
                .flashAttr("form", formTraining)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("checking"))
            .andExpect(MockMvcResultMatchers.model().attribute("checking", resultChecking))
    }

}