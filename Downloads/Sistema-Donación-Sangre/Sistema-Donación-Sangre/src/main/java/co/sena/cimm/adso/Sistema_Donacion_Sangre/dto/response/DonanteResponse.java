package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonanteResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String documento;
    private LocalDate fechaNacimiento;
    private TipoSangre tipoSangre;
    private Double peso;
    private String telefono;
    private String correo;
    private String direccion;
    private LocalDate fechaUltimaDonacion;
    private Boolean aceptaConsentimiento;
    private Boolean activo;
}
