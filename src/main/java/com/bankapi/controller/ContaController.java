package com.bankapi.controller;

import com.bankapi.model.Conta;
import com.bankapi.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private final ContaService contaService;
    private final List<Conta> repository = new ArrayList<>();

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Conta criarConta(@RequestBody Conta conta) {
        System.out.println("Salvando a conta de " + conta.getTitular());
        contaService.validarDadosConta(conta);
        repository.add(conta);
        System.out.println("Conta salva com sucesso!");
        return conta;
    }
}