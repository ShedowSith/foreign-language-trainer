package ru.shadowsith.foreignlanguagetrainerserver.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary
import ru.shadowsith.foreignlanguagetrainerserver.services.DictionaryService

@RestController
@RequestMapping("/api/v1/dictionaries")
class DictionaryController(
    private val dictionaryService: DictionaryService
) {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addDictionary(@RequestBody dictionary: Dictionary): ResponseEntity<Dictionary?> {
        return dictionaryService.addDictionary(dictionary)?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDictionaries(): List<Dictionary> {
        return dictionaryService.getDictionaries()
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getDictionary(
        @PathVariable id: Long
    ): ResponseEntity<Dictionary?> {
        return dictionaryService.getDictionary(id)?.let { ResponseEntity.status(HttpStatus.OK).body(it) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

    @DeleteMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteDictionary(
        @PathVariable id: Long
    ): ResponseEntity<Dictionary?> {
        return dictionaryService.deleteDictionary(id)?.let { ResponseEntity.status(HttpStatus.OK).body(it) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

}