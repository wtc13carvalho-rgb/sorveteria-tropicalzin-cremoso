# Etapa 1 – Definição do Projeto e Criação das Primeiras Classes

**Curso:** Técnico em Desenvolvimento de Sistemas (TDS) – Senac  
**Unidade Curricular:** Projeto Integrador Assistente de Desenvolvimento de Sistemas  
**Nome do Sistema:** Sistema Sorveteria Tropicalzin Cremoso  
**Autores:** Wallace Teixeira Carvalho / Edcarlos Cardôso de Farias  
**Versão:** 1.0  

---

## 1. Descrição e Contexto
O **Sistema Sorveteria Tropicalzin Cremoso** é uma aplicação desktop em Java desenvolvida para gerenciar as rotinas de atendimento, controle de sabores, estoque de produtos e fluxo de pedidos de uma sorveteria artesanal, trazendo como diferencial a seleção e sorteio aleatório de sabores para os clientes.

## 2. Conteúdo desta Etapa
- **`Documentacao_Etapa1_Definicao_e_Classes.docx`**: Documentação formal com justificativa, objetivos, Requisitos Funcionais (RF001 a RF008), Requisitos Não Funcionais (RNF001 a RNF005) e detalhamento da arquitetura das classes.
- **`src/com/sorveteria/model/`**:
  - `Usuario.java`: Autenticação e perfil de acesso (Gerente/Atendente).
  - `Sabor.java`: Entidade de sabores (Tradicional, Especial, Zero Açúcar).
  - `Produto.java`: Formatos de recipientes (Casquinha, Copos, Taças) e limites de bolas.
  - `Cliente.java`: Dados de identificação e contato de clientes.
  - `ItemPedido.java`: Associação de produto, sabores escolhidos e quantidades.
  - `Pedido.java`: Agregação de itens, descontos, cálculo de valor total e status.
  - `SorteadorSabores.java`: Regra de negócio do sorteador aleatório de sabores (RF005).
  - `TesteModelosEtapa1.java`: Classe executável de teste validando o funcionamento de todas as classes.

## 3. Instruções de Compilação e Execução
```bash
javac -d bin src/com/sorveteria/model/*.java
java -cp bin com.sorveteria.model.TesteModelosEtapa1
```
