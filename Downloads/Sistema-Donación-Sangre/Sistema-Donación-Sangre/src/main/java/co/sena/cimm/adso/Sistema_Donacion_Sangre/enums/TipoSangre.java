package co.sena.cimm.adso.Sistema_Donacion_Sangre.enums;

public enum TipoSangre {
    O_POSITIVO("O+"),
    O_NEGATIVO("O-"),
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-");

    private final String codigo;

    TipoSangre(String codigo){this.codigo = codigo;}
    public String getCodigo() {return codigo;}
}
