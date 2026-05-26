package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimientoResponse {

    private Long id;
    private Long donanteId;
    private String nombreDonante;
    private boolean aceptaConsentimiento;
    private LocalDateTime fechaFirma;

    private String firmaConsentimiento;
}