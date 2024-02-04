package ru.shadowsith.foreignlanguagetrainerfront.dto

data class ResultChecking(
    val total: Int? = null,
    val correctAnswerCount: Int? = null,
    val errors: List<ErrorTranslation>? = null
) {
    class Builder(
        var total: Int? = null,
        var correctAnswerCount: Int? = null,
        var errors: List<ErrorTranslation>? = null
    ) {
        fun total(total: Int?) = apply { this.total = total }
        fun correctAnswerCount(correctAnswerCount: Int?) = apply { this.correctAnswerCount = correctAnswerCount }
        fun errors(errors: List<ErrorTranslation>?) = apply { this.errors = errors }
        fun build() = ResultChecking(total, correctAnswerCount, errors)
    }
}