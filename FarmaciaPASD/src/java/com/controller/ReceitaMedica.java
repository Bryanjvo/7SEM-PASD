package com.controller;

import java.sql.Timestamp;

public class ReceitaMedica {
    private int id;
    private int id_cliente;
    private int id_pedido;
    private String arquivo_path;
    private String status; // 'PENDENTE', 'APROVADA', 'REJEITADA'
    private Timestamp data_envio;
    private Timestamp data_analise;
    private Integer id_farmaceutico;
    private String motivo_rejeicao;

    public ReceitaMedica() {}

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getArquivo_path() {
        return arquivo_path;
    }

    public void setArquivo_path(String arquivo_path) {
        this.arquivo_path = arquivo_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getData_envio() {
        return data_envio;
    }

    public void setData_envio(Timestamp data_envio) {
        this.data_envio = data_envio;
    }

    public Timestamp getData_analise() {
        return data_analise;
    }

    public void setData_analise(Timestamp data_analise) {
        this.data_analise = data_analise;
    }

    public Integer getId_farmaceutico() {
        return id_farmaceutico;
    }

    public void setId_farmaceutico(Integer id_farmaceutico) {
        this.id_farmaceutico = id_farmaceutico;
    }

    public String getMotivo_rejeicao() {
        return motivo_rejeicao;
    }

    public void setMotivo_rejeicao(String motivo_rejeicao) {
        this.motivo_rejeicao = motivo_rejeicao;
    }
}