package br.com.sysprise.produto.model;


import pb.Categoria;
import pb.Unidade;

import java.math.BigDecimal;

public record DadosDetalhamentoProduto(Long id, String nome, String descricao, String observacao, String codigoDeBarras,
                                       Double pesoBruto, Double pesoLiquido, BigDecimal precoDeCompra, BigDecimal precoDeVenda,
                                       Double altura, Double largura, Double profundidade, Double estoqueMinimo,
                                       Boolean vendaFracionada,
                                       DadosCategoria categoria, DadosUnidade unidade) {

    public DadosDetalhamentoProduto(Produto produto, Unidade unidade, Categoria categoria) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getObservacao(), produto.getCodigoDeBarras(),
                produto.getPesoBruto(), produto.getPesoLiquido(), produto.getPrecoDeCompra(), produto.getPrecoDeVenda(),
                produto.getAltura(), produto.getLargura(), produto.getProfundidade(), produto.getEstoqueMinimo(),
                produto.getVendaFracionada(), new DadosCategoria(categoria), new DadosUnidade(unidade));
    }

    private record DadosCategoria(Long id, String nome) {

        public DadosCategoria(Categoria categoria) {
            this(categoria.getId(), categoria.getNome());
        }
    }

    private record DadosUnidade(Long id,String nome) {

        public DadosUnidade(Unidade unidade) {
            this(unidade.getId(), unidade.getNome());
        }
    }
}
