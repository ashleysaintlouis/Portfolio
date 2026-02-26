package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.RiscoProjetoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class RiscoProjetoServiceImpl implements RiscoProjetoService {

    private static final BigDecimal LIMITE_BAIXO_RISCO = new BigDecimal("100000");
    private static final BigDecimal LIMITE_ALTO_RISCO = new BigDecimal("500000");
    private static final int MESES_BAIXO_RISCO = 3;
    private static final int MESES_ALTO_RISCO = 6;

    @Override
    public ClassificacaoRisco calcularRisco(Projeto projeto) {
        if (projeto.getDataInicio() == null || projeto.getDataPrevisaoTermino() == null || projeto.getOrcamento() == null) {
            return ClassificacaoRisco.MEDIO;
        }

        long meses = ChronoUnit.MONTHS.between(
                projeto.getDataInicio(),
                projeto.getDataPrevisaoTermino()
        );

        BigDecimal orcamento = projeto.getOrcamento();

        if (orcamento.compareTo(LIMITE_BAIXO_RISCO) <= 0 && meses <= MESES_BAIXO_RISCO) {
            return ClassificacaoRisco.BAIXO;
        }

        if (orcamento.compareTo(LIMITE_ALTO_RISCO) > 0 || meses > MESES_ALTO_RISCO) {
            return ClassificacaoRisco.ALTO;
        }
        return ClassificacaoRisco.MEDIO;
    }
}
