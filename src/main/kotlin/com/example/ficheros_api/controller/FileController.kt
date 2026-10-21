package com.example.ficheros_api.controller

import com.example.ficheros_api.model.CrearTextoRequest
import com.example.ficheros_api.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/api/file")
    fun write(@RequestBody(required = false) datos: CrearTextoRequest?): String {
        if (datos == null) {
            fileService.writeMessage()
            return "Fichero guardado: mensaje.txt"
        }
        fileService.writeMessage(datos.nombre, datos.contenido)
        return "Fichero guardado: ${datos.nombre}"
    }

    @GetMapping("/api/file")
    fun read(@RequestParam(defaultValue = "mensaje.txt") nombre: String): String {
        return fileService.readMessage(nombre)
    }

    @GetMapping("/api/files")
    fun list(): List<String> {
        return fileService.list()
    }
}