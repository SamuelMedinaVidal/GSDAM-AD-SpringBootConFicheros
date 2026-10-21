package com.example.ficheros_api.controller

import com.example.ficheros_api.model.SaludoResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SaludoController {

    @GetMapping("/api/saludos/{nombre}")
    fun obtenerSaludo(@PathVariable nombre: String): SaludoResponse {
        val mensaje = "Hola, $nombre"
        return SaludoResponse(mensaje, mensaje.length)
    }
}