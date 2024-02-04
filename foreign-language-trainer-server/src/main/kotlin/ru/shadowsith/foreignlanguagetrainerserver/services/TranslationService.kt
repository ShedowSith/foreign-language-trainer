package ru.shadowsith.foreignlanguagetrainerserver.services

import ru.shadowsith.foreignlanguagetrainerserver.model.Translation

interface TranslationService {
    fun addTranslation(transient: Translation): Translation?
    fun updateTranslation(id: Long, transient: Translation): Translation?
    fun deleteTranslation(id: Long): Translation?
}