package com.sorveteria.dao;

import com.sorveteria.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Produtos no MySQL (Etapa 4).
 */
public class ProdutoDAO {

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, categoria, max_bolas, estoque) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setString(3, p.getCategoria());
            stmt.setInt(4, p.getMaxBolas());
            stmt.setInt(5, p.getEstoque());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, categoria = ?, max_bolas = ?, estoque = ? WHERE id = ?";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setString(3, p.getCategoria());
            stmt.setInt(4, p.getMaxBolas());
            stmt.setInt(5, p.getEstoque());
            stmt.setInt(6, p.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT id, nome, preco, categoria, max_bolas, estoque FROM produtos ORDER BY preco ASC";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getString("categoria"),
                    rs.getInt("max_bolas"),
                    rs.getInt("estoque")
                ));
            }
        }
        return lista;
    }
}
