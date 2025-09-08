package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_consenso")
public abstract class AlgoritmoConsenso implements IAlgoritmoConsenso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
