package ru.shadowsith.foreignlanguagetrainerfront.dto

data class Dictionary(
    val id: Long? = null,
    val name: String? = null,
    var sourceLocale: Locale? = null,
    var targetLocale: Locale? = null,
    val translations: List<Translation> = listOf()
)
