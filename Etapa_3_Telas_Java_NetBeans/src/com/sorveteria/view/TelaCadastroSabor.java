package com.sorveteria.view;

import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.Sabor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de Gestão / CRUD de Sabores de Sorvete (Etapa 3).
 */
public class TelaCadastroSabor extends JDialog {
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtDescricao;
    private JComboBox<String> cbTipo;
    private JCheckBox chkDisponivel;
    private JTable tblSabores;
    private DefaultTableModel tableModel;

    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnExcluir;
    private JButton btnFechar;

    public TelaCadastroSabor(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Gestão de Sabores", true);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(30, 58, 138));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Gerenciamento de Sabores de Sorvete");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Central Dividido (Esquerda: Form, Direita: Tabela)
        JPanel pnlConteudo = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlConteudo.setBackground(new Color(248, 250, 252));

        // Formulário (Esquerda)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Dados do Sabor"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(new JLabel("Código ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(241, 245, 249));
        pnlForm.add(txtId, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nome do Sabor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNome = new JTextField();
        pnlForm.add(txtNome, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtDescricao = new JTextField();
        pnlForm.add(txtDescricao, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbTipo = new JComboBox<>(new String[]{"TRADICIONAL", "ESPECIAL", "ZERO_ACUCAR"});
        pnlForm.add(cbTipo, gbc);

        // Disponível
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        chkDisponivel = new JCheckBox("Disponível para venda imediata no balcão", true);
        chkDisponivel.setBackground(Color.WHITE);
        pnlForm.add(chkDisponivel, gbc);

        // Botões do Formulário
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel pnlBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        pnlBotoesForm.setBackground(Color.WHITE);

        btnSalvar = new JButton("Salvar / Adicionar");
        btnSalvar.setBackground(new Color(5, 150, 105));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(e -> salvarSabor());

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(100, 116, 139));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.addActionListener(e -> limparCampos());

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 38, 38));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.addActionListener(e -> excluirSabor());

        pnlBotoesForm.add(btnSalvar);
        pnlBotoesForm.add(btnLimpar);
        pnlBotoesForm.add(btnExcluir);
        pnlForm.add(pnlBotoesForm, gbc);

        pnlConteudo.add(pnlForm);

        // Tabela (Direita)
        JPanel pnlTabela = new JPanel(new BorderLayout(5, 5));
        pnlTabela.setBackground(Color.WHITE);
        pnlTabela.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "Sabores em Linha"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Categoria", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSabores = new JTable(tableModel);
        tblSabores.setRowHeight(24);
        tblSabores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSabores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tblSabores);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        pnlConteudo.add(pnlTabela);

        add(pnlConteudo, BorderLayout.CENTER);

        // Rodapé com botão Fechar
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlFooter.setBackground(new Color(241, 245, 249));

        btnFechar = new JButton("Voltar ao Menu Principal");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFechar.addActionListener(e -> dispose());
        pnlFooter.add(btnFechar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        List<Sabor> lista = RepositorioMemoria.getInstancia().getSabores();
        for (Sabor s : lista) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getNome(),
                s.getTipo(),
                s.isDisponivel() ? "Disponível" : "Esgotado"
            });
        }
    }

    private void preencherFormularioComLinhaSelecionada() {
        int sel = tblSabores.getSelectedRow();
        if (sel >= 0) {
            int id = (int) tableModel.getValueAt(sel, 0);
            for (Sabor s : RepositorioMemoria.getInstancia().getSabores()) {
                if (s.getId() == id) {
                    txtId.setText(String.valueOf(s.getId()));
                    txtNome.setText(s.getNome());
                    txtDescricao.setText(s.getDescricao());
                    cbTipo.setSelectedItem(s.getTipo());
                    chkDisponivel.setSelected(s.isDisponivel());
                    break;
                }
            }
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
            // Novo
            Sabor novo = new Sabor(0, nome, desc, tipo, disp);
            RepositorioMemoria.getInstancia().adicionarSabor(novo);
            JOptionPane.showMessageDialog(this, "Sabor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Atualizar
            int id = Integer.parseInt(txtId.getText());
            Sabor atualizado = new Sabor(id, nome, desc, tipo, disp);
            RepositorioMemoria.getInstancia().atualizarSabor(atualizado);
            JOptionPane.showMessageDialog(this, "Sabor atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        limparCampos();
        carregarTabela();
    }

    private void excluirSabor() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um sabor na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o sabor selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            RepositorioMemoria.getInstancia().removerSabor(id);
            JOptionPane.showMessageDialog(this, "Sabor removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
