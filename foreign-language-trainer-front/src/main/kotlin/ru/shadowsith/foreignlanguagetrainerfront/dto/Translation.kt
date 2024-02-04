package ru.shadowsith.foreignlanguagetrainerfront.dto

data class Translation(
    val id: Long? = null,
    val word: String? = null,
    val translation: String? = null,
    val isStudied: Boolean? = false,
    val dictionary: Dictionary? = null
) {
    class Builder(
        var id: Long? = null,
        var word: String? = null,
        var translation: String? = null,
        var isStudied: Boolean? = false,
        var dictionary: Dictionary? = null
    ) {
        fun id(id: Long?) = apply { this.id = id }
        fun word(word: String?) = apply { this.word = word }
        fun translation(translation: String?) = apply { this.translation = translation }
        fun isStudied(isStudied: Boolean?) = apply { this.isStudied = isStudied }
        fun dictionary(dictionary: Dictionary?) = apply { this.dictionary = dictionary }
        fun build() = Translation(
            id, word, translation, isStudied, dictionary
        )
    }

    override fun equals(other: Any?) = (other is Translation)
            && word.equals(other.word, true) && translation.equals(other.translation, true)

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (word?.hashCode() ?: 0)
        result = 31 * result + (translation?.hashCode() ?: 0)
        result = 31 * result + (isStudied?.hashCode() ?: 0)
        result = 31 * result + (dictionary?.hashCode() ?: 0)
        return result
    }
}
