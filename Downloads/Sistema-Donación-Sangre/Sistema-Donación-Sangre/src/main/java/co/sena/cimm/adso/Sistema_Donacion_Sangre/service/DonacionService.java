package co.sena.cimm.adso.Sistema_Donacion_Sangre.service;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonacionRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonacionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface DonacionService {
    DonacionResponse registrar(DonacionRequest request);
    DonacionResponse buscarPorId(Long id);
    Page<DonacionResponse> listarTodas(Pageable pageable);
    List<DonacionResponse> listarPorDonante(Long donanteId);
    void exportarHistorialPdf(Long donanteId, OutputStream outputStream) throws IOException;

    List<DonacionResponse> listarTodos();
    void exportarTodosPdf(OutputStream outputStream) throws IOException;
}