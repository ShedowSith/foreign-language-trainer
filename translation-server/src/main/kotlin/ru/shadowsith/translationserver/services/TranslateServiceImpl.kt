package ru.shadowsith.translationserver.services

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.shadowsith.translationserver.client.ApiClient
import ru.shadowsith.translationserver.dto.TranslateRequestSpecification
import ru.shadowsith.translationserver.dto.TranslateRq
import ru.shadowsith.translationserver.dto.TranslateRs

@Service
class TranslateServiceImpl(
    private val apiClient: ApiClient,
    private val restTemplate: RestTemplate
) : TranslateService {
    override fun translate(translateRq: TranslateRq?): TranslateRs? {

        val requestSpecification: TranslateRequestSpecification = apiClient.getRequestSpecification(
            translateRq?.sourceLanguage,
            translateRq?.targetLanguage,
            translateRq?.originalText
        )
        val translateResponse: String? = sendRequestForTranslate(requestSpecification)
        return prepareResponse(translateRq?.requestId, apiClient, translateResponse)
    }

    protected fun sendRequestForTranslate(rqSpec: TranslateRequestSpecification): String? {
        return when (rqSpec.httpMethod) {
            HttpMethod.GET -> {
                val entity = HttpEntity<Any>(rqSpec.headers!!)
                restTemplate.getForObject(rqSpec.url!!, String::class.java, entity)
            }
            else -> throw Exception("Внутренняя ошибка сервиса перевода!")
        }
    }
    fun prepareResponse(requestId: String?, apiClient: ApiClient, translateResponse: String?): TranslateRs {
        val prepareTextByUsedApi = apiClient.prepareJsonByUsedApi(translateResponse!!)
        return TranslateRs(requestId, prepareTextByUsedApi)

    }

}