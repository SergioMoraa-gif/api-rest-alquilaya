package com.api.alquilaya.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.services.OficinaAlquilerService;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaAlquilerController {

    private final OficinaAlquilerService oficinaService;

    OficinaAlquilerController(OficinaAlquilerService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public List<OficinaAlquilerModel> getAllOficinas() {
        return oficinaService.getAllOficinas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OficinaAlquilerModel> getOficinaById(@PathVariable Long id) {
        return oficinaService.getOficinaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ciudad/{ciudad}")
    public List<OficinaAlquilerModel> getOficinasByCiudad(@PathVariable String ciudad) {
        return oficinaService.getOficinasByCiudad(ciudad);
    }

    @PostMapping
    public ResponseEntity<OficinaAlquilerModel> crearOficina(@RequestBody OficinaAlquilerModel oficina) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oficinaService.saveOficina(oficina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaAlquilerModel> actualizarOficina(@PathVariable Long id, @RequestBody OficinaAlquilerModel oficina) {
        return oficinaService.updateOficina(id, oficina)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOficina(@PathVariable Long id) {
        if (oficinaService.deleteOficina(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
