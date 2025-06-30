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

    @Override
    public Categoria agregarCategoria(Categoria nuevaCategoria) {
        if (nuevaCategoria.getId() == null) {
            String idnuevaCategoria = crearIdCategoria(nuevaCategoria.getNombre());
            Categoria categoriaExistente = categoriasRepository.findByID(idnuevaCategoria);

            if (categoriaExistente == null) {return categoriasRepository.save(nuevaCategoria);}
            else {return categoriaExistente;}

        } else {
            String idNuevaCategoria = crearIdCategoria(nuevaCategoria.getNombre());
            if (! idNuevaCategoria.equals(nuevaCategoria.getId())) { //categoria con id mal cargado
                nuevaCategoria.setId(idNuevaCategoria);
                return this.agregarCategoria(nuevaCategoria);
            }
            Categoria categoriaExistente = categoriasRepository.findByID(nuevaCategoria.getId());
            if (categoriaExistente == null) {return categoriasRepository.save(nuevaCategoria);}
            else {return categoriaExistente;}
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
