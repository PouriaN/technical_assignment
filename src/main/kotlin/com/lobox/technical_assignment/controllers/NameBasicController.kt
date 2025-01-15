package com.lobox.technical_assignment.controllers

import com.lobox.technical_assignment.services.NameBasicsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/names")
class NameBasicController(val nameBasicsService: NameBasicsService) {
    @GetMapping
    fun getTen() = nameBasicsService.getTopTen()
}