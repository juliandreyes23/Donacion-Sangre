package co.sena.cimm.adso.Sistema_Donacion_Sangre.model;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "donors")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false,unique = true,length =  20)
    private String documento;

    @Column(name = "fecha_nacimiento",nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", nullable = false,length = 5)
    private TipoSangre tiposangre;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false,length = 15)
    private String telefono;

    @Column(nullable = false,unique = true,length = 150)
    private String correo;

    @Column(length = 200)
    private String direccion;

    @Column(name = "fecha_ultima_donacion")
    private LocalDate fechaUltimaDonacion;

    @Column(name = "acepta_consentimiento",nullable = false)
    private Boolean aceptaConsentimiento = false;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
