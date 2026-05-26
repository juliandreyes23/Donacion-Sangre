package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.InventarioResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;

import java.util.List;

public interface InventarioService {
    List<InventarioResponse> listarTodo();
    InventarioResponse obtenerInventarioPorTipoSangre(TipoSangre tipoSangre);
    void actualizarInventario(TipoSangre tipoSangre, Integer cantidadMl);
}
