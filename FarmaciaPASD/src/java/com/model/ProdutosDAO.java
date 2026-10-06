package com.model;

import com.controller.Produtos;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author bryan
 */
public class ProdutosDAO extends DAO {

    public ArrayList<Produtos> pesquisarTudo() {
        ArrayList<Produtos> listaProdutos = new ArrayList<Produtos>();
        try {
            abrirBanco();
            String query = "SELECT id, nome, preco, estoque, receita, imagem FROM produtos ORDER BY id DESC";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            Produtos produto;
            while (rs.next()) {
                produto = new Produtos();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setReceita(rs.getBoolean("receita"));
                produto.setImagem(rs.getString("imagem"));
                listaProdutos.add(produto);
            }
            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro " + e.getMessage());
        }
        return listaProdutos;
    }

    public Produtos buscarPorId(int id) {
        Produtos produto = null;
        try {
            abrirBanco();
            String query = "SELECT id, nome, preco, estoque, receita, imagem FROM produtos WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                produto = new Produtos();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setReceita(rs.getBoolean("receita"));
                produto.setImagem(rs.getString("imagem"));
            }
            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return produto;
    }

    public boolean inserir(Produtos p) {
        boolean sucesso = false;
        try {
            abrirBanco();
            String query = "INSERT INTO produtos (nome, preco, estoque, receita, imagem) VALUES (?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, p.getNome());
            pst.setDouble(2, p.getPreco());
            pst.setInt(3, p.getEstoque());
            pst.setBoolean(4, p.isReceita());
            pst.setString(5, p.getImagem());
            int res = pst.executeUpdate();
            sucesso = (res > 0);
            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
        return sucesso;
    }

    public boolean alterar(Produtos p) {
        boolean sucesso = false;
        try {
            abrirBanco();
            String query = "UPDATE produtos SET nome = ?, preco = ?, estoque = ?, receita = ?, imagem = ? WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, p.getNome());
            pst.setDouble(2, p.getPreco());
            pst.setInt(3, p.getEstoque());
            pst.setBoolean(4, p.isReceita());
            pst.setString(5, p.getImagem());
            pst.setInt(6, p.getId());
            int res = pst.executeUpdate();
            sucesso = (res > 0);
            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao alterar produto: " + e.getMessage());
        }
        return sucesso;
    }

    public boolean excluir(int id) {
        boolean sucesso = false;
        try {
            abrirBanco();
            String query = "DELETE FROM produtos WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
            int res = pst.executeUpdate();
            sucesso = (res > 0);
            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
        return sucesso;
    }
}