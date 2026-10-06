<%-- 
    Document   : receitasPendentes
    Created on : Oct 6, 2026, 11:55:44 AM
    Author     : bryan
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.controller.ReceitaMedica" %>
<%
    String perfil = (String) session.getAttribute("perfil");
    if (!"FARMACEUTICO".equals(perfil)) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<ReceitaMedica> listaReceitas = (List<ReceitaMedica>) request.getAttribute("listaReceitas");
    String statusFiltro = (String) request.getAttribute("statusFiltro");
    if (statusFiltro == null) statusFiltro = "PENDENTE";

    String msgSucesso = (String) session.getAttribute("msgSucesso");
    String msgErro = (String) session.getAttribute("msgErro");
    session.removeAttribute("msgSucesso");
    session.removeAttribute("msgErro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Análise de Receitas - DrogaBryan</title>
    <link rel="icon" type="image/x-icon" href="assets/img/drogabryan.png">
    <link rel="stylesheet" href="assets/css/index.css">
    <style>
        .container-receitas {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }
        .header-titulos {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        .aba-btn {
            padding: 8px 16px;
            border-radius: 20px;
            text-decoration: none;
            font-weight: bold;
            color: #555;
            background: #e9ecef;
            margin-left: 5px;
        }
        .aba-btn.active {
            background: #007bff;
            color: white;
        }
        .card-receita {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            background: #fafafa;
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            align-items: center;
            justify-content: space-between;
        }
        .info-cliente p {
            margin: 5px 0;
            font-size: 0.95rem;
            color: #333;
        }
        .btn-ver-arquivo {
            display: inline-block;
            padding: 8px 14px;
            background-color: #17a2b8;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-size: 0.9rem;
            font-weight: bold;
        }
        .btn-ver-arquivo:hover {
            background-color: #138496;
        }
        .acoes-farmaceutico {
            display: flex;
            flex-direction: column;
            gap: 10px;
            min-width: 250px;
        }
        .btn-aprovar {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
        }
        .btn-rejeitar {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
        }
        .input-motivo {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 0.85rem;
        }
        .badge-status {
            padding: 4px 10px;
            border-radius: 12px;
            font-weight: bold;
            font-size: 0.85rem;
        }
        .badge-PENDENTE { background: #fff3cd; color: #856404; }
        .badge-APROVADA { background: #d4edda; color: #155724; }
        .badge-REJEITADA { background: #f8d7da; color: #721c24; }
        .alerta-sucesso { background: #d4edda; color: #155724; padding: 12px; border-radius: 6px; margin-bottom: 15px; }
        .alerta-erro { background: #f8d7da; color: #721c24; padding: 12px; border-radius: 6px; margin-bottom: 15px; }
    </style>
</head>
<body>

<header>
    <nav>
        <div class="nav-container">
            <button class="menu-toggle" onclick="toggleMenu()">☰</button>
            <ul id="menu">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="view/viewFuncionario/Produtofuncionario.jsp">Produtos (Gestão)</a></li>
                <li><a href="ServletListarReceitas?status=PENDENTE">Receitas Pendentes</a></li>
                <li><a href="ServletListarReceitas?status=HISTORICO">Histórico de Receitas</a></li>
                <li><a href="perfil.jsp">Meu Perfil</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>
    </nav>
</header>

<main>
    <div class="container-receitas">
        <div class="header-titulos">
            <h2>Gestão de Receitas Médicas</h2>
            <div>
                <a href="ServletListarReceitas?status=PENDENTE" class="aba-btn <%= "PENDENTE".equals(statusFiltro) ? "active" : "" %>">Pendentes</a>
                <a href="ServletListarReceitas?status=HISTORICO" class="aba-btn <%= "HISTORICO".equals(statusFiltro) ? "active" : "" %>">Histórico</a>
            </div>
        </div>

        <% if (msgSucesso != null) { %>
            <div class="alerta-sucesso"><%= msgSucesso %></div>
        <% } %>
        <% if (msgErro != null) { %>
            <div class="alerta-erro"><%= msgErro %></div>
        <% } %>

        <% if (listaReceitas == null || listaReceitas.isEmpty()) { %>
            <p style="text-align: center; color: #666; margin: 40px 0;">Nenhuma receita encontrada para o filtro selecionado.</p>
        <% } else { 
            for (ReceitaMedica r : listaReceitas) { %>
            <div class="card-receita">
                <div class="info-cliente">
                    <p><strong>Pedido #<%= r.getId_pedido() %></strong> - <span class="badge-status badge-<%= r.getStatus() %>"><%= r.getStatus() %></span></p>
                    <p><strong>Cliente:</strong> <%= r.getNomeCliente() %> (<%= r.getEmailCliente() %>)</p>
                    <p><strong>Data de Envio:</strong> <%= r.getData_envio() %></p>
                    <br>
                    <a href="<%= r.getArquivo_path() %>" target="_blank" class="btn-ver-arquivo">📄 Visualizar Receita Enviada</a>
                </div>

                <% if ("PENDENTE".equals(r.getStatus())) { %>
                    <div class="acoes-farmaceutico">
                        <!-- Formulário de Aprovação -->
                        <form action="ServletAnalisarReceita" method="post">
                            <input type="hidden" name="idReceita" value="<%= r.getId() %>">
                            <input type="hidden" name="acao" value="APROVAR">
                            <button type="submit" class="btn-aprovar" style="width: 100%;">Aprovar Receita</button>
                        </form>

                        <!-- Formulário de Rejeição -->
                        <form action="ServletAnalisarReceita" method="post" onsubmit="return validarRejeicao(this);">
                            <input type="hidden" name="idReceita" value="<%= r.getId() %>">
                            <input type="hidden" name="acao" value="REJEITAR">
                            <input type="text" name="motivoRejeicao" placeholder="Motivo da Rejeição" class="input-motivo" required>
                            <button type="submit" class="btn-rejeitar" style="width: 100%; margin-top: 5px;">Rejeitar Receita</button>
                        </form>
                    </div>
                <% } else { %>
                    <div class="info-analise">
                        <p><strong>Data da Análise:</strong> <%= r.getData_analise() %></p>
                        <% if (r.getMotivo_rejeicao() != null && !r.getMotivo_rejeicao().trim().isEmpty()) { %>
                            <p style="color: #dc3545;"><strong>Motivo da Rejeição:</strong> <%= r.getMotivo_rejeicao() %></p>
                        <% } %>
                    </div>
                <% } %>
            </div>
        <%  } 
           } %>
    </div>
</main>

<script>
function toggleMenu() {
    document.getElementById("menu").classList.toggle("show");
}

function validarRejeicao(form) {
    var motivo = form.motivoRejeicao.value.trim();
    if (motivo === "") {
        alert("Por favor, informe o motivo da rejeição da receita.");
        return false;
    }
    return confirm("Tem certeza de que deseja REJEITAR esta receita médica?");
}
</script>

</body>
</html>
