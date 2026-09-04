package com.sorveteria.view;

import com.sorveteria.dao.ProdutoDAO;
import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de Gestão de Produtos integrada ao MySQL via ProdutoDAO (Etapa 4).
 */
public class TelaCadastroProduto extends JDialog {
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JComboBox<String> cbCategoria;
    private JSpinner spMaxBolas;
    private JSpinner spEstoque;
    private JTable tblProdutos;
    private DefaultTableModel tableModel;

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private boolean modoBanco = true;

    public TelaCadastroProduto(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Catálogo de Produtos e Recipientes (MySQL)", true);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(124, 58, 237));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Catálogo de Produtos e Preços (Persistência MySQL)");
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
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Dados do Produto (MySQL)"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        pnlForm.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nome do Item:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Preço Unitário (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPreco = new JTextField();
        pnlForm.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbCategoria = new JComboBox<>(new String[]{"CASQUINHA", "COPO", "TAÇA", "POTE", "PICOLÉ"});
        pnlForm.add(cbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("Limite de Bolas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        spMaxBolas = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        pnlForm.add(spMaxBolas, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("Estoque Disp.:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        spEstoque = new JSpinner(new SpinnerNumberModel(50, 0, 9999, 1));
        pnlForm.add(spEstoque, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        pnlBotoes.setBackground(Color.WHITE);

        JButton btnSalvar = new JButton("Salvar no Banco");
        btnSalvar.setBackground(new Color(5, 150, 105));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvarProduto());

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(100, 116, 139));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.addActionListener(e -> limparCampos());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 38, 38));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirProduto());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnLimpar);
        pnlBotoes.add(btnExcluir);
        pnlForm.add(pnlBotoes, gbc);

        pnlConteudo.add(pnlForm);

        // Tabela
        JPanel pnlTabela = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço (R$)", "Max Bolas", "Estoque"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProdutos = new JTable(tableModel);
        tblProdutos.setRowHeight(24);
        tblProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherForm();
        });
        pnlTabela.add(new JScrollPane(tblProdutos), BorderLayout.CENTER);
        pnlConteudo.add(pnlTabela);

        add(pnlConteudo, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        List<Produto> lista;
        try {
            lista = produtoDAO.listarTodos();
            modoBanco = true;
        } catch (SQLException e) {
            modoBanco = false;
            lista = RepositorioMemoria.getInstancia().getProdutos();
        }

        for (Produto p : lista) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                String.format("%.2f", p.getPreco()),
                p.getMaxBolas(),
                p.getEstoque()
            });
        }
    }

    private void preencherForm() {
        int sel = tblProdutos.getSelectedRow();
        if (sel >= 0) {
            int id = (int) tableModel.getValueAt(sel, 0);
            txtId.setText(String.valueOf(id));
            txtNome.setText((String) tableModel.getValueAt(sel, 1));
            txtPreco.setText(String.valueOf(tableModel.getValueAt(sel, 2)).replace(",", "."));
            spMaxBolas.setValue((int) tableModel.getValueAt(sel, 3));
            spEstoque.setValue((int) tableModel.getValueAt(sel, 4));
        }
    }

    private void salvarProduto() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");
        if (nome.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome e preço.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cat = (String) cbCategoria.getSelectedItem();
        int maxBolas = (int) spMaxBolas.getValue();
        int estoque = (int) spEstoque.getValue();

        if (txtId.getText().isEmpty()) {
            Produto novo = new Produto(0, nome, preco, cat, maxBolas, estoque);
            if (modoBanco) {
                try {
                    produtoDAO.inserir(novo);
                    JOptionPane.showMessageDialog(this, "Produto registrado no MySQL!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().adicionarProduto(novo);
                }
            } else {
                RepositorioMemoria.getInstancia().adicionarProduto(novo);
            }
        } else {
            int id = Integer.parseInt(txtId.getText());
            Produto edit = new Produto(id, nome, preco, cat, maxBolas, estoque);
            if (modoBanco) {
                try {
                    produtoDAO.atualizar(edit);
                    JOptionPane.showMessageDialog(this, "Produto atualizado no MySQL!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().atualizarProduto(edit);
                }
            } else {
                RepositorioMemoria.getInstancia().atualizarProduto(edit);
            }
        }

        limparCampos();
        carregarTabela();
    }

    private void excluirProduto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int op = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            if (modoBanco) {
                try {
                    produtoDAO.excluir(id);
                } catch (SQLException e) {
                    RepositorioMemoria.getInstancia().removerProduto(id);
                }
            } else {
                RepositorioMemoria.getInstancia().removerProduto(id);
            }
            limparCampos();
            carregarTabela();
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        cbCategoria.setSelectedIndex(0);
        spMaxBolas.setValue(1);
        spEstoque.setValue(50);
        tblProdutos.clearSelection();
    }
}
