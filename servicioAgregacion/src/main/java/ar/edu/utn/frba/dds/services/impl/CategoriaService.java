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
    public Categoria agregarCategoria(Categoria categoria) {
        if (categoria.getId() == null) {
            // no esta y creamos una nueva
            categoria.setId(crearIdCategoria(categoria.getNombre()));
            categoria = categoriasRepository.save(categoria);
            return categoria;
        } else {
            //posible verificacion de que el id que tiene la categoria corresponda a la forma de crearlos (crear el id y verificarlo con el que tiene)
            //ya existe
            return categoriasRepository.findByID(categoria.getId());
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
