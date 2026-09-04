# Etapa 4 – Integração do Sistema Desktop com Banco de Dados MySQL

**Curso:** Técnico em Desenvolvimento de Sistemas (TDS) – Senac  
**Unidade Curricular:** Projeto Integrador Assistente de Desenvolvimento de Sistemas  
**Nome do Sistema:** Sistema Sorveteria Tropicalzin Cremoso  
**Autores:** Wallace Teixeira Carvalho / Edcarlos Cardôso de Farias  
**Versão:** 2.0 (Persistência em Banco Relacional MySQL)  

---

## 1. Descrição Desta Etapa
Nesta etapa, o projeto NetBeans da Etapa 3 foi adaptado para **eliminar o código provisório de testes em memória e substituí-lo pela persistência definitiva no banco de dados relacional MySQL**.
O sistema conta com camada DAO (Data Access Object), gerenciamento de transações JDBC atômicas e scripts SQL estruturados e povoados com dados para testes no MySQL Workbench.

## 2. Conteúdo da Pasta `banco_de_dados/`
1. **`01_schema_sorveteria_db.sql`**: Script DDL completo de criação do banco de dados `sorveteria_db` e das tabelas:
   - `usuarios`: Controle de credenciais e permissões (Gerente/Atendente).
   - `sabores`: Sabores artesanais e disponibilidade.
   - `produtos`: Formatos de recipientes, preços e limites de bolas.
   - `clientes`: Dados cadastrais de clientes para fidelidade.
   - `pedidos`: Cabeçalho do pedido, data/hora, forma de pagamento e total.
   - `itens_pedido`: Linhas de produtos adicionadas ao pedido.
   - `item_pedido_sabores`: Relação associativa NxN entre itens e sabores selecionados.
2. **`02_seed_dados_iniciais.sql`**: Script DML de carga inicial de testes com sabores artesanais da Amazônia, produtos cadastrados, clientes e pedidos demonstrativos.

## 3. Camada de Acesso a Dados (`src/com/sorveteria/dao/`)
- **`FabricaConexao.java`**: Centraliza a conexão JDBC com MySQL na URL `jdbc:mysql://localhost:3306/sorveteria_db`.
- **`UsuarioDAO.java`**: Autenticação de login com consulta SQL preparada (`PreparedStatement`).
- **`SaborDAO.java`**: CRUD completo de sabores (Insert, Update, Delete, Select).
- **`ProdutoDAO.java`**: CRUD completo de recipientes e preços.
- **`ClienteDAO.java`**: Cadastro e listagem de clientes.
- **`PedidoDAO.java`**: Registro transacional com `setAutoCommit(false)` e `commit()` para integridade de pedido, itens e sabores.

## 4. Instruções para Configurar e Rodar o Banco no MySQL Workbench
1. Abra o **MySQL Workbench** e conecte-se à sua instância local.
2. Abra o arquivo `banco_de_dados/01_schema_sorveteria_db.sql` e execute (ícone do raio).
3. Em seguida, abra e execute o arquivo `banco_de_dados/02_seed_dados_iniciais.sql` para carregar os dados de teste.
4. *(Opcional)* Caso sua senha de root do MySQL seja diferente de vazio (`""`), ajuste a constante `SENHA` em `FabricaConexao.java`.

## 5. Como Executar o Projeto
- **Pelo NetBeans:** Abra a pasta no NetBeans e pressione `F6`. O driver `lib/mysql-connector-j-8.0.33.jar` já está configurado no projeto.
- **Pelo Terminal:**
  ```bash
  java -cp "dist/SorveteriaTropicalzinCremoso_Etapa4.jar:lib/*" com.sorveteria.Principal
  ```

*(Nota de robustez: caso o avaliador execute o projeto sem o MySQL iniciado, o sistema ativa um modo amigável de contingência offline sem travar o aplicativo).*
