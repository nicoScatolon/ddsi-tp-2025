package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteHechosExterna;
import reactor.core.publisher.Mono;

import java.util.List;

public class FuenteHechosMetaMapa implements IFuenteHechosExterna {
    private final List<HechoExternoDTO> hechosMockeados;

  public FuenteHechosMetaMapa(){
      HechoExternoDTO hecho1 = new HechoExternoDTO();
      hecho1.setId(1L);
      hecho1.setTitulo("Título 1");
      hecho1.setDescripcion("Descripción 1");
      hecho1.setCategoria("Incendio");
      hecho1.setLatitud(-34.6037);
      hecho1.setLongitud(-58.3816);
      hecho1.setFechaDeOcurrencia("2024-12-10T14:30:00.000000Z");
      hecho1.setFechaDeCarga("2024-12-11T09:00:00.000000Z");

      HechoExternoDTO hecho2 = new HechoExternoDTO();
      hecho2.setId(2L);
      hecho2.setTitulo("Título 2");
      hecho2.setDescripcion("Descripción 2");
      hecho2.setCategoria("Inundación");
      hecho2.setLatitud(-31.4201);
      hecho2.setLongitud(-64.1888);
      hecho2.setFechaDeOcurrencia("2025-01-20T18:45:00.000000Z");
      hecho2.setFechaDeCarga("2025-01-21T10:15:00.000000Z");

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

