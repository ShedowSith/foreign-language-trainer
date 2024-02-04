package ru.shadowsith.translationserver.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import ru.shadowsith.translationserver.dto.TranslateRequestSpecification
import java.nio.charset.StandardCharsets
import java.util.*

@Service
@ConfigurationProperties(prefix = "google")
class GoogleApiClient(
    val agents: List<String>
): ApiClient {
    private var url: String? = null
    private var headers: MultiValueMap<String, String>? = null
    private var params: Map<String, String>? = null
    private var urlTemplate: String? = null
    private val rnd = Random()
    override fun getRequestSpecification(
        sourceLanguage: String?,
        targetLanguage: String?,
        originalText: String?
    ): TranslateRequestSpecification {
        setUrlTemplate(sourceLanguage, targetLanguage)
        headers!!.remove("User-Agent")
        headers!!.add("User-Agent", agents!![rnd.nextInt(agents!!.size)])
        return TranslateRequestSpecification.Builder()
            .httpMethod(HttpMethod.GET)
            .headers(headers)
            .params(params)
            .url(urlTemplate + originalText)
            .build()
    }

    override fun prepareJsonByUsedApi(json: String): String {
        return json.substring(2, json.length - 2)
    }

    fun setUrlTemplate(sourceLanguage: String?, targetLanguage: String?) {
        this.urlTemplate = UriComponentsBuilder.fromHttpUrl(url!!)
            .queryParam("sl", sourceLanguage)
            .queryParam("tl", targetLanguage)
            .queryParam("q", "")
            .encode(StandardCharsets.UTF_8)
            .toUriString()
    }

    fun getHeaders(): Map<String, List<String>> {
        return headers!!
    }

    fun setHeaders(headers: MultiValueMap<String, String>?) {
        this.headers = headers
    }

    fun getParams(): Map<String, String> {
        return params!!
    }

    fun setParams(params: Map<String, String>?) {
        this.params = params
    }

    fun getUrlTemplate(): String? {
        return urlTemplate
    }

    fun setUrlTemplate(urlTemplate: String) {
        this.urlTemplate = urlTemplate
    }

    fun getUrl(): String {
        return url!!
    }

    fun setUrl(url: String?) {
        this.url = url
    }


}