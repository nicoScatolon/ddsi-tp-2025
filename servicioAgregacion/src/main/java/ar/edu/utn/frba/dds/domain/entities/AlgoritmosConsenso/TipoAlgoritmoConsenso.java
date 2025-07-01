package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;


public enum TipoAlgoritmoConsenso {
    ABSOLUTO {
        public IAlgoritmoConsenso obtenerConsenso(){
            return new ConsensoAbsoluto();
        }
    },
    MAYORIASIMPLE {
        public IAlgoritmoConsenso obtenerConsenso(){
            return new ConsensoMayoriaSimple();
        }
    },
    MULTIPLEMENCION {
        public IAlgoritmoConsenso obtenerConsenso(){
            return new ConsensoMultipleMencion();
        }
    };

    public abstract IAlgoritmoConsenso obtenerConsenso();
}
