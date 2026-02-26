package ashleysaintlouis.github.portifolioprojetos.service;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import ashleysaintlouis.github.portifolioprojetos.service.impl.ValidacaoProjetoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoProjetoServiceImplTest {

    private MembroExternoService membroExternoService;
    private ValidacaoProjetoServiceImpl service;

    @BeforeEach
    void setup() {
        membroExternoService = Mockito.mock(MembroExternoService.class);
        service = new ValidacaoProjetoServiceImpl(membroExternoService);
    }

    @Test
    void deveAceitarGerenteFuncionario() {
        Projeto projeto = new Projeto();
        projeto.setGerenteId("1");

        when(membroExternoService.ehFuncionario("1")).thenReturn(true);

        assertDoesNotThrow(() -> service.validarGerenteEhFuncionario(projeto));
    }

    @Test
    void deveRejeitarGerenteNaoFuncionario() {
        Projeto projeto = new Projeto();
        projeto.setGerenteId("36");

        when(membroExternoService.ehFuncionario("36")).thenReturn(false);

        assertThrows(RegraNegocioException.class,
                () -> service.validarGerenteEhFuncionario(projeto));
    }
}