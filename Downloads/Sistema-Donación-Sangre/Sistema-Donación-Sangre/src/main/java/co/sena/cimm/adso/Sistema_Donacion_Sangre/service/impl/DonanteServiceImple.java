package co.sena.cimm.adso.Sistema_Donacion_Sangre.service.impl;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonanteRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonanteResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.BusinessException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.ResourceNotFoundException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper.DonanteMapper;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.DonanteRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.DonanteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DonanteServiceImple implements DonanteService {

    private final DonanteRepository donanteRepository;
    private final DonanteMapper donanteMapper;

    @Override
    public DonanteResponse registrar(DonanteRequest request) {
        log.info("Registrado nuevo donante con documento: {}", request.getDocumento());
        verificarDocumentoUnico(request.getDocumento());
        verificarCorreoUnico(request.getCorreo());
        Donante donante = donanteMapper.toEntity(request);
        Donante guardado = donanteRepository.save(donante);
        log.info("Donante registrado exitosamente con id {}", guardado.getId());
        return donanteMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public DonanteResponse buscarPorId(Long id) {
        log.info("Buscando donante con id {}", id);
        return donanteMapper.toResponse(buscarDonantePorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public DonanteResponse buscarPorDocumento(String documento) {
        log.info("Buscando donante con documento: {}", documento);
        Donante donante = donanteRepository.findByDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Donante no encontrado con documento: " + documento));
        return donanteMapper.toResponse(donante);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DonanteResponse> listarTodos(Pageable pageable) {
        log.info("Consultando todos los donantes paginados");
        return donanteRepository.findAll(pageable)
                .map(donanteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonanteResponse> listarPorTipoSangre(TipoSangre tipoSangre) {
        log.info("Filtrando donantes por tipo de sangre: {}", tipoSangre);
        return donanteRepository.findByTiposangre(tipoSangre)
                .stream()
                .map(donanteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DonanteResponse actualizar(Long id, DonanteRequest request) {
        log.info("Actualizando donante con id {}", id);
        Donante donante = buscarDonantePorId(id);
        boolean documentoCambio = !donante.getDocumento().equals(request.getDocumento());
        boolean correoCambio = !donante.getCorreo().equals(request.getCorreo());
        if (documentoCambio) verificarDocumentoUnico(request.getDocumento());
        if (correoCambio) verificarCorreoUnico(request.getCorreo());
        donanteMapper.actualizarEntidad(donante, request);
        Donante actualizado = donanteRepository.save(donante);
        log.info("Donante actualizado exitosamente con id {}", actualizado.getId());
        return donanteMapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando (desactivando) donante con id {}", id);
        Donante donante = buscarDonantePorId(id);
        donante.setActivo(false);
        donanteRepository.save(donante);
        log.info("Donante desactivado exitosamente con id: {}", id);
    }

    private Donante buscarDonantePorId(Long id) {
        return donanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con id: " + id));
    }

    private void verificarDocumentoUnico(String documento) {
        if (donanteRepository.existsByDocumento(documento)) {
            throw new BusinessException("Ya existe un donante con el documento: " + documento);
        }
    }

    private void verificarCorreoUnico(String correo) {
        if (donanteRepository.existsByCorreo(correo)) {
            throw new BusinessException("Ya existe un donante con el correo: " + correo);
        }
    }
}