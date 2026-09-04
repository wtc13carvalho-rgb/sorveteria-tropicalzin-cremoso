# Sistema Sorveteria Tropicalzin Cremoso 🍨🍦

Projeto Integrador desenvolvido para o curso **Técnico em Desenvolvimento de Sistemas (TDS) – Senac**.  
Aplicação desktop completa para gestão de atendimento, controle de sabores, estoque de produtos, frente de caixa com regras de negócio dinâmicas e persistência em banco de dados relacional.

---

## 📌 1. Status do Projeto
> **Status:** Em desenvolvimento (Etapa 5 – Versionamento com Git & GitHub) 🚀

---

## 🛠️ 2. Tecnologias Aplicadas
O desenvolvimento do sistema utiliza exclusivamente as tecnologias abordadas e consolidadas ao longo da formação:

* **Linguagem de Programação:** Java (Java SE / JDK 17+)
* **Interface Gráfica (Desktop):** Java Swing com Look and Feel nativo
* **Ambiente de Desenvolvimento (IDE):** NetBeans IDE 
* **Banco de Dados Relacional:** MySQL Server 8.0
* **Modelagem e Administração de Banco:** MySQL Workbench
* **Camada de Integração:** JDBC (Java Database Connectivity) via driver `mysql-connector-j-8.0.33`
* **Controle de Versão e Colaboração:** Git & GitHub

---

## 👥 3. Time de Desenvolvedores
Projeto desenvolvido em equipe colaborativa:
* **Wallace Teixeira Carvalho** – [GitHub: @wtc13carvalho-rgb](https://github.com/wtc13carvalho-rgb)
* **Edcarlos Cardôso de Farias** – [GitHub: @edcarloscardoso](https://github.com/edcarloscardoso)

---

## 🎯 4. Objetivo do Software
O **Sistema Sorveteria Tropicalzin Cremoso** tem por finalidade automatizar e modernizar a operação diária de uma sorveteria artesanal. O sistema substitui comandas e registros manuais em papel por uma solução informatizada, ágil e segura, que proporciona:
* Controle eficiente de fluxo de pedidos e totalização imediata de vendas no balcão;
* Gestão em tempo real de sabores artesanais e recipientes (copos, casquinhas, potes);
* Fidelização de clientes através de cadastro rápido e histórico de pedidos;
* Inovação na experiência do cliente com o recurso exclusivo de sorteio aleatório de sabores para indecisos;
* Confiabilidade dos dados operacionais e financeiros por meio de transações ACID no MySQL.

---

## ✨ 5. Funcionalidades do Sistema (Requisitos)

### Requisitos Funcionais (RF)
* **RF001 – Autenticação e Perfis de Acesso:** Controle de login seguro com diferenciação de permissões entre perfil `Gerente` (acesso irrestrito) e perfil `Atendente` (frente de caixa e cadastros básicos).
* **RF002 – Gestão de Sabores:** Cadastro, listagem, atualização e exclusão lógica de sabores com categorização (Tradicional, Especial, Zero Açúcar) e disponibilidade de estoque.
* **RF003 – Gestão de Produtos/Formatos:** Controle de formatos de venda (Casquinha Simples, Cascão, Copo 300ml, Taça Especial), quantidade máxima de bolas e precificação.
* **RF004 – Gestão de Clientes:** Cadastro completo com nome, CPF, telefone e histórico para acompanhamento de consumo e programas de fidelidade.
* **RF005 – Sorteador Aleatório de Sabores:** Algoritmo dinâmico que seleciona automaticamente sabores disponíveis de acordo com a quantidade de bolas do recipiente, oferecendo sugestões para clientes em dúvida.
* **RF006 – Frente de Caixa e Registro de Pedidos:** Montagem interativa do pedido com seleção de formato, sabores, cálculo automático de totais e suporte a múltiplas formas de pagamento (Dinheiro, PIX, Cartão de Crédito/Débito).
* **RF007 – Persistência Transacional (JDBC/MySQL):** Registro seguro e atômico de pedidos (`pedidos`, `itens_pedido` e `item_pedido_sabores`) utilizando transações manuais com `commit()` e `rollback()` para evitar inconsistências.
* **RF008 – Consulta e Histórico de Vendas:** Rastreabilidade dos pedidos efetuados por data, cliente e atendente responsável.

### Requisitos Não Funcionais (RNF)
* **RNF001 – Usabilidade:** Telas com navegação intuitiva, suporte a teclas de atalho (Enter/Esc) e mensagens claras ao operador.
* **RNF002 – Desempenho:** Tempo de resposta instantâneo na montagem e finalização de pedidos.
* **RNF003 – Integridade dos Dados:** Relacionamentos de chaves estrangeiras com integridade referencial garantida pelo MySQL (`InnoDB`).
* **RNF004 – Portabilidade:** Compatibilidade com sistemas operacionais Windows e Linux através da Máquina Virtual Java (JVM).

---

## 📂 6. Estrutura do Repositório

```text
├── Etapa_1_Definicao_e_Classes/       # Modelagem orientada a objetos (POO) e documentação inicial
│   ├── src/com/sorveteria/model/     # Classes de domínio (Usuario, Sabor, Produto, Pedido, etc.)
│   └── Documentacao_Etapa1_Definicao_e_Classes.docx
│
├── Etapa_2_UX_UI_Acessibilidade/      # Protótipos de interface, wireframes e estudo de acessibilidade
│   ├── wireframes/                   # Telas em alta definição
│   └── Projeto_Interfaces_UX_UI_Acessibilidade.docx
│
├── Etapa_3_Telas_Java_NetBeans/       # Interfaces desktop completas desenvolvidas em Java Swing
│   ├── src/com/sorveteria/view/      # Telas (Login, Principal, Sabores, Produtos, Pedido)
│   ├── src/com/sorveteria/dados/     # Repositório de simulação em memória (testes offline)
│   └── dist/                         # Executável JAR funcional
│
├── Etapa_4_Banco_MySQL_Integracao/    # Integração completa com banco de dados relacional MySQL
│   ├── banco_de_dados/               # Scripts SQL (01_schema DDL e 02_seed DML)
│   ├── src/com/sorveteria/dao/       # Camada DAO (Data Access Object) e FabricaConexao
│   └── lib/                          # Driver JDBC MySQL Connector/J
│
├── Etapa_5_Versionamento_GitHub/      # Evidências de versionamento, histórico de commits e documentação
│   ├── README.md
│   └── Documentacao_Etapa5_Versionamento_GitHub.docx
│
├── .gitignore                         # Filtro de arquivos binários e temporários
└── README.md                          # Especificações gerais do projeto
```

---

## 🚀 7. Instruções para Execução

### Pré-requisitos
1. **Java JDK 17** ou superior instalado e configurado no `PATH`.
2. **NetBeans IDE 17+** (ou VS Code com Java Extension Pack).
3. **MySQL Server 8.0** em execução na porta padrão `3306`.

### Configuração do Banco de Dados
1. Abra o **MySQL Workbench** e conecte-se à sua instância local.
2. Execute o script `Etapa_4_Banco_MySQL_Integracao/banco_de_dados/01_schema_sorveteria_db.sql` para criar a base e as tabelas.
3. Execute o script `Etapa_4_Banco_MySQL_Integracao/banco_de_dados/02_seed_dados_iniciais.sql` para carregar a massa de dados inicial.

### Execução via NetBeans IDE
1. Abra o NetBeans e vá em **File (Arquivo) > Open Project (Abrir Projeto)**.
2. Selecione a pasta `Etapa_4_Banco_MySQL_Integracao`.
3. Pressione **F6** para executar a aplicação.

### Credenciais Padrão de Acesso:
* **Gerente:** Usuário: `admin` | Senha: `1234`
* **Atendente:** Usuário: `atendente` | Senha: `1234`
