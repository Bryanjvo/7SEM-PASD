<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.model.PedidoDAO, com.controller.Pedido, com.controller.ItemPedido" %>

<%
    Integer idCliente = (Integer) session.getAttribute("id");
    List<Pedido> listaPedidos = new ArrayList<>();

    if (idCliente != null) {
        PedidoDAO pedidoDAO = new PedidoDAO();
        listaPedidos = pedidoDAO.listarPedidos(idCliente);
    } else {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title>DrogaBryan - Meus Pedidos</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="assets/css/pedidos.css">
        <link rel="icon" type="image/x-icon" href="assets/img/drogabryan.png">
    </head>
    <body>

        <header>
            <nav>
                <div class="nav-container">

                    <button class="menu-toggle" onclick="toggleMenu()">☰</button>

                    <ul id="menu">
                        <li><a href="index.jsp">Home</a></li>
                        <li><a href="produtos.jsp">Produtos</a></li>

                        <% if (session.getAttribute("nome") == null) { %>
                        <li><a href="../../viewCliente/Login.jsp">Login</a></li>
                        <li><a href="../../viewCliente/CadCliente.jsp">Cadastro</a></li>
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
            <div>
                <h1>Meus Pedidos</h1>

                <section class="lista-carrinho">
                    <% if (listaPedidos.isEmpty()) { %>
                    <p>Você ainda não fez nenhum pedido.</p>
                    <% } else { 
                        for (Pedido pedido : listaPedidos) {
                    %>

                    <div class="item">
                        <div>
                            <h3>Pedido #<%= pedido.getId() %></h3>
                            <p><strong>Data:</strong> <%= pedido.getDataPedido() %></p>

                            <% if(pedido.getPrazoEntrega() > 0){ %>
                            <p><strong>Prazo:</strong> <%= pedido.getPrazoEntrega() %> dias</p>
                            <% } %>

                            <div style="margin-top:10px;">
                                <% for (ItemPedido item : pedido.getItens()) { %>
                                <p>
                                    <strong><%= item.getNomeProduto() %></strong><br>
                                    Qtd: <%= item.getQuantidade() %><br>
                                    Subtotal: R$<%= String.format("%.2f", item.getSubtotal()) %>
                                </p>
                                <% } %>

                                <% if(pedido.getFrete() > 0.0){ %>
                                <p><strong>Frete:</strong> R$<%= String.format("%.2f",pedido.getFrete()) %></p>
                                <% } %>
                            </div>

                            <p><strong>Total:</strong> R$<%= String.format("%.2f", pedido.getValorTotal()) %></p>

                            <!-- BLOCO DE STATUS DA RECEITA E PAGAMENTO -->
                            <div style="margin-top: 15px; padding-top: 10px; border-top: 1px solid #ddd;">

                                <% if (pedido.getReceita() != null) { %>
                                <p><strong>Status da Receita:</strong> 
                                    <% if ("PENDENTE".equalsIgnoreCase(pedido.getReceita().getStatus())) { %>
                                    <span style="color: #f0ad4e; font-weight: bold;">⏳ Em Análise</span>
                                    <% } else if ("APROVADA".equalsIgnoreCase(pedido.getReceita().getStatus())) { %>
                                    <span style="color: #28a745; font-weight: bold;">✅ Aprovada</span>
                                    <% } else if ("REJEITADA".equalsIgnoreCase(pedido.getReceita().getStatus())) { %>
                                    <span style="color: #dc3545; font-weight: bold;">❌ Rejeitada</span>
                                    <% if (pedido.getReceita().getMotivo_rejeicao() != null) { %>
                                    <br><small style="color: #666;">Motivo: <%= pedido.getReceita().getMotivo_rejeicao() %></small>
                                    <% } %>
                                    <% } %>
                                </p>
                                <% } %>

                                <p><strong>Status do Pagamento:</strong> 
                                    <% if ("APROVADO".equalsIgnoreCase(pedido.getStatusPagamento())) { %>
                                    <span style="color: #28a745; font-weight: bold;">✅ Pago</span>
                                    <% } else { %>
                                    <span style="color: #dc3545; font-weight: bold;">⏳ Não Realizado</span>
                                    <% } %>
                                </p>

                                <!-- BOTÃO DE PAGAMENTO (Liberado quando aprovada a receita ou sem necessidade dela) -->
                                <% 
                                   boolean liberadoParaPagamento = false;
                                   if (!"APROVADO".equalsIgnoreCase(pedido.getStatusPagamento())) {
                                       if (pedido.getReceita() == null || "APROVADA".equalsIgnoreCase(pedido.getReceita().getStatus())) {
                                           liberadoParaPagamento = true;
                                       }
                                   }
                                %>

                                <% if (liberadoParaPagamento) { %>
                                <form action="pagar" method="post" style="margin-top: 10px;">
                                    <input type="hidden" name="idPedido" value="<%= pedido.getId() %>">
                                    <button type="submit" style="background-color: #28a745; color: white; padding: 10px 18px; border: none; border-radius: 6px; cursor: pointer; font-weight: bold;">
                                        💳 Pagar Agora
                                    </button>
                                </form>
                                <% } else if (pedido.getReceita() != null && "PENDENTE".equalsIgnoreCase(pedido.getReceita().getStatus())) { %>
                                <p style="font-size: 0.85em; color: #555; margin-top: 5px;">
                                    ℹ️ O botão de pagamento será liberado assim que o farmacêutico aprovar sua receita médica.
                                </p>
                                <% } %>

                            </div>

                        </div>
                    </div>

                    <% } } %>
                </section>
            </div>
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
                        <a href="mailto:drogabryan@gmail.com">drogabryan@gmail.com</a>
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

            <a href="pedidos.jsp" class="bottom-item active">
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