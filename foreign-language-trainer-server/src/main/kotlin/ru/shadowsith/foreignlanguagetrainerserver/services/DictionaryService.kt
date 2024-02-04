package ru.shadowsith.foreignlanguagetrainerserver.services

import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary

interface DictionaryService {
    fun addDictionary(dictionary: Dictionary): Dictionary?
    fun getDictionaries(): List<Dictionary>
    fun getDictionary(id: Long): Dictionary?
    fun deleteDictionary(id: Long): Dictionary?
}