package ucr.ac.cr.c5h060.medpharmexpress.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RecetaResponseDTO(
        Long id, String codigoReceta, String pacienteNombre, String medicoNombre,
        String estado, LocalDateTime fechaEmision, List<DetalleResponseDTO> detalles) {
}