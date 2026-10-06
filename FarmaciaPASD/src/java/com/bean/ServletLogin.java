package com.bean;

import com.controller.Login;
import com.model.LoginDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ServletLogin", urlPatterns = {"/ServletLogin"})
public class ServletLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Login login = new Login();
        login.setEmail(email);
        login.setSenha(senha);

        LoginDAO loginDAO = new LoginDAO();
        Login loginBuscado = loginDAO.pesquisar(login);

        if (loginBuscado != null && loginBuscado.getEmail() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("id", loginBuscado.getId());
            session.setAttribute("nome", loginBuscado.getNome());
            session.setAttribute("email", loginBuscado.getEmail());
            session.setAttribute("perfil", loginBuscado.getPerfil());

            if ("FARMACEUTICO".equals(loginBuscado.getPerfil())) {
                session.setAttribute("crf", loginBuscado.getCrf());
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else if ("ADMINISTRADOR".equals(loginBuscado.getPerfil())) {
                response.sendRedirect(request.getContextPath() + "/ServletAdminDashboard");
            } else {
                session.setAttribute("endereco", loginBuscado.getEndereco());
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            request.setAttribute("erroLogin", "Email e/ou senha incorretos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

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