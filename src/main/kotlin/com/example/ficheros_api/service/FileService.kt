package com.example.ficheros_api.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import org.springframework.web.multipart.MultipartFile
import java.nio.file.StandardCopyOption

@Service
class FileService {

    private val folder: Path = Path.of("data")

    init {
        Files.createDirectories(folder)
    }

    private fun resolveName(nombre: String): Path {
        require(nombre.isNotBlank() && nombre != "." && nombre != "..") {
            "El nombre del fichero no es válido"
        }
        require(nombre.none { it in "/\\:" }) {
            "Introduce solo un nombre, sin carpetas"
        }
        return folder.resolve(nombre)
    }

    // Mantiene la operación inicial, que escribía un mensaje fijo.
    fun writeMessage() {
        Files.writeString(folder.resolve("mensaje.txt"), "Hola desde un fichero")
    }

    fun writeMessage(nombre: String, contenido: String) {
        Files.writeString(resolveName(nombre), contenido)
    }

    fun readMessage(nombre: String): String {
        return Files.readString(resolveName(nombre))
    }

    fun list(): List<String> {
        return Files.list(folder).use { paths ->
            paths
                .filter { Files.isRegularFile(it) }
                .map { it.fileName.toString() }
                .sorted()
                .toList()
        }
    }

    fun save(file: MultipartFile): String {
        require(!file.isEmpty) { "El fichero está vacío" }

        val name = file.originalFilename ?: ""
        val destination = resolveName(name)

        file.inputStream.use { input ->
            Files.copy(
                input,
                destination,
                StandardCopyOption.REPLACE_EXISTING
            )
        }

        return "Fichero guardado: $name"
    }
}