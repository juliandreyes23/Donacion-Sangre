package co.sena.cimm.adso.Sistema_Donacion_Sangre.model;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_inventory")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioSangre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre",nullable = false,unique = true,length = 5)
    private TipoSangre tipoSangre;

    @Column(name = "cantidad_disponible_ml", nullable = false)
    private Integer cantidadDisponibleMl;

    @Column(name = "ultima_actualizacion",nullable = false)
    private LocalDateTime ultimaActualizacion;
}
