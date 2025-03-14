
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
    private String numero;
    private String agencia;
    private String titular;
    private String cpf;
    private LocalDate dataAbertura;
    private double saldo;
    private boolean ativa;
    private String tipo;
}