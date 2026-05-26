package co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.ConsentimientoResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Consentimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsentimientoMapper {

    @Mapping(source = "donante.id", target = "donanteId")
    @Mapping(target = "nombreDonante", expression = "java(consentimiento.getDonante().getNombres() + ' ' + consentimiento.getDonante().getApellidos())")
    ConsentimientoResponse toResponse(Consentimiento consentimiento);

}