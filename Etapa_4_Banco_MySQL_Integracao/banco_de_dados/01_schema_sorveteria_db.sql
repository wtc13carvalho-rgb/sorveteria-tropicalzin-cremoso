-- =============================================================================
-- PROJETO INTEGRADOR - SENAC (TDS - UC15)
-- Sistema Sorveteria Tropicalzin Cremoso
-- Script de Criação do Banco de Dados Relacional (MySQL Workbench)
-- =============================================================================

CREATE DATABASE IF NOT EXISTS sorveteria_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sorveteria_db;

-- 1. Tabela de Usuários / Operadores
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    perfil ENUM('GERENTE', 'ATENDENTE') NOT NULL DEFAULT 'ATENDENTE',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Tabela de Sabores de Sorvete (RF001)
CREATE TABLE IF NOT EXISTS sabores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    tipo ENUM('TRADICIONAL', 'ESPECIAL', 'ZERO_ACUCAR') NOT NULL DEFAULT 'TRADICIONAL',
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Tabela de Produtos / Formatos de Venda (RF002)
CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    categoria ENUM('CASQUINHA', 'COPO', 'TAÇA', 'POTE', 'PICOLÉ') NOT NULL,
    max_bolas INT NOT NULL DEFAULT 1,
    estoque INT NOT NULL DEFAULT 0,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 4. Tabela de Clientes (RF003)
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    cadastrado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 5. Tabela de Pedidos / Vendas (RF004)
CREATE TABLE IF NOT EXISTS pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NULL,
    atendente_id INT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    forma_pagamento ENUM('PIX', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'DINHEIRO') NOT NULL,
    status ENUM('RECEBIDO', 'EM_PREPARO', 'ENTREGUE', 'CANCELADO') NOT NULL DEFAULT 'RECEBIDO',
    valor_total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    desconto DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL,
    CONSTRAINT fk_pedido_atendente FOREIGN KEY (atendente_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 6. Tabela de Itens do Pedido (RF006)
CREATE TABLE IF NOT EXISTS itens_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    observacao VARCHAR(255),
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 7. Tabela Associativa entre Item do Pedido e Sabores Escolhidos
CREATE TABLE IF NOT EXISTS item_pedido_sabores (
    item_pedido_id INT NOT NULL,
    sabor_id INT NOT NULL,
    PRIMARY KEY (item_pedido_id, sabor_id),
    CONSTRAINT fk_ips_item FOREIGN KEY (item_pedido_id) REFERENCES itens_pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_ips_sabor FOREIGN KEY (sabor_id) REFERENCES sabores(id) ON DELETE RESTRICT
) ENGINE=InnoDB;
