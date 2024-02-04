package ru.shadowsith.translationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TranslationServerApplication

fun main(args: Array<String>) {
    runApplication<TranslationServerApplication>(*args)
}
