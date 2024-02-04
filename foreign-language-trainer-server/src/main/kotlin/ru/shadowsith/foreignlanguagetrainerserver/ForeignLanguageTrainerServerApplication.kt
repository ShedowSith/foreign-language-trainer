package ru.shadowsith.foreignlanguagetrainerserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ForeignLanguageTrainerServerApplication

fun main(args: Array<String>) {
	runApplication<ForeignLanguageTrainerServerApplication>(*args)
}
