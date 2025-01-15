package com.lobox.technical_assignment.db.repositories

import com.lobox.technical_assignment.db.entities.TitleBasicsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TitleBasicsRepository: JpaRepository<TitleBasicsEntity, String> {
}