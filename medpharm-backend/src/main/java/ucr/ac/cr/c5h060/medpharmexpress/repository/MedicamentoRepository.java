package ucr.ac.cr.c5h060.medpharmexpress.repository;

import ucr.ac.cr.c5h060.medpharmexpress.domain.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
}