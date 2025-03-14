package com.bankapi.repository;

import com.bankapi.model.Conta;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ContaRepository {
    private List<Conta> contas = new ArrayList<>();
    private Long proximoId = 1L;

    public Conta salvar(Conta conta) {
        conta.setId(proximoId++);
        contas.add(conta);
        return conta;
    }

    public List<Conta> listarTodas() {
        return contas;
    }

    public Optional<Conta> buscarPorId(Long id) {
        return contas.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Conta> buscarPorCpf(String cpf) {
        return contas.stream().filter(c -> c.getCpf().equals(cpf)).findFirst();
    }

    public void encerrarConta(Long id) {
        buscarPorId(id).ifPresent(c -> c.setAtiva(false));
    }
}