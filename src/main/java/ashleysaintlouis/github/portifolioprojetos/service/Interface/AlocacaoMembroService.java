package ashleysaintlouis.github.portifolioprojetos.service.Interface;

import java.util.UUID;

public interface AlocacaoMembroService {

    void adicionarMembro(UUID projetoId, String membroId);

    void removerMembro(UUID projetoId, String membroId);

    void validarLimiteProjetosDoMembro(String membroId);
}
