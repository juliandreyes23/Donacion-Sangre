package co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String rol;
}