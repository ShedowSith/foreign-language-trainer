package ru.shadowsith.foreignlanguagetrainerserver.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary
import ru.shadowsith.foreignlanguagetrainerserver.repositories.DictionaryRepository

@Service
class DictionaryServiceImpl(
    private val dictionaryRepository: DictionaryRepository,
    private val localeService: LocaleService
) : DictionaryService {
    override fun addDictionary(dictionary: Dictionary): Dictionary? {
        localeService.getLocale(dictionary.sourceLocale?.id!!)?.let {sourceLocale ->
            localeService.getLocale(dictionary.targetLocale?.id!!)?.let { targetLocale ->
                return dictionaryRepository.save(dictionary.copy(sourceLocale = sourceLocale, targetLocale = targetLocale))
            }
        }
        return null
    }

    override fun getDictionaries(): List<Dictionary> {
         return dictionaryRepository.findAll()
    }

    override fun getDictionary(id: Long): Dictionary? {
        return dictionaryRepository.findByIdOrNull(id)
    }

    override fun deleteDictionary(id: Long): Dictionary? {
        return dictionaryRepository.findByIdOrNull(id)?.let { dictionary ->
            dictionaryRepository.delete(dictionary)
            dictionary
        }
    }
}