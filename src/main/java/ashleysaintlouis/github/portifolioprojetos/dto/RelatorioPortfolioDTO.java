package ashleysaintlouis.github.portifolioprojetos.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RelatorioPortfolioDTO(
        Map<String, Long> quantidadeProjetosPorStatus,
        Map<String, BigDecimal> totalOrcadoPorStatus,
        Double mediaDuracaoProjetosEncerrados,
        Long totalMembrosUnicosAlocados
) {}
