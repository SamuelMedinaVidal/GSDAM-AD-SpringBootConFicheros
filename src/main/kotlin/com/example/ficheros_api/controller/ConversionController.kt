package com.example.ficheros_api.controller

import com.example.ficheros_api.model.ArchivoInfo
import com.example.ficheros_api.service.ConversionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ConversionController(
    private val conversionService: ConversionService
) {

    @PostMapping("/api/conversions/csv-json")
    fun csvToJson(
        @RequestParam("file") file: MultipartFile
    ): List<ArchivoInfo> {
        return conversionService.csvToFiles(file)
    }
}