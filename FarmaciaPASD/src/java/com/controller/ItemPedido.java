package com.controller;

public class ItemPedido {
    private int idPedido;
    private int idProduto;
    private String nomeProduto;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;

    // Construtor vazio (necessário para instanciação padrão)
    public ItemPedido() {
    }

    // Construtor mantido para compatibilidade com códigos legados
    public ItemPedido(String nomeProduto, int quantidade, double subtotal) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
        this.precoUnitario = (quantidade > 0) ? subtotal / quantidade : 0.0;
    }

    // Construtor incluindo preço unitário
    public ItemPedido(String nomeProduto, int quantidade, double precoUnitario, double subtotal) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }

    // Construtor completo
    public ItemPedido(int idPedido, int idProduto, String nomeProduto, int quantidade, double subtotal) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
        this.precoUnitario = (quantidade > 0) ? subtotal / quantidade : 0.0;
    }

    // Construtor completo com preço unitário explícito
    public ItemPedido(int idPedido, int idProduto, String nomeProduto, int quantidade, double precoUnitario, double subtotal) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
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

    public double getPrecoUnitario() {
        if (this.precoUnitario > 0) {
            return this.precoUnitario;
        }
        if (this.quantidade > 0) {
            return this.subtotal / this.quantidade;
        }
        return 0.0;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}