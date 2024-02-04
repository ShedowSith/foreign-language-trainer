package ru.shadowsith.translationserver.client

import ru.shadowsith.translationserver.dto.TranslateRequestSpecification

interface ApiClient {
    fun getRequestSpecification(
        sourceLanguage: String?,
        targetLanguage: String?,
        originalText: String?
    ): TranslateRequestSpecification

    fun prepareJsonByUsedApi(json: String): String
}