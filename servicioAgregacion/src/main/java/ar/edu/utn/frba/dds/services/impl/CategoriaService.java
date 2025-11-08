package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.EquivalenteOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorTexto;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import ar.edu.utn.frba.dds.domain.repository.IEquivalenteCatRepository;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import org.springframework.transaction.annotation.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;


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
    public List<String> findAllShort(){
        return categoriasRepository.findAllNombres();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Hecho> cargarCategoriasHechos(List<Hecho> hechosActualizados) {
        List<Categoria> categoriasGuardadas = this.categoriasRepository.findAll();
        List<EquivalenteCategoria> equivalenteCategorias = this.equivalenteCatRepository.findAll();

        List<Categoria> nuevasCategorias = new ArrayList<>();

        for (Hecho hecho : hechosActualizados) {
            Categoria catHecho = hecho.getCategoria();

            Categoria categoriaFinal = this.cargarCategoria(catHecho, equivalenteCategorias, categoriasGuardadas);

            // Si es una categoría nueva, la agregamos a las listas
            if (!categoriasGuardadas.contains(categoriaFinal)) {
                categoriasGuardadas.add(categoriaFinal);
                nuevasCategorias.add(categoriaFinal);
            }

            hecho.setCategoria(categoriaFinal);
        }

        this.categoriasRepository.saveAll(nuevasCategorias);
        this.categoriasRepository.flush();
        return hechosActualizados;
    }

    @Override
    public ResponseEntity<Void> crearCategoria(CategoriaInputDTO categoriaInputDTO) {
        try {
            List<Categoria> categoriasGuardadas = this.categoriasRepository.findAll();
            List<EquivalenteCategoria> equivalenteCategorias = this.equivalenteCatRepository.findAll();
            Categoria categoria= DTOConverter.categoriaInputDTO(categoriaInputDTO);

            Categoria categoriaActualizada = this.cargarCategoria(categoria, equivalenteCategorias, categoriasGuardadas);

            // Solo guardamos si es una categoría nueva
            if (!categoriasGuardadas.contains(categoriaActualizada)) {
                this.categoriasRepository.save(categoriaActualizada);
            }

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public List<EquivalenteOutputDTO> findAllEquivalentes() {
        return DTOConverter.categoriaEquivalenteOutput(equivalenteCatRepository.findAll());
    }

    @Override
    @Transactional
    public ResponseEntity<Void> editarEquivalentes(String codigoCategoria, String nombreEquivalente, String nuevoNombre) {
        Optional<EquivalenteCategoria> optEquivalente =
                equivalenteCatRepository.findByCategoria_CodigoCategoriaAndNombreEquivalente(codigoCategoria, nombreEquivalente);

        if (optEquivalente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2) Si el nuevo nombre es igual al actual, no hace falta nada
        EquivalenteCategoria existente = optEquivalente.get();
        if (existingNameEqualsCurrent(existente, nuevoNombre)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        // 3) Verificar colisión: ¿ya existe otro equivalente con ese nuevoNombre en la misma categoría?
        boolean collision = equivalenteCatRepository.existsByCategoria_CodigoCategoriaAndNombreEquivalente(codigoCategoria, nuevoNombre);
        if (collision) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        existente.setNombreEquivalente(nuevoNombre);
        equivalenteCatRepository.save(existente);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private boolean existingNameEqualsCurrent(EquivalenteCategoria existente, String nuevoNombre) {
        if (nuevoNombre == null) return false;
        return nuevoNombre.trim().equalsIgnoreCase(
                existente.getNombreEquivalente() == null ? "" : existente.getNombreEquivalente().trim()
        );
    }



    @Override
    public ResponseEntity<Void> editarCategoria(CategoriaInputDTO dto) {
        if (dto == null || dto.getCodigoCategoria() == null || dto.getNuevoNombre() == null || dto.getNuevoNombre().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String nuevoNombre = dto.getNuevoNombre();

        // Buscar la categoría original por su código
        Optional<Categoria> categoriaOpt = categoriasRepository.findCategoriaByCodigoCategoria(dto.getCodigoCategoria());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria = categoriaOpt.get();

        // Verificar que el nuevo nombre no exista
        if (categoriasRepository.existsByNombre(nuevoNombre)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        categoria.setNombre(nuevoNombre);
        categoriasRepository.save(categoria);

        return ResponseEntity.ok().build();
    }


    // Analiza si la categoria existe, analiza si existe una equivalente, sino la crea.
    private Categoria cargarCategoria(Categoria categoria, List<EquivalenteCategoria> equivalenteCategorias, List<Categoria> categoriasGuardadas) {
        if (categoria.getNombre() == null && categoria.getCodigoCategoria() == null) {
            throw new IllegalArgumentException("La categoría esta vacia");
        }

        //normalizamos el nombre para obtener el id en nuestra base de datos
        String nombreNormalizado = NormalizadorTexto.normalizarTexto(categoria.getNombre());
        if (categoria.getCodigoCategoria() != null && !Objects.equals(categoria.getCodigoCategoria(), nombreNormalizado)) {
            categoria.setCodigoCategoria(nombreNormalizado);
        }
        if (categoria.getCodigoCategoria() == null) {
            categoria.setCodigoCategoria(nombreNormalizado);
        }

        //verificar si existe la categoria
        var categoriaExistente = categoriasGuardadas.stream()
                .filter(c -> c.getCodigoCategoria().equals(categoria.getCodigoCategoria()))
                .findFirst();
        if (categoriaExistente.isPresent()) {
            return categoriaExistente.get();
        }

        // si no la encontramos, verificamos sus equivalentes a ver si existe
        var categoriaEquivalente = equivalenteCategorias.stream()
                .filter(e -> e.getNombreEquivalente().equals(categoria.getCodigoCategoria()))
                .findFirst();
        if (categoriaEquivalente.isPresent()) {
            return categoriaEquivalente.get().getCategoria();
        }

        if (categoria.getNombre() == null) {
            throw new IllegalArgumentException("No se puede crear una categoria sin nombre");
        }

        return categoria;
    }

    @Override
    @Transactional
    public void agregarEquivalentes(String codigoCategoria, String nombreEquivalente){
        Categoria categoria = categoriasRepository.findCategoriaByCodigoCategoria(codigoCategoria)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + codigoCategoria));

        EquivalenteCategoria equivalenteEntidad = new EquivalenteCategoria(nombreEquivalente, categoria);

        equivalenteCatRepository.save(equivalenteEntidad);
    }

    @Override
    @Transactional
    public void eliminarEquivalentes(String equivalente){
        equivalenteCatRepository.deleteByNombreEquivalente(equivalente);
    }
}
