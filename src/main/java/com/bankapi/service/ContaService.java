package com.bankapi.service;

import com.bankapi.model.Conta;
import com.bankapi.model.TipoConta;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.EnumSet;

@Service
public class ContaService {

    
    public void validarDadosConta(Conta conta) {
        if (isCampoInvalido(conta.getTitular()) ||
                isCampoInvalido(conta.getCpf()) ||
                isSaldoInvalido(conta.getSaldo()) ||
                isTipoContaInvalido(conta.getTipo()) ||
                isDataAberturaInvalida(conta.getDataAbertura())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro no corpo da requisição");
        }
    }

    
    public boolean validarDeposito(Double valor) {
        if (valor == null || valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de depósito deve ser positivo");
        }
        return true;
    }

    
    public boolean validarSaque(Conta conta, Double valor) {
        if (valor == null || valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de saque deve ser positivo");
        }
        if (conta.getSaldo() < valor) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }
        return true;
    }

    
    public boolean validarTransferencia(Conta contaOrigem, Conta contaDestino, Double valor) {
        if (valor == null || valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de transferência deve ser positivo");
        }
        if (contaOrigem.getSaldo() < valor) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente na conta de origem");
        }
        if (contaDestino == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta de destino não encontrada");
        }
        return true;
    }

    
    private boolean isCampoInvalido(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    
    private boolean isSaldoInvalido(Double saldo) {
        return saldo == null || saldo < 0;
    }

    
    private boolean isTipoContaInvalido(TipoConta tipo) {
        return tipo == null || !EnumSet.allOf(TipoConta.class).contains(tipo);
    }

    
    private boolean isDataAberturaInvalida(LocalDate data) {
        return data == null || LocalDate.now().isBefore(data);
    }
    
    
    public void encerrarConta(Conta conta) {
        if (conta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não encontrada");
        }
        conta.setAtiva(false);  
    }
}
