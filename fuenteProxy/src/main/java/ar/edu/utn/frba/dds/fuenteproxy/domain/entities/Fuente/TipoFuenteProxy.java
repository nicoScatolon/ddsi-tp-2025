package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.AdapterFuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.AdapterFuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.IFuenteAdapter;

public enum TipoFuenteProxy {

    EXTERNADDS{
        public IFuente crearFuente(String baseUrl, String token){return new FuenteDDS(baseUrl, token);}
        public IFuenteAdapter crearAdapter(){return new AdapterFuenteDDS();}
    },


    METAMAPA{
        public IFuente crearFuente(String baseUrl){return new FuenteMetaMapa(baseUrl);}
        public IFuenteAdapter crearAdapter(){return new AdapterFuenteMetaMapa();}
    },
}
