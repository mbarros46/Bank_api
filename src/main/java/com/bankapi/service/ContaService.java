package com.bankapi.service;

import com.bankapi.model.Conta;
import com.bankapi.model.TipoConta;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;

public class ContaService {

    public void validarDadosConta(Conta conta) {
        if (campoInvalido(conta.getTitular()) ||
                campoInvalido(conta.getCpf()) ||
                saldoNegativo(conta.getSaldo()) ||
                tipoContaInvalido(conta.getTipo()) ||
                dataInvalida(conta.getDataAbertura())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da conta inválidos.");
        }
    }

    private boolean campoInvalido(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private boolean saldoNegativo(Double saldo) {
        return saldo == null || saldo < 0;
    }

    private boolean tipoContaInvalido(TipoConta tipo) {
        return tipo == null || Arrays.stream(TipoConta.values()).noneMatch(t -> t.equals(tipo));
    }

    private boolean dataInvalida(LocalDate data) {
        return data == null || data.isAfter(LocalDate.now());
    }
}
