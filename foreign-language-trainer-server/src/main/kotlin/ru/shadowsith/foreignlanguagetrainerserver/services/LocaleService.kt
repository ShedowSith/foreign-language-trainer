package ru.shadowsith.foreignlanguagetrainerserver.services

import ru.shadowsith.foreignlanguagetrainerserver.model.Locale

interface LocaleService {
    fun addLocale(locale: Locale): Locale

    fun getLocales(): List<Locale>

    fun getLocale(id: Long): Locale?

    fun deleteLocale(id: Long): Locale?
}