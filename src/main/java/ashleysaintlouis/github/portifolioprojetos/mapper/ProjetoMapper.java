package ashleysaintlouis.github.portifolioprojetos.mapper;

import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProjetoMapper {

    Projeto toEntity(ProjetoDTO dto);

}