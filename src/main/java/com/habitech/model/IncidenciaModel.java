package com.habitech.model;

import java.sql.Timestamp;

public class IncidenciaModel {
    private int id;
    private int inmuebleId;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String estado;
    private Timestamp fechaReporte;
    private Timestamp fechaCierre;
    private Integer conserjeId; // Permite almacenar nulos al inicio

    // Objeto embebido para mostrar Torre y Departamento implicado
    private InmuebleModel inmueble;

    public IncidenciaModel() {}

    public IncidenciaModel(int id, int inmuebleId, String titulo, String descripcion, String prioridad,
                           String estado, Timestamp fechaReporte, Timestamp fechaCierre, Integer conserjeId) {
        this.id = id;
        this.inmuebleId = inmuebleId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
        this.fechaCierre = fechaCierre;
        this.conserjeId = conserjeId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInmuebleId() { return inmuebleId; }
    public void setInmuebleId(int inmuebleId) { this.inmuebleId = inmuebleId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Timestamp getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(Timestamp fechaReporte) { this.fechaReporte = fechaReporte; }
    public Timestamp getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Timestamp fechaCierre) { this.fechaCierre = fechaCierre; }
    public Integer getConserjeId() { return conserjeId; }
    public void setConserjeId(Integer conserjeId) { this.conserjeId = conserjeId; }
    public InmuebleModel getInmueble() { return inmueble; }
    public void setInmueble(InmuebleModel inmueble) { this.inmueble = inmueble; }
}