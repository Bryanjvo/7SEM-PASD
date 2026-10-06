package com.bean;

import com.controller.Login;
import com.model.LoginDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author bryan
 */
@WebServlet(name = "ServletLogin", urlPatterns = {"/ServletLogin"})
public class ServletLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");

            Login login = new Login();
            login.setEmail(email);
            login.setSenha(senha);

            LoginDAO loginDAO = new LoginDAO();
            Login loginBuscado = loginDAO.pesquisar(login);

            if (loginBuscado.getEmail() != null) {
                HttpSession session = request.getSession();
                session.setAttribute("id", loginBuscado.getId());
                session.setAttribute("nome", loginBuscado.getNome());
                session.setAttribute("email", loginBuscado.getEmail());
                session.setAttribute("perfil", loginBuscado.getPerfil()); // Define o perfil ("CLIENTE", "FARMACEUTICO", "ADMINISTRADOR")

                // Redirecionamento condicional de acordo com o perfil
                if ("FARMACEUTICO".equals(loginBuscado.getPerfil())) {
                    session.setAttribute("crf", loginBuscado.getCrf());
                    response.sendRedirect("index.jsp"); // Ou dispatch para a página do farmacêutico
                } else if ("ADMINISTRADOR".equals(loginBuscado.getPerfil())) {
                    response.sendRedirect("admin/dashboard.jsp");
                } else {
                    // Cliente padrão
                    session.setAttribute("endereco", loginBuscado.getEndereco());
                    response.sendRedirect("index.jsp");
                }
            } else {
                request.setAttribute("erroLogin", "Email e/ou senha incorretos.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }

    // === MÉTODOS OBRIGATÓRIOS PARA TRATAR AS REQUISIÇÕES GET E POST ===

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}