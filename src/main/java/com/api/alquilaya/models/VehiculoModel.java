package com.api.alquilaya.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vehiculo")
public class VehiculoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo; // Identificador único del vehículo - no puede ser nulo por base de datos - generado automáticamente

    @Column(nullable = false, length = 50)
    private String marca; // Marca del vehículo - no puede ser nulo por base de datos - longitud máxima de 50 caracteres

    @Column(nullable = false, length = 50)
    private String modelo; // Modelo del vehículo - no puede ser nulo por base de datos - longitud máxima de 50 caracteres

    @Column(nullable = false)
    private Integer anio; // Año del vehículo - no puede ser nulo por base de datos

    @Column(nullable = false, unique = true, length = 20)
    private String matricula; // Matrícula del vehículo - no puede ser nulo por base de datos - unico por base de datos - longitud máxima de 20 caracteres

    @Column(name = "tarifa_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaDiaria; // Tarifa diaria del vehículo - no puede ser nulo por base de datos - precisión 10, escala 2

    @Column(nullable = false, length = 20)
    private String estado; // Estado del vehículo - no puede ser nulo por base de datos - longitud máxima de 20 caracteres

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vehiculo") 
    private List<AlquilerModel> alquileres; // Lista de alquileres asociados al vehículo

    // Constructores, getters y setters para la clase VehiculoModel
    // Constructor por defecto (permite crear instancias sin inicializar atributos)
    public VehiculoModel() {
    }

    // Constructor con parámetros (permite crear instancias inicializando atributos)
    public VehiculoModel(String marca, String modelo, Integer anio, String matricula, BigDecimal tarifaDiaria, String estado) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.matricula = matricula;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<AlquilerModel> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerModel> alquileres) {
        this.alquileres = alquileres;
    }
}