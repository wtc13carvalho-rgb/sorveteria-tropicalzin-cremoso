package com.sorveteria.view;

import com.sorveteria.dao.SaborDAO;
import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Sabor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de Gestão de Sabores integrada ao MySQL via SaborDAO (Etapa 4).
 */
public class TelaCadastroSabor extends JDialog {
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtDescricao;
    private JComboBox<String> cbTipo;
    private JCheckBox chkDisponivel;
    private JTable tblSabores;
    private DefaultTableModel tableModel;

    private SaborDAO saborDAO = new SaborDAO();
    private boolean modoBanco = true;

    public TelaCadastroSabor(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Gestão de Sabores (MySQL)", true);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(30, 58, 138));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Gerenciamento de Sabores de Sorvete (Persistência MySQL)");
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
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Dados do Sabor (MySQL)"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(new JLabel("Código ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nome do Sabor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtDescricao = new JTextField();
        pnlForm.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbTipo = new JComboBox<>(new String[]{"TRADICIONAL", "ESPECIAL", "ZERO_ACUCAR"});
        pnlForm.add(cbTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        chkDisponivel = new JCheckBox("Disponível para venda imediata no balcão", true);
        chkDisponivel.setBackground(Color.WHITE);
        pnlForm.add(chkDisponivel, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel pnlBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        pnlBotoesForm.setBackground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar no Banco");
        btnSalvar.setBackground(new Color(5, 150, 105));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvarSabor());

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(100, 116, 139));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.addActionListener(e -> limparCampos());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 38, 38));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirSabor());

        pnlBotoesForm.add(btnSalvar);
        pnlBotoesForm.add(btnLimpar);
        pnlBotoesForm.add(btnExcluir);
        pnlForm.add(pnlBotoesForm, gbc);

        pnlConteudo.add(pnlForm);

        // Tabela
        JPanel pnlTabela = new JPanel(new BorderLayout(5, 5));
        pnlTabela.setBackground(Color.WHITE);
        pnlTabela.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Sabores Registrados"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Categoria", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblSabores = new JTable(tableModel);
        tblSabores.setRowHeight(24);
        tblSabores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherForm();
        });

        pnlTabela.add(new JScrollPane(tblSabores), BorderLayout.CENTER);
        pnlConteudo.add(pnlTabela);
        add(pnlConteudo, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnFechar = new JButton("Voltar ao Menu Principal");
        btnFechar.addActionListener(e -> dispose());
        pnlFooter.add(btnFechar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        List<Sabor> lista;
        try {
            lista = saborDAO.listarTodos();
            modoBanco = true;
        } catch (SQLException e) {
            modoBanco = false;
            lista = RepositorioMemoria.getInstancia().getSabores();
        }

        for (Sabor s : lista) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getNome(),
                s.getTipo(),
                s.isDisponivel() ? "Disponível" : "Esgotado"
            });
        }
    }

    private void preencherForm() {
        int sel = tblSabores.getSelectedRow();
        if (sel >= 0) {
            int id = (int) tableModel.getValueAt(sel, 0);
            txtId.setText(String.valueOf(id));
            txtNome.setText((String) tableModel.getValueAt(sel, 1));
            cbTipo.setSelectedItem((String) tableModel.getValueAt(sel, 2));
            chkDisponivel.setSelected("Disponível".equals(tableModel.getValueAt(sel, 3)));
        }
    }

    private void salvarSabor() {
        String nome = txtNome.getText().trim();
        String desc = txtDescricao.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();
        boolean disp = chkDisponivel.isSelected();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do sabor.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtId.getText().isEmpty()) {
            Sabor novo = new Sabor(0, nome, desc, tipo, disp);
            if (modoBanco) {
                try {
                    saborDAO.inserir(novo);
                    JOptionPane.showMessageDialog(this, "Sabor gravado no MySQL!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().adicionarSabor(novo);
                    JOptionPane.showMessageDialog(this, "Salvo via fallback em memória.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                RepositorioMemoria.getInstancia().adicionarSabor(novo);
            }
        } else {
            int id = Integer.parseInt(txtId.getText());
            Sabor edit = new Sabor(id, nome, desc, tipo, disp);
            if (modoBanco) {
                try {
                    saborDAO.atualizar(edit);
                    JOptionPane.showMessageDialog(this, "Sabor atualizado no MySQL!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().atualizarSabor(edit);
                }
            } else {
                RepositorioMemoria.getInstancia().atualizarSabor(edit);
            }
        }

        limparCampos();
        carregarTabela();
    }

    private void excluirSabor() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um sabor na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir do banco?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (modoBanco) {
                try {
                    saborDAO.excluir(id);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().removerSabor(id);
                }
            } else {
                RepositorioMemoria.getInstancia().removerSabor(id);
            }
            limparCampos();
            carregarTabela();
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        cbTipo.setSelectedIndex(0);
        chkDisponivel.setSelected(true);
        tblSabores.clearSelection();
    }
}
