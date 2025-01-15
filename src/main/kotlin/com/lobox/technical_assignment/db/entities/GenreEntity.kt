package com.lobox.technical_assignment.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "genre")
class GenreEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val name: String,
    @ManyToMany
    val titles: List<TitleBasicsEntity>
)