<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String perfil = (String) session.getAttribute("perfil");
    if (!"ADMINISTRADOR".equals(perfil)) {
        response.sendRedirect("../login.jsp");
        return;
    }
    Integer totalPedidosMes = (Integer) request.getAttribute("totalPedidosMes");
    if (totalPedidosMes == null) totalPedidosMes = 0;
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Painel do Administrador - DrogaBryan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/drogabryan.png">
    <style>
        .dashboard-container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 20px;
        }
        .stat-card {
            background: #ffffff;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            text-align: center;
            max-width: 400px;
            margin: 20px auto;
            border-top: 5px solid #2ecc71;
        }
        .stat-card h3 { color: #555; margin-bottom: 10px; }
        .stat-card .number { font-size: 3rem; font-weight: bold; color: #2c3e50; }
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

<main class="dashboard-container">
    <h2>Relatório Geral do Administrador 📊</h2>
    
    <div class="stat-card">
        <h3>Pedidos Feitos no Mês Atual</h3>
        <div class="number"><%= totalPedidosMes %></div>
        <p>Total registrado no banco de dados</p>
    </div>
</main>

<div class="bottom-nav">
    <a href="${pageContext.request.contextPath}/ServletAdminDashboard" class="bottom-item active">
        <span>📊</span>
        <p>Dashboard</p>
    </a>
    <a href="${pageContext.request.contextPath}/ServletAdminProdutos" class="bottom-item">
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