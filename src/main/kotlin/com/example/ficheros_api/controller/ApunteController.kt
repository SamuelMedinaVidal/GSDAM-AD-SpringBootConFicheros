package com.example.ficheros_api.controller

import com.example.ficheros_api.model.ArchivoInfo
import com.example.ficheros_api.model.CrearApunteRequest
import com.example.ficheros_api.service.ApunteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.MediaType

@RestController
class ApunteController (private val apunteService: ApunteService)
{
    @PostMapping("/api/apuntes")
   fun crearApunte(@RequestBody datos : CrearApunteRequest) : String{
       apunteService.guardarApunte(datos.nombre, datos.contenido)
       return "Apunte guardado: ${datos.nombre}"
   }

    @GetMapping("/api/apuntes")
    fun leerApunte(@RequestParam nombre: String): String {
        return apunteService.leerApunte(nombre)
    }

    @GetMapping("/api/apuntes/listado")
    fun listarApuntes(): List<String> {
        return apunteService.listarApuntes()
    }

    @PostMapping("/api/apuntes/upload")
    fun subirApunte(@RequestParam("file") file: MultipartFile): String {
        return apunteService.subirApunte(file)
    }

    @PostMapping("/api/apuntes/importar-csv")
    open fun importarApuntes(@RequestParam("file") file: MultipartFile) :
    List<String>{
        return apunteService.importarApuntesCsv(file)
    }

    @GetMapping("/api/apuntes/xml")
    fun convertirApunteXML(@RequestParam nombre: String): String{
        return apunteService.convertirApunteXML(nombre)
    }

    @PostMapping("/api/apuntes/imagen")
    fun importarImagen(@RequestParam("file") file: MultipartFile): String{
        return apunteService.importarImagenes(file)
    }

    @GetMapping("/api/apuntes/imagen")
    fun leerImagen(@RequestParam nombre: String): ResponseEntity<ByteArray> {
        val imagenBytes = apunteService.mostrarImagen(nombre)

        val formato = comprobarFormato(nombre)

        return ResponseEntity.ok()
            .contentType(formato)
            .body(imagenBytes)

    }

    private fun comprobarFormato(nombre: String): MediaType{
        return if (nombre.lowercase().endsWith(".png")){
            MediaType.IMAGE_PNG
        } else {
            MediaType.IMAGE_JPEG
        }
    }

}