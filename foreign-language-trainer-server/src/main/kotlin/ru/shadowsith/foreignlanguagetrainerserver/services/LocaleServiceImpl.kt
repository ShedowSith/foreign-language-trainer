package ru.shadowsith.foreignlanguagetrainerserver.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.shadowsith.foreignlanguagetrainerserver.model.Locale
import ru.shadowsith.foreignlanguagetrainerserver.repositories.LocaleRepository

@Service
class LocaleServiceImpl(
    private val localeRepository: LocaleRepository
) : LocaleService {
    override fun addLocale(locale: Locale): Locale {
        return localeRepository.save(locale)
    }

    override fun getLocales(): List<Locale> {
        return localeRepository.findAll()
    }

    override fun getLocale(id: Long): Locale? {
        return localeRepository.findByIdOrNull(id)
    }

    override fun deleteLocale(id: Long): Locale? {
        return localeRepository.findByIdOrNull(id)?.let {
            localeRepository.delete(it)
            it
        }
    }
}