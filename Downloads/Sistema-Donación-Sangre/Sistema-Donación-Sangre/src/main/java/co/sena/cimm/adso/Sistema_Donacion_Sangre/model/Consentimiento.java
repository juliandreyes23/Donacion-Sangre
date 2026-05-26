package co.sena.cimm.adso.Sistema_Donacion_Sangre.model;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.domain.Auditable;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "consents")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consentimiento extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donante_id", nullable = false, unique = true)
    private Donante donante;

    @Column(name = "acepta_consentimiento", nullable = false)
    private boolean aceptaConsentimiento;

    @Column(name = "firma_consentimiento", nullable = false, columnDefinition = "TEXT")
    private String firmaConsentimiento;

    @Column(name = "fecha_firma", nullable = false)
    private LocalDate fechaFirma;
}