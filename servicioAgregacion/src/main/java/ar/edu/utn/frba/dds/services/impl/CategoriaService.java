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
    public Categoria agregarCategoria(CategoriaInputDTO categoriaInputDTO) {
        Categoria categoria = DTOConverter.categoriaInputDTO(categoriaInputDTO);
        if (categoria == null) {
            categoria = categoriasRepository.save(categoria);
        }
        return categoria;
    }
}
