package br.com.sysprise.produto.model;

public record DadosListagemProduto(Long id, String nome, String codigoDeBarras) {

    public DadosListagemProduto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getCodigoDeBarras());
    }
}
