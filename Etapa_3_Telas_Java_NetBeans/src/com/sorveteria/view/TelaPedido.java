package com.sorveteria.view;

import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela de Frente de Caixa & Registro de Pedidos (RF004 e RF005).
 * Implementa a seleção de produtos, montagem de itens, sorteador aleatório de sabores e fechamento de pedidos.
 */
public class TelaPedido extends JDialog {
    private JComboBox<String> cbClientes;
    private JComboBox<Produto> cbProdutos;
    private JList<Sabor> lstSabores;
    private DefaultListModel<Sabor> modelSabores;
    private JSpinner spQuantidade;
    private JLabel lblMaxBolasInfo;
    private JLabel lblResultadoSorteio;

    private JTable tblItens;
    private DefaultTableModel modelItens;
    private JLabel lblTotalGeral;
    private JComboBox<String> cbPagamento;

    private List<Cliente> listaClientes;
    private List<Produto> listaProdutos;
    private List<Sabor> listaSabores;
    private Pedido pedidoAtual;

    public TelaPedido(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Frente de Caixa & Registro de Pedidos", true);
        this.pedidoAtual = new Pedido();
        initComponents();
        carregarDadosIniciais();
    }

    private void initComponents() {
        setSize(980, 640);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(5, 150, 105)); // Verde Esmeralda
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Frente de Caixa – Novo Pedido (RF004 / RF005)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Principal com 2 Colunas (Esquerda: Montagem do Item / Direita: Carrinho e Fechamento)
        JPanel pnlConteudo = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlConteudo.setBackground(new Color(248, 250, 252));

        // 1. COLUNA ESQUERDA: MONTAGEM DO ITEM E SORTEADOR
        JPanel pnlEsquerda = new JPanel();
        pnlEsquerda.setLayout(new BoxLayout(pnlEsquerda, BoxLayout.Y_AXIS));
        pnlEsquerda.setBackground(Color.WHITE);
        pnlEsquerda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "1. Configuração do Item"),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // Seleção de Cliente
        JPanel pnlCli = new JPanel(new BorderLayout(5, 5));
        pnlCli.setBackground(Color.WHITE);
        pnlCli.add(new JLabel("Cliente:"), BorderLayout.NORTH);
        cbClientes = new JComboBox<>();
        pnlCli.add(cbClientes, BorderLayout.CENTER);
        pnlEsquerda.add(pnlCli);
        pnlEsquerda.add(Box.createVerticalStrut(10));

        // Seleção de Produto / Recipiente
        JPanel pnlProd = new JPanel(new BorderLayout(5, 5));
        pnlProd.setBackground(Color.WHITE);
        pnlProd.add(new JLabel("Produto / Recipiente:"), BorderLayout.NORTH);
        cbProdutos = new JComboBox<>();
        cbProdutos.addActionListener(e -> atualizarLimiteBolas());
        pnlProd.add(cbProdutos, BorderLayout.CENTER);
        pnlEsquerda.add(pnlProd);
        pnlEsquerda.add(Box.createVerticalStrut(5));

        lblMaxBolasInfo = new JLabel("Limite para este produto: até 1 bola(s) de sorvete.");
        lblMaxBolasInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMaxBolasInfo.setForeground(new Color(30, 58, 138));
        pnlEsquerda.add(lblMaxBolasInfo);
        pnlEsquerda.add(Box.createVerticalStrut(10));

        // Bloco do Sorteador Aleatório (RF005)
        JPanel pnlSorteador = new JPanel(new BorderLayout(5, 5));
        pnlSorteador.setBackground(new Color(254, 243, 199)); // Amarelo/Laranja suave
        pnlSorteador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(245, 158, 11), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel lblTitSorteio = new JLabel("★ Sorteador Aleatório de Sabores (RF005) ★");
        lblTitSorteio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitSorteio.setForeground(new Color(180, 83, 9));
        pnlSorteador.add(lblTitSorteio, BorderLayout.NORTH);

        JButton btnSortear = new JButton("🎲 Sortear Sabores Agora!");
        btnSortear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSortear.setBackground(new Color(217, 119, 6));
        btnSortear.setForeground(Color.WHITE);
        btnSortear.setFocusPainted(false);
        btnSortear.addActionListener(e -> sortearSabores());
        pnlSorteador.add(btnSortear, BorderLayout.CENTER);

        lblResultadoSorteio = new JLabel("Clique para surpreender o cliente com sabores da casa!");
        lblResultadoSorteio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblResultadoSorteio.setForeground(new Color(120, 53, 15));
        pnlSorteador.add(lblResultadoSorteio, BorderLayout.SOUTH);

        pnlEsquerda.add(pnlSorteador);
        pnlEsquerda.add(Box.createVerticalStrut(10));

        // Lista de Sabores para Escolha Manual
        JPanel pnlSab = new JPanel(new BorderLayout(5, 5));
        pnlSab.setBackground(Color.WHITE);
        pnlSab.add(new JLabel("Ou selecione manualmente os sabores (Ctrl + Clique):"), BorderLayout.NORTH);

        modelSabores = new DefaultListModel<>();
        lstSabores = new JList<>(modelSabores);
        lstSabores.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollSab = new JScrollPane(lstSabores);
        scrollSab.setPreferredSize(new Dimension(200, 110));
        pnlSab.add(scrollSab, BorderLayout.CENTER);
        pnlEsquerda.add(pnlSab);
        pnlEsquerda.add(Box.createVerticalStrut(10));

        // Quantidade e Botão Adicionar
        JPanel pnlQtd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlQtd.setBackground(Color.WHITE);
        pnlQtd.add(new JLabel("Quantidade:"));
        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        pnlQtd.add(spQuantidade);

        JButton btnAdicionarItem = new JButton("+ Adicionar ao Pedido");
        btnAdicionarItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdicionarItem.setBackground(new Color(5, 150, 105));
        btnAdicionarItem.setForeground(Color.WHITE);
        btnAdicionarItem.addActionListener(e -> adicionarItemAoPedido());
        pnlQtd.add(btnAdicionarItem);

        pnlEsquerda.add(pnlQtd);
        pnlConteudo.add(pnlEsquerda);

        // 2. COLUNA DIREITA: CARRINHO / ITENS DO PEDIDO E FECHAMENTO
        JPanel pnlDireita = new JPanel(new BorderLayout(10, 10));
        pnlDireita.setBackground(Color.WHITE);
        pnlDireita.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "2. Itens do Pedido & Fechamento"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Tabela de Itens
        modelItens = new DefaultTableModel(new String[]{"Produto", "Sabores", "Qtd", "Unitário", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblItens = new JTable(modelItens);
        tblItens.setRowHeight(24);
        pnlDireita.add(new JScrollPane(tblItens), BorderLayout.CENTER);

        // Bloco de Pagamento e Total
        JPanel pnlFechamento = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlFechamento.setBackground(new Color(241, 245, 249));
        pnlFechamento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Forma de Pagamento
        JPanel pnlForma = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlForma.setBackground(new Color(241, 245, 249));
        pnlForma.add(new JLabel("Forma de Pagamento:"));
        cbPagamento = new JComboBox<>(new String[]{"PIX (Instantâneo)", "Cartão de Crédito", "Cartão de Débito", "Dinheiro em Espécie"});
        pnlForma.add(cbPagamento);
        pnlFechamento.add(pnlForma);

        // Total
        lblTotalGeral = new JLabel("TOTAL A PAGAR: R$ 0,00", SwingConstants.CENTER);
        lblTotalGeral.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalGeral.setForeground(new Color(5, 150, 105));
        pnlFechamento.add(lblTotalGeral);

        // Botões de Conclusão
        JPanel pnlAcoesFinais = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlAcoesFinais.setBackground(new Color(241, 245, 249));

        JButton btnRemoverItem = new JButton("Remover Item");
        btnRemoverItem.setBackground(new Color(220, 38, 38));
        btnRemoverItem.setForeground(Color.WHITE);
        btnRemoverItem.addActionListener(e -> removerItemSelecionado());

        JButton btnFinalizar = new JButton("✓ FINALIZAR PEDIDO (F5)");
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(5, 150, 105));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.addActionListener(e -> finalizarPedido());

        pnlAcoesFinais.add(btnRemoverItem);
        pnlAcoesFinais.add(btnFinalizar);
        pnlFechamento.add(pnlAcoesFinais);

        pnlDireita.add(pnlFechamento, BorderLayout.SOUTH);
        pnlConteudo.add(pnlDireita);

        add(pnlConteudo, BorderLayout.CENTER);

        // Rodapé
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        pnlFooter.setBackground(new Color(241, 245, 249));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarDadosIniciais() {
        // Carrega Clientes
        listaClientes = RepositorioMemoria.getInstancia().getClientes();
        cbClientes.removeAllItems();
        cbClientes.addItem("Consumidor Final (Sem Identificação)");
        for (Cliente c : listaClientes) {
            cbClientes.addItem(c.getNome() + " (" + c.getCpf() + ")");
        }

        // Carrega Produtos
        listaProdutos = RepositorioMemoria.getInstancia().getProdutos();
        cbProdutos.removeAllItems();
        for (Produto p : listaProdutos) {
            cbProdutos.addItem(p);
        }

        // Carrega Sabores
        listaSabores = RepositorioMemoria.getInstancia().getSabores();
        modelSabores.clear();
        for (Sabor s : listaSabores) {
            if (s.isDisponivel()) {
                modelSabores.addElement(s);
            }
        }

        atualizarLimiteBolas();
    }

    private void atualizarLimiteBolas() {
        Produto p = (Produto) cbProdutos.getSelectedItem();
        if (p != null) {
            lblMaxBolasInfo.setText("Limite para este produto: até " + p.getMaxBolas() + " bola(s) de sorvete.");
        }
    }

    private void sortearSabores() {
        Produto p = (Produto) cbProdutos.getSelectedItem();
        if (p == null) return;

        int qtdBolas = p.getMaxBolas();
        List<Sabor> sorteados = SorteadorSabores.sortear(listaSabores, qtdBolas);

        if (sorteados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum sabor disponível para sorteio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Atualiza seleção na JList
        List<Integer> indices = new ArrayList<>();
        StringBuilder nomes = new StringBuilder();
        for (int i = 0; i < modelSabores.size(); i++) {
            Sabor sb = modelSabores.getElementAt(i);
            for (Sabor sort : sorteados) {
                if (sb.getId() == sort.getId()) {
                    indices.add(i);
                    if (nomes.length() > 0) nomes.append(" + ");
                    nomes.append("[").append(sb.getNome()).append("]");
                }
            }
        }

        int[] arrIndices = indices.stream().mapToInt(Integer::intValue).toArray();
        lstSabores.setSelectedIndices(arrIndices);

        lblResultadoSorteio.setText("Sabores Sorteados: " + nomes.toString());
    }

    private void adicionarItemAoPedido() {
        Produto p = (Produto) cbProdutos.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Sabor> selecionados = lstSabores.getSelectedValuesList();
        if (selecionados.isEmpty() && p.getMaxBolas() > 0) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um sabor ou clique no Sorteador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selecionados.size() > p.getMaxBolas()) {
            JOptionPane.showMessageDialog(this, 
                "Atenção: O produto '" + p.getNome() + "' aceita no máximo " + p.getMaxBolas() + " sabor(es).\nVocê selecionou " + selecionados.size() + ".", 
                "Limite de Sabores Excedido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int qtd = (int) spQuantidade.getValue();
        ItemPedido item = new ItemPedido(0, p, selecionados, qtd, p.getPreco(), "");
        pedidoAtual.adicionarItem(item);

        atualizarTabelaItens();
    }

    private void removerItemSelecionado() {
        int sel = tblItens.getSelectedRow();
        if (sel >= 0) {
            pedidoAtual.removerItem(sel);
            atualizarTabelaItens();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item na lista para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTabelaItens() {
        modelItens.setRowCount(0);
        for (ItemPedido it : pedidoAtual.getItens()) {
            modelItens.addRow(new Object[]{
                it.getProduto().getNome(),
                it.getNomesSabores(),
                it.getQuantidade(),
                String.format("R$ %.2f", it.getPrecoUnitario()),
                String.format("R$ %.2f", it.getSubtotal())
            });
        }
        lblTotalGeral.setText(String.format("TOTAL A PAGAR: R$ %.2f", pedidoAtual.getValorTotal()));
    }

    private void finalizarPedido() {
        if (pedidoAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens ao pedido antes de finalizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        pedidoAtual.setFormaPagamento((String) cbPagamento.getSelectedItem());
        RepositorioMemoria.getInstancia().adicionarPedido(pedidoAtual);

        StringBuilder resumo = new StringBuilder();
        resumo.append("========================================\n");
        resumo.append("   SORVETERIA TROPICALZIN CREMOSO\n");
        resumo.append("   Comprovante de Atendimento\n");
        resumo.append("========================================\n");
        resumo.append("Data/Hora: ").append(pedidoAtual.getDataFormatada()).append("\n");
        resumo.append("Forma de Pagamento: ").append(pedidoAtual.getFormaPagamento()).append("\n\n");
        resumo.append("ITENS:\n");
        for (ItemPedido item : pedidoAtual.getItens()) {
            resumo.append(item.toString()).append("\n");
        }
        resumo.append("----------------------------------------\n");
        resumo.append(String.format("VALOR TOTAL: R$ %.2f\n", pedidoAtual.getValorTotal()));
        resumo.append("========================================\n");
        resumo.append("Obrigado pela preferência e volte sempre!");

        JOptionPane.showMessageDialog(this, resumo.toString(), "Pedido Finalizado com Sucesso!", JOptionPane.INFORMATION_MESSAGE);

        // Reinicia pedido
        this.pedidoAtual = new Pedido();
        atualizarTabelaItens();
        lblResultadoSorteio.setText("Clique para surpreender o cliente com sabores da casa!");
        lstSabores.clearSelection();
    }
}
