package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;

public interface IHechosService {
    Hecho cargarHecho(Hecho hecho);
    Hecho modificarHecho(Hecho hecho);
    Hecho revisarHecho(Hecho hecho);
}
