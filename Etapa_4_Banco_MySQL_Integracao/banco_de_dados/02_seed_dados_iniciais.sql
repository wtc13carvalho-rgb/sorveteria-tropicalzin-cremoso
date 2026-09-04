-- =============================================================================
-- PROJETO INTEGRADOR - SENAC (TDS - UC15)
-- Sistema Sorveteria Tropicalzin Cremoso
-- Script de Povoamento Inicial de Dados para Testes (MySQL Workbench)
-- =============================================================================

USE sorveteria_db;

-- Inserindo Usuários Padrão
INSERT INTO usuarios (nome, login, senha, perfil) VALUES
('Wallace Teixeira Carvalho (Gerente)', 'admin', '1234', 'GERENTE'),
('Atendente de Balcão', 'atendente', '1234', 'ATENDENTE');

-- Inserindo Sabores de Sorvete (Artesanais, Tradicionais e Zero)
INSERT INTO sabores (nome, descricao, tipo, disponivel) VALUES
('Açaí do Pará Especial', 'Açaí artesanal cremoso puro da Amazônia', 'ESPECIAL', TRUE),
('Cupuaçu com Castanha do Pará', 'Fruta nativa amazônica combinada com crocante de castanhas', 'ESPECIAL', TRUE),
('Ninho Trufado com Nutella', 'Leite ninho cremoso mesclado com autêntica Nutella', 'TRADICIONAL', TRUE),
('Chocolate Belga 70%', 'Cacau selecionado com textura aveludada e raspas', 'ESPECIAL', TRUE),
('Maracujá Tropical Zero', 'Fruta fresca com sementes e sem adição de açúcares', 'ZERO_ACUCAR', TRUE),
('Morango Silvestre', 'Pedaços selecionados de morango com calda artesanal', 'TRADICIONAL', TRUE),
('Torta de Limão Siciliano', 'Sorvete cítrico com farofa de biscoito e merengue', 'ESPECIAL', TRUE),
('Pistache Italiano', 'Feito com pasta pura de pistache e pedaços tostados', 'ESPECIAL', FALSE); -- Teste de esgotado

-- Inserindo Produtos / Formatos
INSERT INTO produtos (nome, preco, categoria, max_bolas, estoque) VALUES
('Casquinha Simples Crocante', 8.00, 'CASQUINHA', 1, 150),
('Casquinha Dupla Crocante', 13.50, 'CASQUINHA', 2, 120),
('Cascão Especial Cestinha', 17.00, 'CASQUINHA', 3, 80),
('Copo Médio 300ml', 14.00, 'COPO', 2, 200),
('Copo Grande 500ml', 19.50, 'COPO', 3, 180),
('Pote Térmico 1 Litro', 38.00, 'POTE', 4, 50),
('Picolé Gourmet Artesanal', 6.50, 'PICOLÉ', 1, 90);

-- Inserindo Clientes para Teste
INSERT INTO clientes (nome, cpf, telefone, email) VALUES
('Maria das Graças Silva', '123.456.789-01', '(93) 98111-2233', 'maria.silva@email.com'),
('João Pereira dos Santos', '234.567.890-12', '(93) 98444-5566', 'joao.santos@email.com'),
('Ana Paula Oliveira', '345.678.901-23', '(93) 98777-8899', 'ana.oliveira@email.com');

-- Inserindo Pedido Inicial de Teste
INSERT INTO pedidos (cliente_id, atendente_id, data_hora, forma_pagamento, status, valor_total, desconto) VALUES
(1, 1, NOW(), 'PIX', 'ENTREGUE', 27.50, 0.00);

-- Inserindo Itens do Pedido de Teste
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal, observacao) VALUES
(1, 2, 1, 13.50, 13.50, 'Casquinha Dupla com Açaí e Ninho Trufado'),
(1, 4, 1, 14.00, 14.00, 'Copo Médio com Cupuaçu e Morango');

-- Vinculando Sabores aos Itens
INSERT INTO item_pedido_sabores (item_pedido_id, sabor_id) VALUES
(1, 1), -- Item 1: Açaí
(1, 3), -- Item 1: Ninho Trufado
(2, 2), -- Item 2: Cupuaçu
(2, 6); -- Item 2: Morango
