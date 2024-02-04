package ru.shadowsith.foreignlanguagetrainerserver.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerserver.model.Locale
import ru.shadowsith.foreignlanguagetrainerserver.services.LocaleService

@RestController
@RequestMapping("/api/v1/locales")
class LocaleController(
    private val localeService: LocaleService
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addLocale(@RequestBody locale: Locale): ResponseEntity<Locale?> {
        return ResponseEntity.status(HttpStatus.CREATED).body(localeService.addLocale(locale))
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLocales(): List<Locale> {
        return localeService.getLocales()
    }

    @DeleteMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteLocale(
        @PathVariable id: Long
    ): ResponseEntity<Locale?> {
        return localeService.deleteLocale(id)?.let { ResponseEntity.status(HttpStatus.OK).body(it) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }
}