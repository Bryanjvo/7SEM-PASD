package com.controller;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private String dataPedido;
    private double valorTotal;
    private int idCliente;
    private double frete;
    private int prazoEntrega;
    private String statusPagamento = "PENDENTE"; // 'PENDENTE', 'APROVADO', 'CANCELADO'
    private List<ItemPedido> itens = new ArrayList<>();
    private ReceitaMedica receita; // Receita médica associada ao pedido

    public Pedido() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDataPedido() { return dataPedido; }
    public void setDataPedido(String dataPedido) { this.dataPedido = dataPedido; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public double getFrete() { return frete; }
    public void setFrete(double frete) { this.frete = frete; }

    public int getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(int prazoEntrega) { this.prazoEntrega = prazoEntrega; }

    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public ReceitaMedica getReceita() { return receita; }
    public void setReceita(ReceitaMedica receita) { this.receita = receita; }
}