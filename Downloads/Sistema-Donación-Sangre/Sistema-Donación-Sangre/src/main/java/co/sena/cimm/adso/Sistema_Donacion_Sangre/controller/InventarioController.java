package co.sena.cimm.adso.Sistema_Donacion_Sangre.controller;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.InventarioResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Consulta de inventario de sangre disponible")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Consultar inventario completo de todos los tipos de sangre")
    public ResponseEntity<List<InventarioResponse>> listarTodo() {
        log.info("GET /api/inventario - Consultando inventario completo");
        return ResponseEntity.ok(inventarioService.listarTodo());
    }

    @GetMapping("/{tipoSangre}")
    @Operation(summary = "Consultar inventario por tipo de sangre")
    public ResponseEntity<InventarioResponse> obtenerPorTipoSangre(@PathVariable TipoSangre tipoSangre) {
        log.info("GET /api/inventario/{} - Consultando inventario", tipoSangre);
        return ResponseEntity.ok(inventarioService.obtenerInventarioPorTipoSangre(tipoSangre));
    }
}