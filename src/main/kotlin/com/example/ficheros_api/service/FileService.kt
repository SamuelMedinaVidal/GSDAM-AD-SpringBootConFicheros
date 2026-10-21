package com.example.ficheros_api.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class FileService {

    private val file: Path = Path.of("data", "mensaje.txt")

    fun writeMessage() {
        Files.createDirectories(file.parent)
        Files.writeString(file, "Hola desde un fichero")
    }

    fun readMessage(): String {
        return Files.readString(file)
    }
}