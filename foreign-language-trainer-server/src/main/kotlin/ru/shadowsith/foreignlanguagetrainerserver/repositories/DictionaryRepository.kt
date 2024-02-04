package ru.shadowsith.foreignlanguagetrainerserver.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.shadowsith.foreignlanguagetrainerserver.model.Dictionary

@Repository
interface DictionaryRepository: CrudRepository<Dictionary, Long> {
    override fun findAll(): List<Dictionary>
}