package cl.duoc.miregistro.siniestros.repository;

import cl.duoc.miregistro.siniestros.model.Siniestro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiniestroRepository extends JpaRepository<Siniestro, Long> {
    boolean existsByNumero(String numero);
    Optional<Siniestro> findByNumero(String numero);
    List<Siniestro> findByUsuarioId(Long usuarioId);
    List<Siniestro> findByLiquidadorId(Long liquidadorId);
}