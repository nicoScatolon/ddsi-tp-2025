package ar.edu.utn.frba.dds.domain.normalizadores;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IEquivalenteCatRepository;

import java.util.Optional;

import static ar.edu.utn.frba.dds.domain.normalizadores.NormalizadorTexto.normalizarTexto;

public final class NormalizadorCategoria {

    private static ICategoriasRepository categoriasRepository;
    private static IEquivalenteCatRepository equivalenteCatRepository;

    private NormalizadorCategoria() {}

    public static void configurarRepositorios(ICategoriasRepository categoriasRepo, IEquivalenteCatRepository equivalentesRepo) {
        if (categoriasRepo == null || equivalentesRepo == null) {
            throw new IllegalArgumentException("Repositorios nulos");
        }

        categoriasRepository = categoriasRepo;
        equivalenteCatRepository = equivalentesRepo;
    }

    public static void normalizarCategoria(CategoriaInputDTO categoria) {

        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía.");
        }

        if (categoriasRepository == null || equivalenteCatRepository == null) {
            throw new IllegalStateException("NormalizadorCategoria no fue configurado con los repos.");
        }


        // PRIMERO ME FIJO SI MATCHEA EL NOMBRE ORIGINAL
        final String original = categoria.getNombre().trim();

        Optional<Categoria> categoriaOriginal = categoriasRepository.findByNombre(original);
        categoriaOriginal.ifPresent(value -> categoria.setNombre(value.getNombre()));

         // ME FIJO SI MATCHEA COMO EQUIVALENTE
        final String originalNormalizado = normalizarTexto(original);

        String nombreCategoria = equivalenteCatRepository.findNombreCategoriaByEquivalente(originalNormalizado)
                .orElseGet(() -> {
                    // SI NO EXISTE EL EQUIVALENTE CREO UNA NUEVA CATEGORIA
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
