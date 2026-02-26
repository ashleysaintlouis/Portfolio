package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.ValidacaoProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidacaoProjetoServiceImpl implements ValidacaoProjetoService {

    private final MembroExternoService membroExternoService;

    @Override
    public void validarDatas(Projeto projeto) {
        if (projeto.getDataInicio() != null && projeto.getDataPrevisaoTermino() != null) {
            if (projeto.getDataInicio().isAfter(projeto.getDataPrevisaoTermino())) {
                throw new RegraNegocioException("Data de início não pode ser maior que previsão de término.");
            }
        }
    }

    @Override
    public void validarOrcamento(Projeto projeto) {
        if (projeto.getOrcamento() != null && projeto.getOrcamento().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Orçamento deve ser maior que zero.");
        }
    }

    @Override
    public void validarExclusao(Projeto projeto) {
        if (Set.of(
                StatusProjeto.INICIADO,
                StatusProjeto.EM_ANDAMENTO,
                StatusProjeto.ENCERRADO
        ).contains(projeto.getStatus())) {
            throw new RegraNegocioException(
                    "Projeto não pode ser excluído quando está em status iniciado, em andamento ou encerrado."
            );
        }
    }

    @Override
    public void validarQuantidadeMembros(Projeto projeto) {
        int total = projeto.getMembros() != null ? projeto.getMembros().size() : 0;

        if (total < 1 || total > 10) {
            throw new RegraNegocioException("Projeto deve ter entre 1 e 10 membros.");
        }
    }

    @Override
    public void validarGerenteEhFuncionario(Projeto projeto) {

        if (projeto.getGerenteId() == null || projeto.getGerenteId().isBlank()) {
            throw new RegraNegocioException("Gerente é obrigatório.");
        }

        boolean ehFuncionario = membroExternoService.ehFuncionario(projeto.getGerenteId());

        if (!ehFuncionario) {
            throw new RegraNegocioException(
                    "O gerente informado deve possuir atribuição 'funcionário'."
            );
        }
    }

    @Override
    public void validarMembrosSaoFuncionarios(Projeto projeto) {

        if (projeto.getMembros() == null || projeto.getMembros().isEmpty()) {
            return;
        }

        projeto.getMembros().forEach(membro -> {

            String membroId = membro.getMembroId();

            if (membroId == null || membroId.isBlank()) {
                throw new RegraNegocioException("Existe membro com ID inválido.");
            }

            boolean ehFuncionario = membroExternoService.ehFuncionario(membroId);

            if (!ehFuncionario) {
                throw new RegraNegocioException(
                        "O membro " + membroId + " não possui atribuição de funcionário."
                );
            }
        });
    }
}