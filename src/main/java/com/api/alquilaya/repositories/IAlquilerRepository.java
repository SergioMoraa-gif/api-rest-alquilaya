package com.api.alquilaya.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.AlquilerModel;

@Repository
public interface IAlquilerRepository extends JpaRepository<AlquilerModel, Long> {

    // Alquileres de un cliente
    List<AlquilerModel> findByClienteIdCliente(Long idCliente);

    // Alquileres de un vehículo
    List<AlquilerModel> findByVehiculoIdVehiculo(Long idVehiculo);
}
