package ucr.ac.cr.c5h060.medpharmexpress.service;

import ucr.ac.cr.c5h060.medpharmexpress.dto.*;
import ucr.ac.cr.c5h060.medpharmexpress.exception.*;
import ucr.ac.cr.c5h060.medpharmexpress.domain.*;
import ucr.ac.cr.c5h060.medpharmexpress.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecetaService {

    private final RecetaMedicaRepository recetaRepo;
    private final MedicamentoRepository medicamentoRepo;
    private final UsuarioRepository usuarioRepo;

    public RecetaService(RecetaMedicaRepository recetaRepo,
            MedicamentoRepository medicamentoRepo,
            UsuarioRepository usuarioRepo) {
        this.recetaRepo = recetaRepo;
        this.medicamentoRepo = medicamentoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> listar() {
        return recetaRepo.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> listarPorEstado(String estado) {
        return recetaRepo.findByEstado(estado.toUpperCase()).stream().map(this::toDTO).toList();
    }

    @Transactional
    public RecetaResponseDTO crear(RecetaRequestDTO req) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario medico = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + username));

        RecetaMedica receta = new RecetaMedica();
        receta.setCodigoReceta("REC-" + Year.now().getValue() + "-"
                + String.format("%03d", recetaRepo.count() + 1));
        receta.setPacienteNombre(req.pacienteNombre());
        receta.setMedico(medico);
        receta.setEstado("PENDIENTE");
        receta.setFechaEmision(LocalDateTime.now());

        List<DetalleReceta> detalles = new ArrayList<>();
        for (DetalleRequestDTO d : req.detalles()) {
            Medicamento med = medicamentoRepo.findById(d.medicamentoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Medicamento no encontrado: " + d.medicamentoId()));

            if (med.getStock() < d.cantidad()) {
                throw new StockInsuficienteException("Stock insuficiente de " + med.getNombre()
                        + ". Disponible: " + med.getStock() + ", solicitado: " + d.cantidad());
            }

            DetalleReceta det = new DetalleReceta();
            det.setReceta(receta);
            det.setMedicamento(med);
            det.setCantidad(d.cantidad());
            det.setDosisIndicada(d.dosisIndicada());
            detalles.add(det);
        }
        receta.setDetalles(detalles);

        return toDTO(recetaRepo.save(receta));
    }

    @Transactional
    public RecetaResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        RecetaMedica receta = recetaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Receta no encontrada: " + id));

        if (!"PENDIENTE".equals(receta.getEstado())) {
            throw new EstadoInvalidoException(
                    "Solo se puede cambiar el estado de recetas PENDIENTE. Estado actual: "
                            + receta.getEstado());
        }

        if ("DESPACHADA".equals(nuevoEstado)) {
            for (DetalleReceta det : receta.getDetalles()) {
                Medicamento med = det.getMedicamento();
                if (med.getStock() < det.getCantidad()) {
                    throw new StockInsuficienteException("Stock insuficiente de " + med.getNombre()
                            + " para despachar. Disponible: " + med.getStock());
                }
                med.setStock(med.getStock() - det.getCantidad());
                medicamentoRepo.save(med);
            }
        }

        receta.setEstado(nuevoEstado);
        return toDTO(recetaRepo.save(receta));
    }

    private RecetaResponseDTO toDTO(RecetaMedica r) {
        List<DetalleResponseDTO> detalles = r.getDetalles().stream()
                .map(d -> new DetalleResponseDTO(d.getMedicamento().getId(),
                        d.getMedicamento().getNombre(), d.getCantidad(), d.getDosisIndicada()))
                .toList();
        return new RecetaResponseDTO(r.getId(), r.getCodigoReceta(), r.getPacienteNombre(),
                r.getMedico().getNombreCompleto(), r.getEstado(), r.getFechaEmision(), detalles);
    }
}