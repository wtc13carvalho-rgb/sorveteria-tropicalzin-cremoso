package com.sorveteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe utilitária com a regra de negócio do sorteador aleatório de sabores (RF005).
 * Permite selecionar N sabores aleatórios disponíveis no cardápio.
 */
public class SorteadorSabores {

    /**
     * Sorteia uma quantidade de sabores dentre os sabores disponíveis.
     * @param todosSabores Lista com todos os sabores cadastrados
     * @param quantidade Quantidade de sabores a sortear
     * @return Lista de sabores sorteados
     */
    public static List<Sabor> sortear(List<Sabor> todosSabores, int quantidade) {
        List<Sabor> disponiveis = new ArrayList<>();
        if (todosSabores != null) {
            for (Sabor s : todosSabores) {
                if (s.isDisponivel()) {
                    disponiveis.add(s);
                }
            }
        }

        if (disponiveis.isEmpty()) {
            return new ArrayList<>();
        }

        // Embaralha os sabores disponíveis
        List<Sabor> copia = new ArrayList<>(disponiveis);
        Collections.shuffle(copia);

        int qtdFinal = Math.min(quantidade, copia.size());
        return new ArrayList<>(copia.subList(0, qtdFinal));
    }
}
