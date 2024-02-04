package ru.shadowsith.foreignlanguagetrainerfront.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.shadowsith.foreignlanguagetrainerfront.dto.AutoTranslate
import ru.shadowsith.foreignlanguagetrainerfront.dto.TranslateRq
import ru.shadowsith.foreignlanguagetrainerfront.dto.TranslateRs
import ru.shadowsith.foreignlanguagetrainerfront.dto.Translation
import java.util.UUID

@Service
class TranslationServiceImpl(
    private val restTemplate: RestTemplate,
    private val dictionaryService: DictionaryService
) : TranslationService {
    @Value("\${translation-server.uri}")
    private lateinit var translateServerUri: String
    override fun addTranslation(translation: Translation) {
        restTemplate.postForEntity("/api/v1/translations", translation, Translation::class.java)
    }

    override fun updateTranslation(id: Long, translation: Translation) {
        restTemplate.put("/api/v1/translations/$id", translation, Translation::class.java)
    }

    override fun deleteTranslation(id: Long) {
        restTemplate.delete("/api/v1/translations/$id")
    }

    override fun autoTranslate(autoTranslate: AutoTranslate): String? {
        val dictionary = dictionaryService.getDictionary(autoTranslate.dictionaryId!!)
        val rq = TranslateRq(UUID.randomUUID().toString(), dictionary.sourceLocale?.code, dictionary.targetLocale?.code,
            autoTranslate.word)
        val rs = restTemplate.postForObject("${translateServerUri}/api/v1/translate", rq, TranslateRs::class.java)
        return rs?.translateText
    }
}