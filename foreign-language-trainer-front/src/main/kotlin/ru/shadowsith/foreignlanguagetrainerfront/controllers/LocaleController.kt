package ru.shadowsith.foreignlanguagetrainerfront.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import ru.shadowsith.foreignlanguagetrainerfront.dto.Locale
import ru.shadowsith.foreignlanguagetrainerfront.services.LocaleService

@Controller
@RequestMapping("/locales")
class LocaleController(
    private val localeService: LocaleService
) {
    @GetMapping
    fun locales(model: Model): String {
        val locales = localeService.getLocales()
        model["locales"] = locales
        model["locale"] = Locale()
        return "locales"
    }

    @PostMapping
    fun addLocales(
        @ModelAttribute("locale") locale: Locale,
        model: Model
    ): String? {
        localeService.addLocale(locale)
        return "redirect:/locales"
    }

    @DeleteMapping("/{id}")
    fun deleteLocale(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        localeService.deleteLocale(id)
        return ResponseEntity.ok().build()
    }
}