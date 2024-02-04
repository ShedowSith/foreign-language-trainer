package ru.shadowsith.foreignlanguagetrainerfront.services

import ru.shadowsith.foreignlanguagetrainerfront.dto.Dictionary
import ru.shadowsith.foreignlanguagetrainerfront.dto.ResultChecking
import ru.shadowsith.foreignlanguagetrainerfront.dto.FormTraining

interface DictionaryService {

    fun getDictionaries(): List<Dictionary>
    fun addDictionary(dictionary: Dictionary)
    fun getDictionary(id: Long): Dictionary

    fun checkTraining(dictionaryId: Long, formTraining: FormTraining, testId: Int): ResultChecking
    fun deleteDictionary(id: Long)
}