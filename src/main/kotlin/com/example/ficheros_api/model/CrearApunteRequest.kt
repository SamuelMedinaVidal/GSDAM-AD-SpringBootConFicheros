package com.example.ficheros_api.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

// Añadimos el '@JacksonXmlRootElement' para poder conseguir el resultado solicitado en el reto 6 del ejercicio 4,
// más explicación en el ApunteService
@JacksonXmlRootElement(localName = "apunte")
data class CrearApunteRequest (
    val nombre: String,
    val contenido: String
)