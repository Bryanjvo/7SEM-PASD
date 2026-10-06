<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.controller.Produtos" %>
<%
    String perfil = (String) session.getAttribute("perfil");
    if (!"ADMINISTRADOR".equals(perfil)) {
        response.sendRedirect("../login.jsp");
        return;
    }
    List<Produtos> listaProdutos = (List<Produtos>) request.getAttribute("listaProdutos");
    Produtos produtoEditar = (Produtos) request.getAttribute("produtoEditar");
    boolean editando = (produtoEditar != null);
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Produtos - DrogaBryan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/drogabryan.png">
    <style>
        .admin-container { max-width: 1100px; margin: 30px auto; padding: 20px; }
        .form-box { background: #fff; padding: 25px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 15px; }
        .form-group { display: flex; flex-direction: column; }
        .form-group label { font-weight: bold; margin-bottom: 5px; }
        .form-group input { padding: 8px; border: 1px solid #ccc; border-radius: 5px; }
        .btn-submit { background-color: #27ae60; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; margin-top: 15px; font-weight: bold; }
        .btn-submit:hover { background-color: #219150; }
        .btn-cancel { background-color: #7f8c8d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-left: 10px; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05); }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #34495e; color: white; }
        .img-thumb { width: 45px; height: 45px; object-fit: cover; border-radius: 5px; }
        .btn-edit { color: #2980b9; font-weight: bold; text-decoration: none; margin-right: 10px; }
        .btn-delete { color: #c0392b; font-weight: bold; text-decoration: none; }
    </style>
</head>
<body>

<header>
    <nav>
        <div class="nav-container">
            <button class="menu-toggle" onclick="toggleMenu()">☰</button>
            <ul id="menu">
                <li><a href="${pageContext.request.contextPath}/ServletAdminDashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/ServletAdminProdutos">Gerenciar Produtos</a></li>
                <li><a href="${pageContext.request.contextPath}/ServletAdminPedidos">Status dos Pedidos</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </nav>
</header>

<main class="admin-container">
    <h2>Gerenciar Catálogo de Produtos 📦</h2>

    <!-- FORMULÁRIO DE CADASTRO E EDIÇÃO -->
    <div class="form-box">
        <h3><%= editando ? "Editar Produto #" + produtoEditar.getId() : "Adicionar Novo Produto" %></h3>
        <form action="${pageContext.request.contextPath}/ServletAdminProdutos" method="post">
            <input type="hidden" name="id" value="<%= editando ? produtoEditar.getId() : "" %>">
            
            <div class="form-grid">
                <div class="form-group">
                    <label>Nome do Produto:</label>
                    <input type="text" name="nome" value="<%= editando ? produtoEditar.getNome() : "" %>" required>
                </div>
                <div class="form-group">
                    <label>Preço (R$):</label>
                    <input type="number" step="0.01" name="preco" value="<%= editando ? produtoEditar.getPreco() : "" %>" required>
                </div>
                <div class="form-group">
                    <label>Quantidade em Estoque:</label>
                    <input type="number" name="estoque" value="<%= editando ? produtoEditar.getEstoque() : "" %>" required>
                </div>
                <div class="form-group">
                    <label>URL da Imagem:</label>
                    <input type="text" name="imagem" value="<%= editando && produtoEditar.getImagem() != null ? produtoEditar.getImagem() : "" %>" placeholder="https://...">
                </div>
                <div class="form-group" style="justify-content: center;">
                    <label>
                        <input type="checkbox" name="receita" <%= editando && produtoEditar.isReceita() ? "checked" : "" %>> Exige Receita Médica?
                    </label>
                </div>
            </div>

            <button type="submit" class="btn-submit"><%= editando ? "Atualizar Produto" : "Salvar Produto" %></button>
            <% if (editando) { %>
                <a href="${pageContext.request.contextPath}/ServletAdminProdutos" class="btn-cancel">Cancelar</a>
            <% } %>
        </form>
    </div>

    <!-- TABELA DE PRODUTOS -->
    <h3>Produtos Cadastrados</h3>
    <table>
        <thead>
            <tr>
                <th>Imagem</th>
                <th>Nome</th>
                <th>Preço</th>
                <th>Estoque</th>
                <th>Receita</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <% if (listaProdutos != null && !listaProdutos.isEmpty()) { 
                for (Produtos p : listaProdutos) { %>
                <tr>
                    <td><img src="<%= (p.getImagem() != null && !p.getImagem().isEmpty()) ? p.getImagem() : "../assets/img/drogabryan.png" %>" class="img-thumb"></td>
                    <td><%= p.getNome() %></td>
                    <td>R$ <%= String.format("%.2f", p.getPreco()) %></td>
                    <td><%= p.getEstoque() %></td>
                    <td><%= p.isReceita() ? "Sim 📄" : "Não" %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/ServletAdminProdutos?acao=editar&id=<%= p.getId() %>" class="btn-edit">Editar</a>
                        <a href="${pageContext.request.contextPath}/ServletAdminProdutos?acao=excluir&id=<%= p.getId() %>" class="btn-delete" onclick="return confirm('Tem certeza que deseja excluir?')">Excluir</a>
                    </td>
                </tr>
            <%  } 
               } else { %>
                <tr><td colspan="6">Nenhum produto cadastrado no catálogo.</td></tr>
            <% } %>
        </tbody>
    </table>
</main>

<div class="bottom-nav">
    <a href="${pageContext.request.contextPath}/ServletAdminDashboard" class="bottom-item">
        <span>📊</span>
        <p>Dashboard</p>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdminProdutos" class="bottom-item active">
        <span>📦</span>
        <p>Produtos</p>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdminPedidos" class="bottom-item">
        <span>🚚</span>
        <p>Pedidos</p>
    </a>
</div>

<script>
function toggleMenu() {
    document.getElementById("menu").classList.toggle("show");
}
</script>
</body>
</html>