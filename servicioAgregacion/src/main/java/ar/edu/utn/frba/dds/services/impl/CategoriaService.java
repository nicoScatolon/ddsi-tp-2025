package ar.edu.utn.frba.dds.services.impl;

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
    public Categoria agregarCategoria(String nombreCategoria) {
        String id = this.StringToHandle(nombreCategoria);
        Categoria categoria = categoriasRepository.findByID(id);
        if (categoria == null) {
            categoria = categoriasRepository.save(id, nombreCategoria);
        }
        return categoria;
    }

    private String StringToHandle(String string) {
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
