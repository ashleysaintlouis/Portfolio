package ashleysaintlouis.github.portifolioprojetos.repository;

import ashleysaintlouis.github.portifolioprojetos.entity.ProjetoMembro;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.StatusProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjetoMembroRepository extends JpaRepository<ProjetoMembro, UUID> {

    @Query("SELECT COUNT(DISTINCT pm.membroId) FROM ProjetoMembro pm " +
           "JOIN pm.projeto p WHERE p.status NOT IN (:statusExcluidos)")
    Long countMembrosUnicosAlocados(@Param("statusExcluidos") List<StatusProjeto> statusExcluidos);

    @Query("SELECT COUNT(pm) FROM ProjetoMembro pm " +
           "JOIN pm.projeto p WHERE pm.membroId = :membroId " +
           "AND p.status NOT IN (:statusExcluidos)")
    Long countProjetosPorMembro(@Param("membroId") String membroId,
                                @Param("statusExcluidos") List<StatusProjeto> statusExcluidos);
}
