package ru.shadowsith.translationserver.dto

import org.springframework.http.HttpMethod
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody

data class TranslateRequestSpecification(
    val url: String? = null,
    val headers: MultiValueMap<String, String>? = null,
    val params: Map<String, String>? = null,
    val httpMethod: HttpMethod? = null,
    val body: RequestBody? = null
    ) {
    class Builder(
        var url: String? = null,
        var headers: MultiValueMap<String, String>? = null,
        var params: Map<String, String>? = null,
        var httpMethod: HttpMethod? = null,
        var body: RequestBody? = null
    ) {
        fun url(url: String?) = apply { this.url = url }
        fun headers(headers: MultiValueMap<String, String>?) = apply { this.headers = headers }
        fun params(params: Map<String, String>?) = apply { this.params = params }
        fun httpMethod(httpMethod: HttpMethod?) = apply { this.httpMethod = httpMethod }
        fun body(body: RequestBody?) = apply { this.body = body }
        fun build() = TranslateRequestSpecification(
            url = url,
            headers = headers,
            params = params,
            httpMethod = httpMethod,
            body = body
        )

    }
}