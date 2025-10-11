package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@RestController
@RequestMapping("/api/fuenteDinamica/hechos")
public class HechosController {
    private final HechosService hechosService;
    private final Executor executorHechos;

    public HechosController(
            HechosService hechosService,
            @Qualifier("executorHechos") Executor executorHechos) {
        this.hechosService = hechosService;
        this.executorHechos = executorHechos;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> obtenerHechos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDeCarga)
    {
        return hechosService.getHechos(fechaDeCarga);
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> crearHecho(@RequestBody HechoInputDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ContribuyenteInputDTO contribuyenteDTO = new ContribuyenteInputDTO();

        if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Object principal = auth.getPrincipal();
            if(principal instanceof Contribuyente contribuyente){
                contribuyenteDTO.setNombre(contribuyente.getNombre());
                contribuyenteDTO.setApellido(contribuyente.getApellido());
                contribuyenteDTO.setId(contribuyente.getId());
            }
        }else{
            contribuyenteDTO.setNombre("Anonimo");
            contribuyenteDTO.setApellido("");
            contribuyenteDTO.setId(null);
        }
        dto.setContribuyente(contribuyenteDTO);
        CompletableFuture.runAsync(() -> hechosService.cargarHecho(dto), executorHechos).whenComplete((ok, ex) -> {
                    if (ex != null) {
                        log.error("Fallo al cargar hecho", ex);}});
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/pruebas")
    public ResponseEntity<Void> crearHechoPrueba(@RequestBody HechoInputDTO dto) {
        hechosService.cargarHecho(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CONTRIBUYENTE') and hasAuthority('EDITAR_HECHO_PROPIO')")
    public void modificarHecho(@PathVariable Long id, @RequestBody HechoInputDTO hechoInputDTO ) {
        if (hechoInputDTO.getId() == null) {throw new IllegalArgumentException("El hecho no contiene id");}
        if (!id.equals(hechoInputDTO.getId())) {throw new IllegalArgumentException("Id del hecho no matchea con la url utilizada");}
        this.hechosService.modificarHecho(hechoInputDTO);
    }
}

