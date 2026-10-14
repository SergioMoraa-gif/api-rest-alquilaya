package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;

@Service
public class OficinaAlquilerService {

    private final IOficinaAlquilerRepository oficinaRepository;

    OficinaAlquilerService(IOficinaAlquilerRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public List<OficinaAlquilerModel> getAllOficinas() {
        return oficinaRepository.findAll();
    }

    public Optional<OficinaAlquilerModel> getOficinaById(Long id) {
        return oficinaRepository.findById(id);
    }

    public List<OficinaAlquilerModel> getOficinasByCiudad(String ciudad) {
        return oficinaRepository.findByCiudad(ciudad);
    }

    public OficinaAlquilerModel saveOficina(OficinaAlquilerModel oficina) {
        return oficinaRepository.save(oficina);
    }

    public Optional<OficinaAlquilerModel> updateOficina(Long id, OficinaAlquilerModel datos) {
        return oficinaRepository.findById(id).map(oficina -> {
            oficina.setNombre(datos.getNombre());
            oficina.setDireccion(datos.getDireccion());
            oficina.setCiudad(datos.getCiudad());
            oficina.setRegion(datos.getRegion());
            return oficinaRepository.save(oficina);
        });
    }

    public boolean deleteOficina(Long id) {
        if (!oficinaRepository.existsById(id)) {
            return false;
        }
        oficinaRepository.deleteById(id);
        return true;
    }
}
