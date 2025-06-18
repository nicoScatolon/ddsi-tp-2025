package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Contribuyente {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Boolean esAnonimo;

    public String getNombreDisplay(){
        if (esAnonimo) {return "Anonimo";}
        else return String.join(nombre, " ", apellido);
    }
}
