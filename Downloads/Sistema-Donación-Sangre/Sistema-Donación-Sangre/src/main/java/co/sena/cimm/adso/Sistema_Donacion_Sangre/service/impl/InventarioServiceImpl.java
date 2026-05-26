package co.sena.cimm.adso.Sistema_Donacion_Sangre.service.impl;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.InventarioResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.ResourceNotFoundException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper.InventarioMapper;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.InventarioSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.InventarioSangreRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {


    private final InventarioSangreRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    public List<InventarioResponse> listarTodo() {
        log.info("Consultando inventario completo de sangre");
        return inventarioRepository.findAll()
                .stream()
                .map(inventarioMapper::toResponse)
                .toList();
    }

    @Override
    public InventarioResponse obtenerInventarioPorTipoSangre(TipoSangre tipoSangre) {

        log.info("Consultando inventario para tipo de sangre: {}", tipoSangre);
        return inventarioMapper.toResponse(buscarPorTipoSangre(tipoSangre));
    }

    @Override
    public void actualizarInventario(TipoSangre tipoSangre, Integer cantidadMl) {
        log.info("Actualizando inventario: tipo={}, cantidad={}ml", tipoSangre,cantidadMl);

        InventarioSangre inventario = inventarioRepository.findByTipoSangre(tipoSangre)
                .orElseGet(() -> crearRegistroInventario(tipoSangre));

        inventario.setCantidadDisponibleMl(inventario.getCantidadDisponibleMl() + cantidadMl);
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);
    }

    private InventarioSangre buscarPorTipoSangre(TipoSangre tipoSangre) {
        return inventarioRepository.findByTipoSangre(tipoSangre)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay inventario registrado para el tipo de sangre: " + tipoSangre.getCodigo()));
    }

    private InventarioSangre crearRegistroInventario(TipoSangre tipoSangre) {
        log.info("Creando registro de inventario para nuevo tipo de sangre: {}", tipoSangre);
        return InventarioSangre.builder()
                .tipoSangre(tipoSangre)
                .cantidadDisponibleMl(0)
                .ultimaActualizacion(LocalDateTime.now())
                .build();
    }
}
