package com.bean;

import com.controller.Carrinho;
import com.controller.ItemCarrinho;
import com.controller.Pedido; // Adicionado import do Pedido
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

        String fretePrecoStr = (String) session.getAttribute("fretePreco");
        double fretePreco = 0.0;
        if (fretePrecoStr != null) {
            try {
                fretePreco = Double.parseDouble(fretePrecoStr.replace(",", "."));
            } catch (NumberFormatException e) {
                fretePreco = 0.0; // fallback se o valor não for um número válido
            }
        }
        
        // Recupera o prazo com validação de nulo para evitar NullPointerException
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

        // 3️⃣ Busca itens do carrinho
        CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
        Carrinho carrinhoBean = new Carrinho();
        carrinhoBean.setId_cliente(idCliente);
        List<ItemCarrinho> itens = carrinhoDAO.listarCarrinho(carrinhoBean);

        if (itens.isEmpty()) {
            response.sendRedirect("carrinho.jsp");
            return;
        }

        // 4️⃣ Monta o objeto Pedido e faz o cadastro via PedidoDAO
        try {
            double subtotalItens = 0.0;
            for (ItemCarrinho item : itens) {
                subtotalItens += item.getSubtotal();
            }
            double totalCompra = subtotalItens + fretePreco;

            // Instancia o Pedido com status "APROVADO"
            Pedido pedido = new Pedido();
            pedido.setIdCliente(idCliente);
            pedido.setValorTotal(totalCompra);
            pedido.setFrete(fretePreco);
            pedido.setPrazoEntrega(fretePrazo);
            pedido.setStatusPagamento("APROVADO");

            // O cadastrarPedido já cria o pedido E insere os itens em pedido_produto
            PedidoDAO pedidoDAO = new PedidoDAO();
            int idPedido = pedidoDAO.cadastrarPedido(pedido, itens);

            if (idPedido > 0) {
                // 5️⃣ Limpa carrinho após pedido registrado com sucesso
                carrinhoDAO.limparCarrinho(idCliente);

                // 6️⃣ Redireciona para a página de pedidos
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