package ru.shadowsith.foreignlanguagetrainerfront.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerfront.dto.AutoTranslate
import ru.shadowsith.foreignlanguagetrainerfront.dto.Translation
import ru.shadowsith.foreignlanguagetrainerfront.services.TranslationService

@RestController
@RequestMapping("/translations")
class TranslationController(
    private val translationService: TranslationService
) {

    @PutMapping("/{id}")
    fun updateTranslation(
        @PathVariable id: Long,
        @RequestBody translation: Translation
    ): ResponseEntity<Unit> {
        translationService.updateTranslation(id, translation)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteTranslation(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        translationService.deleteTranslation(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/auto")
    fun autoTranslation(
        @RequestBody autoTranslate: AutoTranslate
    ): ResponseEntity<String> {
        val translate = translationService.autoTranslate(autoTranslate)

        return translate?.let { ResponseEntity.status(HttpStatus.OK).body(it) }
            ?: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
    }
}