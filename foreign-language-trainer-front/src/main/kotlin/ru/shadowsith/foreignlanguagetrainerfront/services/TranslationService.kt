package ru.shadowsith.foreignlanguagetrainerfront.services

import ru.shadowsith.foreignlanguagetrainerfront.dto.AutoTranslate
import ru.shadowsith.foreignlanguagetrainerfront.dto.Translation

interface TranslationService {
    fun addTranslation(translation: Translation)
    fun updateTranslation(id: Long, translation: Translation)
    fun deleteTranslation(id: Long)
    fun autoTranslate(autoTranslate: AutoTranslate): String?
}