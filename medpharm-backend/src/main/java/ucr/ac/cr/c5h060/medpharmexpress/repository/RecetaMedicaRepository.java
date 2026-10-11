package ucr.ac.cr.c5h060.medpharmexpress.repository;

import ucr.ac.cr.c5h060.medpharmexpress.domain.RecetaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long> {
    List<RecetaMedica> findByEstado(String estado);
}