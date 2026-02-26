package ashleysaintlouis.github.portifolioprojetos.entity;

import ashleysaintlouis.github.portifolioprojetos.entity.projeto.PapelProjeto;
import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "projeto_membro",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"projeto_id", "membroId"})
        })
public class ProjetoMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String membroId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PapelProjeto papel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    protected ProjetoMembro() {}

    public ProjetoMembro(String membroId, PapelProjeto papel) {
        this.membroId = membroId;
        this.papel = papel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjetoMembro that)) return false;
        return Objects.equals(membroId, that.membroId) &&
                Objects.equals(projeto != null ? projeto.getId() : null,
                        that.projeto != null ? that.projeto.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membroId);
    }
}