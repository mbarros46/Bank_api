package com.bankapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conta {
    private Long id;
    private Long agencia;
    private Long numero;
    private double saldo;
    private boolean ativa;
    private String cpf;
    private String titular;
    private TipoConta tipo;
    private LocalDate dataAbertura;

    public Conta(Long numero, Long agencia, String titular, String cpf, TipoConta tipo, Double saldo) {
        this.numero = numero;
        this.agencia = agencia;
        this.titular = titular;
        this.cpf = cpf;
        this.tipo = tipo;
        this.saldo = saldo;
        this.ativa = true;
        this.dataAbertura = LocalDate.now();
    }
}
