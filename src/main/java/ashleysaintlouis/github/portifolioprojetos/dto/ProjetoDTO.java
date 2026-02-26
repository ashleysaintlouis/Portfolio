package ashleysaintlouis.github.portifolioprojetos.dto;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjetoDTO(
        UUID id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "Data de início é obrigatória")
        LocalDate dataInicio,
        @NotNull(message = "Previsão de término é obrigatória")
        LocalDate dataPrevisaoTermino,
        LocalDate dataRealTermino,
        @NotNull(message = "Orçamento é obrigatório")
        @Positive(message = "Orçamento deve ser maior que zero")
        BigDecimal orcamento,
        StatusProjeto status,
        ClassificacaoRisco risco,
        String gerenteId,
        List<String> membrosIds
) {}
