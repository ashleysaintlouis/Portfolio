package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.dto.RelatorioPortfolioDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoMembroRepository;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoRepository;
import ashleysaintlouis.github.portifolioprojetos.repository.projecao.ProjetoStatusCount;
import ashleysaintlouis.github.portifolioprojetos.repository.projecao.ProjetoStatusTotal;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.RelatorioPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioPortfolioServiceImpl implements RelatorioPortfolioService {

    private final ProjetoRepository projetoRepository;
    private final ProjetoMembroRepository projetoMembroRepository;

    @Override
    public RelatorioPortfolioDTO gerarRelatorio() {
        Map<String, Long> quantidadePorStatus =
                projetoRepository.contarProjetosPorStatus()
                        .stream()
                        .collect(Collectors.toMap(
                                item -> item.getStatus().name(),
                                ProjetoStatusCount::getQuantidade
                        ));

        Map<String, BigDecimal> totalOrcadoPorStatus =
                projetoRepository.somarOrcamentoPorStatus()
                        .stream()
                        .collect(Collectors.toMap(
                                item -> item.getStatus().name(),
                                ProjetoStatusTotal::getTotal
                        ));

        Double mediaDuracao =
                projetoRepository.calcularMediaDuracaoPorStatus(StatusProjeto.ENCERRADO.name());

        if (mediaDuracao == null) {
            mediaDuracao = 0.0;
        }

        List<StatusProjeto> statusExcluidos =
                List.of(StatusProjeto.ENCERRADO, StatusProjeto.CANCELADO);

        Long totalMembrosUnicos =
                projetoMembroRepository.countMembrosUnicosAlocados(statusExcluidos);

        if (totalMembrosUnicos == null) {
            totalMembrosUnicos = 0L;
        }

        return new RelatorioPortfolioDTO(
                quantidadePorStatus,
                totalOrcadoPorStatus,
                mediaDuracao,
                totalMembrosUnicos
        );
    }
}