package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteDinamicaAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteEstaticaAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteProxyAdapter;

public enum TipoFuente {
    ESTATICA {
        public IFuente crearFuente(String url){
            return new FuenteEstatica(url);
        }
        public FuenteAdapter crearAdapter(){
            return new FuenteEstaticaAdapter();
        }
    },
    DINAMICA {
        public IFuente crearFuente(String url){
            return new FuenteDinamica(url);
        }
        public FuenteAdapter crearAdapter(){
            return new FuenteDinamicaAdapter();
        }
    },
    PROXY {
        public IFuente crearFuente(String url){
            return new FuenteProxy(url);
        }
        public FuenteAdapter crearAdapter(){
            return new FuenteProxyAdapter();
        }
    };

    public abstract IFuente crearFuente(String url);
    public abstract FuenteAdapter crearAdapter();
}
