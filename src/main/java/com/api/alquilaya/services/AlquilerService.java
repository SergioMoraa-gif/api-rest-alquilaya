package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.alquilaya.dto.AlquilerRequest;
import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IClienteRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

@Service
public class AlquilerService {

    private final IAlquilerRepository alquilerRepository;
    private final IVehiculoRepository vehiculoRepository;
    private final IClienteRepository clienteRepository;

    AlquilerService(IAlquilerRepository alquilerRepository, IVehiculoRepository vehiculoRepository,
            IClienteRepository clienteRepository) {
        this.alquilerRepository = alquilerRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<AlquilerModel> getAllAlquileres() {
        return alquilerRepository.findAll();
    }

    public Optional<AlquilerModel> getAlquilerById(Long id) {
        return alquilerRepository.findById(id);
    }

    public List<AlquilerModel> getAlquileresByCliente(Long idCliente) {
        return alquilerRepository.findByClienteIdCliente(idCliente);
    }

    public List<AlquilerModel> getAlquileresByVehiculo(Long idVehiculo) {
        return alquilerRepository.findByVehiculoIdVehiculo(idVehiculo);
    }

    // Registrar un alquiler: valida fechas, que el vehículo esté disponible
    // y calcula el precio total (días * tarifa diaria)
    public AlquilerModel crearAlquiler(AlquilerRequest datos) {

        if (datos.getIdVehiculo() == null || datos.getIdCliente() == null) {
            throw new IllegalArgumentException("El alquiler necesita un vehículo y un cliente");
        }
        if (datos.getFechaAlquiler() == null || datos.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("Las fechas son obligatorias");
        }
        if (!datos.getFechaDevolucion().isAfter(datos.getFechaAlquiler())) {
            throw new IllegalArgumentException("La fecha de devolución debe ser después de la fecha de alquiler");
        }

        VehiculoModel vehiculo = vehiculoRepository.findById(datos.getIdVehiculo())
                .orElseThrow(() -> new IllegalArgumentException("El vehículo no existe"));
        ClienteModel cliente = clienteRepository.findById(datos.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("El cliente no existe"));

        if (!vehiculo.getEstado().equals("DISPONIBLE")) {
            throw new IllegalArgumentException("El vehículo no está disponible");
        }

        long dias = ChronoUnit.DAYS.between(datos.getFechaAlquiler(), datos.getFechaDevolucion());
        BigDecimal precioTotal = vehiculo.getTarifaDiaria().multiply(BigDecimal.valueOf(dias));

        // El vehículo pasa a estar alquilado
        vehiculo.setEstado("ALQUILADO");
        vehiculoRepository.save(vehiculo);

        AlquilerModel alquiler = new AlquilerModel(vehiculo, cliente,
                datos.getFechaAlquiler(), datos.getFechaDevolucion(), precioTotal);
        return alquilerRepository.save(alquiler);
    }

    // Devolver el vehículo: vuelve a quedar DISPONIBLE
    public Optional<AlquilerModel> devolverVehiculo(Long idAlquiler) {
        return alquilerRepository.findById(idAlquiler).map(alquiler -> {
            VehiculoModel vehiculo = alquiler.getVehiculo();
            vehiculo.setEstado("DISPONIBLE");
            vehiculoRepository.save(vehiculo);
            return alquiler;
        });
    }

    public boolean deleteAlquiler(Long id) {
        if (!alquilerRepository.existsById(id)) {
            return false;
        }
        alquilerRepository.deleteById(id);
        return true;
    }
}
