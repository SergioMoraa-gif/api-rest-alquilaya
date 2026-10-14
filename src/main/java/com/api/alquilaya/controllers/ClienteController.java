package com.api.alquilaya.controllers;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.services.ClienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Obtener todos los clientes
    @GetMapping
    public List<ClienteModel> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> getClienteById(@PathVariable Long id) {
        return clienteService.getClienteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Obtener un cliente por email
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteModel> getClienteByEmail(@PathVariable String email) {
        Optional <ClienteModel> cliente = clienteService.getClienteByEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());        
    }
    // Obtener un cliente por licencia
    @GetMapping("/licencia/{licencia}")
    public ResponseEntity<ClienteModel> getClienteByLicencia(@PathVariable String licencia) {
        Optional <ClienteModel> cliente = clienteService.getClienteByLicencia(licencia);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());        
    }

    // Crear un cliente
    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody ClienteModel cliente) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.saveCliente(cliente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteModel> actualizarCliente(@PathVariable Long id, @RequestBody ClienteModel cliente) {
        return clienteService.updateCliente(id, cliente)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        if (clienteService.deleteCliente(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
