package ru.shadowsith.translationserver.dto

import org.jetbrains.annotations.NotNull

data class TranslateRq(
    @NotNull("Отсутствует обязательное поле")
    val requestId: String? = null,
    val sourceLanguage: String? = null,
    val targetLanguage: String? = null,
    val originalText: String? = null,
    val api: String = "google",
)
