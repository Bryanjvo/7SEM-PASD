package com.model;

import com.controller.Login;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class LoginDAO extends DAO {

    /**
     * Autentica o usuário procurando sequencialmente nas tabelas:
     * clientes, farmaceuticos e administradores.
     */
    public Login pesquisar(Login login) {
        Login loginBuscado = new Login();

        try {
            abrirBanco();

            // 1. Tenta buscar na tabela de CLIENTES
            String queryCliente = "SELECT id, nome, email, senha, endereco FROM clientes WHERE email = ?";
            pst = con.prepareStatement(queryCliente);
            pst.setString(1, login.getEmail());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                if (BCrypt.checkpw(login.getSenha(), rs.getString("senha"))) {
                    loginBuscado.setId(rs.getInt("id"));
                    loginBuscado.setNome(rs.getString("nome"));
                    loginBuscado.setEmail(rs.getString("email"));
                    loginBuscado.setEndereco(rs.getString("endereco"));
                    loginBuscado.setPerfil("CLIENTE");
                    fecharBanco();
                    return loginBuscado;
                }
            }

            // 2. Tenta buscar na tabela de FARMACÊUTICOS
            String queryFarm = "SELECT id, nome, email, senha, crf FROM farmaceuticos WHERE email = ?";
            pst = con.prepareStatement(queryFarm);
            pst.setString(1, login.getEmail());
            rs = pst.executeQuery();

            if (rs.next()) {
                if (BCrypt.checkpw(login.getSenha(), rs.getString("senha"))) {
                    loginBuscado.setId(rs.getInt("id"));
                    loginBuscado.setNome(rs.getString("nome"));
                    loginBuscado.setEmail(rs.getString("email"));
                    loginBuscado.setCrf(rs.getString("crf"));
                    loginBuscado.setPerfil("FARMACEUTICO");
                    fecharBanco();
                    return loginBuscado;
                }
            }

            // 3. Tenta buscar na tabela de ADMINISTRADORES
            String queryAdmin = "SELECT id, nome, email, senha FROM administradores WHERE email = ?";
            pst = con.prepareStatement(queryAdmin);
            pst.setString(1, login.getEmail());
            rs = pst.executeQuery();

            if (rs.next()) {
                if (BCrypt.checkpw(login.getSenha(), rs.getString("senha"))) {
                    loginBuscado.setId(rs.getInt("id"));
                    loginBuscado.setNome(rs.getString("nome"));
                    loginBuscado.setEmail(rs.getString("email"));
                    loginBuscado.setPerfil("ADMINISTRADOR");
                    fecharBanco();
                    return loginBuscado;
                }
            }

            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao realizar autenticação: " + e.getMessage());
        }

        return loginBuscado;
    }

    /**
     * Verifica se o e-mail informado já existe em QUALQUER uma das tabelas de usuário.
     */
    public boolean emailExiste(String email) {
        boolean existe = false;
        try {
            abrirBanco();

            // 1. Verifica na tabela clientes
            String sqlClientes = "SELECT id FROM clientes WHERE email = ?";
            pst = con.prepareStatement(sqlClientes);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                fecharBanco();
                return true;
            }

            // 2. Verifica na tabela farmaceuticos
            String sqlFarm = "SELECT id FROM farmaceuticos WHERE email = ?";
            pst = con.prepareStatement(sqlFarm);
            pst.setString(1, email);
            rs = pst.executeQuery();
            if (rs.next()) {
                fecharBanco();
                return true;
            }

            // 3. Verifica na tabela administradores
            String sqlAdmin = "SELECT id FROM administradores WHERE email = ?";
            pst = con.prepareStatement(sqlAdmin);
            pst.setString(1, email);
            rs = pst.executeQuery();
            if (rs.next()) {
                fecharBanco();
                return true;
            }

            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao verificar existência do e-mail: " + e.getMessage());
        }
        return existe;
    }

    /**
     * Cadastra um novo CLIENTE no banco de dados com a senha criptografada em BCrypt.
     */
    public boolean inserir(Login login) {
        boolean sucesso = false;
        try {
            abrirBanco();

            String sql = "INSERT INTO clientes (nome, email, senha, endereco) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, login.getNome());
            pst.setString(2, login.getEmail());

            // Gera o hash BCrypt para a senha do cliente antes de salvar
            String senhaHash = BCrypt.hashpw(login.getSenha(), BCrypt.gensalt());
            pst.setString(3, senhaHash);

            pst.setString(4, login.getEndereco());

            int res = pst.executeUpdate();
            if (res > 0) {
                sucesso = true;
            }

            fecharBanco();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
        return sucesso;
    }
}