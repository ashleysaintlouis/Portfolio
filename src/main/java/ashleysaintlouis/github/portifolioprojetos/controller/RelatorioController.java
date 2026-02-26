package ashleysaintlouis.github.portifolioprojetos.controller;

import ashleysaintlouis.github.portifolioprojetos.dto.RelatorioPortfolioDTO;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.RelatorioPortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "API para geração de relatórios")
public class RelatorioController {

    private final RelatorioPortfolioService relatorioPortfolioService;

    @GetMapping("/portfolio")
    @Operation(summary = "Gerar relatório resumido do portfólio")
    public ResponseEntity<RelatorioPortfolioDTO> gerarRelatorioPortfolio() {
        RelatorioPortfolioDTO relatorio = relatorioPortfolioService.gerarRelatorio();
        return ResponseEntity.ok(relatorio);
    }
}
