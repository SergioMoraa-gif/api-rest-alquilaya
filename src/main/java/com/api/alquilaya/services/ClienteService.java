package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.repositories.IClienteRepository;

@Service 
public class ClienteService {

    private final IClienteRepository clienteRepository;


    ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteModel> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Optional<ClienteModel> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    // Guardar un cliente nuevo, revisando que el email y la licencia no estén repetidos
    public ClienteModel saveCliente(ClienteModel cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (clienteRepository.findByLicenciaConducir(cliente.getLicenciaConducir()).isPresent()) {
            throw new IllegalArgumentException("La licencia ya está registrada");
        }
        return clienteRepository.save(cliente);
    }

    // Actualizar los datos de un cliente
    public Optional<ClienteModel> updateCliente(Long id, ClienteModel datos) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(datos.getNombre());
            cliente.setApellido(datos.getApellido());
            cliente.setEmail(datos.getEmail());
            cliente.setLicenciaConducir(datos.getLicenciaConducir());
            cliente.setTelefono(datos.getTelefono());
            return clienteRepository.save(cliente);
        });
    }

    public Optional<ClienteModel>  getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<ClienteModel>  getClienteByLicencia(String licencia) {
        return clienteRepository.findByLicenciaConducir(licencia);
    }

    public boolean deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }
        clienteRepository.deleteById(id);
        return true;
    }
}
