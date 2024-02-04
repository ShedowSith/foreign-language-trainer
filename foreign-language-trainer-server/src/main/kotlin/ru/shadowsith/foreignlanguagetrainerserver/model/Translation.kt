package ru.shadowsith.foreignlanguagetrainerserver.model

import jakarta.persistence.*


@Entity
@Table(name = "translations")
data class Translation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,
    @Column(name = "word")
    var word: String?,
    @Column(name = "translation")
    var translation: String?,
    @Column(name = "is_studied")
    var isStudied: Boolean? = false,
    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    var dictionary: Dictionary? = null
)
