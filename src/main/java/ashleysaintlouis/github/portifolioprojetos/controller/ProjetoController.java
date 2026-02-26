package ashleysaintlouis.github.portifolioprojetos.controller;

import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoDTO;
import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoResponseDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
@Tag(name = "Projetos", description = "API para gerenciamento de projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    @Operation(summary = "Criar novo projeto")
    public ResponseEntity<ProjetoResponseDTO> criar(@Valid @RequestBody ProjetoDTO dto) {
        ProjetoResponseDTO projeto = projetoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(projeto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable UUID id) {
        ProjetoResponseDTO projeto = projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping
    @Operation(summary = "Listar projetos com paginação e filtros")
    public ResponseEntity<Page<ProjetoResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusProjeto status,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ProjetoResponseDTO> projetos = projetoService.listar(nome, status, pageable);
        return ResponseEntity.ok(projetos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto")
    public ResponseEntity<ProjetoResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProjetoDTO dto) {
        ProjetoResponseDTO projeto = projetoService.atualizar(id, dto);
        return ResponseEntity.ok(projeto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do projeto")
    public ResponseEntity<ProjetoResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestParam StatusProjeto status) {
        ProjetoResponseDTO projeto = projetoService.atualizarStatus(id, status);
        return ResponseEntity.ok(projeto);
    }

    @PostMapping("/{id}/membros/{membroId}")
    @Operation(summary = "Adicionar membro ao projeto")
    public ResponseEntity<ProjetoResponseDTO> adicionarMembro(
            @PathVariable UUID id,
            @PathVariable String membroId) {
        ProjetoResponseDTO projeto = projetoService.adicionarMembro(id, membroId);
        return ResponseEntity.ok(projeto);
    }

    @DeleteMapping("/{id}/membros/{membroId}")
    @Operation(summary = "Remover membro do projeto")
    public ResponseEntity<ProjetoResponseDTO> removerMembro(
            @PathVariable UUID id,
            @PathVariable String membroId) {
        ProjetoResponseDTO projeto = projetoService.removerMembro(id, membroId);
        return ResponseEntity.ok(projeto);
    }
}
