package com.api.alquilaya.repositories;

import com.api.alquilaya.models.UsuariosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface IUsuariosRepository extends JpaRepository<UsuariosModel, Long> {

    // Método para buscar un usuario por su email
    Optional<UsuariosModel> findByEmail(String email);

    // Método para verificar si un usuario existe por su email
    boolean existsByEmail(String email);
    
}
