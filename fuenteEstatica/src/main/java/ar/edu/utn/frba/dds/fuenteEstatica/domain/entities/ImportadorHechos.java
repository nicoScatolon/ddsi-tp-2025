package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import java.util.List;

public interface ImportadorHechos {
    public List<Hecho> importarHechosArchivo(String path);
    public String getFormato();
}
