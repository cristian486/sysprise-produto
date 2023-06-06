package br.com.sysprise.produto.controller;

import br.com.sysprise.produto.model.Produto;
import br.com.sysprise.produto.service.ProdutoService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pb.*;

@GrpcService
public class ProdutoGrpcController extends ProdutoServiceGrpc.ProdutoServiceImplBase {

    @Autowired
    private ProdutoService produtoService;

    @Override
    public void verifyProductExistence(ProdutoId request, StreamObserver<ProdutoExiste> responseObserver) {
        Boolean produtoExiste = produtoService.produtoExiste(request.getProdutoId());
        responseObserver.onNext(ProdutoExiste.newBuilder().setExiste(produtoExiste).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProductname(ProdutoId request, StreamObserver<ProductName> responseObserver) {
        Produto produto = produtoService.findProdutoById(request.getProdutoId());
        responseObserver.onNext(ProductName.newBuilder().setNome(produto.getNome()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProdutoVenda(ProdutoId request, StreamObserver<ProdutoVenda> responseObserver) {
        Produto produto = produtoService.findProdutoById(request.getProdutoId());

        ProdutoVenda produtoVenda = ProdutoVenda.newBuilder()
                .setPrecoDeVenda(produto.getPrecoDeVenda().doubleValue())
                .setVendaFracionada(produto.getVendaFracionada())
                .setId(produto.getId())
                .build();

        responseObserver.onNext(produtoVenda);
        responseObserver.onCompleted();
    }

    @Override
    public void getEstoqueMinimo(ProdutoId request, StreamObserver<EstoqueMinimo> responseObserver) {
        Produto produto = produtoService.findProdutoById(request.getProdutoId());
        EstoqueMinimo estoqueMinimo = EstoqueMinimo.newBuilder().setEstoqueMinimo(produto.getEstoqueMinimo()).build();
        responseObserver.onNext(estoqueMinimo);
        responseObserver.onCompleted();
    }

    @Override
    public void verifyIfExistsProductsAssociatedWithCategory(CategoriaId request, StreamObserver<ProdutoExiste> responseObserver) {
        Boolean existeProdutos = produtoService.existeProdutosAssociadosCategoria(request.getId());
        responseObserver.onNext(ProdutoExiste.newBuilder().setExiste(existeProdutos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void verifyIfExistsProductsAssociatedWithUnit(UnidadeId request, StreamObserver<ProdutoExiste> responseObserver) {
        Boolean existeProdutos = produtoService.existeProdutosAssociadosUnidade(request.getId());
        responseObserver.onNext(ProdutoExiste.newBuilder().setExiste(existeProdutos).build());
        responseObserver.onCompleted();
    }
}
