package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.EstadoDonacion;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonacionResponse {

    private Long id;
    private String codigoDonacion;
    private Long donanteId;
    private String nombreDonante;
    private Integer cantidadMl;
    private LocalDateTime fechaDonacion;
    private TipoSangre tipoSangre;
    private EstadoDonacion estado;
    private String observaciones;
}
