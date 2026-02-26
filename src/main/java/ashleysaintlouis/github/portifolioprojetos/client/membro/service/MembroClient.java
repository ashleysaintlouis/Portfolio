package ashleysaintlouis.github.portifolioprojetos.client.membro.service;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;

import java.util.List;

public interface MembroClient {

    MembroDTO buscarPorId(String id);

    MembroDTO criar(MembroDTO dto);
}