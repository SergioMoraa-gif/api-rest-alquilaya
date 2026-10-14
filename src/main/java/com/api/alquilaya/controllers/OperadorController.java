package com.api.alquilaya.controllers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/operador")
public class OperadorController {
    
    // Método de prueba para verificar el acceso del operador
    @GetMapping("/prueba")
    public String prueba() {
        return "Acceso de operador autorizado";
    }
}
