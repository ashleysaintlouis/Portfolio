package ashleysaintlouis.github.portifolioprojetos.repository;

import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoRelatorioDTO;

import java.time.LocalDate;
import java.util.List;

public interface ProjetoRepositoryCustom {

    List<ProjetoRelatorioDTO> buscarRelatorio(
            String status,
            LocalDate dataInicio,
            LocalDate dataFim);
}
