package ashleysaintlouis.github.portifolioprojetos.service.impl;

import ashleysaintlouis.github.portifolioprojetos.client.membro.dto.MembroDTO;
import ashleysaintlouis.github.portifolioprojetos.client.membro.service.MembroClient;
import ashleysaintlouis.github.portifolioprojetos.exception.RegraNegocioException;
import ashleysaintlouis.github.portifolioprojetos.service.Interface.MembroExternoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembroExternoServiceImpl implements MembroExternoService {

    private final MembroClient membroClient;

    @Override
    public MembroDTO buscarPorId(String id) {
        try {
            return membroClient.buscarPorId(id);
        } catch (Exception e) {
            throw new RegraNegocioException("Membro não encontrado: " + id);
        }
    }

    @Override
    public MembroDTO criarMembro(MembroDTO dto) {
        try {
            return membroClient.criar(dto);
        } catch (Exception e) {
            throw new RegraNegocioException("Erro ao criar membro: " + e.getMessage());
        }
    }

    @Override
    public boolean ehFuncionario(String id) {
        try {
            MembroDTO membro = buscarPorId(id);
            return "funcionário".equalsIgnoreCase(membro.atribuicao()) ||
                   "funcionario".equalsIgnoreCase(membro.atribuicao());
        } catch (Exception e) {
            return false;
        }
    }

    private List<MembroDTO> buscarMembrosPorIds(List<MembroDTO> membros) {
        return membros.stream()
                .map(m -> membroClient.buscarPorId(m.id()))
                .toList();
    }
}
