package com.sorveteria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um pedido concluído ou em andamento na sorveteria.
 */
public class Pedido {
    private int id;
    private Cliente cliente;
    private Usuario atendente;
    private LocalDateTime dataHora;
    private List<ItemPedido> itens;
    private String formaPagamento; // "DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"
    private String status;         // "RECEBIDO", "EM_PREPARO", "ENTREGUE", "CANCELADO"
    private double valorTotal;
    private double desconto;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.dataHora = LocalDateTime.now();
        this.status = "RECEBIDO";
        this.formaPagamento = "PIX";
    }

    public Pedido(int id, Cliente cliente, Usuario atendente, String formaPagamento) {
        this();
        this.id = id;
        this.cliente = cliente;
        this.atendente = atendente;
        this.formaPagamento = formaPagamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
        recalcularTotal();
    }

    public void adicionarItem(ItemPedido item) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }
        this.itens.add(item);
        recalcularTotal();
    }

    public void removerItem(int index) {
        if (this.itens != null && index >= 0 && index < this.itens.size()) {
            this.itens.remove(index);
            recalcularTotal();
        }
    }

    public void recalcularTotal() {
        double sub = 0.0;
        if (itens != null) {
            for (ItemPedido item : itens) {
                sub += item.getSubtotal();
            }
        }
        this.valorTotal = Math.max(0.0, sub - this.desconto);
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
        recalcularTotal();
    }

    public String getDataFormatada() {
        if (dataHora == null) return "";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHora.format(fmt);
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d - %s - Total: R$ %.2f (%s)", 
            id, 
            cliente != null ? cliente.getNome() : "Consumidor Final", 
            valorTotal, 
            status);
    }
}
