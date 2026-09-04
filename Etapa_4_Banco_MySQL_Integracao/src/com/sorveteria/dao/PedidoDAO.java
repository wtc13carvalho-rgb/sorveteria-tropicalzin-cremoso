package com.sorveteria.dao;

import com.sorveteria.model.*;
import java.sql.*;

/**
 * Data Access Object para Registro de Pedidos com Transação Atômica no MySQL (Etapa 4).
 */
public class PedidoDAO {

    public void salvarPedido(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (cliente_id, atendente_id, data_hora, forma_pagamento, status, valor_total, desconto) "
                         + "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal, observacao) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlItemSabor = "INSERT INTO item_pedido_sabores (item_pedido_id, sabor_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = FabricaConexao.getConexao();
            conn.setAutoCommit(false); // Início da transação

            // 1. Inserir Pedido
            int pedidoId;
            try (PreparedStatement stmtP = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                if (pedido.getCliente() != null && pedido.getCliente().getId() > 0) {
                    stmtP.setInt(1, pedido.getCliente().getId());
                } else {
                    stmtP.setNull(1, Types.INTEGER);
                }

                if (pedido.getAtendente() != null && pedido.getAtendente().getId() > 0) {
                    stmtP.setInt(2, pedido.getAtendente().getId());
                } else {
                    stmtP.setNull(2, Types.INTEGER);
                }

                stmtP.setString(3, pedido.getFormaPagamento());
                stmtP.setString(4, pedido.getStatus());
                stmtP.setDouble(5, pedido.getValorTotal());
                stmtP.setDouble(6, pedido.getDesconto());
                stmtP.executeUpdate();

                try (ResultSet rs = stmtP.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedidoId = rs.getInt(1);
                        pedido.setId(pedidoId);
                    } else {
                        throw new SQLException("Falha ao obter o ID do pedido gerado.");
                    }
                }
            }

            // 2. Inserir Itens do Pedido e seus Sabores
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtSabor = conn.prepareStatement(sqlItemSabor)) {

                for (ItemPedido item : pedido.getItens()) {
                    stmtItem.setInt(1, pedidoId);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.setDouble(5, item.getSubtotal());
                    stmtItem.setString(6, item.getObservacao());
                    stmtItem.executeUpdate();

                    int itemId;
                    try (ResultSet rsItem = stmtItem.getGeneratedKeys()) {
                        if (rsItem.next()) {
                            itemId = rsItem.getInt(1);
                        } else {
                            throw new SQLException("Falha ao obter ID do item do pedido.");
                        }
                    }

                    // Sabores do item
                    if (item.getSabores() != null) {
                        for (Sabor s : item.getSabores()) {
                            if (s.getId() > 0) {
                                stmtSabor.setInt(1, itemId);
                                stmtSabor.setInt(2, s.getId());
                                stmtSabor.executeUpdate();
                            }
                        }
                    }
                }
            }

            conn.commit(); // Confirmação da transação
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverte em caso de falha
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
