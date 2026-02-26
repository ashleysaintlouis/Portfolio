package ashleysaintlouis.github.portifolioprojetos.service.Interface;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;

public interface RiscoProjetoService {

    ClassificacaoRisco calcularRisco(Projeto projeto);
}
