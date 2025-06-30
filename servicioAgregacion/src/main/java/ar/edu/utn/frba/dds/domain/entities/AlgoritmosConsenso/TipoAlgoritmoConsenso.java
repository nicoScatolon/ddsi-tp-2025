package ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso;


public enum TipoAlgoritmoConsenso {
    ABSOLUTO {
        public IAlgoritmoConsenso obtenerConsenso(String url){
            return new ConsensoAbsoluto();
        }
    },
    MAYORIASIMPLE {
        public IAlgoritmoConsenso obtenerConsenso(String url){
            return new ConsensoMayoriaSimple();
        }
    },
    MULTIPLEMENCION {
        public IAlgoritmoConsenso obtenerConsenso(String url){
            return new ConsensoMultipleMencion();
        }
    };
}
