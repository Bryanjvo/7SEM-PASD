<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro</title>
    <link rel="stylesheet" href="assets/css/cadcliente.css">
    <link rel="icon" type="image/x-icon" href="assets/img/drogabryan.png">
</head>

<body>
    <div class="form">
        <h4>Formulário de Cadastro de Cliente</h4>
        <%
    String erro = request.getParameter("erro");

    if (erro != null) {
        %>

        <div class="mensagem-erro">
            <%= erro %>
        </div>

        <%
            }
        %>
        <form method="post" action="ServletCadastroLogin">
            <p> 
                Nome:
                <input type="text" id="nome" name="nome" maxlength="40" placeholder="Nome Completo" required/>
            </p>
            <p> 
                E-mail:
                <input type="email" id="email" name="email" maxlength="40" placeholder="E-mail para login" required />
            </p>
            <p> 
                Senha:
                <input type="password" id="senha" name="senha" maxlength="40" placeholder="Senha" required />
            </p>
            <p> 
                Endereço:
                <input type="text" id="endereco" name="endereco" maxlength="40" placeholder="Endereço" required/>
            </p>
            <div>
                <button type="submit" value="Registrar">Cadastrar</button>
                <button type="reset" value="Limpar">Limpar</button>
 
            </div>
            
            <div class="login-link">
                Já possui uma conta?
                <a href="login.jsp">Fazer Login</a>
            </div>
        </form>

    </div>
</body>


</html>