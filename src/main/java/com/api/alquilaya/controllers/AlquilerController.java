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

import com.api.alquilaya.dto.AlquilerRequest;
import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.services.AlquilerService;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    // Obtener todos los alquileres
    @GetMapping
    public List<AlquilerModel> getAllAlquileres() {
        return alquilerService.getAllAlquileres();
    }

    // Obtener un alquiler por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlquilerModel> getAlquilerById(@PathVariable Long id) {
        return alquilerService.getAlquilerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Alquileres de un cliente
    @GetMapping("/cliente/{idCliente}")
    public List<AlquilerModel> getAlquileresByCliente(@PathVariable Long idCliente) {
        return alquilerService.getAlquileresByCliente(idCliente);
    }

    // Alquileres de un vehículo
    @GetMapping("/vehiculo/{idVehiculo}")
    public List<AlquilerModel> getAlquileresByVehiculo(@PathVariable Long idVehiculo) {
        return alquilerService.getAlquileresByVehiculo(idVehiculo);
    }

    // Registrar un alquiler
    @PostMapping
    public ResponseEntity<?> crearAlquiler(@RequestBody AlquilerRequest alquiler) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(alquilerService.crearAlquiler(alquiler));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Devolver el vehículo de un alquiler
    @PutMapping("/{id}/devolver")
    public ResponseEntity<AlquilerModel> devolverVehiculo(@PathVariable Long id) {
        return alquilerService.devolverVehiculo(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un alquiler
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlquiler(@PathVariable Long id) {
        if (alquilerService.deleteAlquiler(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
