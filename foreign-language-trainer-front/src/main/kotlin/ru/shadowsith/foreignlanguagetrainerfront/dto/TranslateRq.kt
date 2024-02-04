package ru.shadowsith.foreignlanguagetrainerfront.dto

data class TranslateRq(
    val requestId: String? = null,
    val sourceLanguage: String? = null,
    val targetLanguage: String? = null,
    val originalText: String? = null,
    val api: String = "google",
)
