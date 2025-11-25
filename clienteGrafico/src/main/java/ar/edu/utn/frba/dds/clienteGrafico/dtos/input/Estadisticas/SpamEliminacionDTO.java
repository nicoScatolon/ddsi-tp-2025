package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpamEliminacionDTO {
    private Integer spam;   // solicitadas como spam
    private Integer total;  // spam + no spam
}