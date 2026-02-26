package ashleysaintlouis.github.portifolioprojetos.service;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.service.impl.FluxoStatusProjetoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FluxoStatusProjetoServiceImplTest {

    private FluxoStatusProjetoServiceImpl fluxoStatusService;

    @BeforeEach
    void setUp() {
        fluxoStatusService = new FluxoStatusProjetoServiceImpl();
    }

    @Test
    void devePermitirTransicaoSequencial() {
        assertDoesNotThrow(() -> 
            fluxoStatusService.validarTransicao(StatusProjeto.EM_ANALISE, StatusProjeto.ANALISE_REALIZADA));
    }

    @Test
    void devePermitirCancelamentoDeQualquerStatus() {
        assertDoesNotThrow(() -> 
            fluxoStatusService.validarTransicao(StatusProjeto.EM_ANALISE, StatusProjeto.CANCELADO));
        
        assertDoesNotThrow(() -> 
            fluxoStatusService.validarTransicao(StatusProjeto.EM_ANDAMENTO, StatusProjeto.CANCELADO));
    }

    @Test
    void deveLancarExcecao_quandoTransicaoPulaEtapas() {
        assertThrows(RegraNegocioException.class, () -> 
            fluxoStatusService.validarTransicao(StatusProjeto.EM_ANALISE, StatusProjeto.ANALISE_APROVADA));
    }

    @Test
    void deveLancarExcecao_quandoTransicaoRetrocede() {
        assertThrows(RegraNegocioException.class, () -> 
            fluxoStatusService.validarTransicao(StatusProjeto.EM_ANDAMENTO, StatusProjeto.INICIADO));
    }
}
