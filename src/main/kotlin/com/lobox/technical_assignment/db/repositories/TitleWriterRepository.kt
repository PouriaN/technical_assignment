package com.lobox.technical_assignment.db.repositories

import com.lobox.technical_assignment.db.entities.TitleDirectorEntity
import com.lobox.technical_assignment.db.entities.TitleWriterEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TitleWriterRepository: JpaRepository<TitleWriterEntity, Long> {
}