package com.example.ficheros_api.controller

import com.example.ficheros_api.model.SaludoResponse
import org.springframework.stereotype.Service

@Service
class SaludoService {
    fun crearSaludo(nombre: String): SaludoResponse {
        val mensaje = "Hola, $nombre"
        return SaludoResponse(mensaje, mensaje.length)
    }
}