package com.sorveteria.view;

import com.sorveteria.dao.ClienteDAO;
import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de Cadastro de Clientes com integração MySQL via ClienteDAO (Etapa 4).
 */
public class TelaCadastroCliente extends JDialog {
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTable tblClientes;
    private DefaultTableModel tableModel;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private boolean modoBanco = true;

    public TelaCadastroCliente(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Cadastro de Clientes (MySQL)", true);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setSize(850, 480);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(217, 119, 6));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Cadastro e Fidelização de Clientes (Persistência MySQL)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlConteudo = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlConteudo.setBackground(new Color(248, 250, 252));

        // Formulário
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Dados Pessoais (MySQL)"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtCpf = new JTextField();
        pnlForm.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Telefone / WhatsApp:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtTelefone = new JTextField();
        pnlForm.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        txtEmail = new JTextField();
        pnlForm.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        pnlBtns.setBackground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar no Banco");
        btnSalvar.setBackground(new Color(5, 150, 105));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvarCliente());

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(100, 116, 139));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.addActionListener(e -> limpar());

        pnlBtns.add(btnSalvar);
        pnlBtns.add(btnLimpar);
        pnlForm.add(pnlBtns, gbc);

        pnlConteudo.add(pnlForm);

        // Tabela
        JPanel pnlTabela = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblClientes = new JTable(tableModel);
        tblClientes.setRowHeight(24);
        pnlTabela.add(new JScrollPane(tblClientes), BorderLayout.CENTER);
        pnlConteudo.add(pnlTabela);

        add(pnlConteudo, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnFechar = new JButton("Voltar ao Menu");
        btnFechar.addActionListener(e -> dispose());
        pnlFooter.add(btnFechar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        List<Cliente> list;
        try {
            list = clienteDAO.listarTodos();
            modoBanco = true;
        } catch (SQLException e) {
            modoBanco = false;
            list = RepositorioMemoria.getInstancia().getClientes();
        }

        for (Cliente c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone()});
        }
    }

    private void salvarCliente() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String tel = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = new Cliente(0, nome, cpf, tel, email);
        if (modoBanco) {
            try {
                clienteDAO.inserir(c);
                JOptionPane.showMessageDialog(this, "Cliente gravado no MySQL!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                RepositorioMemoria.getInstancia().adicionarCliente(c);
            }
        } else {
            RepositorioMemoria.getInstancia().adicionarCliente(c);
        }
        limpar();
        carregarTabela();
    }

    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
    }
}
