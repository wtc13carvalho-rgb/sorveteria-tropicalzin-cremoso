package com.sorveteria.view;

import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de Gestão de Produtos / Recipientes (Etapa 3).
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

    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnExcluir;

    public TelaCadastroProduto(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Catálogo de Produtos e Recipientes", true);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(124, 58, 237)); // Roxo
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Catálogo de Produtos, Recipientes e Preços");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Central
        JPanel pnlConteudo = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlConteudo.setBackground(new Color(248, 250, 252));

        // Formulário
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Dados do Produto"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        pnlForm.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtId, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nome do Item:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        // Preço
        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Preço Unitário (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPreco = new JTextField();
        pnlForm.add(txtPreco, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbCategoria = new JComboBox<>(new String[]{"CASQUINHA", "COPO", "TAÇA", "POTE", "PICOLÉ"});
        pnlForm.add(cbCategoria, gbc);

        // Max Bolas
        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("Limite de Bolas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        spMaxBolas = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        pnlForm.add(spMaxBolas, gbc);

        // Estoque
        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("Estoque Disp.:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        spEstoque = new JSpinner(new SpinnerNumberModel(50, 0, 9999, 1));
        pnlForm.add(spEstoque, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        pnlBotoes.setBackground(Color.WHITE);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(5, 150, 105));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvarProduto());

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(100, 116, 139));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.addActionListener(e -> limparCampos());

        btnExcluir = new JButton("Excluir");
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
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProdutos = new JTable(tableModel);
        tblProdutos.setRowHeight(24);
        tblProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherForm();
        });
        pnlTabela.add(new JScrollPane(tblProdutos), BorderLayout.CENTER);
        pnlConteudo.add(pnlTabela);

        add(pnlConteudo, BorderLayout.CENTER);

        // Rodapé
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        List<Produto> lista = RepositorioMemoria.getInstancia().getProdutos();
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
            for (Produto p : RepositorioMemoria.getInstancia().getProdutos()) {
                if (p.getId() == id) {
                    txtId.setText(String.valueOf(p.getId()));
                    txtNome.setText(p.getNome());
                    txtPreco.setText(String.valueOf(p.getPreco()));
                    cbCategoria.setSelectedItem(p.getCategoria());
                    spMaxBolas.setValue(p.getMaxBolas());
                    spEstoque.setValue(p.getEstoque());
                    break;
                }
            }
        }
    }

    private void salvarProduto() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");
        if (nome.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome e o preço do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            RepositorioMemoria.getInstancia().adicionarProduto(novo);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int id = Integer.parseInt(txtId.getText());
            Produto editado = new Produto(id, nome, preco, cat, maxBolas, estoque);
            RepositorioMemoria.getInstancia().atualizarProduto(editado);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        limparCampos();
        carregarTabela();
    }

    private void excluirProduto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int op = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            RepositorioMemoria.getInstancia().removerProduto(id);
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
