package com.bean;

import com.controller.Pedido;
import com.model.PedidoDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ServletAdminPedidos", urlPatterns = {"/ServletAdminPedidos"})
public class ServletAdminPedidos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!"ADMINISTRADOR".equals(session.getAttribute("perfil"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        PedidoDAO dao = new PedidoDAO();
        List<Pedido> pedidos = dao.listarTodosPedidosAdmin();
        request.setAttribute("listaPedidos", pedidos);
        request.getRequestDispatcher("admin/pedidosadmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!"ADMINISTRADOR".equals(session.getAttribute("perfil"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idPedido = Integer.parseInt(request.getParameter("idPedido"));
        String novoStatus = request.getParameter("novoStatus");

        PedidoDAO dao = new PedidoDAO();
        dao.atualizarStatusPedido(idPedido, novoStatus);

        response.sendRedirect("ServletAdminPedidos");
    }
}