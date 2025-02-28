package com.notaaiassignmentrdbac.application.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
class ProjectCommandController {
    @PostMapping("")
    fun createProject() {

    }

    @PutMapping("")
    fun updateProject() {

    }

    @DeleteMapping("")
    fun deleteProject() {

    }

    @PostMapping("/member/{accountId}")
    fun addMember() {

    }
}