package com.bean;

import com.model.PedidoDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ServletAdminDashboard", urlPatterns = {"/ServletAdminDashboard"})
public class ServletAdminDashboard extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String perfil = (String) session.getAttribute("perfil");

        if (!"ADMINISTRADOR".equals(perfil)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        PedidoDAO pedidoDAO = new PedidoDAO();
        int totalPedidosMes = pedidoDAO.contarPedidosMesAtual();

        request.setAttribute("totalPedidosMes", totalPedidosMes);
        
        // Garanta que o arquivo exista exatamente neste caminho no seu projeto
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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