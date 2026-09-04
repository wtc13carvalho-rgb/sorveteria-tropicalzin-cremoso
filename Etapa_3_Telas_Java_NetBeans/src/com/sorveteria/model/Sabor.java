package com.sorveteria.model;

/**
 * Representa um sabor de sorvete disponível na sorveteria.
 */
public class Sabor {
    private int id;
    private String nome;
    private String descricao;
    private String tipo; // "TRADICIONAL", "ESPECIAL", "ZERO_ACUCAR"
    private boolean disponivel;

    public Sabor() {
        this.disponivel = true;
    }

    public Sabor(int id, String nome, String descricao, String tipo, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.disponivel = disponivel;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return nome + " [" + tipo + "]" + (disponivel ? "" : " (Esgotado)");
    }
}
