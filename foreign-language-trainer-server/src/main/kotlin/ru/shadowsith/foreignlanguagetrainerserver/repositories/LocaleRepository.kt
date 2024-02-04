package ru.shadowsith.foreignlanguagetrainerserver.repositories

import org.springframework.data.repository.CrudRepository
import ru.shadowsith.foreignlanguagetrainerserver.model.Locale

interface LocaleRepository: CrudRepository<Locale, Long> {
    override fun findAll(): List<Locale>
}