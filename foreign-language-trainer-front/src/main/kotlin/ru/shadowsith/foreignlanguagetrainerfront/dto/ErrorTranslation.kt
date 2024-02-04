package ru.shadowsith.foreignlanguagetrainerfront.dto

data class ErrorTranslation(
    val word: String? = null,
    val incorrectAnswer: String? = null,
    val correctAnswer: String? = null
) {
    class Builder(
        var word: String? = null,
        var incorrectAnswer: String? = null,
        var correctAnswer: String? = null,
    ) {
        fun word(word: String?) = apply { this.word = word }
        fun incorrectAnswer(incorrectAnswer: String?) = apply { this.incorrectAnswer = incorrectAnswer }
        fun correctAnswer(correctAnswer: String?) = apply { this.correctAnswer = correctAnswer }
        fun build() = ErrorTranslation(word, incorrectAnswer, correctAnswer)
    }
}