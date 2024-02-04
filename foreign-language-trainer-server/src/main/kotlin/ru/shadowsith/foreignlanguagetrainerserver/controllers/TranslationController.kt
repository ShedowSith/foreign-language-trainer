package ru.shadowsith.foreignlanguagetrainerserver.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerserver.model.Translation
import ru.shadowsith.foreignlanguagetrainerserver.services.TranslationService


@RestController
@RequestMapping("/api/v1/translations")
class TranslationController(
    private val translationService: TranslationService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTranslation(@RequestBody translation: Translation): Translation? {
        return translationService.addTranslation(translation)
    }

    @PutMapping("/{id}")
    fun updateTranslation(@PathVariable id: Long, @RequestBody translation: Translation): ResponseEntity<Translation> {
        return translationService.updateTranslation(id, translation).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTranslation(@PathVariable id: Long): ResponseEntity<Translation> {
        return translationService.deleteTranslation(id).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

}