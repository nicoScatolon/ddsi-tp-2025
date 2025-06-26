package ar.edu.utn.frba.dds.domain.dtos.input.APIs.ApiPublica;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SolicitudEliminacionInputDTO {
    private String razon;
    private String nombre;
    private String apellido;
}
