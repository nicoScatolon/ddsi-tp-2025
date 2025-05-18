package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriaRepository;
import ar.edu.utn.frba.dds.services.ICategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriasService implements ICategoriasService {
    @Autowired //ToDO Se puede reemplazar (Es lo mas recomendable)
    private ICategoriaRepository repository;

    @Override
    public List<CategoriaOutputDTO> buscarTodasLasCategorias() {
        //ToDO, si es Admin, aquí se debería verificar
        return this.repository
                .findAll()
                .stream()
                .map(this::categoriaOutputDTO)
                .toList();
    }

    private CategoriaOutputDTO categoriaOutputDTO(Categoria categoria) {
        return CategoriaOutputDTO.builder()
                .nombre(categoria.getNombre())
                .hash(categoria.getHash())
                .build();
    }
}
