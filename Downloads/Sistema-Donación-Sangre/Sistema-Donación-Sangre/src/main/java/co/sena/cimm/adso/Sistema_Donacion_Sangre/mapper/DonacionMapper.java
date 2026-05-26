package co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonacionResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donacion;
import org.springframework.stereotype.Component;

@Component
public class DonacionMapper {

    public DonacionResponse toResponse(Donacion donacion) {
        return DonacionResponse.builder()
                .id(donacion.getId())
                .codigoDonacion(donacion.getCodigoDonacion())
                .donanteId(donacion.getDonante().getId())
                .nombreDonante(donacion.getDonante().getNombres() + " " + donacion.getDonante().getApellidos())
                .cantidadMl(donacion.getCantidadMl())
                .fechaDonacion(donacion.getFechaDonacion())
                .tipoSangre(donacion.getTipoSangre())
                .estado(donacion.getEstado())
                .observaciones(donacion.getObservaciones())
                .build();
    }

}
