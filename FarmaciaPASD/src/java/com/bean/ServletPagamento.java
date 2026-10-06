package com.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.controller.Carrinho;
import com.controller.ItemCarrinho;
import com.controller.Pedido;
import com.controller.ItemPedido;
import com.model.CarrinhoDAO;
import com.model.PedidoDAO;
import okhttp3.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@WebServlet("/pagar")
public class ServletPagamento extends HttpServlet {
    private static final String ACCESS_TOKEN = "APP_USR-503418991994651-052312-d0acd30f0927106e87c52af4e050a344-2454289616";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer idCliente = (session != null) ? (Integer) session.getAttribute("id") : null;

        if (idCliente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        JsonArray itemsArray = new JsonArray();
        String idPedidoStr = request.getParameter("idPedido");

        // CASO 1: Pagamento de pedido existente (Receita Aprovada em pedidos.jsp)
        if (idPedidoStr != null && !idPedidoStr.trim().isEmpty()) {
            int idPedido = Integer.parseInt(idPedidoStr);
            PedidoDAO pedidoDAO = new PedidoDAO();
            Pedido pedido = pedidoDAO.buscarPedidoPorId(idPedido);

            if (pedido == null) {
                response.sendRedirect("pedidos.jsp");
                return;
            }

            for (ItemPedido item : pedido.getItens()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("title", item.getNomeProduto());
                obj.addProperty("quantity", item.getQuantidade());
                obj.addProperty("currency_id", "BRL");
                double precoUnitario = item.getQuantidade() > 0 ? (item.getSubtotal() / item.getQuantidade()) : 0.0;
                obj.addProperty("unit_price", precoUnitario);
                itemsArray.add(obj);
            }

            if (pedido.getFrete() > 0) {
                JsonObject freteObj = new JsonObject();
                freteObj.addProperty("title", "Frete");
                freteObj.addProperty("quantity", 1);
                freteObj.addProperty("currency_id", "BRL");
                freteObj.addProperty("unit_price", pedido.getFrete());
                itemsArray.add(freteObj);
            }

        // CASO 2: Fluxo direto do Carrinho (Sem receita médica)
        } else {
            Carrinho carrinhoBean = new Carrinho();
            carrinhoBean.setId_cliente(idCliente);
            CarrinhoDAO dao = new CarrinhoDAO();
            List<ItemCarrinho> lista = dao.listarCarrinho(carrinhoBean);

            if (lista.isEmpty()) {
                request.setAttribute("erro", "Seu carrinho está vazio!");
                request.getRequestDispatcher("carrinho.jsp").forward(request, response);
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

            for (ItemCarrinho item : lista) {
                JsonObject obj = new JsonObject();
                obj.addProperty("title", item.getProduto().getNome());
                obj.addProperty("quantity", item.getQuantidade());
                obj.addProperty("currency_id", "BRL");
                obj.addProperty("unit_price", item.getProduto().getPreco());
                itemsArray.add(obj);
            }

            if (valorFrete > 0) {
                JsonObject freteObj = new JsonObject();
                freteObj.addProperty("title", "Frete");
                freteObj.addProperty("quantity", 1);
                freteObj.addProperty("currency_id", "BRL");
                freteObj.addProperty("unit_price", valorFrete);
                itemsArray.add(freteObj);
            }
        }

        JsonObject preference = new JsonObject();
        preference.add("items", itemsArray);
        preference.add("back_urls", new JsonParser().parse("""
            {
              "success": "https://849d-2804-14c-65c0-56c1-5583-8e1c-5c57-5e19.ngrok-free.app/FarmaciaPASD/confirmacao",
              "failure": "https://849d-2804-14c-65c0-56c1-5583-8e1c-5c57-5e19.ngrok-free.app/FarmaciaPASD/carrinho.jsp",
              "pending": "https://849d-2804-14c-65c0-56c1-5583-8e1c-5c57-5e19.ngrok-free.app/FarmaciaPASD/carrinho.jsp"
            }
        """).getAsJsonObject());
        preference.addProperty("auto_return", "approved");

        OkHttpClient client;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) {}
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sf = sc.getSocketFactory();
            client = new OkHttpClient.Builder()
                .sslSocketFactory(sf, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier((h, s) -> true)
                .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new ServletException("Erro ao configurar SSL", e);
        }

        String json = preference.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request req = new Request.Builder()
            .url("https://api.mercadopago.com/checkout/preferences")
            .post(body)
            .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
            .build();

        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                throw new IOException("Erro na API MP: " + res);
            }
            JsonObject mpResp = JsonParser.parseString(res.body().string()).getAsJsonObject();
            String initPoint = mpResp.get("init_point").getAsString();
            response.sendRedirect(initPoint);
        }
    }
}