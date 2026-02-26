package ashleysaintlouis.github.portifolioprojetos.service;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.service.impl.RiscoProjetoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiscoProjetoServiceImplTest {

    private RiscoProjetoServiceImpl riscoProjetoService;

    @BeforeEach
    void setUp() {
        riscoProjetoService = new RiscoProjetoServiceImpl();
    }

    @Test
    void deveCalcularRiscoBaixo_quandoOrcamentoAte100000EPrazoAte3Meses() {
        Projeto projeto = criarProjeto(new BigDecimal("50000"), 2);
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        assertEquals(ClassificacaoRisco.BAIXO, risco);
    }

    @Test
    void deveCalcularRiscoAlto_quandoOrcamentoAcima500000() {
        Projeto projeto = criarProjeto(new BigDecimal("600000"), 2);
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        assertEquals(ClassificacaoRisco.ALTO, risco);
    }

    @Test
    void deveCalcularRiscoAlto_quandoPrazoAcima6Meses() {
        Projeto projeto = criarProjeto(new BigDecimal("200000"), 8);
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        assertEquals(ClassificacaoRisco.ALTO, risco);
    }

    @Test
    void deveCalcularRiscoMedio_quandoOrcamentoEntre100001E500000() {
        Projeto projeto = criarProjeto(new BigDecimal("300000"), 4);
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        assertEquals(ClassificacaoRisco.MEDIO, risco);
    }

    @Test
    void deveCalcularRiscoMedio_quandoPrazoEntre3E6Meses() {
        Projeto projeto = criarProjeto(new BigDecimal("50000"), 5);
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        assertEquals(ClassificacaoRisco.MEDIO, risco);
    }

    private Projeto criarProjeto(BigDecimal orcamento, long meses) {
        Projeto projeto = new Projeto();
        projeto.setOrcamento(orcamento);
        projeto.setDataInicio(LocalDate.now());
        projeto.setDataPrevisaoTermino(LocalDate.now().plusMonths(meses));
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        return projeto;
    }
}
