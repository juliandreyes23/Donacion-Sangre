package co.sena.cimm.adso.Sistema_Donacion_Sangre.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
