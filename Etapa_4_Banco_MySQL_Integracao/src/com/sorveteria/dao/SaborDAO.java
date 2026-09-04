package com.sorveteria.dao;

import com.sorveteria.model.Sabor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para operações CRUD de Sabores no MySQL (Etapa 4).
 */
public class SaborDAO {

    public void inserir(Sabor sabor) throws SQLException {
        String sql = "INSERT INTO sabores (nome, descricao, tipo, disponivel) VALUES (?, ?, ?, ?)";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getDescricao());
            stmt.setString(3, sabor.getTipo());
            stmt.setBoolean(4, sabor.isDisponivel());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sabor.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Sabor sabor) throws SQLException {
        String sql = "UPDATE sabores SET nome = ?, descricao = ?, tipo = ?, disponivel = ? WHERE id = ?";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getDescricao());
            stmt.setString(3, sabor.getTipo());
            stmt.setBoolean(4, sabor.isDisponivel());
            stmt.setInt(5, sabor.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM sabores WHERE id = ?";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Sabor> listarTodos() throws SQLException {
        List<Sabor> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, tipo, disponivel FROM sabores ORDER BY nome ASC";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Sabor s = new Sabor(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("tipo"),
                    rs.getBoolean("disponivel")
                );
                lista.add(s);
            }
        }
        return lista;
    }

    public List<Sabor> listarDisponiveis() throws SQLException {
        List<Sabor> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, tipo, disponivel FROM sabores WHERE disponivel = TRUE ORDER BY nome ASC";
        try (Connection conn = FabricaConexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Sabor(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("tipo"),
                    rs.getBoolean("disponivel")
                ));
            }
        }
        return lista;
    }
}
