package ru.shadowsith.foreignlanguagetrainerserver.model

import jakarta.persistence.*

@Entity
@Table(name = "locales")
data class Locale(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    val caption: String? = null,

    val code: String? = null
)