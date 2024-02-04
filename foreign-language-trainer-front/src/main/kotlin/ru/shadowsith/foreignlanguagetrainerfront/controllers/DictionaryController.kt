package ru.shadowsith.foreignlanguagetrainerfront.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerfront.dto.Dictionary
import ru.shadowsith.foreignlanguagetrainerfront.dto.FormTraining
import ru.shadowsith.foreignlanguagetrainerfront.dto.Translation
import ru.shadowsith.foreignlanguagetrainerfront.services.DictionaryService
import ru.shadowsith.foreignlanguagetrainerfront.services.LocaleService
import ru.shadowsith.foreignlanguagetrainerfront.services.TranslationService


@Controller
@RequestMapping("/dictionaries", "")
class DictionaryController(
    private val dictionaryService: DictionaryService,
    private val translationService: TranslationService,
    private val localeService: LocaleService
) {

    @GetMapping("/", "")
    fun dictionary(model: Model): String {
        val dictionaries = dictionaryService.getDictionaries()
        val locales = localeService.getLocales()
        model["dictionaries"] = dictionaries
        model["dictionary"] = Dictionary()
        model["locales"] = locales
        return "dictionaries"
    }

    @PostMapping("/", "")
    fun addDictionary(
        @ModelAttribute("dictionary") dictionary: Dictionary,
        model: Model
    ): String? {
        dictionaryService.addDictionary(dictionary)
        return "redirect:/dictionaries"
    }

    @DeleteMapping("/{id}")
    fun deleteDictionary(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        dictionaryService.deleteDictionary(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{id}")
    fun dictionary(model: Model, @PathVariable id: Long): String {
        val dictionary = dictionaryService.getDictionary(id)
        model["dictionary"] = dictionary
        model["translation"] = Translation()
        return "dictionary"
    }

    @PostMapping("/{dictionaryId}/translations")
    fun addTranslation(
        model: Model,
        @PathVariable dictionaryId: Long,
        @ModelAttribute("translation") translation: Translation
    ): String {
        translationService.addTranslation(translation.copy(dictionary = Dictionary(id = dictionaryId)))
        val dictionary = dictionaryService.getDictionary(dictionaryId)
        model["dictionary"] = dictionary
        model["translation"] = Translation()
        return "redirect:/dictionaries/$dictionaryId"
    }

    @GetMapping("/{id}/training")
    fun training(model: Model, @PathVariable id: Long, @RequestParam(defaultValue = "1") testId: Int):String {
        val dictionary = dictionaryService.getDictionary(id)

        val formTraining = FormTraining()

        val translations = dictionary.translations.filter { !it.isStudied!! }.map {
            when (testId) {
                2 -> it.copy(word = null)
                else -> it.copy(translation = null)
            }
        }.toMutableList()
        model["dictionary"] = dictionary.copy(translations = translations)
        model["form"] = formTraining

        when (testId) {
            2 -> return "training-two"
            else -> return "training"
        }
    }

    @PostMapping("/{id}/training")
    fun checkTraining(
        model: Model,
        @PathVariable id: Long,
        @ModelAttribute("form") formTraining: FormTraining,
        @RequestParam(defaultValue = "1") testId: Int
    ): String {
        val resultChecking = dictionaryService.checkTraining(id, formTraining, testId)
        model["checking"] = resultChecking
        model["id"] = id
        return "checking"
    }
}