package ucr.ac.cr.c5h060.medpharmexpress.dto;

public record DetalleResponseDTO(
        Long medicamentoId, String medicamentoNombre, Integer cantidad, String dosisIndicada) {
}