package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.ConsentimientoRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.ConsentimientoResponse;

public interface ConsentimientoService {
    ConsentimientoResponse registrarConsentimiento(ConsentimientoRequest request);
    ConsentimientoResponse buscarPorDonante(Long donanteId);
    boolean tieneConsentimientoFirmado(Long donanteId);
}