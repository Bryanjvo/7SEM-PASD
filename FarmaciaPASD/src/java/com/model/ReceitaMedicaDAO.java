package com.model;

import com.controller.ReceitaMedica;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Lista as receitas com base no filtro.
     * Se status for "PENDENTE", traz apenas receitas pendentes.
     * Se status for "HISTORICO", traz receitas APROVADAS ou REJEITADAS.
     */
    public List<ReceitaMedica> listarPorStatus(String statusFiltro) {
        List<ReceitaMedica> lista = new ArrayList<>();
        String sql;

        if ("HISTORICO".equalsIgnoreCase(statusFiltro)) {
            sql = "SELECT r.*, c.nome AS nome_cliente, c.email AS email_cliente " +
                  "FROM receitas_medicas r " +
                  "INNER JOIN clientes c ON r.id_cliente = c.id " +
                  "WHERE r.status IN ('APROVADA', 'REJEITADA') " +
                  "ORDER BY r.data_analise DESC";
        } else {
            sql = "SELECT r.*, c.nome AS nome_cliente, c.email AS email_cliente " +
                  "FROM receitas_medicas r " +
                  "INNER JOIN clientes c ON r.id_cliente = c.id " +
                  "WHERE r.status = 'PENDENTE' " +
                  "ORDER BY r.data_envio ASC";
        }

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                ReceitaMedica r = new ReceitaMedica();
                r.setId(rs.getInt("id"));
                r.setId_cliente(rs.getInt("id_cliente"));
                r.setId_pedido(rs.getInt("id_pedido"));
                r.setArquivo_path(rs.getString("arquivo_path"));
                r.setStatus(rs.getString("status"));
                r.setData_envio(rs.getTimestamp("data_envio"));
                r.setData_analise(rs.getTimestamp("data_analise"));
                r.setId_farmaceutico((Integer) rs.getObject("id_farmaceutico"));
                r.setMotivo_rejeicao(rs.getString("motivo_rejeicao"));
                r.setNomeCliente(rs.getString("nome_cliente"));
                r.setEmailCliente(rs.getString("email_cliente"));

                lista.add(r);
            }
            fecharBanco();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Atualiza o status da receita (APROVADA ou REJEITADA) e registra o farmacêutico responsável.
     */
    public boolean atualizarStatus(int idReceita, String novoStatus, int idFarmaceutico, String motivoRejeicao) {
        String sql = "UPDATE receitas_medicas SET status = ?, id_farmaceutico = ?, motivo_rejeicao = ?, data_analise = CURRENT_TIMESTAMP WHERE id = ?";

        try {
            abrirBanco();
            pst = con.prepareStatement(sql);
            pst.setString(1, novoStatus);
            pst.setInt(2, idFarmaceutico);
            pst.setString(3, motivoRejeicao);
            pst.setInt(4, idReceita);

            int linhas = pst.executeUpdate();
            fecharBanco();
            return linhas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}