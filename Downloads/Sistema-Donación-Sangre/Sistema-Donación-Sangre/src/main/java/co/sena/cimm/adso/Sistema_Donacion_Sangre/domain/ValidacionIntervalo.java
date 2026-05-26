package co.sena.cimm.adso.Sistema_Donacion_Sangre.domain;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.DonanteNoAptoException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidacionIntervalo implements ValidacionDonante {

    private static final int MESES_ENTRE_DONACIONES = 3;

    @Override
    public void validar(Donante donante) {
        if (donante.getFechaUltimaDonacion() == null) return;

        LocalDate minimaFechaPermitida = donante.getFechaUltimaDonacion().plusMonths(MESES_ENTRE_DONACIONES);
        if (LocalDate.now().isBefore(minimaFechaPermitida)) {
            throw new DonanteNoAptoException(
                    "Deben pasar al menos " + MESES_ENTRE_DONACIONES + " meses desde la última donación. "
                            + "Próxima fecha permitida: " + minimaFechaPermitida);
        }
    }
}