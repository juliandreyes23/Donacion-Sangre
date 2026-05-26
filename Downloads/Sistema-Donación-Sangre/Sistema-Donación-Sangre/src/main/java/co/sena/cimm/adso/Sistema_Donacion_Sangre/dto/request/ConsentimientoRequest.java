package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimientoRequest {

    @NotNull(message = "El donante es obligatorio")
    private Long donanteId;

    @AssertTrue(message = "El donante debe aceptar el consentimiento informado")
    private boolean aceptaConsentimiento;

    @NotBlank(message = "La firma es obligatoria")
    private String firmaConsentimiento;

    @NotNull(message = "La fecha de la firma es obligatoria")
    @PastOrPresent(message = "La fecha de firma no puede ser futura")
    private LocalDate fechaFirma;
}