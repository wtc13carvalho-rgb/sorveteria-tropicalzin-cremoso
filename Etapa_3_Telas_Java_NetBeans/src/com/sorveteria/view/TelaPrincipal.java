package com.sorveteria.view;

import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * Tela Principal / Dashboard do Sistema Desktop (Etapa 3).
 * Centraliza a navegação entre todas as telas e exibe a barra de status.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("Tropicalzin Cremoso - Sistema de Gestão e Frente de Caixa Desktop");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. BARRA DE MENUS SUPERIOR
        JMenuBar menuBar = new JMenuBar();

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.setMnemonic(KeyEvent.VK_A);
        JMenuItem mnuSair = new JMenuItem("Sair do Sistema");
        mnuSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        mnuSair.addActionListener(e -> fecharSistema());
        menuArquivo.add(mnuSair);
        menuBar.add(menuArquivo);

        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        menuCadastros.setMnemonic(KeyEvent.VK_C);

        JMenuItem mnuSabores = new JMenuItem("Gestão de Sabores (Alt+S)");
        mnuSabores.setMnemonic(KeyEvent.VK_S);
        mnuSabores.addActionListener(e -> abrirTelaSabores());

        JMenuItem mnuProdutos = new JMenuItem("Catálogo de Produtos (Alt+P)");
        mnuProdutos.setMnemonic(KeyEvent.VK_P);
        mnuProdutos.addActionListener(e -> abrirTelaProdutos());

        JMenuItem mnuClientes = new JMenuItem("Cadastro de Clientes (Alt+L)");
        mnuClientes.setMnemonic(KeyEvent.VK_L);
        mnuClientes.addActionListener(e -> abrirTelaClientes());

        menuCadastros.add(mnuSabores);
        menuCadastros.add(mnuProdutos);
        menuCadastros.addSeparator();
        menuCadastros.add(mnuClientes);
        menuBar.add(menuCadastros);

        // Menu Operações / Caixa
        JMenu menuVendas = new JMenu("Atendimento e Caixa");
        menuVendas.setMnemonic(KeyEvent.VK_T);

        JMenuItem mnuNovoPedido = new JMenuItem("Novo Pedido / Caixa (F2)");
        mnuNovoPedido.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        mnuNovoPedido.addActionListener(e -> abrirTelaPedido());

        menuVendas.add(mnuNovoPedido);
        menuBar.add(menuVendas);

        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem mnuSobre = new JMenuItem("Sobre o Projeto Integrador");
        mnuSobre.addActionListener(e -> exibirSobre());
        menuAjuda.add(mnuSobre);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);

        // 2. PAINEL SUPERIOR DE IDENTIFICAÇÃO (BANNER)
        JPanel pnlBanner = new JPanel(new BorderLayout());
        pnlBanner.setBackground(new Color(2, 132, 199)); // Azul Celeste
        pnlBanner.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        Usuario logado = RepositorioMemoria.getInstancia().getUsuarioLogado();
        JLabel lblTitulo = new JLabel("Sorveteria Tropicalzin Cremoso");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel("Operador: " + logado.getNome() + " | Nível: " + logado.getPerfil());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(224, 242, 254));

        pnlBanner.add(lblTitulo, BorderLayout.NORTH);
        pnlBanner.add(lblUsuario, BorderLayout.SOUTH);
        add(pnlBanner, BorderLayout.NORTH);

        // 3. PAINEL CENTRAL COM CARDS DE AÇÃO RÁPIDA (DASHBOARD)
        JPanel pnlCentro = new JPanel(new GridLayout(2, 2, 20, 20));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        pnlCentro.setBackground(new Color(241, 245, 249));

        // Card 1: Novo Pedido / Frente de Caixa
        pnlCentro.add(criarCardAcao(
            "Frente de Caixa & Pedidos (F2)", 
            "Atendimento de clientes, seleção de recipientes, cálculo total e sorteador aleatório de sabores.",
            new Color(5, 150, 105), // Verde Esmeralda
            e -> abrirTelaPedido()
        ));

        // Card 2: Gestão de Sabores
        pnlCentro.add(criarCardAcao(
            "Gestão de Sabores de Sorvete", 
            "Cadastre novos sabores artesanais, altere descrições e controle a disponibilidade em balcão.",
            new Color(37, 99, 235), // Azul Cobalto
            e -> abrirTelaSabores()
        ));

        // Card 3: Produtos e Recipientes
        pnlCentro.add(criarCardAcao(
            "Catálogo de Produtos & Recipientes", 
            "Configuração de casquinhas, copos de 300ml/500ml, potes térmicos de 1L e limites de bolas.",
            new Color(124, 58, 237), // Roxo
            e -> abrirTelaProdutos()
        ));

        // Card 4: Clientes
        pnlCentro.add(criarCardAcao(
            "Cadastro de Clientes", 
            "Registro de clientes cadastrados, histórico para fidelidade e localização rápida por CPF.",
            new Color(217, 119, 6), // Laranja Âmbar
            e -> abrirTelaClientes()
        ));

        add(pnlCentro, BorderLayout.CENTER);

        // 4. BARRA DE STATUS INFERIOR
        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBackground(new Color(226, 232, 240));
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));

        JLabel lblStatus = new JLabel("● Sistema Online | Modo de Teste em Memória (Etapa 3)");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(15, 23, 42));

        JLabel lblAtalhos = new JLabel("Atalhos: [F2: Novo Pedido] [Alt+S: Sabores] [Alt+P: Produtos] [Alt+L: Clientes]");
        lblAtalhos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAtalhos.setForeground(new Color(71, 85, 105));

        pnlStatus.add(lblStatus, BorderLayout.WEST);
        pnlStatus.add(lblAtalhos, BorderLayout.EAST);
        add(pnlStatus, BorderLayout.SOUTH);
    }

    private JPanel criarCardAcao(String titulo, String descricao, Color corTema, java.awt.event.ActionListener acao) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel topoCard = new JPanel(new BorderLayout());
        topoCard.setBackground(Color.WHITE);
        JLabel lblCardTit = new JLabel(titulo);
        lblCardTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCardTit.setForeground(corTema);
        topoCard.add(lblCardTit, BorderLayout.CENTER);
        card.add(topoCard, BorderLayout.NORTH);

        JTextArea txtDesc = new JTextArea(descricao);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDesc.setForeground(new Color(71, 85, 105));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(Color.WHITE);
        card.add(txtDesc, BorderLayout.CENTER);

        JButton btnAcessar = new JButton("Abrir Módulo →");
        btnAcessar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAcessar.setBackground(corTema);
        btnAcessar.setForeground(Color.WHITE);
        btnAcessar.setFocusPainted(false);
        btnAcessar.addActionListener(acao);
        card.add(btnAcessar, BorderLayout.SOUTH);

        return card;
    }

    private void abrirTelaSabores() {
        new TelaCadastroSabor(this).setVisible(true);
    }

    private void abrirTelaProdutos() {
        new TelaCadastroProduto(this).setVisible(true);
    }

    private void abrirTelaClientes() {
        new TelaCadastroCliente(this).setVisible(true);
    }

    private void abrirTelaPedido() {
        new TelaPedido(this).setVisible(true);
    }

    private void exibirSobre() {
        JOptionPane.showMessageDialog(this,
            "Sistema Sorveteria Tropicalzin Cremoso v1.0\n" +
            "Projeto Integrador Assistente de Desenvolvimento de Sistemas (TDS - Senac)\n" +
            "Autores: Wallace Teixeira Carvalho / Edcarlos Cardôso de Farias\n\n" +
            "Etapa 3: Telas em Java Desktop com navegação funcional e dados simulados.",
            "Sobre o Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void fecharSistema() {
        int op = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente sair do sistema Tropicalzin Cremoso?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
