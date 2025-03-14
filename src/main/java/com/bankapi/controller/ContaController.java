package com.bankapi.controller;

import com.bankapi.model.Conta;
import com.bankapi.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {
    private ContaService cts = new ContaService();
    private List<Conta> repository = new ArrayList<>();  

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Conta criarConta(@RequestBody Conta conta) {
        System.out.println("Salvando a conta de " + conta.getTitular());
        
        cts.validarDadosConta(conta);
        
        repository.add(conta);
        
        System.out.println("Conta salva com sucesso!");
        
        return conta;
    }

    @GetMapping
    public List<Conta> listarContas() {
        return repository;  
    }

    @GetMapping("/{id}")
    public Conta buscarContaPorId(@PathVariable Long id) {
       
        return repository.stream()
                         .filter(conta -> conta.getId().equals(id))
                         .findFirst()
                         .orElse(null);  
    }

    @GetMapping("/cpf/{cpf}")
    public Conta buscarContaPorCpf(@PathVariable String cpf) {
        return repository.stream()
                         .filter(conta -> conta.getCpf().equals(cpf))
                         .findFirst()
                         .orElse(null);  
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void encerrarConta(@PathVariable Long id) {
        Conta conta = buscarContaPorId(id);
        if (conta != null) {
            conta.setAtiva(false);  
            System.out.println("Conta encerrada com sucesso!");
        }
    }

    @PostMapping("/{id}/depositar")
    @ResponseStatus(HttpStatus.OK)
    public void depositar(@PathVariable Long id, @RequestParam double valor) {
        Conta conta = buscarContaPorId(id);
        if (conta != null && valor > 0) {
            conta.setSaldo(conta.getSaldo() + valor);
            System.out.println("Depósito realizado com sucesso!");
        }
    }

    @PostMapping("/{id}/sacar")
    @ResponseStatus(HttpStatus.OK)
    public void sacar(@PathVariable Long id, @RequestParam double valor) {
        Conta conta = buscarContaPorId(id);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.setSaldo(conta.getSaldo() - valor);
            System.out.println("Saque realizado com sucesso!");
        } else if (conta == null) {
            System.out.println("Conta não encontrada!");
        } else {
            System.out.println("Saldo insuficiente para saque!");
        }
    }

    @PostMapping("/pix")
    @ResponseStatus(HttpStatus.OK)
    public void transferirPix(@RequestParam Long idOrigem, @RequestParam Long idDestino, @RequestParam double valor) {
        Conta contaOrigem = buscarContaPorId(idOrigem);
        Conta contaDestino = buscarContaPorId(idDestino);
        
        if (contaOrigem != null && contaDestino != null && valor > 0 && contaOrigem.getSaldo() >= valor) {
            contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
            contaDestino.setSaldo(contaDestino.getSaldo() + valor);
            System.out.println("Transferência PIX realizada com sucesso!");
        } else {
            System.out.println("Erro na transferência: Verifique as contas e o saldo.");
        }
    }
}
