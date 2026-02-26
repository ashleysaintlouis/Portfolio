package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.entity.ProjetoMembro;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.PapelProjeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoMembroRepository;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoRepository;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.AlocacaoMembroService;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.ValidacaoProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlocacaoMembroServiceImpl implements AlocacaoMembroService {

    private final ProjetoRepository projetoRepository;
    private final ProjetoMembroRepository projetoMembroRepository;
    private final MembroExternoService membroExternoService;
    private final ValidacaoProjetoService validacaoProjetoService;

    @Override
    public void adicionarMembro(UUID projetoId, String membroId) {

        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));

        if (!membroExternoService.ehFuncionario(membroId)) {
            throw new RegraNegocioException("Apenas funcionários podem ser associados ao projeto");
        }

        if (membroId.equals(projeto.getGerenteId())) {
            throw new RegraNegocioException("O gerente " + membroId + " já é membro automático do projeto");
        }

        boolean jaAlocado = projeto.getMembros().stream()
                .anyMatch(pm -> pm.getMembroId().equals(membroId));

        if (jaAlocado) {
            throw new RegraNegocioException("Membro " + membroId + " já está alocado neste projeto");
        }

        if (projeto.getMembros().size() >= 10) {
            throw new RegraNegocioException("Projeto já possui o número máximo de membros (10)");
        }

        validarLimiteProjetosDoMembro(membroId);

        ProjetoMembro projetoMembro = new ProjetoMembro(membroId, PapelProjeto.PARTICIPANTE);
        projetoMembro.setProjeto(projeto);

        projeto.getMembros().add(projetoMembro);

        projetoRepository.save(projeto);
    }

    @Override
    public void removerMembro(UUID projetoId, String membroId) {

        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));

        if (membroId.equals(projeto.getGerenteId())) {
            throw new RegraNegocioException("O gerente " + membroId + " não pode ser removido do projeto");
        }

        ProjetoMembro projetoMembro = projeto.getMembros().stream()
                .filter(pm -> pm.getMembroId().equals(membroId))
                .findFirst()
                .orElseThrow(() -> new RegraNegocioException("Membro" + membroId + " não está alocado neste projeto"));

        projeto.getMembros().remove(projetoMembro);
        projetoMembroRepository.delete(projetoMembro);

        validacaoProjetoService.validarQuantidadeMembros(projeto);

        projetoRepository.save(projeto);
    }

    @Override
    public void validarLimiteProjetosDoMembro(String membroId) {

        List<StatusProjeto> statusExcluidos =
                List.of(StatusProjeto.ENCERRADO, StatusProjeto.CANCELADO);

        Long quantidadeProjetos =
                projetoMembroRepository.countProjetosPorMembro(membroId, statusExcluidos);

        if (quantidadeProjetos >= 3) {
            throw new RegraNegocioException(
                    "Membro " + membroId + " já está alocado em 3 projetos ativos. Limite máximo atingido."
            );
        }
    }
}