package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponse {
    private Long id;
    private TipoSangre tipoSangre;
    private Integer cantidadDisponibleMl;
    private LocalDateTime ultimaActualizacion;
}
