package com.sorveteria.model;

/**
 * Representa um formato de produto ou recipiente vendido (ex: Casquinha, Copo 300ml, Pote 1L).
 */
public class Produto {
    private int id;
    private String nome;
    private double preco;
    private String categoria; // "CASQUINHA", "COPO", "TAÇA", "POTE", "PICOLÉ"
    private int maxBolas;     // Quantidade máxima de bolas/sabores permitidos
    private int estoque;

    public Produto() {}

    public Produto(int id, String nome, double preco, String categoria, int maxBolas, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.maxBolas = maxBolas;
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getMaxBolas() {
        return maxBolas;
    }

    public void setMaxBolas(int maxBolas) {
        this.maxBolas = maxBolas;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return String.format("%s (R$ %.2f - até %d bola(s))", nome, preco, maxBolas);
    }
}
