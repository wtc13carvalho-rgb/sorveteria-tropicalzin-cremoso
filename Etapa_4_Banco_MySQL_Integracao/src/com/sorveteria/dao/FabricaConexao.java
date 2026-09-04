package com.sorveteria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões JDBC com o banco de dados MySQL (Etapa 4).
 * Gerencia a abertura e encerramento de conexões seguras com o MySQL Workbench.
 */
public class FabricaConexao {
    private static final String HOST = "localhost";
    private static final String PORTA = "3306";
    private static final String BANCO = "sorveteria_db";
    private static final String USUARIO = "root";
    private static final String SENHA = ""; // Altere para a senha do seu MySQL local, se houver

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORTA + "/" + BANCO 
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    static {
        try {
            // Registra o Driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não localizado no Classpath: " + e.getMessage());
        }
    }

    /**
     * Obtém uma nova conexão ativa com o banco MySQL.
     * @return Conexão JDBC
     * @throws SQLException Em caso de falha de autenticação ou serviço inativo
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Testa se o banco de dados está online e acessível.
     * @return true se conectou com sucesso, false caso contrário
     */
    public static boolean testarConexao() {
        try (Connection conn = getConexao()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getUrl() {
        return URL;
    }
}
