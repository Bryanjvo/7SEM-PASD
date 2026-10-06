package com.controller;

public class Login {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String endereco;
    private String perfil; // "CLIENTE", "FARMACEUTICO" ou "ADMINISTRADOR"
    private String crf;    // Opcional: para o farmacêutico

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }   

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getCrf() { return crf; }
    public void setCrf(String crf) { this.crf = crf; }
}