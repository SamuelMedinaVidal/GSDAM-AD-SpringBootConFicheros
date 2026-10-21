package com.example.ficheros_api.service

import com.example.ficheros_api.model.ArchivoInfo
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ConversionService {

    fun csvToFiles(file: MultipartFile): List<ArchivoInfo> {
        val archivos = mutableListOf<ArchivoInfo>()

        file.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
            reader.readLine()

            reader.forEachLine { linea ->
                if (linea.isNotBlank()) {
                    val columnas = linea.split(",")
                    val archivo = ArchivoInfo(
                        nombre = columnas[0].trim(),
                        tipo = columnas[1].trim(),
                        tamanyo = columnas[2].trim().toLong()
                    )
                    archivos.add(archivo)
                }
            }
        }

        return archivos
    }
}