package ru.shadowsith.translationserver.services

import ru.shadowsith.translationserver.dto.TranslateRq
import ru.shadowsith.translationserver.dto.TranslateRs

interface TranslateService {
    fun translate(translateRq: TranslateRq?): TranslateRs?
}