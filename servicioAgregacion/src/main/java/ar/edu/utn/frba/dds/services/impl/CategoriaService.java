package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorTexto;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IEquivalenteCatRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


@Setter
@Getter
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
                .orElse(null);
    }

    public Categoria findByNombre(String nombreCategoria) {
        return categoriasRepository.findByNombre(nombreCategoria)
                .orElse(null);
    }

    @Override
    public List<CategoriaOutputDTO> findAll() {
        return categoriasRepository.findAll()
                .stream()
                .map(DTOConverter::convertirCategoriaOutputDTO)
                .toList();
    }

    @Override
    public List<Hecho> cargarCategoriasHechos( List<Hecho> hechosActualizados ) {
        List<Categoria> categoriasGuardadas = this.categoriasRepository.findAll();
        List<EquivalenteCategoria> equivalenteCategorias = this.equivalenteCatRepository.findAll();

        List<Categoria> nuevasCategorias = new ArrayList<>();

        for (Hecho hecho : hechosActualizados) {
            Categoria catHecho = hecho.getCategoria();

            if (catHecho.getNombre() == null && catHecho.getCodigoCategoria() == null) { throw new IllegalArgumentException("La categoría esta vacia"); }

            //normalizamos el nombre para obtener el id en nuestra base de datos
            String nombreNormalizado = NormalizadorTexto.normalizarTexto(catHecho.getNombre());
            if (catHecho.getCodigoCategoria() != null && !Objects.equals(catHecho.getCodigoCategoria(), nombreNormalizado) )
                { catHecho.setCodigoCategoria(nombreNormalizado); }
            if (catHecho.getCodigoCategoria() == null)
                { catHecho.setCodigoCategoria(nombreNormalizado); }


            //verificar si existe la categoria
            var categoriaExistente = categoriasGuardadas.stream()
                    .filter(c -> ( c.getCodigoCategoria().equals(catHecho.getCodigoCategoria()) ) )
                    .findFirst();
            if (categoriaExistente.isPresent()) {
                hecho.setCategoria(categoriaExistente.get());
                continue;
            }

            // si no la encontramos, verificamos sus equivalentes a ver si existe
            var categoriaEquivalente = equivalenteCategorias.stream()
                    .filter(e -> e.getEquivalente().equals(catHecho.getCodigoCategoria()))
                    .findFirst();
            if (categoriaEquivalente.isPresent()) {
                hecho.setCategoria( categoriaEquivalente.get().getCategoria() );
                continue;
            }
            //

            if (catHecho.getNombre() == null) { throw new IllegalArgumentException("No se puede crear una categoria sin nombre"); }

            // si no existe ni la categoria ni el equivalente, la creamos
            categoriasGuardadas.add(catHecho); //para que la proxima iteracion conoczca la nueva categoria
            nuevasCategorias.add(catHecho); //para guardarlas despues
        }

        this.categoriasRepository.saveAll(nuevasCategorias);
        return hechosActualizados;
    }

    @Override
    public void agregarEquivalentes(String codigoCategoria, String equivalente){
        Categoria categoria = categoriasRepository.findCategoriaByCodigoCategoria(codigoCategoria)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + codigoCategoria));

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
