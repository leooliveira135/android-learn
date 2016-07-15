package com.example.leonardooliveira.boaviagem.model;

import java.util.Date;

/**
 * Created by Leonardo Oliveira on 01/07/2016.
 */
public class Viagem {
    private Long id;
    private String destino;
    private Integer tipoViagem;
    private Date dataChegada;
    private Date dataPartida;
    private Double orcamento;
    private Integer qtdPessoas;

    public Viagem() {}

    public Viagem(Long id, String destino, Integer tipoViagem, Date dataChegada,
                  Date dataPartida, Double orcamento, Integer qtdPessoas) {
        this.id = id;
        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataPartida = dataPartida;
        this.orcamento = orcamento;
        this.qtdPessoas = qtdPessoas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getTipoViagem() {
        return tipoViagem;
    }

    public void setTipoViagem(Integer tipoViagem) {
        this.tipoViagem = tipoViagem;
    }

    public Date getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(Date dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Date getDataPartida() {
        return dataPartida;
    }

    public void setDataPartida(Date dataPartida) {
        this.dataPartida = dataPartida;
    }

    public Double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Double orcamento) {
        this.orcamento = orcamento;
    }

    public Integer getQtdPessoas() {
        return qtdPessoas;
    }

    public void setQtdPessoas(Integer qtdPessoas) {
        this.qtdPessoas = qtdPessoas;
    }
}