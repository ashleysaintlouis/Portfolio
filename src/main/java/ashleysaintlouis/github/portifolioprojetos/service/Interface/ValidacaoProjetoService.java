package ashleysaintlouis.github.portifolioprojetos.service.Interface;


import ashleysaintlouis.github.portifolioprojetos.entity.projeto.Projeto;

public interface ValidacaoProjetoService {

    void validarDatas(Projeto projeto);

    void validarOrcamento(Projeto projeto);

    void validarExclusao(Projeto projeto);

    void validarQuantidadeMembros(Projeto projeto);

    void validarGerenteEhFuncionario(Projeto projeto);

    void validarMembrosSaoFuncionarios(Projeto projeto);

}