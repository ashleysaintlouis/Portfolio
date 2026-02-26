package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;
import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoDTO;
import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoResponseDTO;
import ashleysaintlouis.github.portifolioprojetos.entity.ProjetoMembro;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.ClassificacaoRisco;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.PapelProjeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.mapper.ProjetoMapper;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoRepository;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final ProjetoMapper projetoMapper;
    private final ValidacaoProjetoService validacaoProjetoService;
    private final RiscoProjetoService riscoProjetoService;
    private final FluxoStatusProjetoService fluxoStatusProjetoService;
    private final AlocacaoMembroService alocacaoMembroService;
    private final MembroExternoService membroExternoService;

    @Override
    public ProjetoResponseDTO criar(ProjetoDTO dto) {
        Projeto projeto = projetoMapper.toEntity(dto);

        projeto.setStatus(StatusProjeto.EM_ANALISE);

        adicionarGerenteComoMembro(projeto, dto.gerenteId());
        projeto.getMembros().addAll(mapearMembros(dto.membrosIds(), projeto));

        validarProjeto(projeto);
        aplicarRisco(projeto);

        Projeto salvo = projetoRepository.save(projeto);
        return montarResponse(salvo);
    }

    @Override
    public ProjetoResponseDTO buscarPorId(UUID id) {
        Projeto projeto = buscarProjeto(id);
        aplicarRisco(projeto);
        return montarResponse(projeto);
    }

    @Override
    public Page<ProjetoResponseDTO> listar(String nome, StatusProjeto status, Pageable pageable) {

        String statusParam = status != null ? status.name() : null;

        Page<Projeto> projetos =
                projetoRepository.buscarComFiltros(nome, statusParam, null, null, pageable);

        projetos.forEach(this::aplicarRisco);

        return projetos.map(this::montarResponse);
    }

    @Override
    public ProjetoResponseDTO atualizar(UUID id, ProjetoDTO dto) {
        Projeto projeto = buscarProjeto(id);

        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setDataInicio(dto.dataInicio());
        projeto.setDataPrevisaoTermino(dto.dataPrevisaoTermino());
        projeto.setDataRealTermino(dto.dataRealTermino());
        projeto.setOrcamento(dto.orcamento());
        projeto.setGerenteId(dto.gerenteId());

        projeto.getMembros().clear();

        adicionarGerenteComoMembro(projeto, dto.gerenteId());
        projeto.getMembros().addAll(mapearMembros(dto.membrosIds(), projeto));

        validarProjeto(projeto);

        fluxoStatusProjetoService.validarTransicao(projeto.getStatus(), dto.status());
        projeto.setStatus(dto.status());

        if (dto.status() == StatusProjeto.ENCERRADO && projeto.getDataRealTermino() == null) {
            projeto.setDataRealTermino(LocalDate.now());
        }

        aplicarRisco(projeto);

        Projeto salvo = projetoRepository.save(projeto);
        return montarResponse(salvo);
    }

    @Override
    public void excluir(UUID id) {
        Projeto projeto = buscarProjeto(id);
        validacaoProjetoService.validarExclusao(projeto);
        projetoRepository.delete(projeto);
    }

    @Override
    public ProjetoResponseDTO atualizarStatus(UUID id, StatusProjeto novoStatus) {
        Projeto projeto = buscarProjeto(id);

        fluxoStatusProjetoService.validarTransicao(projeto.getStatus(), novoStatus);
        projeto.setStatus(novoStatus);

        if (novoStatus == StatusProjeto.ENCERRADO && projeto.getDataRealTermino() == null) {
            projeto.setDataRealTermino(LocalDate.now());
        }

        Projeto salvo = projetoRepository.save(projeto);
        aplicarRisco(salvo);

        return montarResponse(projeto);
    }

    @Override
    public ProjetoResponseDTO adicionarMembro(UUID projetoId, String membroId) {
        alocacaoMembroService.adicionarMembro(projetoId, membroId);

        Projeto projeto = buscarProjeto(projetoId);
        aplicarRisco(projeto);

        return montarResponse(projeto);
    }

    @Override
    public ProjetoResponseDTO removerMembro(UUID projetoId, String membroId) {
        alocacaoMembroService.removerMembro(projetoId, membroId);

        Projeto projeto = buscarProjeto(projetoId);
        aplicarRisco(projeto);

        return montarResponse(projeto);
    }

    private Projeto buscarProjeto(UUID id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));
    }

    private void validarProjeto(Projeto projeto) {
        validacaoProjetoService.validarDatas(projeto);
        validacaoProjetoService.validarOrcamento(projeto);
        validacaoProjetoService.validarGerenteEhFuncionario(projeto);
        validacaoProjetoService.validarMembrosSaoFuncionarios(projeto);
        validacaoProjetoService.validarQuantidadeMembros(projeto);

        projeto.getMembros().stream()
                .map(ProjetoMembro::getMembroId)
                .distinct()
                .forEach(alocacaoMembroService::validarLimiteProjetosDoMembro);
    }

    private void aplicarRisco(Projeto projeto) {
        ClassificacaoRisco risco = riscoProjetoService.calcularRisco(projeto);
        projeto.setRisco(risco);
    }

 void adicionarGerenteComoMembro(Projeto projeto, String gerenteId) {

        if (gerenteId == null) {
            throw new RegraNegocioException("Projeto deve possuir um gerente.");
        }

        boolean gerenteJaExiste = projeto.getMembros() != null &&
                projeto.getMembros().stream()
                        .anyMatch(m -> m.getMembroId().equals(gerenteId));

        if (!gerenteJaExiste) {
            ProjetoMembro gerente = new ProjetoMembro(gerenteId, PapelProjeto.GERENTE);
            gerente.setProjeto(projeto);

            projeto.getMembros().add(gerente);
        }
    }

    private List<ProjetoMembro> mapearMembros(List<String> membrosIds, Projeto projeto) {

        if (membrosIds == null || membrosIds.isEmpty()) {
            return new ArrayList<>();
        }

        return membrosIds.stream()
                .distinct()
                .filter(id -> !id.equals(projeto.getGerenteId()))
                .map(id -> {
                    ProjetoMembro pm = new ProjetoMembro(id,  PapelProjeto.PARTICIPANTE);
                    pm.setProjeto(projeto);
                    return pm;
                })
                .toList();
    }

    private List<MembroDTO> montarMembrosResponse(Projeto projeto) {

        if (projeto.getMembros() == null || projeto.getMembros().isEmpty()) {
            return Collections.emptyList();
        }

        return projeto.getMembros().stream()
                .map(pm -> {
                    MembroDTO membroExterno =
                            membroExternoService.buscarPorId(pm.getMembroId());

                    return new MembroDTO(
                            membroExterno.id(),
                            membroExterno.nome(),
                            membroExterno.atribuicao()
                    );
                })
                .toList();
    }

    private ProjetoResponseDTO montarResponse(Projeto projeto) {

        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getDataInicio(),
                projeto.getDataPrevisaoTermino(),
                projeto.getDataRealTermino(),
                projeto.getOrcamento(),
                projeto.getStatus(),
                projeto.getRisco(),
                projeto.getGerenteId(),
                montarMembrosResponse(projeto)
        );
    }
}