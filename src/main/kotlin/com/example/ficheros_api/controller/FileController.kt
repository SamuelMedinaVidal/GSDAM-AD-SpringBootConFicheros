package com.example.ficheros_api.controller

import com.example.ficheros_api.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/api/file")
    fun write(): String {
        fileService.writeMessage()
        return "Fichero guardado"
    }

    @GetMapping("/api/file")
    fun read(): String {
        return fileService.readMessage()
    }
}