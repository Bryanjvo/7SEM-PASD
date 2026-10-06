package com.bean;

import com.controller.ReceitaMedica;
import com.model.ReceitaMedicaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletListarReceitas")
public class ServletListarReceitas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String perfil = (session != null) ? (String) session.getAttribute("perfil") : null;

        // Validação de acesso exclusivo do Farmacêutico
        if (!"FARMACEUTICO".equals(perfil)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String status = request.getParameter("status");
        if (status == null || status.trim().isEmpty()) {
            status = "PENDENTE";
        }

        ReceitaMedicaDAO dao = new ReceitaMedicaDAO();
        List<ReceitaMedica> listaReceitas = dao.listarPorStatus(status);

        request.setAttribute("listaReceitas", listaReceitas);
        request.setAttribute("statusFiltro", status);

        request.getRequestDispatcher("/receitasPendentes.jsp").forward(request, response);
    }
}