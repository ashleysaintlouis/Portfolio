package ashleysaintlouis.github.portifolioprojetos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjetoRelatorioDTO(
        String id,
        String nomeProjeto,
        String gerente,
        String status,
        BigDecimal custoTotal,
        LocalDate dataInicio
) {}
