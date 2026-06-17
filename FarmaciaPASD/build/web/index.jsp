<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>

<%
    String usuario = (String) session.getAttribute("usuario");
    String nome = (String) session.getAttribute("nome");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>DrogaBryan</title>

    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="icon" type="image/x-icon" href="assets/img/drogabryan.png">
</head>

<body>

<header>

    <nav>
        <div class="nav-container">

            <button class="menu-toggle" onclick="toggleMenu()">☰</button>

            <ul id="menu">

                <li><a href="index.jsp">Home</a></li>

                <% if (usuario == null) { %>
                    <li><a href="produtos.jsp">Produtos</a></li>
                <% } else { %>
                    <li><a href="view/viewFuncionario/Produtofuncionario.jsp">ProdutosFuncionario</a></li>
                <% } %>

                <% if (nome == null) { %>
                    <li><a href="login.jsp">Login</a></li>
                    <li><a href="cadcliente.jsp">Cadastre-se</a></li>
                <% } else { %>
                    <li><a href="carrinho.jsp">Carrinho</a></li>
                    <li><a href="pedidos.jsp">Pedidos</a></li>
                    <li><a href="perfil.jsp">Meu Perfil</a></li>
                    <li><a href="logout">Logout</a></li>
                <% } %>

            </ul>

        </div>
    </nav>

</header>

<main>

    <!-- HERO -->
    <section class="hero">

        <div class="hero-texto">

            <span class="badge">Entrega rápida 🚀</span>

            <h1>Farmácia online com praticidade e segurança</h1>

            <p>
                Medicamentos, vitaminas, higiene pessoal e muito mais
                com entrega rápida diretamente na sua casa.
            </p>

            <a href="produtos.jsp" class="hero-btn">
                Ver Produtos
            </a>

        </div>

        <div class="hero-imagem">
            <img src="assets/img/drogabryan.png" alt="DrogaBryan">
        </div>

    </section>

    <!-- BUSCA -->
    <!-- <section class="busca-section">

        <input type="text"
               placeholder="Buscar medicamentos, vitaminas, produtos...">

    </section> -->

    <!-- CATEGORIAS -->
    <section class="categorias">

        <h2>Categorias</h2>

        <div class="categorias-grid">

            <div class="categoria-card">
                💊
                <span>Medicamentos</span>
            </div>

            <div class="categoria-card">
                🧴
                <span>Higiene</span>
            </div>

            <div class="categoria-card">
                ❤️
                <span>Vitaminas</span>
            </div>

            <div class="categoria-card">
                👶
                <span>Bebês</span>
            </div>

        </div>

    </section>

    <!-- PROMOÇÕES -->
    <section class="promocoes">

        <div class="promo-card">
            <h3>Frete grátis</h3>
            <p>Em compras acima de R$99999</p>
        </div>

        <div class="promo-card">
            <h3>Até 40% OFF</h3>
            <p>Vitaminas e suplementos</p>
        </div>

    </section>

    <!-- PRODUTOS -->
    <section class="produtos-home">

        <div class="section-title">
            <h2>Mais vendidos</h2>
            <a href="produtos.jsp">Ver todos</a>
        </div>

        <div class="produtos-scroll">

            <div class="produto-card">

                <img src="https://product-data.raiadrogasil.io/images/5129749.webp">

                <h3>Vitamina C</h3>

                <p class="preco">R$ 29,90</p>

                <a href="AdicionarCarrinho?idproduto=7&quantidade=1" class="comprarButton">
                    Comprar
                </a>

            </div>

            <div class="produto-card">

                <img src="https://th.bing.com/th/id/OIP.9IoA1fpFKXRIZZFU6MaFaQHaHa?rs=1&pid=ImgDetMain">

                <h3>Dipirona</h3>

                <p class="preco">R$ 30,50</p>

                <a href="AdicionarCarrinho?idproduto=1&quantidade=1" class="comprarButton">
                    Comprar
                </a>

            </div>

            <div class="produto-card">

                <img src="https://m.media-amazon.com/images/I/61tBTPR81dL._AC_UF1000,1000_QL80_.jpg">

                <h3>Shampoo</h3>

                <p class="preco">R$ 9,50</p>

                <a href="AdicionarCarrinho?idproduto=5&quantidade=1" class="comprarButton">
                    Comprar
                </a>

            </div>

            <div class="produto-card">

                <img src="https://m.media-amazon.com/images/I/61ttuHTOJ0L._AC_SL1500_.jpg">

                <h3>Protetor Solar</h3>

                <p class="preco">R$ 49,90</p>

                <a href="AdicionarCarrinho?idproduto=8&quantidade=1" class="comprarButton">
                    Comprar
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
                drogabryan@gmail.com
            </li>
        </ul>

        <ul>
            <h3>Endereço</h3>

            <li>Taguatinga - Brasília/DF</li>
        </ul>

    </div>

</footer>

<!-- BOTTOM NAVIGATION -->

<div class="bottom-nav">

    <a href="index.jsp" class="bottom-item active">
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

    <a href="perfil.jsp" class="bottom-item">
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