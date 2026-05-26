package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonacionRequest {
    @NotNull(message = "El donante es obligatorio")
    private Long donanteId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidadMl;

    private String observaciones;
}
