package ucr.ac.cr.c5h060.medpharmexpress.controller;

import ucr.ac.cr.c5h060.medpharmexpress.dto.MedicamentoDTO;
import ucr.ac.cr.c5h060.medpharmexpress.repository.MedicamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicamentos")
public class MedicamentoController {

    private final MedicamentoRepository repo;

    public MedicamentoController(MedicamentoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<MedicamentoDTO> listar() {
        return repo.findAll().stream()
                .map(m -> new MedicamentoDTO(m.getId(), m.getCodigo(), m.getNombre(),
                        m.getStock(), m.getPrecioUnitario()))
                .toList();
    }
}