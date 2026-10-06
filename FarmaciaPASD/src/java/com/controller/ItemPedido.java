package com.controller;

public class ItemPedido {
    private int idPedido;
    private int idProduto;
    private String nomeProduto;
    private int quantidade;
    private double subtotal;

    // Construtor vazio (necessário para instanciação padrão)
    public ItemPedido() {
    }

    // Construtor mantido para compatibilidade com códigos legados
    public ItemPedido(String nomeProduto, int quantidade, double subtotal) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    // Construtor completo
    public ItemPedido(int idPedido, int idProduto, String nomeProduto, int quantidade, double subtotal) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}