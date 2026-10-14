package com.api.alquilaya.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity 
public class OficinaAlquilerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina") // Identificador único de la oficina - no puede ser nulo por base de datos - generado automáticamente
    private Long idOficina;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre; // Nombre de la oficina - no puede ser nulo por base de datos - longitud máxima de 100 caracteres

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion; // Dirección de la oficina - no puede ser nulo por base de datos - longitud máxima de 200 caracteres

    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad; // Ciudad donde se encuentra la oficina - no puede ser nulo por base de datos - longitud máxima de 100 caracteres

    @Column(name = "region", nullable = false, length = 100)
    private String region; // Región donde se encuentra la oficina - no puede ser nulo por base de datos - longitud máxima de 100 caracteres
    
    // Constructores, getters y setters para la clase OficinaAlquilerModel
    // Constructor por defecto de la clase OficinaAlquilerModel
    public OficinaAlquilerModel() {
    }

    // Constructor con todos los atributos de la clase OficinaAlquilerModel
    public OficinaAlquilerModel(Long idOficina, String nombre, String direccion, String ciudad, String region) {
        this.idOficina = idOficina;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.region = region;
    }

    public Long getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Long idOficina) {
        this.idOficina = idOficina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
