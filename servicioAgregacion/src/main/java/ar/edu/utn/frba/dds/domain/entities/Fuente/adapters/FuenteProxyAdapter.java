package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteProxy;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FuenteProxyAdapter implements FuenteAdapter {
    private FuenteProxy fuenteProxy;

    @Value("${hechos.proxy.actualizacion.duracion}")
    private Duration duracionActualizacion;

    public LocalDateTime calcularProximaActualizacion(LocalDateTime ultimaActualizacion) {
        return ultimaActualizacion.plus(duracionActualizacion);
    }

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteProxy) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteProxy = (FuenteProxy) fuente;
    }

    @Override
    public List<Hecho> actualizarHechos() {
        //proxy no se actualiza por peticion, lo gestiona de forma propia
        return null;
    }

    @Override
    public List<Hecho> obtenerHechos() {
        LocalDateTime ultimafecha = fuenteProxy.getUltimaActualizacion();
        if( this.verificarActualizarPorTiempo(ultimafecha)  || fuenteProxy.getHechos() == null ) {
            fuenteProxy.updateHechos();
        }
        return fuenteProxy.getHechos();
    }

    private Boolean verificarActualizarPorTiempo(LocalDateTime ultimafecha) {
        return ultimafecha.plus(duracionActualizacion).isBefore( LocalDateTime.now() );
    }
}
