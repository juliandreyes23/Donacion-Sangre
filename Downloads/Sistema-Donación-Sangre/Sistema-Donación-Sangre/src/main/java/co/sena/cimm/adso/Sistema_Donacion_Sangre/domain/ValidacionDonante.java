package co.sena.cimm.adso.Sistema_Donacion_Sangre.domain;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;

public interface ValidacionDonante {
    void validar(Donante donante);
}