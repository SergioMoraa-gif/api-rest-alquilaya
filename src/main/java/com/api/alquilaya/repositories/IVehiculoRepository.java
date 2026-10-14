package com.api.alquilaya.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.VehiculoModel;

@Repository
public interface IVehiculoRepository extends JpaRepository<VehiculoModel, Long> {

    // Buscar un vehículo por su matrícula
    Optional<VehiculoModel> findByMatricula(String matricula);

    // Buscar vehículos por estado (DISPONIBLE, ALQUILADO, MANTENIMIENTO)
    List<VehiculoModel> findByEstado(String estado);

    // Buscar vehículos por marca
    List<VehiculoModel> findByMarca(String marca);
}
