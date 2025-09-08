package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;


public enum TipoAlgoritmoConsenso {
    ABSOLUTO {
        public AlgoritmoConsenso obtenerConsenso(){
            return new ConsensoAbsoluto();
        }
    },
    MAYORIASIMPLE {
        public AlgoritmoConsenso obtenerConsenso(){
            return new ConsensoMayoriaSimple();
        }
    },
    MULTIPLEMENCION {
        public AlgoritmoConsenso obtenerConsenso(){
            return new ConsensoMultipleMencion();
        }
    };

    public abstract AlgoritmoConsenso obtenerConsenso();
}
