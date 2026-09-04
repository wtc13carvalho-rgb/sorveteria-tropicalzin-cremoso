package com.sorveteria.view;

import com.sorveteria.dao.*;
import com.sorveteria.dados.RepositorioMemoria;
import com.sorveteria.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela de Frente de Caixa com persistência definitiva em MySQL (Etapa 4).
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

    private List<Cliente> listaClientes = new ArrayList<>();
    private List<Produto> listaProdutos = new ArrayList<>();
    private List<Sabor> listaSabores = new ArrayList<>();
    private Pedido pedidoAtual;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private SaborDAO saborDAO = new SaborDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private boolean modoBanco = true;

    public TelaPedido(JFrame parent) {
        super(parent, "Tropicalzin Cremoso - Frente de Caixa & Registro de Pedidos (MySQL)", true);
        this.pedidoAtual = new Pedido();
        initComponents();
        carregarDadosIniciais();
    }

    private void initComponents() {
        setSize(980, 640);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(5, 150, 105));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Frente de Caixa – Novo Pedido (Persistência MySQL)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlConteudo = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlConteudo.setBackground(new Color(248, 250, 252));

        // Coluna Esquerda
        JPanel pnlEsquerda = new JPanel();
        pnlEsquerda.setLayout(new BoxLayout(pnlEsquerda, BoxLayout.Y_AXIS));
        pnlEsquerda.setBackground(Color.WHITE);
        pnlEsquerda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "1. Configuração do Item"),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // Cliente
        JPanel pnlCli = new JPanel(new BorderLayout(5, 5));
        pnlCli.setBackground(Color.WHITE);
        pnlCli.add(new JLabel("Cliente:"), BorderLayout.NORTH);
        cbClientes = new JComboBox<>();
        pnlCli.add(cbClientes, BorderLayout.CENTER);
        pnlEsquerda.add(pnlCli);
        pnlEsquerda.add(Box.createVerticalStrut(10));

        // Produto
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

        // Sorteador (RF005)
        JPanel pnlSorteador = new JPanel(new BorderLayout(5, 5));
        pnlSorteador.setBackground(new Color(254, 243, 199));
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

        // Lista de Sabores
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

        // Quantidade e Adicionar
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

        // Coluna Direita (Itens e Fechamento)
        JPanel pnlDireita = new JPanel(new BorderLayout(10, 10));
        pnlDireita.setBackground(Color.WHITE);
        pnlDireita.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), "2. Itens do Pedido & Fechamento"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        modelItens = new DefaultTableModel(new String[]{"Produto", "Sabores", "Qtd", "Unitário", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblItens = new JTable(modelItens);
        tblItens.setRowHeight(24);
        pnlDireita.add(new JScrollPane(tblItens), BorderLayout.CENTER);

        // Fechamento
        JPanel pnlFechamento = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlFechamento.setBackground(new Color(241, 245, 249));
        pnlFechamento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlForma = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlForma.setBackground(new Color(241, 245, 249));
        pnlForma.add(new JLabel("Forma de Pagamento:"));
        cbPagamento = new JComboBox<>(new String[]{"PIX", "CARTAO_CREDITO", "CARTAO_DEBITO", "DINHEIRO"});
        pnlForma.add(cbPagamento);
        pnlFechamento.add(pnlForma);

        lblTotalGeral = new JLabel("TOTAL A PAGAR: R$ 0,00", SwingConstants.CENTER);
        lblTotalGeral.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalGeral.setForeground(new Color(5, 150, 105));
        pnlFechamento.add(lblTotalGeral);

        JPanel pnlAcoesFinais = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlAcoesFinais.setBackground(new Color(241, 245, 249));

        JButton btnRemoverItem = new JButton("Remover Item");
        btnRemoverItem.setBackground(new Color(220, 38, 38));
        btnRemoverItem.setForeground(Color.WHITE);
        btnRemoverItem.addActionListener(e -> removerItemSelecionado());

        JButton btnFinalizar = new JButton("✓ GRAVAR PEDIDO NO BANCO (F5)");
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

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        pnlFooter.setBackground(new Color(241, 245, 249));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarDadosIniciais() {
        try {
            listaClientes = clienteDAO.listarTodos();
            listaProdutos = produtoDAO.listarTodos();
            listaSabores = saborDAO.listarDisponiveis();
            modoBanco = true;
        } catch (SQLException e) {
            modoBanco = false;
            listaClientes = RepositorioMemoria.getInstancia().getClientes();
            listaProdutos = RepositorioMemoria.getInstancia().getProdutos();
            listaSabores = RepositorioMemoria.getInstancia().getSabores();
        }

        cbClientes.removeAllItems();
        cbClientes.addItem("Consumidor Final (Sem Identificação)");
        for (Cliente c : listaClientes) {
            cbClientes.addItem(c.getNome() + " (" + c.getCpf() + ")");
        }

        cbProdutos.removeAllItems();
        for (Produto p : listaProdutos) {
            cbProdutos.addItem(p);
        }

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
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um sabor ou sorteie.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selecionados.size() > p.getMaxBolas()) {
            JOptionPane.showMessageDialog(this, 
                "Limite de sabores excedido! Máximo permitido para " + p.getNome() + ": " + p.getMaxBolas(), 
                "Atenção", 
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
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Adicione itens ao pedido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        pedidoAtual.setFormaPagamento((String) cbPagamento.getSelectedItem());
        pedidoAtual.setAtendente(RepositorioMemoria.getInstancia().getUsuarioLogado());

        // Vincula cliente se selecionado
        int cliIndex = cbClientes.getSelectedIndex();
        if (cliIndex > 0 && cliIndex - 1 < listaClientes.size()) {
            pedidoAtual.setCliente(listaClientes.get(cliIndex - 1));
        }

        boolean gravouBanco = false;
        if (modoBanco) {
            try {
                pedidoDAO.salvarPedido(pedidoAtual);
                gravouBanco = true;
            } catch (SQLException e) {
                System.err.println("Erro ao gravar pedido no banco: " + e.getMessage());
                RepositorioMemoria.getInstancia().adicionarPedido(pedidoAtual);
            }
        } else {
            RepositorioMemoria.getInstancia().adicionarPedido(pedidoAtual);
        }

        StringBuilder msg = new StringBuilder();
        msg.append("========================================\n");
        msg.append("   SORVETERIA TROPICALZIN CREMOSO\n");
        msg.append("   Comprovante de Venda Gravada\n");
        msg.append("========================================\n");
        msg.append("ID do Pedido: #").append(pedidoAtual.getId()).append("\n");
        msg.append("Persistência: ").append(gravouBanco ? "Gravado no MySQL com Sucesso!" : "Registrado em Memória").append("\n");
        msg.append("Data/Hora: ").append(pedidoAtual.getDataFormatada()).append("\n");
        msg.append("Forma de Pagamento: ").append(pedidoAtual.getFormaPagamento()).append("\n\n");
        msg.append("ITENS:\n");
        for (ItemPedido item : pedidoAtual.getItens()) {
            msg.append(item.toString()).append("\n");
        }
        msg.append("----------------------------------------\n");
        msg.append(String.format("VALOR TOTAL: R$ %.2f\n", pedidoAtual.getValorTotal()));
        msg.append("========================================\n");

        JOptionPane.showMessageDialog(this, msg.toString(), "Pedido Finalizado!", JOptionPane.INFORMATION_MESSAGE);

        this.pedidoAtual = new Pedido();
        atualizarTabelaItens();
        lblResultadoSorteio.setText("Clique para surpreender o cliente com sabores da casa!");
        lstSabores.clearSelection();
    }
}
