package com.habitech.model;

public class UnidadEspecifica {
    private int id;
    private int infraestructuraId;
    private String codigoUnidad;
    private String estadoOcupacion;
    private String estado;

    public UnidadEspecifica() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        // Asignación de ID único de la unidad específica
        this.id = id;
    }

    public int getInfraestructuraId() {
        return infraestructuraId;
    }
    public void setInfraestructuraId(int infraestructuraId) {
        // Enlace al maestro de infraestructura
        this.infraestructuraId = infraestructuraId;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }
    public void setCodigoUnidad(String codigoUnidad) {
        // Código de la unidad física generado
        this.codigoUnidad = codigoUnidad;
    }

    public String getEstadoOcupacion() {
        return estadoOcupacion;
    }
    public void setEstadoOcupacion(String estadoOcupacion) {
        // Control de disponibilidad (DISPONIBLE, OCUPADO, MANTENIMIENTO)
        this.estadoOcupacion = estadoOcupacion;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        // Estado lógico del registro (ACTIVO, INACTIVO)
        this.estado = estado;
    }
}