package com.bankapi.bank_cp;

import com.bankapi.model.Conta;
import com.bankapi.repository.ContaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaRepository repository;

    public ContaController(ContaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        if (conta.getTitular() == null || conta.getCpf() == null || conta.getDataAbertura() == null || conta.getSaldo() < 0 ||
                !(conta.getTipo().equals("corrente") || conta.getTipo().equals("poupança") || conta.getTipo().equals("salário"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.salvar(conta));
    }

    @GetMapping
    public List<Conta> listarContas() {
        return repository.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarPorId(@PathVariable Long id) {
        return repository.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Conta> buscarPorCpf(@PathVariable String cpf) {
        return repository.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarConta(@PathVariable Long id) {
        repository.encerrarConta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deposito")
    public ResponseEntity<Conta> depositar(@RequestParam Long id, @RequestParam double valor) {
        Optional<Conta> contaOpt = repository.buscarPorId(id);
        if (contaOpt.isPresent() && valor > 0) {
            Conta conta = contaOpt.get();
            conta.setSaldo(conta.getSaldo() + valor);
            return ResponseEntity.ok(conta);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/saque")
    public ResponseEntity<Conta> sacar(@RequestParam Long id, @RequestParam double valor) {
        Optional<Conta> contaOpt = repository.buscarPorId(id);
        if (contaOpt.isPresent() && valor > 0 && contaOpt.get().getSaldo() >= valor) {
            Conta conta = contaOpt.get();
            conta.setSaldo(conta.getSaldo() - valor);
            return ResponseEntity.ok(conta);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/pix")
    public ResponseEntity<String> transferirPix(@RequestParam Long idOrigem, @RequestParam Long idDestino, @RequestParam double valor) {
        Optional<Conta> origemOpt = repository.buscarPorId(idOrigem);
        Optional<Conta> destinoOpt = repository.buscarPorId(idDestino);
        if (origemOpt.isPresent() && destinoOpt.isPresent() && valor > 0 && origemOpt.get().getSaldo() >= valor) {
            Conta origem = origemOpt.get();
            Conta destino = destinoOpt.get();
            origem.setSaldo(origem.getSaldo() - valor);
            destino.setSaldo(destino.getSaldo() + valor);
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        }
        return ResponseEntity.badRequest().build();
    }
}