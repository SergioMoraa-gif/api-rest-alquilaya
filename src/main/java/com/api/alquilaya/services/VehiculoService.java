package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IVehiculoRepository;

@Service
public class VehiculoService {

    private final IVehiculoRepository vehiculoRepository;

    VehiculoService(IVehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<VehiculoModel> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    public Optional<VehiculoModel> getVehiculoById(Long id) {
        return vehiculoRepository.findById(id);
    }

    public Optional<VehiculoModel> getVehiculoByMatricula(String matricula) {
        return vehiculoRepository.findByMatricula(matricula);
    }

    public List<VehiculoModel> getVehiculosByEstado(String estado) {
        return vehiculoRepository.findByEstado(estado.toUpperCase());
    }

    public List<VehiculoModel> getVehiculosByMarca(String marca) {
        return vehiculoRepository.findByMarca(marca);
    }

    // Guardar un vehículo nuevo, si no trae estado se pone como DISPONIBLE
    public VehiculoModel saveVehiculo(VehiculoModel vehiculo) {
        if (vehiculoRepository.findByMatricula(vehiculo.getMatricula()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo con esa matrícula");
        }
        if (vehiculo.getEstado() == null) {
            vehiculo.setEstado("DISPONIBLE");
        }
        return vehiculoRepository.save(vehiculo);
    }

    // Actualizar los datos de un vehículo
    public Optional<VehiculoModel> updateVehiculo(Long id, VehiculoModel datos) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            vehiculo.setMarca(datos.getMarca());
            vehiculo.setModelo(datos.getModelo());
            vehiculo.setAnio(datos.getAnio());
            vehiculo.setMatricula(datos.getMatricula());
            vehiculo.setTarifaDiaria(datos.getTarifaDiaria());
            vehiculo.setEstado(datos.getEstado());
            return vehiculoRepository.save(vehiculo);
        });
    }

    public boolean deleteVehiculo(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            return false;
        }
        vehiculoRepository.deleteById(id);
        return true;
    }
}
