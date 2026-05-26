package co.sena.cimm.adso.Sistema_Donacion_Sangre.controller;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonacionRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonacionResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.DonacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
@Tag(name = "Donaciones", description = "Gestión de donaciones de sangre")
public class DonacionController {

    private final DonacionService donacionService;

    @PostMapping
    @Operation(summary = "Registrar una nueva donación")
    public ResponseEntity<DonacionResponse> registrar(@Valid @RequestBody DonacionRequest request) {
        log.info("POST /api/donaciones - Registrando donación para donante id: {}", request.getDonanteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(donacionService.registrar(request));
    }

    @GetMapping
    @Operation(summary = "Listar todas las donaciones con paginación")
    public ResponseEntity<Page<DonacionResponse>> listarTodas(
            @PageableDefault(size = 10, sort = "fechaDonacion") Pageable pageable) {
        log.info("GET /api/donaciones - Consultando donaciones paginadas");
        return ResponseEntity.ok(donacionService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar donación por ID")
    public ResponseEntity<DonacionResponse> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/donaciones/{} - Buscando donación", id);
        return ResponseEntity.ok(donacionService.buscarPorId(id));
    }

    @GetMapping("/donante/{donanteId}")
    @Operation(summary = "Listar donaciones de un donante específico")
    public ResponseEntity<List<DonacionResponse>> listarPorDonante(@PathVariable Long donanteId) {
        log.info("GET /api/donaciones/donante/{} - Consultando historial", donanteId);
        return ResponseEntity.ok(donacionService.listarPorDonante(donanteId));
    }

    @GetMapping("/donante/{donanteId}/historial-pdf")
    @Operation(summary = "Exportar historial de donaciones de un donante en PDF")
    public void exportarHistorialPdf(@PathVariable Long donanteId,
                                     HttpServletResponse response) throws IOException {
        log.info("GET /api/donaciones/donante/{}/historial-pdf - Exportando PDF", donanteId);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=historial-donante-" + donanteId + ".pdf");
        donacionService.exportarHistorialPdf(donanteId, response.getOutputStream());
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todas las donaciones del historial (Sin paginar)")
    public ResponseEntity<List<DonacionResponse>> listarTodos() {
        log.info("GET /api/donaciones/todos - Consultando lista completa de donaciones");
        return ResponseEntity.ok(donacionService.listarTodos());
    }

    @GetMapping("/reporte-pdf")
    @Operation(summary = "Exportar el historial completo de donaciones en PDF")
    public void exportarTodosPdf(HttpServletResponse response) throws IOException {
        log.info("GET /api/donaciones/reporte-pdf - Exportando PDF de historial completo");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=historial-completo-donaciones.pdf");
        donacionService.exportarTodosPdf(response.getOutputStream());
    }
}