package ru.shadowsith.foreignlanguagetrainerserver.repositories


import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary

import ru.shadowsith.foreignlanguagetrainerserver.model.Translation

@Repository
interface TranslationRepository: CrudRepository<Translation, Long> {
//    @Query("select * from translation where dictionaries_id = :id")
//    fun findByDictionary(id: Long): Flux<Translation>

    fun findByDictionary(dictionary: Dictionary): List<Translation>
}