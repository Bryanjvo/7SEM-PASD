package com.bean;

import com.model.ReceitaMedicaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ServletAnalisarReceita")
public class ServletAnalisarReceita extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String perfil = (session != null) ? (String) session.getAttribute("perfil") : null;
        Integer idFarmaceutico = (session != null) ? (Integer) session.getAttribute("id") : null;

        if (!"FARMACEUTICO".equals(perfil) || idFarmaceutico == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idReceitaStr = request.getParameter("idReceita");
        String acao = request.getParameter("acao"); // "APROVAR" ou "REJEITAR"
        String motivoRejeicao = request.getParameter("motivoRejeicao");

        if (idReceitaStr != null && acao != null) {
            int idReceita = Integer.parseInt(idReceitaStr);
            String novoStatus = "APROVAR".equalsIgnoreCase(acao) ? "APROVADA" : "REJEITADA";

            if ("APROVADA".equals(novoStatus)) {
                motivoRejeicao = null;
            }

            ReceitaMedicaDAO dao = new ReceitaMedicaDAO();
            boolean sucesso = dao.atualizarStatus(idReceita, novoStatus, idFarmaceutico, motivoRejeicao);

            if (sucesso) {
                request.getSession().setAttribute("msgSucesso", "Receita " + novoStatus.toLowerCase() + " com sucesso!");
            } else {
                request.getSession().setAttribute("msgErro", "Falha ao processar a receita.");
            }
        }

        response.sendRedirect("ServletListarReceitas?status=PENDENTE");
    }
}