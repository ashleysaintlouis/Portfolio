package ashleysaintlouis.github.portifolioprojetos.dto;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjetoResponseDTO(
        UUID id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataPrevisaoTermino,
        LocalDate dataRealTermino,
        BigDecimal orcamento,
        StatusProjeto status,
        ClassificacaoRisco risco,
        String gerenteId,
        List<MembroDTO> membros
) {}