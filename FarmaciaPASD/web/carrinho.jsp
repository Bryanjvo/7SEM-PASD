<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.controller.ItemCarrinho" %>
<%@ page import="com.controller.Carrinho" %>
<%@ page import="com.model.CarrinhoDAO" %>

<%
    Integer idCliente = (Integer) session.getAttribute("id");

    ArrayList<ItemCarrinho> listaCarrinho = new ArrayList<>();

    if (idCliente != null) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId_cliente(idCliente);

        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
        listaCarrinho = (ArrayList<ItemCarrinho>) carrinhoDAO.listarCarrinho(carrinho);
    } else {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DrogaBryan - Carrinho</title>
        <link rel="stylesheet" href="assets/css/carrinho.css">
    </head>

    <body>

        <header>
            <nav>
                <div class="nav-container">
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

            <h1>Carrinho de Compras</h1>

            <section class="lista-carrinho">

                <%
                    double total = 0.0;

                    for (ItemCarrinho itemcarrinho : listaCarrinho) {
                        double subtotal = itemcarrinho.getSubtotal();
                        total += subtotal;
                %>

                <div class="item">
                    <img class="imgProduto" src="<%= itemcarrinho.getProduto().getImagem() %>">

                    <h3><%= itemcarrinho.getProduto().getNome() %></h3>

                    <div class="item-info-acoes">

                        <div class="item-info">
                            <p>Preço: R$<%= itemcarrinho.getProduto().getPreco() %></p>
                            <p>Quantidade: <%= itemcarrinho.getQuantidade() %></p>
                            <p>Subtotal: R$<%= String.format("%.2f", subtotal) %></p>
                        </div>

                        <div class="item-acoes">
                            <a href="AdicionarCarrinho?idproduto=<%= itemcarrinho.getProduto().getId() %>&quantidade=1" class="comprarButton">+</a>

                            <% if(itemcarrinho.getQuantidade() > 1){ %>
                            <a href="DiminuirProdutoCarrinho?idproduto=<%= itemcarrinho.getProduto().getId() %>&quantidade=1" class="comprarButton">-</a>
                            <% } else { %>
                            <a href="ExcluirProdutoCarrinho?idproduto=<%= itemcarrinho.getProduto().getId() %>&quantidade=1" class="comprarButton">🗑</a>
                            <% } %>
                        </div>

                    </div>
                </div>

                <% } %>

                <!-- FRETE (RESTAURADO) -->
                <div class="frete-form">
                    <form action="calcular-frete" method="post">
                        <input type="text" name="cep" placeholder="Digite seu CEP" required>
                        <button type="submit">Calcular Frete</button>
                    </form>
                </div>

                <%
                    String valorFrete = (String) request.getAttribute("valorFrete");
                    Object prazoEntregaObj = request.getAttribute("prazoEntrega");
                    String prazoEntregaFormatado = "";

                    if (prazoEntregaObj != null) {
                        if (prazoEntregaObj instanceof Integer) {
                            int prazo = (Integer) prazoEntregaObj;
                            prazoEntregaFormatado = prazo + (prazo == 1 ? " dia" : " dias");
                        } else {
                            prazoEntregaFormatado = (String) prazoEntregaObj;
                        }
                    }

                    if (valorFrete != null && prazoEntregaObj != null) {
                %>

                <div>
                    <p>Frete: R$ <%= valorFrete %></p>
                    <p>Prazo: <%= prazoEntregaFormatado %></p>
                </div>

                <% } %>

            </section>

            <section class="total-carrinho">

                <h2>Subtotal: R$ <%= String.format("%.2f", total) %></h2>

                <%
                double valorFreteDouble = 0.0;

                if (valorFrete != null && !valorFrete.isEmpty()) {
                    valorFreteDouble = Double.parseDouble(valorFrete.replace(",", "."));
                }

                double totalCompra = total + valorFreteDouble;
                %>

                <% if (valorFrete != null && prazoEntregaObj != null) { %>

                <h2>Total: R$ <%= String.format("%.2f", totalCompra) %></h2>

                <form action="pagar" method="post">
                    <input type="hidden" name="frete" value="<%= valorFrete %>">
                    <button class="finalizarButton">Finalizar Compra</button>
                </form>

                <% } else { %>

                <h2>Calcule o frete para finalizar</h2>

                <% } %>

            </section>

        </main>

        <!-- FOOTER ORIGINAL RESTAURADO -->
        <footer>
            <div class="footer-content">
                <ul class="autores">
                    <h3>Autor</h3>
                    <li> <img class="autoresImg" src="assets/img/github-mark.png"> <a href="https://github.com/Bryanjvo">Bryan</a></li>
                </ul>
                <ul>
                    <h3>Contato</h3>
                    <li> <img class="autoresImg" src="assets/img/telefone.png"> (61) 91234-5678</li>
                    <li> <img class="autoresImg" src="assets/img/email.png"> <a style="color: white;" href="mailto:#">drogabryan@gmail.com</a></li>
                </ul>
                <ul>
                    <h3>Endereço</h3>
                    <li>CEP: 260.333-299</li>
                    <li>CNB 10</li>
                    <li>Taguatinga - Brasília/DF</li>
                </ul>
                <ul>
                    <h3>Redes Sociais</h3>
                    <li> <img class="autoresImg" src="assets/img/ig icon.png"> <a href="#">Instagram</a></li>
                    <li> <img class="autoresImg" src="assets/img/whatsapp.png"> <a href="#">WhatsApp</a></li>
                </ul>
            </div>
        </footer>
        <!-- BOTTOM NAVIGATION -->

        <div class="bottom-nav">

            <a href="index.jsp" class="bottom-item">
                <span>🏠</span>
                <p>Home</p>
            </a>

            <a href="produtos.jsp" class="bottom-item active">
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

            <a href="carrinho.jsp" class="bottom-item active">
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