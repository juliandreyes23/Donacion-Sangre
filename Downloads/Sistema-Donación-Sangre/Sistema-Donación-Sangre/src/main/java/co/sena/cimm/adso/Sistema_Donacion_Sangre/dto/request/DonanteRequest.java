package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonanteRequest {

    @NotBlank(message = "El nombres es obligatorio")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El tipo de sangre es obligatorio")
    private TipoSangre tipoSangre;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe se mayor a 0")
    private Double peso;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    private String correo;

    private String direccion;

    private LocalDate fechaUltimaDonacion;

    @NotNull( message = "Debe indicar si acepta el consentimiento")
    private Boolean aceptaConsentimiento;
}
