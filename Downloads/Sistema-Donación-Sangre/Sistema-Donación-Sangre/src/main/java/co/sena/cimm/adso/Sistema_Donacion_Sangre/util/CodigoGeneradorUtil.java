package co.sena.cimm.adso.Sistema_Donacion_Sangre.util;

import java.util.UUID;

public class CodigoGeneradorUtil {

    private CodigoGeneradorUtil() {}

    public static String generarCodigoDonacion() {
        return "DON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}