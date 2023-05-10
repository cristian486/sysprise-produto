package br.com.sysprise.produto.service;

import br.com.sysprise.produto.model.*;
import br.com.sysprise.produto.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pb.*;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    @GrpcClient("categoria")
    private CategoriaServiceGrpc.CategoriaServiceBlockingStub categoriaStub;

    @GrpcClient("unidade")
    private UnidadeServiceGrpc.UnidadeServiceBlockingStub unidadeStub;

    public Produto findProdutoById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("O produto requisitado não foi encontrado!"));
    }

    public Page<DadosListagemProduto> listar(Pageable pageable) {
        return produtoRepository.findProdutoByHabilitadoTrue(pageable).map(DadosListagemProduto::new);
    }

    public DadosDetalhamentoProduto detalhar(Long id) {
        Produto produto = this.findProdutoById(id);
        Categoria categoria = this.getCategoriaDoProduto(produto.getCategoriaId());
        Unidade unidade = this.getUnidadeDoProduto(produto.getUnidadeId());
        return new DadosDetalhamentoProduto(produto, unidade, categoria);
    }

    public DadosDetalhamentoProduto cadastrar(DadosCadastroProduto dadosCadastro) {
        Categoria categoria = this.getCategoriaDoProduto(dadosCadastro.categoria_id());
        Unidade unidade = this.getUnidadeDoProduto(dadosCadastro.unidade_id());

        if(categoria == null || unidade == null)
            throw new IllegalArgumentException("Não foi possível encontrar a categoria ou unidade informados!");

        Produto produto = new Produto(dadosCadastro);
        produtoRepository.save(produto);
        return new DadosDetalhamentoProduto(produto, unidade, categoria);
    }

    public DadosDetalhamentoProduto atualizar(DadosAtualizarProduto dadosAtualizar) {
        Produto produto = this.findProdutoById(dadosAtualizar.id());
        // TODO
        produto.atualizarCadastro(dadosAtualizar);
        return this.detalhar(produto.getId());
    }

    public void deletar(Long id) {
        Produto produto = this.findProdutoById(id);
        produto.desabilitarCadastro();
    }

    private Categoria getCategoriaDoProduto(Long id) {
        return categoriaStub.getCategoria(pb.CategoriaId.newBuilder().setId(id).build());
    }

    private Unidade getUnidadeDoProduto(Long id) {
        return unidadeStub.getUnidade(UnidadeId.newBuilder().setId(id).build());
    }
}
