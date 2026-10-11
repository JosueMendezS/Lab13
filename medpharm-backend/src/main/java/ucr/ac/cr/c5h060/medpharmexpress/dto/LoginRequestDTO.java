package ucr.ac.cr.c5h060.medpharmexpress.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El username es requerido") String username,
        @NotBlank(message = "La contraseña es requerida") String password) {
}