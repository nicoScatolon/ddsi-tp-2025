package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteHechosExterna;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Service
public class FuenteHechosMetaMapa implements IFuenteHechosExterna {
    private final List<HechoExternoDTO> hechosMockeados;

  public FuenteHechosMetaMapa(){
      HechoExternoDTO hecho1 = HechoExternoDTO.builder()
              .id(1L)
              .titulo("Título 1")
              .descripcion("Descripción 1")
              .categoria("Incendio")
              .latitud(-34.6037)
              .longitud(-58.3816)
              .fechaDeOcurrencia("2024-12-10T14:30:00.000000Z")
              .fechaDeCarga("2024-12-11T09:00:00.000000Z")
              .build();


      HechoExternoDTO hecho2 = HechoExternoDTO.builder()
              .id(2L)
              .titulo("Título 2")
              .descripcion("Descripción 2")
              .categoria("Inundación")
              .latitud(-31.4201)
              .longitud(-64.1888)
              .fechaDeOcurrencia("2025-01-20T18:45:00.000000Z")
              .fechaDeCarga("2025-01-21T10:15:00.000000Z")
              .build();


      this.hechosMockeados = List.of(hecho1, hecho2);
  }

  @Override
  public Mono<List<HechoExternoDTO>> buscarTodos(){
      return Mono.just(this.hechosMockeados);
  }

  @Override
    public Mono<HechoExternoDTO> buscarPorId(Long id){
      return hechosMockeados.stream()
              .filter(hecho -> hecho.getId().equals(id))
              .findFirst()
              .map(Mono::just)
              .orElse(Mono.error(new RuntimeException("Hecho no encontrado con ID " + id)));
  }


  }

