package ucr.ac.cr.c5h060.medpharmexpress.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DetalleRequestDTO(
        @NotNull(message = "El medicamento es requerido") Long medicamentoId,
        @NotNull(message = "La cantidad es requerida") @Min(value = 1, message = "La cantidad debe ser mayor a 0") Integer cantidad,
        @NotBlank(message = "La dosis es requerida") String dosisIndicada) {
}