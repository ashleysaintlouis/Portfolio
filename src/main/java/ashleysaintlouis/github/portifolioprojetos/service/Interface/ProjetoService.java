package ashleysaintlouis.github.portifolioprojetos.service.Interface;

import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoDTO;
import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoResponseDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProjetoService {

    ProjetoResponseDTO criar(ProjetoDTO dto);

    ProjetoResponseDTO buscarPorId(UUID id);

    Page<ProjetoResponseDTO> listar(String nome, StatusProjeto status, Pageable pageable);

    ProjetoResponseDTO atualizar(UUID id, ProjetoDTO dto);

    void excluir(UUID id);

    ProjetoResponseDTO atualizarStatus(UUID id, StatusProjeto novoStatus);

    ProjetoResponseDTO adicionarMembro(UUID projetoId, String membroId);

    ProjetoResponseDTO removerMembro(UUID projetoId, String membroId);
}