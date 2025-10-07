package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;


public interface IFuente {
    Long getId();
    void setId(Long id);
    TipoFuenteProxy getTipo();
    void setNombre(String nombre);
    String getBaseUrl();

}
