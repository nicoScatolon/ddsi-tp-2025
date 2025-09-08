package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IEquivalenteCatRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class CategoriaService implements ICategoriaService {

    private final ICategoriasRepository categoriasRepository;
    private final IEquivalenteCatRepository equivalenteCatRepository;

    public CategoriaService(ICategoriasRepository categoriasRepository, IEquivalenteCatRepository equivalenteCatRepository) {
        this.categoriasRepository = categoriasRepository;
        this.equivalenteCatRepository = equivalenteCatRepository;
    }

    public Categoria findById(Long idCategoria) {
        return categoriasRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + idCategoria));
    }

    public Categoria findByNombre(String nombreCategoria) {
        return categoriasRepository.findByNombre(nombreCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con nombre " + nombreCategoria));
    }

    @Override
    public List<CategoriaOutputDTO> findAll() {
        return categoriasRepository.findAll()
                .stream()
                .map(DTOConverter::convertirCategoriaOutputDTO)
                .toList();
    }

    @Override
    public Categoria agregarCategoria(Categoria nuevaCategoria) {
        if (nuevaCategoria.getNombre() == null) {
            throw new IllegalArgumentException("La categoría debe tener un nombre");
        }

        // Normalizar nombre antes de persistir
        String normalizado = normalizarNombre(nuevaCategoria.getNombre());
        nuevaCategoria.setNombre(normalizado);

        // Buscar si ya existe por nombre
        Categoria existente = findByNombre(normalizado);
        if (existente == null) {
            return categoriasRepository.save(nuevaCategoria);
        } else {
            return existente;
        }
    }


    @Override
    public void agregarEquivalentes(Long idCategoria, String equivalente){
        Categoria categoria = categoriasRepository.findById(idCategoria)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + idCategoria));

        EquivalenteCategoria equivalenteEntidad = new EquivalenteCategoria(equivalente, categoria);

        equivalenteCatRepository.save(equivalenteEntidad);
    }

    @Override
    public void eliminarEquivalentes(String equivalente){
        equivalenteCatRepository.deleteByEquivalente(equivalente);
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
