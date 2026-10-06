package com.model;

import com.controller.Pedido;
import com.controller.ItemPedido;
import com.controller.ItemCarrinho;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends DAO {

    public int cadastrarPedido(Pedido pedido, List<ItemCarrinho> itens) {
        String sqlPedido = "INSERT INTO pedidos (id_cliente, valor_total, frete, prazoEntrega, status_pagamento) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO pedido_produto (id_pedido, id_produto, quantidade, subtotal) VALUES (?, ?, ?, ?)";
        int idPedidoGerado = 0;

        try {
            abrirBanco();
            pst = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, pedido.getIdCliente());
            pst.setDouble(2, pedido.getValorTotal());
            pst.setDouble(3, pedido.getFrete());
            pst.setInt(4, pedido.getPrazoEntrega());
            pst.setString(5, pedido.getStatusPagamento() != null ? pedido.getStatusPagamento() : "PENDENTE");

            pst.executeUpdate();
            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                idPedidoGerado = rs.getInt(1);
            }

            if (idPedidoGerado > 0) {
                for (ItemCarrinho item : itens) {
                    pst = con.prepareStatement(sqlItem);
                    pst.setInt(1, idPedidoGerado);
                    pst.setInt(2, item.getProduto().getId());
                    pst.setInt(3, item.getQuantidade());
                    pst.setDouble(4, item.getSubtotal());
                    pst.executeUpdate();
                }
            }
            fecharBanco();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idPedidoGerado;
    }

    public List<Pedido> listarPedidos(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_cliente = ? ORDER BY id DESC";

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            pst.setInt(1, idCliente);
            rs = pst.executeQuery();

            ReceitaMedicaDAO receitaDAO = new ReceitaMedicaDAO();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setDataPedido(rs.getString("data_pedido"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setFrete(rs.getDouble("frete"));
                p.setPrazoEntrega(rs.getInt("prazoEntrega"));
                p.setStatusPagamento(rs.getString("status_pagamento"));

                p.setItens(carregarItensPedido(p.getId()));
                p.setReceita(receitaDAO.buscarPorPedido(p.getId()));

                lista.add(p);
            }
            fecharBanco();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ItemPedido> carregarItensPedido(int idPedido) {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT pp.*, p.nome FROM pedido_produto pp " +
                     "JOIN produtos p ON pp.id_produto = p.id " +
                     "WHERE pp.id_pedido = ?";

        PreparedStatement pstItens = null;
        ResultSet rsItens = null;

        try {
            if (con == null || con.isClosed()) {
                abrirBanco();
            }

            pstItens = con.prepareStatement(sql);
            pstItens.setInt(1, idPedido);
            rsItens = pstItens.executeQuery();

            while (rsItens.next()) {
                ItemPedido item = new ItemPedido();
                item.setIdPedido(rsItens.getInt("id_pedido"));
                item.setIdProduto(rsItens.getInt("id_produto"));
                item.setNomeProduto(rsItens.getString("nome"));
                item.setQuantidade(rsItens.getInt("quantidade"));
                item.setSubtotal(rsItens.getDouble("subtotal"));
                itens.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsItens != null) rsItens.close();
                if (pstItens != null) pstItens.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itens;
    }

    public Pedido buscarPedidoPorId(int idPedido) {
        Pedido p = null;
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();

            if (rs.next()) {
                p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setDataPedido(rs.getString("data_pedido"));
                p.setValorTotal(rs.getDouble("valor_total"));
                p.setIdCliente(rs.getInt("id_cliente"));
                p.setFrete(rs.getDouble("frete"));
                p.setPrazoEntrega(rs.getInt("prazoEntrega"));
                p.setStatusPagamento(rs.getString("status_pagamento"));

                p.setItens(carregarItensPedido(p.getId()));
            }
            fecharBanco();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}