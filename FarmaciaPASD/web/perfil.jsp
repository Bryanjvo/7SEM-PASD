<%-- 
    Document   : perfil
    Created on : May 12, 2025, 4:45:36 PM
    Author     : bryan
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%

    if (session == null || session.getAttribute("id") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DrogaBryan</title>
    <link rel="icon" type="image/x-icon" href="assets/img/drogabryan.png">
    <link rel="stylesheet" href="assets/css/perfil.css">
</head>

<body>

<header>
    <nav>

        <div class="nav-container">

            <!-- MENU HAMBURGUER -->
            <button class="menu-toggle" onclick="toggleMenu()">☰</button>

            <ul id="menu">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="produtos.jsp">Produtos</a></li>

                <%
                    if (session.getAttribute("nome") == null) {
                %>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="cadcliente.jsp">Cadastro</a></li>
                <%
                    } else { 
                %>
                    <li><a href="carrinho.jsp">Carrinho</a></li>
                    <li><a href="pedidos.jsp">Pedidos</a></li>
                    <li><a href="perfil.jsp">Meu Perfil</a></li>
                    <li><a href="logout">Logout</a></li>
                <%
                    }
                %>
            </ul>

        </div>

    </nav>
</header>

<main>

    <section class="perfil-section">

        <div class="form">

            <div class="perfil-topo">
                <div class="perfil-avatar">👤</div>

                <div>
                    <h1>Meu Perfil</h1>
                    <p class="subtitulo">Área do cliente DrogaBryan</p>
                </div>
            </div>

            <div class="info-group">

                <h4 class="dados">Seus Dados</h4>

                <div class="info-card">
                    <span>Nome</span>
                    <p><%= session.getAttribute("nome") %></p>
                </div>

                <div class="info-card">
                    <span>E-mail</span>
                    <p><%= session.getAttribute("email") %></p>
                </div>

                <div class="info-card">
                    <span>Endereço</span>
                    <p><%= session.getAttribute("endereco") %></p>
                </div>

            </div>

            <div class="acoes">

                <h4 class="opcoes">Opções</h4>

                <a href="pedidos.jsp" class="perfil-btn">
                    📦 Meus Pedidos
                </a>

                <a href="cadcliente.jsp" class="perfil-btn">
                    ➕ Cadastrar Nova Conta
                </a>

                <a href="logout" class="perfil-btn sair">
                    🚪 Sair da Conta
                </a>

            </div>

        </div>

    </section>

</main>

<footer>
    <div class="footer-content">

        <ul class="autores">
            <h3>Autor</h3>
            <li>
                <img class="autoresImg" src="assets/img/github-mark.png">
                <a href="https://github.com/Bryanjvo">Bryan</a>
            </li>
        </ul>

        <ul>
            <h3>Contato</h3>
            <li>
                <img class="autoresImg" src="assets/img/telefone.png">
                (61) 91234-5678
            </li>
            <li>
                <img class="autoresImg" src="assets/img/email.png">
                <a href="mailto:#">drogabryan@gmail.com</a>
            </li>
        </ul>

        <ul>
            <h3>Endereço</h3>
            <li>CEP: 260.333-299</li>
            <li>CNB 10</li>
            <li>Taguatinga - Brasília/DF</li>
        </ul>

        <ul>
            <h3>Redes Sociais</h3>
            <li>
                <img class="autoresImg" src="assets/img/ig icon.png">
                <a href="#">Instagram</a>
            </li>
            <li>
                <img class="autoresImg" src="assets/img/whatsapp.png">
                <a href="#">WhatsApp</a>
            </li>
        </ul>

    </div>
</footer>

<!-- BOTTOM NAVIGATION -->

<div class="bottom-nav">

    <a href="index.jsp" class="bottom-item">
        <span>🏠</span>
        <p>Home</p>
    </a>

    <a href="produtos.jsp" class="bottom-item">
        <span>🛍</span>
        <p>Produtos</p>
    </a>

    <a href="carrinho.jsp" class="bottom-item">
        <span>🛒</span>
        <p>Carrinho</p>
    </a>

    <a href="pedidos.jsp" class="bottom-item">
        <span>📦</span>
        <p>Pedidos</p>
    </a>

    <a href="perfil.jsp" class="bottom-item active">
        <span>👤</span>
        <p>Perfil</p>
    </a>

</div>

<script>
function toggleMenu() {
    document.getElementById("menu").classList.toggle("show");
}
</script>

</body>
</html>