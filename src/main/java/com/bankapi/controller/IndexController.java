package com.bankapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    
    @GetMapping
    public String index() {
        return "Nome: Miguel Barros Ramos // rm: 556652 // Pedro Valentim // Rm556826 // turma: 2tdspk";
    }
}
