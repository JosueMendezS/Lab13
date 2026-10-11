package ucr.ac.cr.c5h060.medpharmexpress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EstadoRequestDTO(
        @NotBlank(message = "El estado es requerido") @Pattern(regexp = "DESPACHADA|CANCELADA", message = "El estado debe ser DESPACHADA o CANCELADA") String estado) {
}