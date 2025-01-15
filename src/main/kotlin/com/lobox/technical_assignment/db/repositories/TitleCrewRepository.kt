package com.lobox.technical_assignment.db.repositories

import com.lobox.technical_assignment.db.entities.TitleCrewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TitleCrewRepository: JpaRepository<TitleCrewEntity, String> {
}