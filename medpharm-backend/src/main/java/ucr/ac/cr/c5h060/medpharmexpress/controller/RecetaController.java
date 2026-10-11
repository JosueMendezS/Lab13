package ucr.ac.cr.c5h060.medpharmexpress.controller;

import ucr.ac.cr.c5h060.medpharmexpress.dto.*;
import ucr.ac.cr.c5h060.medpharmexpress.service.RecetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recetas")
public class RecetaController {

    private final RecetaService service;

    public RecetaController(RecetaService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecetaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/estado/{estado}")
    public List<RecetaResponseDTO> porEstado(@PathVariable String estado) {
        return service.listarPorEstado(estado);
    }

    @PostMapping
    public ResponseEntity<RecetaResponseDTO> crear(@Valid @RequestBody RecetaRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PatchMapping("/{id}/estado")
    public RecetaResponseDTO cambiarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO req) {
        return service.cambiarEstado(id, req.estado());
    }
}