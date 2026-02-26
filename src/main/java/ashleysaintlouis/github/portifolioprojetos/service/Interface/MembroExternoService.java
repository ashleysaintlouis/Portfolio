package ashleysaintlouis.github.portifolioprojetos.service.Interface;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;

public interface MembroExternoService {

    MembroDTO buscarPorId(String id);

    MembroDTO criarMembro(MembroDTO dto);

    boolean ehFuncionario(String id);
}
