package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.dtos.output.E_HoraOcuPorCategoriaOutputDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IE_HoraOcuPorCategoriaRepository extends JpaRepository<E_HoraOcuPorCategoriaOutputDTO, Long> {
}
