package com.api.alquilaya.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    // Método de prueba para verificar el acceso del administrador
    @GetMapping("/prueba")
    public String prueba() {
        return "Acceso autorizado";
    }
}
