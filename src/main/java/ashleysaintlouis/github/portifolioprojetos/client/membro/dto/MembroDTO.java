package ashleysaintlouis.github.portifolioprojetos.client.membro.dto;

import jakarta.validation.constraints.NotBlank;

public record MembroDTO(
        String id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "Atribuição é obrigatória")
        String atribuicao
) {}
