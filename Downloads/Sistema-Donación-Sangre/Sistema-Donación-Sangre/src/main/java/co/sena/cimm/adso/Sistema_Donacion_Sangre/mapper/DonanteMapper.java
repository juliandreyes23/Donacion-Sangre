package co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonanteRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonanteResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.stereotype.Component;

@Component
public class DonanteMapper {
    public Donante toEntity(DonanteRequest request) {
        return Donante.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .documento(request.getDocumento())
                .fechaNacimiento(request.getFechaNacimiento())
                .tiposangre(request.getTipoSangre())
                .peso(request.getPeso())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .direccion(request.getDireccion())
                .fechaUltimaDonacion(request.getFechaUltimaDonacion())
                .aceptaConsentimiento(request.getAceptaConsentimiento())
                .activo(true)
                .build();
    }

    public DonanteResponse toResponse(Donante donante) {
        return DonanteResponse.builder()
                .id(donante.getId())
                .nombres(donante.getNombres())
                .apellidos(donante.getApellidos())
                .documento(donante.getDocumento())
                .fechaNacimiento(donante.getFechaNacimiento())
                .tipoSangre(donante.getTiposangre())
                .peso(donante.getPeso())
                .telefono(donante.getTelefono())
                .correo(donante.getCorreo())
                .direccion(donante.getDireccion())
                .fechaUltimaDonacion(donante.getFechaUltimaDonacion())
                .aceptaConsentimiento(donante.getAceptaConsentimiento())
                .activo(donante.getActivo())
                .build();
    }

    public void actualizarEntidad(Donante donante, DonanteRequest request) {
        donante.setNombres(request.getNombres());
        donante.setApellidos(request.getApellidos());
        donante.setDocumento(request.getDocumento());
        donante.setFechaNacimiento(request.getFechaNacimiento());
        donante.setTiposangre(request.getTipoSangre());
        donante.setPeso(request.getPeso());
        donante.setTelefono(request.getTelefono());
        donante.setCorreo(request.getCorreo());
        donante.setDireccion(request.getDireccion());
        donante.setAceptaConsentimiento(request.getAceptaConsentimiento());
    }
}
