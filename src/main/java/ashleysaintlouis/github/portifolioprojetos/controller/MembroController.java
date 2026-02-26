package ashleysaintlouis.github.portifolioprojetos.controller;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membros")
@RequiredArgsConstructor
@Tag(name = "Membros", description = "API para gerenciamento de membros (API externa)")
public class MembroController {

    private final MembroExternoService membroExternoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar membro por ID na API externa")
    public ResponseEntity<MembroDTO> buscarPorId(@PathVariable String id) {
        MembroDTO membro = membroExternoService.buscarPorId(id);
        return ResponseEntity.ok(membro);
    }

    @PostMapping
    @Operation(summary = "Criar membro na API externa")
    public ResponseEntity<MembroDTO> criar(@Valid @RequestBody MembroDTO dto) {
        MembroDTO membro = membroExternoService.criarMembro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(membro);
    }
}
