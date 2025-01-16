package com.lobox.technical_assignment.db.entities

import jakarta.persistence.*

private const val sequenceGeneratorName = "title_writer_sequence_generator"
@Entity
@Table(name = "title_writer")
data class TitleWriterEntity(
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
    val writer: String,
)
