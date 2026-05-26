package co.sena.cimm.adso.Sistema_Donacion_Sangre.service.impl;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.ConsentimientoRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.ConsentimientoResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.BusinessException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.ResourceNotFoundException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper.ConsentimientoMapper;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Consentimiento;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.ConsentimientoRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.DonanteRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.ConsentimientoService;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.FileStorageService; // <-- Importar el servicio
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsentimientoServiceImpl implements ConsentimientoService {

    private final DonanteRepository donanteRepository;
    private final ConsentimientoRepository consentimientoRepository;
    private final ConsentimientoMapper consentimientoMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ConsentimientoResponse registrarConsentimiento(ConsentimientoRequest request) {
        log.info("Registrando consentimiento para donante id: {}", request.getDonanteId());

        Donante donante = donanteRepository.findById(request.getDonanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con ID: " + request.getDonanteId()));

        if (consentimientoRepository.existsByDonanteId(request.getDonanteId())) {
            throw new BusinessException("El donante con id " + request.getDonanteId() + " ya tiene un consentimiento registrado");
        }


        String rutaArchivoFirma = fileStorageService.guardarFirmaBase64(request.getFirmaConsentimiento());

        Consentimiento consentimiento = Consentimiento.builder()
                .donante(donante)
                .aceptaConsentimiento(request.isAceptaConsentimiento())
                .firmaConsentimiento(rutaArchivoFirma)
                .fechaFirma(request.getFechaFirma() != null ? request.getFechaFirma() : LocalDate.now())
                .build();

        Consentimiento guardado = consentimientoRepository.save(consentimiento);
        log.info("Consentimiento registrado exitosamente con id {}", guardado.getId());

        return consentimientoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsentimientoResponse buscarPorDonante(Long donanteId) {
        Consentimiento consentimiento = consentimientoRepository.findByDonanteId(donanteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consentimiento no encontrado para el donante con id: " + donanteId));
        return consentimientoMapper.toResponse(consentimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneConsentimientoFirmado(Long donanteId) {
        return consentimientoRepository.existsByDonanteId(donanteId);
    }
}