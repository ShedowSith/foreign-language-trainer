package ru.shadowsith.foreignlanguagetrainerserver.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.shadowsith.foreignlanguagetrainerserver.model.Translation
import ru.shadowsith.foreignlanguagetrainerserver.repositories.DictionaryRepository
import ru.shadowsith.foreignlanguagetrainerserver.repositories.TranslationRepository

@Service
class TranslationServiceImpl(
    private val translationRepository: TranslationRepository,
    private val dictionaryRepository: DictionaryRepository
) : TranslationService {
    override fun addTranslation(transient: Translation): Translation? {
        return dictionaryRepository.findByIdOrNull(transient.dictionary?.id!!)?.let {
            return translationRepository.save(transient)
        }
    }

    override fun updateTranslation(id: Long, transient: Translation): Translation? {
        return translationRepository.findByIdOrNull(id)?.let {
            val update = it.copy(
                word = transient.word ?: it.word,
                translation = transient.translation ?: it.translation,
                isStudied = transient.isStudied ?: it.isStudied
            )
            translationRepository.save(update)
        }
    }

    override fun deleteTranslation(id: Long): Translation? {
        return translationRepository.findByIdOrNull(id)?.let {
            translationRepository.delete(it)
            it
        }
    }
}