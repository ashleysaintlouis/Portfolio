package ashleysaintlouis.github.portifolioprojetos.service.Interface;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;

public interface FluxoStatusProjetoService {

    void validarTransicao(StatusProjeto statusAtual, StatusProjeto novoStatus);
}
