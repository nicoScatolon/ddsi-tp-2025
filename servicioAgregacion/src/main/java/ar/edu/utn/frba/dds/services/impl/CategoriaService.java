package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService implements ICategoriaService {

    private final ICategoriasRepository categoriasRepository;

    public CategoriaService(ICategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public Categoria findById(Long idCategoria) {
        return categoriasRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + idCategoria));
    }

    public Categoria findByNombre(String nombreCategoria) {
        return categoriasRepository.findByNombre(nombreCategoria);}

    @Override
    public Categoria agregarCategoria(Categoria nuevaCategoria) {
        if (nuevaCategoria.getNombre() == null) {
            throw new IllegalArgumentException("La categoría debe tener un nombre");
        }

        // Normalizar nombre antes de persistir
        String normalizado = normalizarNombre(nuevaCategoria.getNombre());
        nuevaCategoria.setNombre(normalizado);

        // Buscar si ya existe por nombre
        Categoria existente = categoriasRepository.findByNombre(normalizado);
        if (existente == null) {
            return categoriasRepository.save(nuevaCategoria);
        } else {
            return existente;
        }
    }

    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }

        return nombre
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }
}
