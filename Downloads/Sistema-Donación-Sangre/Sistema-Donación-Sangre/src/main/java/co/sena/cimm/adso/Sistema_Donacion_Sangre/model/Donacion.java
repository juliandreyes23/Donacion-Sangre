package co.sena.cimm.adso.Sistema_Donacion_Sangre.model;


import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.EstadoDonacion;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "codigo_donacion",nullable = false,unique = true ,length = 30)
    private String codigoDonacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donante_id",nullable = false)
    private Donante donante;

    @Column(name = "cantidad_ml",nullable = false)
    private Integer cantidadMl;

    @Column(name= "fecha_donacion",nullable = false)
    private LocalDateTime fechaDonacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", nullable = false,length = 5)
    private TipoSangre tipoSangre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDonacion estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
