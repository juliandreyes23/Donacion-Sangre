package co.sena.cimm.adso.Sistema_Donacion_Sangre.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

}
