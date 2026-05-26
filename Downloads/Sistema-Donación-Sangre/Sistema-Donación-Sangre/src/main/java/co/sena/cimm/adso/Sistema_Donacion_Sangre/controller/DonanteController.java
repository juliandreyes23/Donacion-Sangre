package co.sena.cimm.adso.Sistema_Donacion_Sangre.controller;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonanteRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonanteResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.DonanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/donantes")
@RequiredArgsConstructor
@Tag(name = "Donantes", description = "Gestión de donantes de sangre")
public class DonanteController {

    private final DonanteService donanteService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo donante")
    public ResponseEntity<DonanteResponse> registrar(@Valid @RequestBody DonanteRequest request) {
        log.info("POST /api/donantes - Registrando donante con documento: {}", request.getDocumento());
        return ResponseEntity.status(HttpStatus.CREATED).body(donanteService.registrar(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los donantes con paginación")
    public ResponseEntity<Page<DonanteResponse>> listarTodos(
            @PageableDefault(size = 10, sort = "nombres") Pageable pageable) {
        log.info("GET /api/donantes - Consultando donantes paginados");
        return ResponseEntity.ok(donanteService.listarTodos(pageable));
    }

    @GetMapping("/tipo-sangre/{tipoSangre}")
    @Operation(summary = "Filtrar donantes por tipo de sangre")
    public ResponseEntity<List<DonanteResponse>> listarPorTipoSangre(@PathVariable TipoSangre tipoSangre) {
        log.info("GET /api/donantes/tipo-sangre/{} - Filtrando donantes", tipoSangre);
        return ResponseEntity.ok(donanteService.listarPorTipoSangre(tipoSangre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar donante por ID")
    public ResponseEntity<DonanteResponse> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/donantes/{} - Buscando donante", id);
        return ResponseEntity.ok(donanteService.buscarPorId(id));
    }

    @GetMapping("/documento/{documento}")
    @Operation(summary = "Buscar donante por número de documento")
    public ResponseEntity<DonanteResponse> buscarPorDocumento(@PathVariable String documento) {
        log.info("GET /api/donantes/documento/{} - Buscando donante", documento);
        return ResponseEntity.ok(donanteService.buscarPorDocumento(documento));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un donante")
    public ResponseEntity<DonanteResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody DonanteRequest request) {
        log.info("PUT /api/donantes/{} - Actualizando donante", id);
        return ResponseEntity.ok(donanteService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar un donante")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/donantes/{} - Desactivando donante", id);
        donanteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}