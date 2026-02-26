package ashleysaintlouis.github.portifolioprojetos.repository.projecao;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;

import java.math.BigDecimal;

public interface ProjetoStatusTotal {
    StatusProjeto getStatus();
    BigDecimal getTotal();
}
