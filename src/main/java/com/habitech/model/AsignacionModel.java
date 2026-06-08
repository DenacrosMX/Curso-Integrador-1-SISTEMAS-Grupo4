package com.habitech.model;

import java.sql.Date;

public class AsignacionModel {
    private int id;
    private int inmuebleId;
    private String nombreResidente;
    private String documentoIdentidad;
    private String tipoAdquisicion;
    private Date fechaIngreso;

    // Objeto embebido para JOINS en las vistas
    private InmuebleModel inmueble;

    public AsignacionModel() {
    }

    public AsignacionModel(int id, int inmuebleId, String nombreResidente, String documentoIdentidad, String tipoAdquisicion, Date fechaIngreso) {
        this.id = id;
        this.inmuebleId = inmuebleId;
        this.nombreResidente = nombreResidente;
        this.documentoIdentidad = documentoIdentidad;
        this.tipoAdquisicion = tipoAdquisicion;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getInmuebleId() { return inmuebleId; }
    public void setInmuebleId(int inmuebleId) { this.inmuebleId = inmuebleId; }

    public String getNombreResidente() { return nombreResidente; }
    public void setNombreResidente(String nombreResidente) { this.nombreResidente = nombreResidente; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getTipoAdquisicion() { return tipoAdquisicion; }
    public void setTipoAdquisicion(String tipoAdquisicion) { this.tipoAdquisicion = tipoAdquisicion; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public InmuebleModel getInmueble() { return inmueble; }
    public void setInmueble(InmuebleModel inmueble) { this.inmueble = inmueble; }
}