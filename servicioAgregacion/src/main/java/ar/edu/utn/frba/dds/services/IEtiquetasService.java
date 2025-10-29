package ar.edu.utn.frba.dds.services;


import ar.edu.utn.frba.dds.domain.entities.Etiqueta;

import java.util.List;

public interface IEtiquetasService {
    Etiqueta crearEtiqueta(String nombreEtiqueta);
    Etiqueta verificarEtiqueta(String nombreEtiqueta);
    List<Etiqueta> findAll();

    List<String> findAllNombres();
    //TODO La idea seria crear un controller especial que permita agregar etiquetas sin tener que vincularlas a un hecho y tambien eliminarlas
}
