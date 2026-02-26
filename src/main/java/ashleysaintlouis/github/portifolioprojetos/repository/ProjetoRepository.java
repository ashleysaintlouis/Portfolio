package ashleysaintlouis.github.portifolioprojetos.repository;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import ashleysaintlouis.github.portifolioprojetos.repository.projecao.ProjetoStatusCount;
import ashleysaintlouis.github.portifolioprojetos.repository.projecao.ProjetoStatusTotal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProjetoRepository
        extends JpaRepository<Projeto, UUID>, ProjetoRepositoryCustom {


    @Query(value = """
        SELECT *
        FROM projetos p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:status IS NULL OR p.status = :status)
          AND (:dataInicio IS NULL OR p.data_inicio >= :dataInicio)
          AND (:dataFim IS NULL OR p.data_inicio <= :dataFim)
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM projetos p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:status IS NULL OR p.status = :status)
          AND (:dataInicio IS NULL OR p.data_inicio >= :dataInicio)
          AND (:dataFim IS NULL OR p.data_inicio <= :dataFim)
        """,
            nativeQuery = true)
    Page<Projeto> buscarComFiltros(
            @Param("nome") String nome,
            @Param("status") String status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );



    @Query("""
           SELECT p.status AS status, COUNT(p) AS quantidade
           FROM Projeto p
           GROUP BY p.status
           """)
    List<ProjetoStatusCount> contarProjetosPorStatus();


    @Query("""
           SELECT p.status AS status, COALESCE(SUM(p.orcamento), 0) AS total
           FROM Projeto p
           GROUP BY p.status
           """)
    List<ProjetoStatusTotal> somarOrcamentoPorStatus();


    @Query(value = """
        SELECT AVG(p.data_real_termino - p.data_inicio)
        FROM projetos p
        WHERE p.status = :status
          AND p.data_inicio IS NOT NULL
          AND p.data_real_termino IS NOT NULL
        """, nativeQuery = true)
    Double calcularMediaDuracaoPorStatus(@Param("status") String status);
}