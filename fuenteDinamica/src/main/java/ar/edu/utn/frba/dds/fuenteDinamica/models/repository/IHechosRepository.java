package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    //agregar find all by estado = Aceptado
}
