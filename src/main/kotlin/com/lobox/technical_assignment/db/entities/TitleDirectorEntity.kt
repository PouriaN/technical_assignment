package com.lobox.technical_assignment.db.entities

import jakarta.persistence.*

private const val sequenceGeneratorName = "title_director_sequence_generator"
@Entity
@Table(name = "title_director")
data class TitleDirectorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(
//        name = sequenceGeneratorName,
//        sequenceName = sequenceGeneratorName,
//        initialValue = 1,
//        allocationSize = 1,
//    )
    val id: Long? = null,
    val tconst: String,
    val director: String,
)