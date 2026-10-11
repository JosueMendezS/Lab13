package ucr.ac.cr.c5h060.medpharmexpress.dto;

import java.math.BigDecimal;

public record MedicamentoDTO(
        Long id, String codigo, String nombre, Integer stock, BigDecimal precioUnitario) {
}