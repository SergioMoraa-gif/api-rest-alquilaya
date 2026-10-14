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

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // Obtener todos los vehículos
    @GetMapping
    public List<VehiculoModel> getAllVehiculos() {
        return vehiculoService.getAllVehiculos();
    }

    // Obtener un vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoModel> getVehiculoById(@PathVariable Long id) {
        return vehiculoService.getVehiculoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener un vehículo por matrícula
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<VehiculoModel> getVehiculoByMatricula(@PathVariable String matricula) {
        return vehiculoService.getVehiculoByMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener vehículos por estado (ej. /api/vehiculos/estado/DISPONIBLE)
    @GetMapping("/estado/{estado}")
    public List<VehiculoModel> getVehiculosByEstado(@PathVariable String estado) {
        return vehiculoService.getVehiculosByEstado(estado);
    }

    // Obtener vehículos por marca
    @GetMapping("/marca/{marca}")
    public List<VehiculoModel> getVehiculosByMarca(@PathVariable String marca) {
        return vehiculoService.getVehiculosByMarca(marca);
    }

    // Crear un vehículo
    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody VehiculoModel vehiculo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.saveVehiculo(vehiculo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar un vehículo
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoModel> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoModel vehiculo) {
        return vehiculoService.updateVehiculo(id, vehiculo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un vehículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        if (vehiculoService.deleteVehiculo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
