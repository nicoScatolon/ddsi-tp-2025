package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService {
    ICategoriasRepository categoriasRepository;

    public CategoriaService(ICategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public Categoria findByID(String idCategoria){
        return categoriasRepository.findByID(idCategoria);
    }

    public Categoria findByNombre(String nombreCategoria){
        return categoriasRepository.findByID(this.crearIdCategoria(nombreCategoria));
    }

    @Override
    public Categoria agregarCategoria(Categoria nuevaCategoria) {
        if (nuevaCategoria.getNombre() == null) {
            throw new IllegalArgumentException("La categoría debe tener un nombre");
        }

        String idNuevaCategoria = crearIdCategoria(nuevaCategoria.getNombre());

        if (nuevaCategoria.getId() == null || !idNuevaCategoria.equals(nuevaCategoria.getId())) {
            nuevaCategoria.setId(idNuevaCategoria);
        }

        Categoria existente = categoriasRepository.findByID(idNuevaCategoria);
        if (existente == null) {
            return categoriasRepository.save(nuevaCategoria);
        } else {
            return existente;
        }
    }


    private String crearIdCategoria(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("El string no puede ser nulo ni vacío.");
        }

        return string
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }
}
