package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.FluxoStatusProjetoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FluxoStatusProjetoServiceImpl implements FluxoStatusProjetoService {

    private static final List<StatusProjeto> FLUXO = List.of(
            StatusProjeto.EM_ANALISE,
            StatusProjeto.ANALISE_REALIZADA,
            StatusProjeto.ANALISE_APROVADA,
            StatusProjeto.INICIADO,
            StatusProjeto.PLANEJADO,
            StatusProjeto.EM_ANDAMENTO,
            StatusProjeto.ENCERRADO
    );

    @Override
    public void validarTransicao(StatusProjeto atual, StatusProjeto novo) {

        if (novo == StatusProjeto.CANCELADO) return;

        int atualIndex = FLUXO.indexOf(atual);
        int novoIndex = FLUXO.indexOf(novo);

        if (novoIndex != atualIndex + 1) {
            throw new RegraNegocioException("Transição de status inválida.");
        }
    }
}