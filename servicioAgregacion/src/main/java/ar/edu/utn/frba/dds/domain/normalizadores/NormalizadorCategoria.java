package ar.edu.utn.frba.dds.domain.normalizadores;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IEquivalenteCatRepository;

import java.util.Optional;

import static ar.edu.utn.frba.dds.domain.normalizadores.NormalizadorTexto.normalizarTexto;

public final class NormalizadorCategoria {

    private NormalizadorCategoria() {}

    public static void normalizarCategoria(CategoriaInputDTO categoria, ICategoriasRepository categoriasRepository, IEquivalenteCatRepository equivalenteCatRepository) {

        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía.");
        }

        final String original = categoria.getNombre().trim();

        Optional<Categoria> categoriaOriginal = categoriasRepository.findByNombre(original);
        categoriaOriginal.ifPresent(value -> categoria.setNombre(value.getNombre()));


        final String originalNormalizado = normalizarTexto(original);

        String nombreCategoria = equivalenteCatRepository.findNombreCategoriaByEquivalente(originalNormalizado)
                .orElseGet(() -> {
                    // Si no existe → creo nueva categoría
                    Categoria nueva = new Categoria(original);
                    Categoria guardada = categoriasRepository.save(nueva);


                    EquivalenteCategoria equivalente = new EquivalenteCategoria();
                    equivalente.setEquivalente(originalNormalizado);
                    equivalente.setCategoria(guardada);
                    equivalenteCatRepository.save(equivalente);

                    return guardada.getNombre();
                });

        categoria.setNombre(nombreCategoria);
    }
}
