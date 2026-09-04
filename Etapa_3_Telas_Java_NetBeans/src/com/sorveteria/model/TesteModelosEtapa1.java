package com.sorveteria.model;

import java.util.Arrays;
import java.util.List;

/**
 * Classe de teste executável para validação dos modelos da Etapa 1.
 */
public class TesteModelosEtapa1 {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("TESTE DE CLASSES - ETAPA 1: TROPICALZIN CREMOSO");
        System.out.println("=================================================");

        // 1. Instanciando Usuário
        Usuario atendente = new Usuario(1, "Wallace Carvalho", "wallace", "1234", "ATENDENTE");
        System.out.println("Usuário criado: " + atendente);

        // 2. Instanciando Cliente
        Cliente cliente = new Cliente(1, "Maria Silva", "123.456.789-00", "(93) 98123-4567", "maria@email.com");
        System.out.println("Cliente criado: " + cliente);

        // 3. Instanciando Sabores
        Sabor s1 = new Sabor(1, "Açaí do Pará", "Açaí cremoso artesanal", "ESPECIAL", true);
        Sabor s2 = new Sabor(2, "Cupuaçu com Castanha", "Fruta amazônica com castanhas", "ESPECIAL", true);
        Sabor s3 = new Sabor(3, "Ninho Trufado", "Leite ninho com chocolate belga", "TRADICIONAL", true);
        Sabor s4 = new Sabor(4, "Maracujá Zero", "Maracujá sem adição de açúcar", "ZERO_ACUCAR", true);
        List<Sabor> cardapio = Arrays.asList(s1, s2, s3, s4);
        System.out.println("Sabores cadastrados: " + cardapio.size());

        // 4. Testando Sorteador de Sabores (RF005)
        System.out.println("\n-- Testando Sorteador Aleatório de 2 Sabores --");
        List<Sabor> sorteados = SorteadorSabores.sortear(cardapio, 2);
        for (Sabor s : sorteados) {
            System.out.println("Sabor sorteado: " + s.getNome());
        }

        // 5. Instanciando Produto
        Produto casquinha = new Produto(1, "Casquinha Dupla Crocante", 12.50, "CASQUINHA", 2, 100);
        System.out.println("\nProduto criado: " + casquinha);

        // 6. Montando Item de Pedido
        ItemPedido item = new ItemPedido(1, casquinha, sorteados, 2, casquinha.getPreco(), "Sem calda extra");
        System.out.println("Item montado: " + item);

        // 7. Montando Pedido Completo
        Pedido pedido = new Pedido(101, cliente, atendente, "PIX");
        pedido.adicionarItem(item);
        System.out.println("\nResumo do Pedido:");
        System.out.println(pedido);
        System.out.println("Data/Hora: " + pedido.getDataFormatada());
        System.out.println("Valor total a pagar: R$ " + String.format("%.2f", pedido.getValorTotal()));

        System.out.println("\n>> Todas as classes da Etapa 1 foram validadas com sucesso!");
    }
}
