package co.sena.cimm.adso.Sistema_Donacion_Sangre.service.impl;

import co.sena.cimm.adso.Sistema_Donacion_Sangre.domain.ValidacionDonante;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.request.DonacionRequest;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.dto.response.DonacionResponse;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.enums.EstadoDonacion;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.exception.ResourceNotFoundException;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.mapper.DonacionMapper;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donacion;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.model.Donante;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.DonacionRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.repository.DonanteRepository;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.ConsentimientoService;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.DonacionService;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.service.InventarioService;
import co.sena.cimm.adso.Sistema_Donacion_Sangre.util.CodigoGeneradorUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;
    private final DonanteRepository donanteRepository;
    private final DonacionMapper donacionMapper;
    private final ConsentimientoService consentimientoService;
    private final InventarioService inventarioService;
    private final List<ValidacionDonante> validaciones;

    @Override
    public DonacionResponse registrar(DonacionRequest request) {
        log.info("Registrando nueva donación para donante id: {}", request.getDonanteId());

        Donante donante = buscarDonantePorId(request.getDonanteId());
        validarApitudDonante(donante);

        Donacion donacion = Donacion.builder()
                .donante(donante)
                .codigoDonacion(CodigoGeneradorUtil.generarCodigoDonacion())
                .cantidadMl(request.getCantidadMl())
                .fechaDonacion(LocalDateTime.now())
                .tipoSangre(donante.getTiposangre())
                .estado(EstadoDonacion.REGISTRADA)
                .observaciones(request.getObservaciones())
                .build();

        Donacion guardada = donacionRepository.save(donacion);

        donante.setFechaUltimaDonacion(LocalDate.now());
        donanteRepository.save(donante);

        inventarioService.actualizarInventario(donante.getTiposangre(), request.getCantidadMl());

        log.info("Donación registrada exitosamente con código: {}", guardada.getCodigoDonacion());
        return donacionMapper.toResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public DonacionResponse buscarPorId(Long id) {
        log.info("Buscando donación con id: {}", id);
        return donacionMapper.toResponse(buscarDonacionPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DonacionResponse> listarTodas(Pageable pageable) {
        log.info("Consultando todas las donaciones paginadas");
        return donacionRepository.findAll(pageable)
                .map(donacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonacionResponse> listarPorDonante(Long donanteId) {
        log.info("Consultando donaciones del donante id: {}", donanteId);
        if (!donanteRepository.existsById(donanteId)) {
            throw new ResourceNotFoundException("Donante no encontrado con id: " + donanteId);
        }
        return donacionRepository.findByDonanteId(donanteId)
                .stream()
                .map(donacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void exportarHistorialPdf(Long donanteId, OutputStream outputStream) throws IOException {
        log.info("Exportando historial PDF para donante id: {}", donanteId);

        Donante donante = buscarDonantePorId(donanteId);
        List<Donacion> donaciones = donacionRepository.findByDonanteId(donanteId);

        Document documento = new Document();
        PdfWriter.getInstance(documento, outputStream);
        documento.open();

        documento.add(new Paragraph("HISTORIAL DE DONACIONES"));
        documento.add(new Paragraph("Donante: " + donante.getNombres() + " " + donante.getApellidos()));
        documento.add(new Paragraph("Documento: " + donante.getDocumento()));
        documento.add(new Paragraph("Tipo de sangre: " + donante.getTiposangre().getCodigo()));
        documento.add(new Paragraph(" "));

        if (donaciones.isEmpty()) {
            documento.add(new Paragraph("No se encontraron donaciones registradas."));
        } else {
            for (Donacion d : donaciones) {
                documento.add(new Paragraph("------------------------------"));
                documento.add(new Paragraph("Código: " + d.getCodigoDonacion()));
                documento.add(new Paragraph("Fecha: " + d.getFechaDonacion()));
                documento.add(new Paragraph("Cantidad: " + d.getCantidadMl() + " ml"));
                documento.add(new Paragraph("Estado: " + d.getEstado()));
                if (d.getObservaciones() != null) {
                    documento.add(new Paragraph("Observaciones: " + d.getObservaciones()));
                }
            }
        }

        documento.close();
        log.info("PDF generado exitosamente para donante id: {}", donanteId);
    }


    private void validarApitudDonante(Donante donante) {
        validaciones.forEach(v -> v.validar(donante));
    }


    private Donante buscarDonantePorId(Long id) {
        return donanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donante no encontrado con id: " + id));
    }

    private Donacion buscarDonacionPorId(Long id) {
        return donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonacionResponse> listarTodos() {
        log.info("Consultando la lista completa de todas las donaciones realizadas");
        return donacionRepository.findAll()
                .stream()
                .map(donacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void exportarTodosPdf(OutputStream outputStream) throws IOException {
        log.info("Exportando PDF del historial global de donaciones");

        List<Donacion> donaciones = donacionRepository.findAll();

        Document documento = new Document();
        PdfWriter.getInstance(documento, outputStream);
        documento.open();

        documento.add(new Paragraph("SISTEMA DE DONACIÓN DE SANGRE - REPORTE HISTÓRICO GLOBAL"));
        documento.add(new Paragraph("Fecha de generación: " + LocalDateTime.now()));
        documento.add(new Paragraph("Total de registros encontrados: " + donaciones.size()));
        documento.add(new Paragraph(" "));

        if (donaciones.isEmpty()) {
            documento.add(new Paragraph("No se registran donaciones en la base de datos hasta la fecha."));
        } else {
            com.lowagie.text.pdf.PdfPTable tabla = new com.lowagie.text.pdf.PdfPTable(6);
            tabla.setWidthPercentage(100);

            tabla.addCell("Código");
            tabla.addCell("Donante");
            tabla.addCell("Tipo Sangre");
            tabla.addCell("Cantidad (ml)");
            tabla.addCell("Fecha");
            tabla.addCell("Estado");

            for (Donacion d : donaciones) {
                String nombreCompleto = d.getDonante() != null ?
                        d.getDonante().getNombres() + " " + d.getDonante().getApellidos() : "N/A";
                String tipoSangre = d.getTipoSangre() != null ? d.getTipoSangre().getCodigo() : "N/A";

                tabla.addCell(d.getCodigoDonacion());
                tabla.addCell(nombreCompleto);
                tabla.addCell(tipoSangre);
                tabla.addCell(d.getCantidadMl() + " ml");
                tabla.addCell(d.getFechaDonacion().toLocalDate().toString());
                tabla.addCell(d.getEstado().toString());
            }

            documento.add(tabla);
        }

        documento.close();
        log.info("PDF global generado exitosamente con {} registros", donaciones.size());
    }
}