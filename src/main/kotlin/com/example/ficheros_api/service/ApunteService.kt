package com.example.ficheros_api.service

import com.example.ficheros_api.model.CrearApunteRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import com.fasterxml.jackson.dataformat.xml.XmlMapper

@Service
class ApunteService {
    private val folder: Path = Path.of("apuntes")

    init {
        Files.createDirectories(folder)
    }

    private fun resolveName(nombre: String): Path {
        require(nombre.isNotBlank() && nombre != "." && nombre != "..") {
            "El nombre del fichero no es valido"
        }
        require(nombre.none {it in "/\\:"}) {
            "Introduce solo un nombre, sin carpetas"
        }
        require(nombre.endsWith(".txt")){
            "El archivo debe de ser de texto y terminar en .txt"
        }
        return folder.resolve(nombre)
    }

    private fun resolveImagen (nombre: String): Path{
        require(nombre.isNotBlank() && nombre != "." && nombre != "..") {
            "El nombre de la imagen no es válido"
        }
        require(nombre.none{it in "/\\:"}){
            "Introduce solo un nombre, sin carpetas"
        }
        require(nombre.endsWith(".png", true) || nombre.endsWith(".jpg", true) || nombre.endsWith(".jpeg", true)){
            "El archivo debe de ser una imagen .png, .jpg o .jpeg"
        }
        return folder.resolve(nombre)
    }

    fun guardarApunte(nombre: String, contenido: String) {
        val pathDestino = resolveName(nombre)
        Files.writeString(pathDestino, contenido)
    }

    fun leerApunte(nombre: String): String {
        val pathOrigen = resolveName(nombre)
        return Files.readString(pathOrigen)
    }

    fun listarApuntes(): List<String> {
        return Files.list(folder).use { paths ->
            paths
                .filter { Files.isRegularFile(it)}
                .map { it.fileName.toString() }
                .filter { it.endsWith(".txt") }
                .sorted()
                .toList()
        }
    }

    fun subirApunte(file: MultipartFile): String {
        require(!file.isEmpty) {"El fichero está vacío"}

        val nombre = file.originalFilename ?: ""
        val pathDestino = resolveName(nombre)

        file.inputStream.use { input ->
            Files.copy(
                input,
                pathDestino,
                StandardCopyOption.REPLACE_EXISTING)
        }
        return "Apunte guardado : $nombre"
    }

    fun importarApuntesCsv(file: MultipartFile): MutableList<String> {
        val nombresImportados = mutableListOf<String>()
        
        file.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
            reader.readLine()

            reader.forEachLine { linea ->
                if (linea.isNotBlank()) {
                    val columnas = linea.split(",")
                    val nombre = columnas[0].trim()
                    val contenido = columnas[1].trim()

                    guardarApunte(nombre, contenido)
                    nombresImportados.add(nombre)
                }
            }
        }
        return nombresImportados
    }

    /*
        Problema: al hacer el GET, el servidor devuelve el XML envolviendo todo con el nombre de la clase
        'CrearApunteRequest', y el enunciado marca que ha de ser envuelto por 'apunte'. Para esto le he consultado a
        Gemini y me ha dicho que se ha de añadir una anotación a la clase
        'CrearApunteRequest': "@JacksonXmlRootElement(localName = "apunte")"
    */
    fun convertirApunteXML(nombre: String): String{
        val texto = leerApunte(nombre)
        val apunte = CrearApunteRequest(nombre, texto)

        val xmlMapper = XmlMapper()
        return xmlMapper.writeValueAsString(apunte)
    }

    fun importarImagenes(file: MultipartFile): String {
        require (!file.isEmpty) {
            "El archivo de imagen está vacío"
        }

        val nombre = file.originalFilename ?: ""
        val pathDestino = resolveImagen(nombre)

        file.inputStream.use { input ->
            Files.copy(
                input,
                pathDestino,
                StandardCopyOption.REPLACE_EXISTING
            )
        }

        return "Imagen guardada: $nombre"
    }

    fun mostrarImagen(nombre: String): ByteArray{
        val pathOrigen = resolveImagen(nombre)
        return Files.readAllBytes(pathOrigen)
    }
}