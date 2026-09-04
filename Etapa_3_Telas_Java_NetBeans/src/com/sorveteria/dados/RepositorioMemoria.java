package com.sorveteria.dados;

import com.sorveteria.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório em memória para simulação das regras de negócio e dados (Etapa 3).
 * Atende ao requisito de testes funcionais sem necessidade de banco de dados conectado.
 */
public class RepositorioMemoria {
    private static RepositorioMemoria instancia;

    private List<Usuario> usuarios;
    private List<Sabor> sabores;
    private List<Produto> produtos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;
    private Usuario usuarioLogado;

    private int proximoIdSabor = 1;
    private int proximoIdProduto = 1;
    private int proximoIdCliente = 1;
    private int proximoIdPedido = 100;

    private RepositorioMemoria() {
        inicializarDados();
    }

    public static synchronized RepositorioMemoria getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioMemoria();
        }
        return instancia;
    }

    private void inicializarDados() {
        usuarios = new ArrayList<>();
        sabores = new ArrayList<>();
        produtos = new ArrayList<>();
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();

        // Usuários padrão de teste
        usuarios.add(new Usuario(1, "Wallace Carvalho (Gerente)", "admin", "1234", "GERENTE"));
        usuarios.add(new Usuario(2, "Atendente Balcão", "atendente", "1234", "ATENDENTE"));

        // Sabores iniciais
        adicionarSabor(new Sabor(0, "Açaí do Pará Especial", "Açaí puro artesanal cremoso", "ESPECIAL", true));
        adicionarSabor(new Sabor(0, "Cupuaçu com Castanhas", "Fruta amazônica com castanha do Pará", "ESPECIAL", true));
        adicionarSabor(new Sabor(0, "Ninho Trufado com Nutella", "Leite ninho cremoso e creme de avelã", "TRADICIONAL", true));
        adicionarSabor(new Sabor(0, "Chocolate Belga 70%", "Cacau intenso com raspas de chocolate", "ESPECIAL", true));
        adicionarSabor(new Sabor(0, "Maracujá Tropical Zero", "Feito da fruta, sem adição de açúcar", "ZERO_ACUCAR", true));
        adicionarSabor(new Sabor(0, "Morango Silvestre", "Pedaços frescos de morango", "TRADICIONAL", true));
        adicionarSabor(new Sabor(0, "Torta de Limão Siciliano", "Sorvete de limão com biscoito triturado", "ESPECIAL", true));
        adicionarSabor(new Sabor(0, "Pistache Italiano", "Sabor sofisticado com pedaços de pistache", "ESPECIAL", false)); // Esgotado para teste

        // Produtos / Recipientes iniciais
        adicionarProduto(new Produto(0, "Casquinha Simples Crocante", 8.00, "CASQUINHA", 1, 150));
        adicionarProduto(new Produto(0, "Casquinha Dupla Crocante", 13.50, "CASQUINHA", 2, 120));
        adicionarProduto(new Produto(0, "Cascão Especial Cestinha", 17.00, "CASQUINHA", 3, 80));
        adicionarProduto(new Produto(0, "Copo Médio 300ml", 14.00, "COPO", 2, 200));
        adicionarProduto(new Produto(0, "Copo Grande 500ml", 19.50, "COPO", 3, 180));
        adicionarProduto(new Produto(0, "Pote Térmico 1 Litro", 38.00, "POTE", 4, 50));
        adicionarProduto(new Produto(0, "Picolé Gourmet Artesanal", 6.50, "PICOLÉ", 1, 90));

        // Clientes iniciais
        adicionarCliente(new Cliente(0, "Maria das Graças Silva", "123.456.789-01", "(93) 98111-2233", "maria.silva@email.com"));
        adicionarCliente(new Cliente(0, "João Pereira dos Santos", "234.567.890-12", "(93) 98444-5566", "joao.santos@email.com"));
        adicionarCliente(new Cliente(0, "Ana Paula Oliveira", "345.678.901-23", "(93) 98777-8899", "ana.oliveira@email.com"));
    }

    // Autenticação
    public Usuario autenticar(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login) && u.getSenha().equals(senha)) {
                this.usuarioLogado = u;
                return u;
            }
        }
        return null;
    }

    public Usuario getUsuarioLogado() {
        if (usuarioLogado == null) {
            return usuarios.get(0); // Fallback para desenvolvimento
        }
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    // Sabores CRUD
    public List<Sabor> getSabores() {
        return new ArrayList<>(sabores);
    }

    public void adicionarSabor(Sabor s) {
        s.setId(proximoIdSabor++);
        sabores.add(s);
    }

    public void atualizarSabor(Sabor saborAtualizado) {
        for (int i = 0; i < sabores.size(); i++) {
            if (sabores.get(i).getId() == saborAtualizado.getId()) {
                sabores.set(i, saborAtualizado);
                return;
            }
        }
    }

    public boolean removerSabor(int id) {
        return sabores.removeIf(s -> s.getId() == id);
    }

    // Produtos CRUD
    public List<Produto> getProdutos() {
        return new ArrayList<>(produtos);
    }

    public void adicionarProduto(Produto p) {
        p.setId(proximoIdProduto++);
        produtos.add(p);
    }

    public void atualizarProduto(Produto produtoAtualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produtoAtualizado.getId()) {
                produtos.set(i, produtoAtualizado);
                return;
            }
        }
    }

    public boolean removerProduto(int id) {
        return produtos.removeIf(p -> p.getId() == id);
    }

    // Clientes CRUD
    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public void adicionarCliente(Cliente c) {
        c.setId(proximoIdCliente++);
        clientes.add(c);
    }

    // Pedidos
    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }

    public void adicionarPedido(Pedido p) {
        p.setId(proximoIdPedido++);
        pedidos.add(p);
    }
}
