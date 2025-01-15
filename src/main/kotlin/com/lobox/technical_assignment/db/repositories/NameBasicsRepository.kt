package com.lobox.technical_assignment.db.repositories

import com.lobox.technical_assignment.db.entities.NameBasicsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NameBasicsRepository: JpaRepository<NameBasicsEntity, String> {
}