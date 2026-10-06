<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.controller.Pedido" %>
<%
    String perfil = (String) session.getAttribute("perfil");
    if (!"ADMINISTRADOR".equals(perfil)) {
        response.sendRedirect("../login.jsp");
        return;
    }
    List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Status dos Pedidos - DrogaBryan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/drogabryan.png">
    <style>
        .admin-container { max-width: 1100px; margin: 30px auto; padding: 20px; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05); }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #34495e; color: white; }
        .select-status { padding: 6px; border-radius: 5px; border: 1px solid #ccc; }
        .btn-update { background-color: #3498db; color: white; border: none; padding: 6px 12px; border-radius: 5px; cursor: pointer; }
        .btn-update:hover { background-color: #2980b9; }
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
    <h2>Gerenciar Status de Entrega dos Pedidos 🚚</h2>

    <table>
        <thead>
            <tr>
                <th>#ID</th>
                <th>Cliente</th>
                <th>Data</th>
                <th>Valor Total</th>
                <th>Status Atual</th>
                <th>Atualizar Status</th>
            </tr>
        </thead>
        <tbody>
            <% if (listaPedidos != null && !listaPedidos.isEmpty()) { 
                for (Pedido p : listaPedidos) { 
                    String st = p.getStatusPedido() != null ? p.getStatusPedido() : "em separacao";
            %>
                <tr>
                    <td>#<%= p.getId() %></td>
                    <td><%= p.getNomeCliente() %></td>
                    <td><%= p.getDataPedido() %></td>
                    <td>R$ <%= String.format("%.2f", p.getValorTotal()) %></td>
                    <td><strong><%= st %></strong></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/ServletAdminPedidos" method="post" style="display:flex; gap: 8px;">
                            <input type="hidden" name="idPedido" value="<%= p.getId() %>">
                            <select name="novoStatus" class="select-status">
                                <option value="em separacao" <%= "em separacao".equals(st) ? "selected" : "" %>>em separação</option>
                                <option value="saiu para entrega" <%= "saiu para entrega".equals(st) ? "selected" : "" %>>saiu para entrega</option>
                                <option value="entregue!" <%= "entregue!".equals(st) ? "selected" : "" %>>entregue!</option>
                            </select>
                            <button type="submit" class="btn-update">Salvar</button>
                        </form>
                    </td>
                </tr>
            <%  } 
               } else { %>
                <tr><td colspan="6">Nenhum pedido encontrado.</td></tr>
            <% } %>
        </tbody>
    </table>
</main>

<div class="bottom-nav">
    <a href="${pageContext.request.contextPath}/ServletAdminDashboard" class="bottom-item">
        <span>📊</span>
        <p>Dashboard</p>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdminProdutos" class="bottom-item">
        <span>📦</span>
        <p>Produtos</p>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdminPedidos" class="bottom-item active">
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