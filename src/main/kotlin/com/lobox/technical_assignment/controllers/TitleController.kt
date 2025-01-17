package com.lobox.technical_assignment.controllers

import com.lobox.technical_assignment.models.PageableRequest
import com.lobox.technical_assignment.services.NameBasicsService
import com.lobox.technical_assignment.services.TitleBasicsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/titles")
class TitleController(val titleBasicsService: TitleBasicsService) {
    @GetMapping("/same_director_writer")
    fun getTitlesWithSameDirectorAndWriter(pageableRequest: PageableRequest) =

        titleBasicsService.getTitlesWithSameAliveDirectorAndWriter(pageableRequest)

    @GetMapping("/actors")
    fun getTitlesBasedOnActors(actors: List<String>) = titleBasicsService.getTitlesPlayedByActor(actors)

    @GetMapping("/genre/{genre}")
    fun getGenreTitles(@PathVariable genre: String) = titleBasicsService.bestGenreTitles(genre)
}