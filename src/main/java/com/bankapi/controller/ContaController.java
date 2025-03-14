package com.bankapi.controller;

import com.bankapi.model.Conta;
import com.bankapi.repository.ContaRepository;
import com.bankapi.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final ContaRepository contaRepository;

    public ContaController(ContaService contaService, ContaRepository contaRepository) {
        this.contaService = contaService;
        this.contaRepository = contaRepository;
    }

    // Criar Conta
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        contaService.validarDadosConta(conta);
        Conta contaSalva = contaRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);
    }

    // todas as contas
    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        List<Conta> contas = contaRepository.findAll();
        if (contas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contas);
    }

    //  ID
    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        return contaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Conta> buscarContaPorCpf(@PathVariable String cpf) {
        return contaRepository.findByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Encerrar conta
    @PutMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarConta(@PathVariable Long id) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        contaService.encerrarConta(conta);
        contaRepository.save(conta); // Atualiza o status da conta no repositório
        return ResponseEntity.noContent().build();
    }

    // Depósito
    @PostMapping("/{id}/deposito")
    public ResponseEntity<Conta> deposito(@PathVariable Long id, @RequestParam Double valor) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        contaService.validarDeposito(valor);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
        return ResponseEntity.ok(conta);
    }

    // Saque
    @PostMapping("/{id}/saque")
    public ResponseEntity<Conta> saque(@PathVariable Long id, @RequestParam Double valor) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        contaService.validarSaque(conta, valor);
        conta.setSaldo(conta.getSaldo() - valor);
        contaRepository.save(conta);
        return ResponseEntity.ok(conta);
    }

    //  Pix
    @PostMapping("/{idOrigem}/pix/{idDestino}")
    public ResponseEntity<Void> transferencia(@PathVariable Long idOrigem, @PathVariable Long idDestino, @RequestParam Double valor) {
        Conta contaOrigem = contaRepository.findById(idOrigem).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada"));
        Conta contaDestino = contaRepository.findById(idDestino).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada"));
        contaService.validarTransferencia(contaOrigem, contaDestino, valor);
        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
        return ResponseEntity.noContent().build();
    }
}
