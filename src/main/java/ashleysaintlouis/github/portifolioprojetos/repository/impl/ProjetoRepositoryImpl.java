package ashleysaintlouis.github.portifolioprojetos.repository.impl;

import ashleysaintlouis.github.portifolioprojetos.dto.ProjetoRelatorioDTO;
import ashleysaintlouis.github.portifolioprojetos.repository.ProjetoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProjetoRepositoryImpl implements ProjetoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProjetoRelatorioDTO> buscarRelatorio(String status,
                                                     LocalDate dataInicio,
                                                     LocalDate dataFim) {

        StringBuilder jpql = new StringBuilder("""
            SELECT new ashleysaintlouis.github.portifolioprojetos.dto.ProjetoRelatorioDTO(
                CAST(p.id AS string),
                p.nome,
                p.gerenteId,
                p.status.toString(),
                p.orcamento,
                p.dataInicio
            )
            FROM Projeto p
            WHERE 1=1
        """);

        if (status != null && !status.isEmpty()) {
            jpql.append(" AND p.status = :status");
        }

        if (dataInicio != null) {
            jpql.append(" AND p.dataInicio >= :dataInicio");
        }

        if (dataFim != null) {
            jpql.append(" AND p.dataInicio <= :dataFim");
        }

        TypedQuery<ProjetoRelatorioDTO> query =
                entityManager.createQuery(jpql.toString(), ProjetoRelatorioDTO.class);

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        if (dataInicio != null) query.setParameter("dataInicio", dataInicio);
        if (dataFim != null) query.setParameter("dataFim", dataFim);

        return query.getResultList();
    }
}
