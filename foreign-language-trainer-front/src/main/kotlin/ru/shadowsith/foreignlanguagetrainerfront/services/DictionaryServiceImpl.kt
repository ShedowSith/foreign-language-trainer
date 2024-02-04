package ru.shadowsith.foreignlanguagetrainerfront.services


import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import ru.shadowsith.foreignlanguagetrainerfront.dto.*

@Service
class DictionaryServiceImpl(
    private val restTemplate: RestTemplate
) : DictionaryService {
    override fun getDictionaries(): List<Dictionary> {
        return restTemplate.getForObject("/api/v1/dictionaries", object: ParameterizedTypeReference<List<Dictionary>> () {})

    }

    override fun addDictionary(dictionary: Dictionary) {
        restTemplate.postForEntity("/api/v1/dictionaries", dictionary, Dictionary::class.java)
    }

    override fun getDictionary(id: Long): Dictionary {
        return restTemplate.getForObject("/api/v1/dictionaries/$id", object: ParameterizedTypeReference<List<Dictionary>> () {})
    }

    override fun checkTraining(dictionaryId: Long, formTraining: FormTraining, testId: Int): ResultChecking {
        val dictionary = getDictionary(dictionaryId)
        val expectedTranslations = dictionary.translations.filter { !it.isStudied!! }
        val actualTranslations = formTraining.translations.map {
            Translation.Builder()
                .id(it.id)
                .word(it.word)
                .translation(it.translation)
                .isStudied(false)
                .build()
        }

        val errors = mutableListOf<ErrorTranslation>()
        actualTranslations.forEach { translation ->
            val expectedTranslation = expectedTranslations.find { it.id == translation.id }
            if(expectedTranslation != translation)
                errors.add(
                    when (testId) {
                        2 -> ErrorTranslation.Builder()
                            .word(translation.translation!!)
                            .incorrectAnswer(translation.word!!)
                            .correctAnswer(expectedTranslation?.word!!)
                            .build()
                        else -> ErrorTranslation.Builder()
                            .word(translation.word!!)
                        .incorrectAnswer(translation.translation!!)
                        .correctAnswer(expectedTranslation?.translation!!)
                        .build()
                    }
                )
        }
        return ResultChecking.Builder()
            .total(actualTranslations.size)
            .correctAnswerCount(actualTranslations.size - errors.size)
            .errors(errors)
            .build()
    }

    override fun deleteDictionary(id: Long) {
        restTemplate.delete("/api/v1/dictionaries/$id")
    }
}