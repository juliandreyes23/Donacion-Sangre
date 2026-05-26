package co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.InventarioResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.InventarioSangre;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioResponse toResponse(InventarioSangre inventario) {
        return InventarioResponse.builder()
                .id(inventario.getId())
                .tipoSangre(inventario.getTipoSangre())
                .cantidadDisponibleMl(inventario.getCantidadDisponibleMl())
                .ultimaActualizacion(inventario.getUltimaActualizacion())
                .build();
    }
}
