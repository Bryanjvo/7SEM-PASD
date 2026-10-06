package com.bean;

import com.controller.Carrinho;
import com.controller.ItemCarrinho;
import com.controller.Pedido;
import com.model.CarrinhoDAO;
import com.model.PedidoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/confirmacao")
public class ServletConfirmacaoPagamento extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        // 1️⃣ Verifica se o pagamento foi aprovado via parâmetro
        String status = request.getParameter("collection_status");  // "approved" ou outro
        if (!"approved".equalsIgnoreCase(status)) {
            response.sendRedirect("carrinho.jsp");
            return;
        }

        // 2️⃣ Recupera ID do cliente
        HttpSession session = request.getSession(false);
        Integer idCliente = (session != null) ? (Integer) session.getAttribute("id") : null;
        if (idCliente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 3️⃣ VERIFICA SE É UM PAGAMENTO DE UM PEDIDO JÁ EXISTENTE
        String externalRef = request.getParameter("external_reference");
        if (externalRef != null && !externalRef.trim().isEmpty()) {
            try {
                int idPedidoExistente = Integer.parseInt(externalRef);
                PedidoDAO pedidoDAO = new PedidoDAO();
                
                // Atualiza o status do pedido existente para "APROVADO"
                pedidoDAO.atualizarStatusPagamento(idPedidoExistente, "APROVADO");
                
                // Redireciona de volta para a lista de pedidos
                response.sendRedirect("pedidos.jsp");
                return;
            } catch (NumberFormatException e) {
                Logger.getLogger(ServletConfirmacaoPagamento.class.getName()).log(Level.SEVERE, "Erro ao converter ID do pedido no retorno do pagamento", e);
            }
        }

        // 4️⃣ FLUXO PADRÃO: Novo pedido vindo direto do Carrinho
        String fretePrecoStr = (String) session.getAttribute("fretePreco");
        double fretePreco = 0.0;
        if (fretePrecoStr != null) {
            try {
                fretePreco = Double.parseDouble(fretePrecoStr.replace(",", "."));
            } catch (NumberFormatException e) {
                fretePreco = 0.0;
            }
        }
        
        Object prazoObj = session.getAttribute("fretePrazo");
        int fretePrazo = 0;
        if (prazoObj != null) {
            if (prazoObj instanceof Integer) {
                fretePrazo = (Integer) prazoObj;
            } else {
                try {
                    fretePrazo = Integer.parseInt(prazoObj.toString());
                } catch (NumberFormatException e) {
                    fretePrazo = 0;
                }
            }
        }

        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
        Carrinho carrinhoBean = new Carrinho();
        carrinhoBean.setId_cliente(idCliente);
        List<ItemCarrinho> itens = carrinhoDAO.listarCarrinho(carrinhoBean);

        if (itens.isEmpty()) {
            response.sendRedirect("carrinho.jsp");
            return;
        }

        try {
            double subtotalItens = 0.0;
            for (ItemCarrinho item : itens) {
                subtotalItens += item.getSubtotal();
            }
            double totalCompra = subtotalItens + fretePreco;

            Pedido pedido = new Pedido();
            pedido.setIdCliente(idCliente);
            pedido.setValorTotal(totalCompra);
            pedido.setFrete(fretePreco);
            pedido.setPrazoEntrega(fretePrazo);
            pedido.setStatusPagamento("APROVADO");

            PedidoDAO pedidoDAO = new PedidoDAO();
            int idPedido = pedidoDAO.cadastrarPedido(pedido, itens);

            if (idPedido > 0) {
                carrinhoDAO.limparCarrinho(idCliente);
                response.sendRedirect("pedidos.jsp");
            } else {
                response.sendRedirect("erro.jsp");
            }

        } catch (Exception ex) {
            Logger.getLogger(ServletConfirmacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("erro.jsp");
        }
    }
}