package co.sena.cimm.adso.Sistema_Donacion_Sangre.domain;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.DonanteNoAptoException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidacionEdad implements ValidacionDonante {

    private static final int EDAD_MINIMA = 18;

    @Override
    public void validar(Donante donante) {
        int edad = Period.between(donante.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA) {
            throw new DonanteNoAptoException(
                    "El donante debe tener al menos " + EDAD_MINIMA + " años. Edad actual: " + edad);
        }
    }
}