package ashleysaintlouis.github.portifolioprojetos.entity.projeto;

import ashleysaintlouis.github.portifolioprojetos.entity.ProjetoMembro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projetos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_previsao_termino", nullable = false)
    private LocalDate dataPrevisaoTermino;

    @Column(name = "data_real_termino")
    private LocalDate dataRealTermino;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal orcamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status = StatusProjeto.EM_ANALISE;

    @Column(name = "gerente_id")
    private String gerenteId;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjetoMembro> membros = new ArrayList<>();

    @Transient
    private ClassificacaoRisco risco;

}