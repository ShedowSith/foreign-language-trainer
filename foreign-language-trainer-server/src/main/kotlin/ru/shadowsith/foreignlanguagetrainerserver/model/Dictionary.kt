package ru.shadowsith.foreignlanguagetrainerserver.model

import jakarta.persistence.*


@Entity
@Table(name = "dictionaries")
data class Dictionary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long?,

    var name: String?,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_locale_id", nullable = false)
    var sourceLocale: Locale? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_locale_id", nullable = false)
    var targetLocale: Locale? = null,

    @OneToMany(mappedBy = "dictionary", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    private var translations: List<Translation>? = null

) {
    fun getTranslations(): List<Translation>? {
        return translations?.map { it.copy(dictionary = null) }?.sortedBy { it.id }
    }
}