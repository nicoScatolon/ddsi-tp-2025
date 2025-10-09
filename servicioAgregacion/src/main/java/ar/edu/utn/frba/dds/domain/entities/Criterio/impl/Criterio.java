package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//Tuve que sacar los final de todos los atributos de los criterios porque sino JPA no permite inicializar correctamente las entidades (necesita que haya un
// noArgConstructor y no se puede hacer eso si tengo un atributo en final) -> Pierdo inmutabilidad pero no debería ser problema porque nadie va a tener acceso
// para editar los criterios de una

@Setter
@Getter
@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
public abstract class Criterio implements ICriterio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // algo común, si aplica

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccion_id", nullable = false)
    private Coleccion coleccion;
}
