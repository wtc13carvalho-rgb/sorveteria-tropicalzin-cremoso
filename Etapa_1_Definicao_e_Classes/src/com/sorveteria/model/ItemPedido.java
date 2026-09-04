package com.sorveteria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um item selecionado dentro de um pedido (Produto + Sabores escolhidos + Quantidade).
 */
public class ItemPedido {
    private int id;
    private Produto produto;
    private List<Sabor> sabores;
    private int quantidade;
    private double precoUnitario;
    private String observacao;

    public ItemPedido() {
        this.sabores = new ArrayList<>();
        this.quantidade = 1;
    }

    public ItemPedido(int id, Produto produto, List<Sabor> sabores, int quantidade, double precoUnitario, String observacao) {
        this.id = id;
        this.produto = produto;
        this.sabores = sabores != null ? sabores : new ArrayList<>();
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacao = observacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null) {
            this.precoUnitario = produto.getPreco();
        }
    }

    public List<Sabor> getSabores() {
        return sabores;
    }

    public void setSabores(List<Sabor> sabores) {
        this.sabores = sabores;
    }

    public void adicionarSabor(Sabor sabor) {
        if (this.sabores == null) {
            this.sabores = new ArrayList<>();
        }
        this.sabores.add(sabor);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public double getSubtotal() {
        return this.precoUnitario * this.quantidade;
    }

    public String getNomesSabores() {
        if (sabores == null || sabores.isEmpty()) {
            return "Sem sabores definidos";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sabores.size(); i++) {
            sb.append(sabores.get(i).getNome());
            if (i < sabores.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%dx %s [%s] - R$ %.2f", 
            quantidade, 
            produto != null ? produto.getNome() : "Item", 
            getNomesSabores(), 
            getSubtotal());
    }
}
