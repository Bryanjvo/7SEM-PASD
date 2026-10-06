package com.bean;

import com.controller.Produtos;
import com.model.ProdutosDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ServletAdminProdutos", urlPatterns = {"/ServletAdminProdutos"})
public class ServletAdminProdutos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!"ADMINISTRADOR".equals(session.getAttribute("perfil"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        ProdutosDAO dao = new ProdutosDAO();
        String acao = request.getParameter("acao");

        if ("excluir".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.excluir(id);
            response.sendRedirect("ServletAdminProdutos");
            return;
        } else if ("editar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Produtos p = dao.buscarPorId(id);
            request.setAttribute("produtoEditar", p);
        }

        ArrayList<Produtos> produtos = dao.pesquisarTudo();
        request.setAttribute("listaProdutos", produtos);
        request.getRequestDispatcher("admin/produtos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!"ADMINISTRADOR".equals(session.getAttribute("perfil"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        double preco = Double.parseDouble(request.getParameter("preco"));
        String imagem = request.getParameter("imagem");
        int estoque = Integer.parseInt(request.getParameter("estoque"));
        boolean receita = request.getParameter("receita") != null;

        Produtos p = new Produtos();
        p.setNome(nome);
        p.setPreco(preco);
        p.setImagem(imagem);
        p.setEstoque(estoque);
        p.setReceita(receita);

        ProdutosDAO dao = new ProdutosDAO();

        if (idStr != null && !idStr.trim().isEmpty()) {
            p.setId(Integer.parseInt(idStr));
            dao.alterar(p);
        } else {
            dao.inserir(p);
        }

        response.sendRedirect("ServletAdminProdutos");
    }
}