package co.sena.cimm.adso.Sistema_Donacion_Sangre.service.impl;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.BusinessException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String uploadDir = "uploads/firmas/";

    public FileStorageServiceImpl() {
        try {
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida de archivos", e);
        }
    }

    @Override
    public String guardarFirma(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new BusinessException("El archivo de la firma no puede estar vacío");
        }

        try {
            String nombreOriginal = archivo.getOriginalFilename();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            String nombreNuevo = UUID.randomUUID().toString() + extension;

            Path rutaFinal = Paths.get(uploadDir + nombreNuevo);

            Files.write(rutaFinal, archivo.getBytes());

            return rutaFinal.toString();

        } catch (IOException e) {
            throw new BusinessException("Error al guardar el archivo de la firma: " + e.getMessage());
        }
    }
    @Override
    public String guardarFirmaBase64(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            throw new BusinessException("El string de la firma no puede estar vacío");
        }

        try {
            String base64Data = base64String;
            if (base64String.contains(",")) {
                base64Data = base64String.split(",")[1];
            }

            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            String nombreNuevo = UUID.randomUUID().toString() + ".png";
            Path rutaFinal = Paths.get(uploadDir + nombreNuevo);

            Files.write(rutaFinal, decodedBytes);

            return rutaFinal.toString();

        } catch (IllegalArgumentException e) {
            throw new BusinessException("El formato de la firma Base64 es inválido");
        } catch (IOException e) {
            throw new BusinessException("Error al guardar el archivo de la firma: " + e.getMessage());
        }
    }
}
