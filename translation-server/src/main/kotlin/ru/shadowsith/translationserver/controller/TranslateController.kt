package ru.shadowsith.translationserver.controller

import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.shadowsith.translationserver.dto.TranslateRq
import ru.shadowsith.translationserver.dto.TranslateRs
import ru.shadowsith.translationserver.services.TranslateService

@RestController
@RequestMapping("/api/v1")
class TranslateController(
    private val translateService: TranslateService
) {

    @PostMapping(
        value = ["/translate"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun translate(
        @RequestBody
        translateRq: TranslateRq): TranslateRs? {
        return translateService.translate(translateRq)
    }

}