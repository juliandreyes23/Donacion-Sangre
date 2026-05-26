package co.sena.cimm.adso.Sistema_Donacion_Sangre.controller;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.AuthRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.RegisterRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.AuthResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Usuario;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.UsuarioRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.AuthService;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login, registro y token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("POST /api/auth/login - usuario: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Buscar el rol real desde la BD
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

        String token = jwtUtil.generarToken(userDetails.getUsername());

        log.info("Login exitoso: {}", request.getUsername());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .rol(usuario.getRol().name())
                .build());
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario con rol")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register - username: {}, rol: {}",
                request.getUsername(), request.getRol());

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}