package com.lobox.technical_assignment.controllers

import com.lobox.technical_assignment.services.NameBasicsService
import com.lobox.technical_assignment.services.TitleBasicsService
import com.lobox.technical_assignment.services.TitleCrewService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/names")
class NameBasicController(
    val nameBasicsService: NameBasicsService,
    val crewService: TitleCrewService,
    val titleBasicsService: TitleBasicsService,
) {
    @GetMapping("/1")
    fun getTen1(): List<Any> {
        val titlesWithSameAliveDirectorAndWriter = titleBasicsService.getTitlesIdWithSameAliveDirectorAndWriter()
        return titlesWithSameAliveDirectorAndWriter
    }

//    @GetMapping("/2")
//    fun getTen2() = crewService.getTopDirector()

//    @GetMapping("/3")
//    fun getTen3() = crewService.getTopWriter()

//    @GetMapping("/4")
//    fun getTen4() = titleBasicsService.getGenres()
//
//    @GetMapping("/5")
//    fun getTen5() = titleBasicsService.getTitles()
}