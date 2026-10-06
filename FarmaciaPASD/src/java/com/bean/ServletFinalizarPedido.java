package com.bean;

import com.controller.Carrinho;
import com.controller.ItemCarrinho;
import com.controller.Pedido;
import com.controller.ReceitaMedica;
import com.model.CarrinhoDAO;
import com.model.PedidoDAO;
import com.model.ReceitaMedicaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@WebServlet("/ServletFinalizarPedido")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ServletFinalizarPedido extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer idCliente = (session != null) ? (Integer) session.getAttribute("id") : null;

        if (idCliente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Carrinho carrinhoBean = new Carrinho();
        carrinhoBean.setId_cliente(idCliente);
        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
        List<ItemCarrinho> listaCarrinho = carrinhoDAO.listarCarrinho(carrinhoBean);

        if (listaCarrinho.isEmpty()) {
            response.sendRedirect("carrinho.jsp");
            return;
        }

        String valorFreteStr = request.getParameter("frete");
        double valorFrete = 0.0;
        if (valorFreteStr != null && !valorFreteStr.trim().isEmpty()) {
            try {
                valorFrete = Double.parseDouble(valorFreteStr.replace(",", "."));
            } catch (NumberFormatException e) {
                valorFrete = 0.0;
            }
        }

        String prazoStr = request.getParameter("prazoEntrega");
        int prazoEntrega = 0;
        if (prazoStr != null && !prazoStr.trim().isEmpty()) {
            try {
                prazoEntrega = Integer.parseInt(prazoStr);
            } catch (NumberFormatException e) {
                prazoEntrega = 0;
            }
        }

        double subtotalItens = 0.0;
        boolean requerReceita = false;
        for (ItemCarrinho item : listaCarrinho) {
            subtotalItens += item.getSubtotal();
            if (item.getProduto().isReceita()) {
                requerReceita = true;
            }
        }
        double totalCompra = subtotalItens + valorFrete;

        // Cria o pedido com status de pagamento PENDENTE
        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);
        pedido.setValorTotal(totalCompra);
        pedido.setFrete(valorFrete);
        pedido.setPrazoEntrega(prazoEntrega);
        pedido.setStatusPagamento("PENDENTE");

        PedidoDAO pedidoDAO = new PedidoDAO();
        int idPedido = pedidoDAO.cadastrarPedido(pedido, listaCarrinho);

        if (idPedido <= 0) {
            request.setAttribute("erro", "Erro ao registrar o pedido.");
            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            return;
        }

        // Salva a receita se houver
        if (requerReceita) {
            Part filePart = request.getPart("receitaFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String novoNomeArquivo = "receita_" + idPedido + "_" + System.currentTimeMillis() + "_" + fileName;
                Path destino = Paths.get(uploadPath, novoNomeArquivo);

                // Cópia direta e segura via Stream (Solução 1)
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, destino, StandardCopyOption.REPLACE_EXISTING);
                }

                ReceitaMedica receita = new ReceitaMedica();
                receita.setId_cliente(idCliente);
                receita.setId_pedido(idPedido);
                receita.setArquivo_path("uploads/" + novoNomeArquivo);
                receita.setStatus("PENDENTE");

                ReceitaMedicaDAO receitaDAO = new ReceitaMedicaDAO();
                receitaDAO.salvar(receita);
            }
        }

        // Limpa o carrinho
        carrinhoDAO.limparCarrinho(idCliente);

        // Redireciona para meus pedidos
        response.sendRedirect("pedidos.jsp");
    }
}