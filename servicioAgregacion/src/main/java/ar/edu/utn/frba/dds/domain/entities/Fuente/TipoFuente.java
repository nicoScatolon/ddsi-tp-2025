package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteDinamicaAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteEstaticaAdapter;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteProxyAdapter;
import org.apache.coyote.Adapter;

public enum TipoFuente {
    ESTATICA {
        public IFuente crearFuente(String url){
            return new FuenteEstatica(url);
        }
        public FuenteAdapter crearAdapter(IFuente fuente){
            FuenteAdapter adapter = new FuenteEstaticaAdapter();
            adapter.setFuente(fuente);
            return adapter;
        }
    },
    DINAMICA {
        public IFuente crearFuente(String url){
            return new FuenteDinamica(url);
        }
        public FuenteAdapter crearAdapter(IFuente fuente){
            FuenteAdapter adapter = new FuenteDinamicaAdapter();
            adapter.setFuente(fuente);
            return adapter;
        }
    },
    PROXY {
        public IFuente crearFuente(String url){ return new FuenteProxy(url); }
        public FuenteAdapter crearAdapter(IFuente fuente){
            FuenteAdapter adapter = new FuenteProxyAdapter();
            adapter.setFuente(fuente);
            return adapter;
        }
    };

    public abstract IFuente crearFuente(String url);
    public abstract FuenteAdapter crearAdapter(IFuente fuente);
}
