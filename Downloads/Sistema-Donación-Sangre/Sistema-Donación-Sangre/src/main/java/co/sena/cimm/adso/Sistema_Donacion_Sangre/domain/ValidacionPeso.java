package co.sena.cimm.adso.Sistema_Donacion_Sangre.domain;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.DonanteNoAptoException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.stereotype.Component;

@Component
public class ValidacionPeso implements ValidacionDonante {

    private static final double PESO_MINIMO_KG = 50.0;

    @Override
    public void validar(Donante donante) {
        if (donante.getPeso() < PESO_MINIMO_KG) {
            throw new DonanteNoAptoException(
                    "El donante debe pesar al menos " + PESO_MINIMO_KG + " kg. Peso actual: " + donante.getPeso() + " kg");
        }
    }
}