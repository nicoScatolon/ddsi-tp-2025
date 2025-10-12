package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IHechosRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
    Hecho getHechoById(Long id);
    List<Hecho> findHechoByDestacado(Boolean destacado);
    List<Hecho> findAllByFuente(Fuente fuente);

    @Query("SELECT new ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO(h.id, h.titulo, c.nombre, u.latitud, u.longitud) " +
            "FROM Hecho h " +
            "JOIN h.categoria c " +
            "JOIN h.ubicacion u " +
            "WHERE u.latitud IS NOT NULL AND u.longitud IS NOT NULL")
    List<HechoMapaOutputDTO> findAllMapaDTO();
}
