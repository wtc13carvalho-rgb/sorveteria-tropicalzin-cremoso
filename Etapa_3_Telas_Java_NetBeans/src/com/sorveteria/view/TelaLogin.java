package com.sorveteria.view;

import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Tela de Autenticação de Usuários Desktop (Etapa 3).
 */
public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCancelar;

    public TelaLogin() {
        super("Tropicalzin Cremoso - Acesso ao Sistema v1.0");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Painel Superior (Cabeçalho)
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1));
        pnlHeader.setBackground(new Color(30, 58, 138)); // Azul Royal
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblTitulo = new JLabel("Sorveteria Tropicalzin Cremoso", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Controle de Balcão e Gestão Desktop", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(224, 242, 254));

        pnlHeader.add(lblTitulo);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Central (Campos)
        JPanel pnlCorpo = new JPanel(new GridBagLayout());
        pnlCorpo.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
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
        txtUsuario = new JTextField("admin"); // Usuário padrão de teste
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCorpo.add(txtUsuario, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCorpo.add(lblSenha, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        txtSenha = new JPasswordField("1234"); // Senha padrão
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCorpo.add(txtSenha, gbc);

        // Dica de acessibilidade
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
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

        // Atalhos de teclado
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

    private void efetuarLogin() {
        String login = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, informe o usuário e a senha.", 
                "Atenção", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = RepositorioMemoria.getInstancia().autenticar(login, senha);
        if (u != null) {
            JOptionPane.showMessageDialog(this, 
                "Bem-vindo(a), " + u.getNome() + "!\nPerfil de Acesso: " + u.getPerfil(), 
                "Acesso Concedido", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abre Tela Principal e descarta a de Login
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            telaPrincipal.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Credenciais inválidas!\nUtilize:\nUsuário: admin | Senha: 1234 (Gerente)\nou\nUsuário: atendente | Senha: 1234", 
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
