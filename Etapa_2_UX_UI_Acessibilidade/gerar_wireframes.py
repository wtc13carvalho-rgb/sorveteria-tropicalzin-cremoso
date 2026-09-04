import os
from PIL import Image, ImageDraw, ImageFont

def get_font(size=14, bold=False):
    # Tenta fontes padrão do sistema Ubuntu
    font_paths = [
        "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf" if bold else "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
        "/usr/share/fonts/truetype/liberation/LiberationSans-Bold.ttf" if bold else "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf",
        "/usr/share/fonts/truetype/ubuntu/Ubuntu-B.ttf" if bold else "/usr/share/fonts/truetype/ubuntu/Ubuntu-R.ttf"
    ]
    for path in font_paths:
        if os.path.exists(path):
            try:
                return ImageFont.truetype(path, size)
            except Exception:
                pass
    return ImageFont.load_default()

def draw_window_frame(draw, title, width, height):
    # Fundo da janela (cinza claro moderno)
    draw.rectangle([(0, 0), (width, height)], fill="#F0F2F5", outline="#CCCCCC", width=1)
    # Barra de título desktop
    draw.rectangle([(0, 0), (width, 36)], fill="#1E3A8A") # Azul Marinho
    # Botões minimizar, maximizar, fechar
    draw.ellipse([(width - 70, 12), (width - 58, 24)], fill="#FBBF24")
    draw.ellipse([(width - 48, 12), (width - 36, 24)], fill="#10B981")
    draw.ellipse([(width - 26, 12), (width - 14, 24)], fill="#EF4444")
    # Título da janela
    font_title = get_font(14, bold=True)
    draw.text((16, 8), title, fill="#FFFFFF", font=font_title)

def draw_button(draw, xy, text, bg="#2563EB", fg="#FFFFFF", font=None):
    if font is None:
        font = get_font(12, bold=True)
    draw.rectangle(xy, fill=bg, outline="#1D4ED8", width=1)
    bbox = font.getbbox(text)
    tw = bbox[2] - bbox[0]
    th = bbox[3] - bbox[1]
    bx = (xy[0] + xy[2] - tw) / 2
    by = (xy[1] + xy[3] - th) / 2 - 2
    draw.text((bx, by), text, fill=fg, font=font)

def draw_input(draw, xy, label, placeholder="", font=None):
    if font is None:
        font = get_font(11)
    font_lbl = get_font(12, bold=True)
    # Rótulo acima
    draw.text((xy[0], xy[1] - 18), label, fill="#374151", font=font_lbl)
    # Caixa de texto
    draw.rectangle(xy, fill="#FFFFFF", outline="#9CA3AF", width=1)
    if placeholder:
        draw.text((xy[0] + 8, xy[1] + 6), placeholder, fill="#6B7280", font=font)

def create_wireframes(output_dir):
    os.makedirs(output_dir, exist_ok=True)
    W, H = 1000, 650

    # 1. TELA DE LOGIN
    img = Image.new("RGB", (W, H), "#E5E7EB")
    draw = ImageDraw.Draw(img)
    draw_window_frame(draw, "Tropicalzin Cremoso - Acesso ao Sistema Desktop v1.0", W, H)
    
    # Card Centralizado de Login
    cx, cy = W // 2, H // 2
    card = [(cx - 220, cy - 180), (cx + 220, cy + 200)]
    draw.rectangle(card, fill="#FFFFFF", outline="#CBD5E1", width=2)
    
    # Header do Card
    draw.rectangle([(cx - 220, cy - 180), (cx + 220, cy - 110)], fill="#0284C7")
    draw.text((cx - 150, cy - 165), "Sorveteria Tropicalzin Cremoso", fill="#FFFFFF", font=get_font(16, bold=True))
    draw.text((cx - 95, cy - 135), "Login de Autenticação", fill="#E0F2FE", font=get_font(12))

    # Inputs
    draw_input(draw, (cx - 180, cy - 60, cx + 180, cy - 25), "Usuário / Operador:", "Ex: wallace.carvalho")
    draw_input(draw, (cx - 180, cy + 25, cx + 180, cy + 60), "Senha de Acesso:", "••••••••")

    # Botões
    draw_button(draw, (cx - 180, cy + 90, cx - 10, cy + 130), "Entrar (F2)", bg="#059669")
    draw_button(draw, (cx + 10, cy + 90, cx + 180, cy + 130), "Cancelar (Esc)", bg="#DC2626")
    
    # Rodapé de acessibilidade
    draw.text((cx - 180, cy + 155), "Dica: Use TAB para alternar campos e ENTER para confirmar.", fill="#6B7280", font=get_font(10))
    img.save(os.path.join(output_dir, "01_wireframe_login.png"))

    # 2. TELA PRINCIPAL (DASHBOARD COM MENU)
    img = Image.new("RGB", (W, H), "#F8FAFC")
    draw = ImageDraw.Draw(img)
    draw_window_frame(draw, "Tropicalzin Cremoso - Sistema de Gestão e Caixa (Desktop)", W, H)

    # Barra de Menu Superior
    draw.rectangle([(0, 36), (W, 68)], fill="#F1F5F9", outline="#CBD5E1", width=1)
    menus = ["Início (Alt+I)", "Sabores (Alt+S)", "Produtos (Alt+P)", "Clientes (Alt+C)", "Novo Pedido (Alt+O)", "Sorteador (Alt+R)", "Ajuda"]
    mx = 16
    for m in menus:
        draw.text((mx, 44), m, fill="#1E293B", font=get_font(12, bold=True))
        mx += 135

    # Banner de boas-vindas
    draw.rectangle([(20, 85), (W - 20, 165)], fill="#0284C7")
    draw.text((40, 100), "Bem-vindo ao Sistema Tropicalzin Cremoso!", fill="#FFFFFF", font=get_font(20, bold=True))
    draw.text((40, 135), "Operador ativo: Wallace Teixeira Carvalho | Perfil: Atendente / Gerente", fill="#E0F2FE", font=get_font(13))

    # Cards de Acesso Rápido (Dashboard)
    cards = [
        ("Novo Pedido / Frente de Caixa", "Atendimento rápido, seleção de potes, casquinhas e adicionais.", "#059669", 20, 190),
        ("Sorteador de Sabores (RF005)", "Sorteie sabores aleatórios e surpreenda o cliente!", "#D97706", 510, 190),
        ("Gestão de Sabores", "Controle de sabores artesanais, especiais e tradicionais.", "#2563EB", 20, 360),
        ("Catálogo de Produtos & Preços", "Cadastro de recipientes, limites de bolas e valores.", "#7C3AED", 510, 360)
    ]
    for title, desc, col, x, y in cards:
        draw.rectangle([(x, y), (x + 470, y + 140)], fill="#FFFFFF", outline=col, width=2)
        draw.rectangle([(x, y), (x + 470, y + 40)], fill=col)
        draw.text((x + 15, y + 10), title, fill="#FFFFFF", font=get_font(14, bold=True))
        draw.text((x + 15, y + 60), desc, fill="#475569", font=get_font(12))
        draw_button(draw, (x + 15, y + 95, x + 160, y + 128), "Acessar Módulo", bg=col)

    # Barra de Status Inferior (Acessibilidade)
    draw.rectangle([(0, H - 32), (W, H)], fill="#E2E8F0", outline="#CBD5E1")
    draw.text((15, H - 24), "Status: Sistema Operacional Online | Atalhos: [F1: Ajuda] [F2: Novo Pedido] [F3: Sorteador] [Esc: Sair]", fill="#334155", font=get_font(11))
    img.save(os.path.join(output_dir, "02_wireframe_principal.png"))

    # 3. TELA DE GESTÃO DE SABORES (CRUD)
    img = Image.new("RGB", (W, H), "#F8FAFC")
    draw = ImageDraw.Draw(img)
    draw_window_frame(draw, "Tropicalzin Cremoso - Gestão de Sabores", W, H)

    # Form à esquerda
    draw.rectangle([(20, 55), (380, 580)], fill="#FFFFFF", outline="#CBD5E1", width=1)
    draw.rectangle([(20, 55), (380, 95)], fill="#0284C7")
    draw.text((35, 65), "Dados do Sabor", fill="#FFFFFF", font=get_font(14, bold=True))

    draw_input(draw, (35, 130, 365, 165), "Nome do Sabor:", "Ex: Açaí com Banana")
    draw_input(draw, (35, 205, 365, 240), "Descrição / Ingredientes:", "Ex: Açaí puro com pedaços de fruta")
    draw_input(draw, (35, 280, 365, 315), "Tipo / Categoria:", "ESPECIAL (ou Tradicional / Zero)")
    draw_input(draw, (35, 355, 365, 390), "Disponibilidade em Balcão:", "DISPONÍVEL")

    draw_button(draw, (35, 430, 190, 470), "Salvar (Alt+S)", bg="#059669")
    draw_button(draw, (210, 430, 365, 470), "Limpar (Alt+L)", bg="#4B5563")
    draw_button(draw, (35, 490, 190, 530), "Editar Seleção", bg="#2563EB")
    draw_button(draw, (210, 490, 365, 530), "Excluir Sabor", bg="#DC2626")

    # Tabela à direita
    draw.rectangle([(400, 55), (W - 20, 580)], fill="#FFFFFF", outline="#CBD5E1", width=1)
    draw.rectangle([(400, 55), (W - 20, 95)], fill="#1E293B")
    draw.text((415, 65), "Sabores Atualmente Cadastrados", fill="#FFFFFF", font=get_font(14, bold=True))

    # Cabeçalho da Tabela
    draw.rectangle([(410, 110), (W - 30, 140)], fill="#E2E8F0")
    draw.text((420, 120), "ID", fill="#1E293B", font=get_font(12, bold=True))
    draw.text((460, 120), "Nome do Sabor", fill="#1E293B", font=get_font(12, bold=True))
    draw.text((680, 120), "Categoria", fill="#1E293B", font=get_font(12, bold=True))
    draw.text((820, 120), "Status", fill="#1E293B", font=get_font(12, bold=True))

    rows = [
        ("1", "Açaí do Pará Especial", "ESPECIAL", "Disponível"),
        ("2", "Cupuaçu com Castanha", "ESPECIAL", "Disponível"),
        ("3", "Ninho Trufado com Nutella", "TRADICIONAL", "Disponível"),
        ("4", "Maracujá Zero Açúcar", "ZERO_ACUCAR", "Disponível"),
        ("5", "Chocolate Belga 70%", "ESPECIAL", "Disponível"),
        ("6", "Morango Silvestre", "TRADICIONAL", "Esgotado")
    ]
    ry = 145
    for rid, rname, rcat, rstat in rows:
        bg_row = "#F8FAFC" if int(rid) % 2 == 1 else "#FFFFFF"
        draw.rectangle([(410, ry), (W - 30, ry + 32)], fill=bg_row, outline="#E2E8F0")
        draw.text((420, ry + 8), rid, fill="#334155", font=get_font(12))
        draw.text((460, ry + 8), rname, fill="#1E293B", font=get_font(12, bold=True))
        draw.text((680, ry + 8), rcat, fill="#475569", font=get_font(12))
        st_color = "#059669" if rstat == "Disponível" else "#DC2626"
        draw.text((820, ry + 8), rstat, fill=st_color, font=get_font(12, bold=True))
        ry += 35

    img.save(os.path.join(output_dir, "03_wireframe_cadastro_sabores.png"))

    # 4. TELA DE FRENTE DE CAIXA / PEDIDO COM SORTEADOR ALEATÓRIO (RF004 e RF005)
    img = Image.new("RGB", (W, H), "#F8FAFC")
    draw = ImageDraw.Draw(img)
    draw_window_frame(draw, "Tropicalzin Cremoso - Frente de Caixa & Registro de Pedidos (RF004 / RF005)", W, H)

    # Painel Esquerdo: Seleção e Sorteador
    draw.rectangle([(20, 50), (480, 580)], fill="#FFFFFF", outline="#CBD5E1", width=1)
    draw.rectangle([(20, 50), (480, 90)], fill="#0284C7")
    draw.text((35, 60), "1. Montagem do Item & Seleção", fill="#FFFFFF", font=get_font(14, bold=True))

    draw_input(draw, (35, 120, 465, 155), "Cliente:", "Consumidor Final (ou selecionar cadastrado)")
    draw_input(draw, (35, 190, 465, 225), "Produto / Recipiente:", "Casquinha Dupla Crocante - R$ 12,50 (Até 2 bolas)")

    # Caixa Destaque Sorteador Aleatório
    draw.rectangle([(35, 250), (465, 390)], fill="#FFFBEB", outline="#F59E0B", width=2)
    draw.text((50, 260), "★ Sorteador Aleatório de Sabores (RF005) ★", fill="#B45309", font=get_font(13, bold=True))
    draw.text((50, 285), "Deixe a sorte decidir os sabores do cliente!", fill="#78350F", font=get_font(11))
    draw_button(draw, (50, 315, 450, 355), "🎲 SORTEAR SABORES AGORA!", bg="#D97706")
    draw.text((50, 365), "Resultado do Sorteio: [Açaí do Pará] + [Ninho Trufado]", fill="#059669", font=get_font(12, bold=True))

    draw_input(draw, (35, 425, 240, 460), "Quantidade:", "1")
    draw_button(draw, (260, 425, 465, 460), "+ Adicionar Item", bg="#059669")

    # Painel Direito: Itens do Pedido e Pagamento
    draw.rectangle([(500, 50), (W - 20, 580)], fill="#FFFFFF", outline="#CBD5E1", width=1)
    draw.rectangle([(500, 50), (W - 20, 90)], fill="#1E293B")
    draw.text((515, 60), "2. Resumo do Pedido & Fechamento", fill="#FFFFFF", font=get_font(14, bold=True))

    # Tabela de Itens
    draw.rectangle([(515, 105), (W - 35, 135)], fill="#E2E8F0")
    draw.text((525, 115), "Item", fill="#1E293B", font=get_font(11, bold=True))
    draw.text((700, 115), "Sabores", fill="#1E293B", font=get_font(11, bold=True))
    draw.text((860, 115), "Qtd", fill="#1E293B", font=get_font(11, bold=True))
    draw.text((910, 115), "Subtotal", fill="#1E293B", font=get_font(11, bold=True))

    item_rows = [
        ("Casquinha Dupla", "Açaí + Ninho Trufado (Sorteados)", "1", "R$ 12,50"),
        ("Copo Grande 500ml", "Cupuaçu + Chocolate Belga", "1", "R$ 18,00")
    ]
    iy = 140
    for it_p, it_s, it_q, it_sub in item_rows:
        draw.rectangle([(515, iy), (W - 35, iy + 30)], fill="#FFFFFF", outline="#E2E8F0")
        draw.text((525, iy + 6), it_p, fill="#1E293B", font=get_font(11, bold=True))
        draw.text((700, iy + 6), it_s, fill="#475569", font=get_font(10))
        draw.text((865, iy + 6), it_q, fill="#334155", font=get_font(11))
        draw.text((910, iy + 6), it_sub, fill="#059669", font=get_font(11, bold=True))
        iy += 34

    # Bloco de Totais
    draw.rectangle([(515, 330), (W - 35, 450)], fill="#F8FAFC", outline="#CBD5E1", width=1)
    draw.text((535, 345), "Forma de Pagamento:", fill="#1E293B", font=get_font(12, bold=True))
    draw.text((535, 375), "(•) PIX Instantâneo     ( ) Cartão Crédito/Débito     ( ) Dinheiro", fill="#334155", font=get_font(11))
    draw.text((535, 410), "VALOR TOTAL A PAGAR:", fill="#0F172A", font=get_font(14, bold=True))
    draw.text((780, 405), "R$ 30,50", fill="#059669", font=get_font(22, bold=True))

    draw_button(draw, (515, 480, 730, 540), "Finalizar Pedido (F5)", bg="#059669")
    draw_button(draw, (750, 480, W - 35, 540), "Cancelar Pedido", bg="#DC2626")

    img.save(os.path.join(output_dir, "04_wireframe_pedidos_sorteador.png"))
    print(f"Wireframes gerados com sucesso em {output_dir}!")

if __name__ == "__main__":
    create_wireframes("/home/edcarlos/workspace/pessoal/desenvolvimento_atv/Etapa_2_UX_UI_Acessibilidade/wireframes")
