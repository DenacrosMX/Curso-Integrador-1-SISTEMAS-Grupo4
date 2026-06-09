package com.habitech.model;

import java.time.OffsetDateTime;

public class Incidencia {
    private int id;
    private int asignacionId;
    private Integer conserjeId; // Permite nulos
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String estado;
    private OffsetDateTime fechaReporte;
    private OffsetDateTime fechaCierre;

    // Variables de apoyo para vistas (JOINs)
    private String nombreConserje;
    private String detalleAsignacion;

    public Incidencia() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAsignacionId() { return asignacionId; }
    public void setAsignacionId(int asignacionId) { this.asignacionId = asignacionId; }

    public Integer getConserjeId() { return conserjeId; }
    public void setConserjeId(Integer conserjeId) { this.conserjeId = conserjeId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescription() { return descripcion; } // Mapeado a la columna descripcion
    public void setDescription(String descripcion) { this.descripcion = descripcion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(OffsetDateTime fechaReporte) { this.fechaReporte = fechaReporte; }

    public OffsetDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(OffsetDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public String getNombreConserje() { return nombreConserje; }
    public void setNombreConserje(String nombreConserje) { this.nombreConserje = nombreConserje; }

    public String getDetalleAsignacion() { return detalleAsignacion; }
    public void setDetalleAsignacion(String detalleAsignacion) { this.detalleAsignacion = detalleAsignacion; }
}