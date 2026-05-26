package co.sena.cimm.adso.Sistema_Donacion_Sangre.controller;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.ConsentimientoRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.ConsentimientoResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.ConsentimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/consentimientos")
@RequiredArgsConstructor
@Tag(name = "Consentimientos", description = "Gestión de firmas de consentimiento")
public class ConsentimientoController {

    private final ConsentimientoService consentimientoService;

    @PostMapping
    @Operation(summary = "Registrar nuevo consentimiento")
    public ResponseEntity<ConsentimientoResponse> registrarConsentimiento(@Valid @RequestBody ConsentimientoRequest request) {
        ConsentimientoResponse response = consentimientoService.registrarConsentimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{donanteId}")
    @Operation(summary = "Consultar consentimiento de un donante")
    public ResponseEntity<ConsentimientoResponse> buscarPorDonante(@PathVariable Long donanteId) {
        log.info("GET /api/consentimientos/{} - Consultando consentimiento", donanteId);
        return ResponseEntity.ok(consentimientoService.buscarPorDonante(donanteId));
    }
}