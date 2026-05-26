package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String guardarFirma(MultipartFile archivo);
    public String guardarFirmaBase64(String base64String);
}
