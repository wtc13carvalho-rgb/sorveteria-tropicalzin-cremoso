package com.sorveteria.view;

import com.sorveteria.dao.FabricaConexao;
import com.sorveteria.dao.UsuarioDAO;
import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 * Tela de Autenticação com Validação no Banco de Dados MySQL (Etapa 4).
 */
public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCancelar;
    private JLabel lblStatusBanco;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public TelaLogin() {
        super("Tropicalzin Cremoso - Acesso ao Sistema v1.0 (MySQL)");
        initComponents();
        verificarStatusBanco();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Painel Superior (Cabeçalho)
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1));
        pnlHeader.setBackground(new Color(30, 58, 138));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblTitulo = new JLabel("Sorveteria Tropicalzin Cremoso", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Etapa 4 - Integração com Banco de Dados MySQL", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(224, 242, 254));

        pnlHeader.add(lblTitulo);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Central (Campos)
        JPanel pnlCorpo = new JPanel(new GridBagLayout());
        pnlCorpo.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));
        pnlCorpo.setBackground(new Color(248, 250, 252));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // Usuário
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblUser = new JLabel("Usuário:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCorpo.add(lblUser, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtUsuario = new JTextField("admin");
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCorpo.add(txtUsuario, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCorpo.add(lblSenha, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        txtSenha = new JPasswordField("1234");
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCorpo.add(txtSenha, gbc);

        // Status da Conexão com o Banco
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        lblStatusBanco = new JLabel("Verificando conexão com MySQL...", SwingConstants.CENTER);
        lblStatusBanco.setFont(new Font("Segoe UI", Font.BOLD, 10));
        pnlCorpo.add(lblStatusBanco, gbc);

        // Dica
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel lblDica = new JLabel("Dica: Pressione ENTER para entrar ou ESC para sair", SwingConstants.CENTER);
        lblDica.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblDica.setForeground(new Color(100, 116, 139));
        pnlCorpo.add(lblDica, gbc);

        add(pnlCorpo, BorderLayout.CENTER);

        // Painel Inferior (Botões)
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        pnlBotoes.setBackground(new Color(241, 245, 249));

        btnCancelar = new JButton("Cancelar (Esc)");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(220, 38, 38));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> System.exit(0));

        btnEntrar = new JButton("Entrar (F2)");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEntrar.setBackground(new Color(5, 150, 105));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.addActionListener(e -> efetuarLogin());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnEntrar);
        add(pnlBotoes, BorderLayout.SOUTH);

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    efetuarLogin();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        };
        txtUsuario.addKeyListener(enterListener);
        txtSenha.addKeyListener(enterListener);
    }

    private void verificarStatusBanco() {
        boolean conectado = FabricaConexao.testarConexao();
        if (conectado) {
            lblStatusBanco.setText("● MySQL Conectado (sorveteria_db)");
            lblStatusBanco.setForeground(new Color(5, 150, 105));
        } else {
            lblStatusBanco.setText("○ MySQL Off-line (Modo Fallback Ativo)");
            lblStatusBanco.setForeground(new Color(217, 119, 6));
        }
    }

    private void efetuarLogin() {
        String login = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe usuário e senha.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = null;
        boolean viaBanco = false;

        try {
            u = usuarioDAO.autenticar(login, senha);
            if (u != null) {
                viaBanco = true;
            }
        } catch (SQLException e) {
            System.err.println("Banco MySQL inacessível, tentando fallback em memória: " + e.getMessage());
        }

        // Fallback em memória caso o tutor não tenha iniciado o MySQL local
        if (u == null) {
            u = RepositorioMemoria.getInstancia().autenticar(login, senha);
        }

        if (u != null) {
            RepositorioMemoria.getInstancia().setUsuarioLogado(u);
            String origem = viaBanco ? "Banco de Dados MySQL" : "Modo Demonstração (Memória)";
            JOptionPane.showMessageDialog(this, 
                "Bem-vindo(a), " + u.getNome() + "!\nAutenticado via: " + origem + "\nPerfil: " + u.getPerfil(), 
                "Login com Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuário ou senha incorretos!\nUtilize:\nUsuário: admin | Senha: 1234", 
                "Erro de Login", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new TelaLogin().setVisible(true);
        });
    }
}
