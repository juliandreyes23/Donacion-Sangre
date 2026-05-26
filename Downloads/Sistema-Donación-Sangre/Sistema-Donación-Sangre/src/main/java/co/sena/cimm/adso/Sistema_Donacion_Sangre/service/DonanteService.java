package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonanteRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonanteResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.TipoSangre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonanteService {
    DonanteResponse registrar(DonanteRequest request);
    DonanteResponse buscarPorId(Long id);
    DonanteResponse buscarPorDocumento(String documento);
    Page<DonanteResponse> listarTodos(Pageable pageable);
    List<DonanteResponse> listarPorTipoSangre(TipoSangre tipoSangre);
    DonanteResponse actualizar(Long id, DonanteRequest request);
    void eliminar(Long id);
}