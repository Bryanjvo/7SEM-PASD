package com.model;

import com.controller.ReceitaMedica;

public class ReceitaMedicaDAO extends DAO {

    public boolean salvar(ReceitaMedica receita) {
        String sql = "INSERT INTO receitas_medicas (id_cliente, id_pedido, arquivo_path, status) VALUES (?, ?, ?, 'PENDENTE')";

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            pst.setInt(1, receita.getId_cliente());
            pst.setInt(2, receita.getId_pedido());
            pst.setString(3, receita.getArquivo_path());

            int linhasAfetadas = pst.executeUpdate();
            fecharBanco();
            return linhasAfetadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ReceitaMedica buscarPorPedido(int idPedido) {
        String sql = "SELECT * FROM receitas_medicas WHERE id_pedido = ?";
        ReceitaMedica receita = null;

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();

            if (rs.next()) {
                receita = new ReceitaMedica();
                receita.setId(rs.getInt("id"));
                receita.setId_cliente(rs.getInt("id_cliente"));
                receita.setId_pedido(rs.getInt("id_pedido"));
                receita.setArquivo_path(rs.getString("arquivo_path"));
                receita.setStatus(rs.getString("status"));
                receita.setData_envio(rs.getTimestamp("data_envio"));
                receita.setData_analise(rs.getTimestamp("data_analise"));
                receita.setId_farmaceutico((Integer) rs.getObject("id_farmaceutico"));
                receita.setMotivo_rejeicao(rs.getString("motivo_rejeicao"));
            }
            fecharBanco();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receita;
    }
}