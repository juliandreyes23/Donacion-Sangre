package co.sena.cimm.adso.Sistema_Donacion_Sangre.domain;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.DonanteNoAptoException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import org.springframework.stereotype.Component;

@Component
public class ValidacionConsentimiento implements ValidacionDonante {

    @Override
    public void validar(Donante donante) {
        if (Boolean.FALSE.equals(donante.getAceptaConsentimiento())) {
            throw new DonanteNoAptoException("El donante no ha aceptado el consentimiento informado");
        }
    }
}