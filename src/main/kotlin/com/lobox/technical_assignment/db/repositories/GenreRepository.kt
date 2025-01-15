package com.lobox.technical_assignment.db.repositories

import com.lobox.technical_assignment.db.entities.GenreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository: JpaRepository<GenreEntity, Long> {
}