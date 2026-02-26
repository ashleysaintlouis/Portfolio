package ashleysaintlouis.github.portifolioprojetos.service;

import ashleysaintlouis.github.portifolioprojetos.entity.ProjetoMembro;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.PapelProjeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoMembroRepository;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoRepository;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.ValidacaoProjetoService;
import ashleysaintlouis.github.portifolioprojetos.service.impl.AlocacaoMembroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlocacaoMembroServiceImplTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private ProjetoMembroRepository projetoMembroRepository;

    @Mock
    private MembroExternoService membroExternoService;

    @Mock
    private ValidacaoProjetoService validacaoProjetoService;

    @InjectMocks
    private AlocacaoMembroServiceImpl alocacaoMembroService;

    private Projeto projeto;
    private UUID projetoId;

    @BeforeEach
    void setUp() {
        projetoId = UUID.randomUUID();
        projeto = criarProjeto();
        projeto.setId(projetoId);
    }

    @Test
    void deveLancarExcecao_quandoProjetoNaoEncontrado() {
        when(projetoRepository.findById(projetoId)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> 
            alocacaoMembroService.adicionarMembro(projetoId, "membro1"));
    }

    @Test
    void deveLancarExcecao_quandoMembroNaoEhFuncionario() {
        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        when(membroExternoService.ehFuncionario("membro1")).thenReturn(false);

        assertThrows(RegraNegocioException.class, () -> 
            alocacaoMembroService.adicionarMembro(projetoId, "membro1"));
    }

    @Test
    void deveLancarExcecao_quandoProjetoJaTem10Membros() {
        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        when(membroExternoService.ehFuncionario("membro1")).thenReturn(true);
        
        List<ProjetoMembro> membros = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            membros.add(new ProjetoMembro("membro" + i, PapelProjeto.PARTICIPANTE));
        }
        projeto.setMembros(membros);

        assertThrows(RegraNegocioException.class, () -> 
            alocacaoMembroService.adicionarMembro(projetoId, "membro1"));
    }

    private Projeto criarProjeto() {
        Projeto projeto = new Projeto();
        projeto.setNome("Projeto Teste");
        projeto.setDataInicio(LocalDate.now());
        projeto.setDataPrevisaoTermino(LocalDate.now().plusMonths(3));
        projeto.setOrcamento(new BigDecimal("100000"));
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        projeto.setMembros(new ArrayList<>());
        return projeto;
    }
}
