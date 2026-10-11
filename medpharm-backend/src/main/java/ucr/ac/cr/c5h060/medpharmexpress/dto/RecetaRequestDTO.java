package ucr.ac.cr.c5h060.medpharmexpress.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RecetaRequestDTO(
        @NotBlank(message = "El nombre del paciente es requerido") @Size(min = 5, message = "El paciente debe tener al menos 5 caracteres") String pacienteNombre,

        @NotEmpty(message = "Debe incluir al menos un medicamento") List<@Valid DetalleRequestDTO> detalles) {
}