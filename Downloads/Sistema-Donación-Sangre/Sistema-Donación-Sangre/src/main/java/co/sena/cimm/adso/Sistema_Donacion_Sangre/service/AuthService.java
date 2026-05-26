package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.RegisterRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.AuthResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Usuario;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.UsuarioRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());

        usuarioRepository.save(usuario);

        String token = jwtUtil.generarToken(usuario.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .build();
    }
}