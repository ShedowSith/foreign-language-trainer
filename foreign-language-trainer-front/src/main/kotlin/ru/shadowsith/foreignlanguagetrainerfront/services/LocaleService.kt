package ru.shadowsith.foreignlanguagetrainerfront.services

import ru.shadowsith.foreignlanguagetrainerfront.dto.Locale

interface LocaleService {

    fun getLocales(): List<Locale>
    fun addLocale(locale: Locale)
    fun deleteLocale(id: Long)
}