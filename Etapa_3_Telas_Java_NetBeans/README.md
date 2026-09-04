# Etapa 3 – Programação das Interfaces Gráficas Desktop (Java / NetBeans)

**Curso:** Técnico em Desenvolvimento de Sistemas (TDS) – Senac  
**Unidade Curricular:** Projeto Integrador Assistente de Desenvolvimento de Sistemas  
**Nome do Sistema:** Sistema Sorveteria Tropicalzin Cremoso  
**Autores:** Wallace Teixeira Carvalho / Edcarlos Cardôso de Farias  
**Versão:** 1.0 (Protótipo Funcional em Memória)  

---

## 1. Descrição Desta Etapa
Nesta etapa, conforme as exigências da atividade, foram desenvolvidas todas as **interfaces gráficas desktop em Java (Swing)** baseadas no projeto de UX/UI da Etapa 2.  
O sistema é **100% funcional com navegação entre telas e dados simulados em memória** através da classe `RepositorioMemoria.java`, **sem dependência de banco de dados externo**, garantindo execução imediata sem erros de conexão na máquina do avaliador.

## 2. Estrutura do Projeto NetBeans
- **`src/com/sorveteria/Principal.java`**: Ponto de entrada com inicialização do tema nativo do sistema operacional.
- **`src/com/sorveteria/view/`**:
  - `TelaLogin.java`: Tela de login com atalhos de teclado (Enter para login, Esc para sair).
  - `TelaPrincipal.java`: Menu desktop (`JMenuBar`), barra de atalhos e cards de ação rápida.
  - `TelaCadastroSabor.java`: Formulário e `JTable` de sabores (Tradicional, Especial, Zero Açúcar).
  - `TelaCadastroProduto.java`: Gestão de formatos (casquinhas, copos, potes), estoques e preços.
  - `TelaCadastroCliente.java`: Cadastro de clientes frequentes para fidelidade.
  - `TelaPedido.java`: **Frente de Caixa completa** com seleção de itens, totalização automática, formas de pagamento e o **Sorteador Aleatório de Sabores (RF005)**.
- **`src/com/sorveteria/dados/RepositorioMemoria.java`**: Simulação da camada de dados em memória (`ArrayList`) povoada com sabores, produtos e usuários de teste.
- **`src/com/sorveteria/model/`**: Classes de domínio (POO) criadas na Etapa 1.
- **`nbproject/`** e **`build.xml`**: Arquivos de projeto padrão do NetBeans IDE.

## 3. Credenciais de Teste para Login
- **Perfil Gerente:** Usuário: `admin` | Senha: `1234`
- **Perfil Atendente:** Usuário: `atendente` | Senha: `1234`

## 4. Instruções para Executar
### Opção A: No NetBeans IDE
1. Abra o NetBeans.
2. Vá em `Arquivo` -> `Abrir Projeto` e selecione esta pasta.
3. Pressione `F6` (Executar Projeto).

### Opção B: Via Linha de Comando / Terminal
```bash
java -jar dist/SorveteriaTropicalzinCremoso.jar
```
*(ou execute a classe `com.sorveteria.Principal`)*
