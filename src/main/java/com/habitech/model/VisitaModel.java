package com.habitech.model;

import java.sql.Timestamp;

public class VisitaModel {
    private int id;
    private int inmuebleId;
    private Integer conserjeId; // Integer que acepta nulos
    private String nombreVisitante;
    private String dniVisitante;
    private String placaVehiculo;
    private String tipoIngreso;
    private Timestamp fechaHoraIngreso;
    private Timestamp fechaHoraSalida;
    private String estado;

    private InmuebleModel inmueble; // Para renderizar Torre y Unidad

    public VisitaModel() {}

    public VisitaModel(int id, int inmuebleId, Integer conserjeId, String nombreVisitante, String dniVisitante,
                       String placaVehiculo, String tipoIngreso, Timestamp fechaHoraIngreso,
                       Timestamp fechaHoraSalida, String estado) {
        this.id = id;
        this.inmuebleId = inmuebleId;
        this.conserjeId = conserjeId;
        this.nombreVisitante = nombreVisitante;
        this.dniVisitante = dniVisitante;
        this.placaVehiculo = placaVehiculo;
        this.tipoIngreso = tipoIngreso;
        this.fechaHoraIngreso = fechaHoraIngreso;
        this.fechaHoraSalida = fechaHoraSalida;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInmuebleId() { return inmuebleId; }
    public void setInmuebleId(int inmuebleId) { this.inmuebleId = inmuebleId; }
    public Integer getConserjeId() { return conserjeId; }
    public void setConserjeId(Integer conserjeId) { this.conserjeId = conserjeId; }
    public String getNombreVisitante() { return nombreVisitante; }
    public void setNombreVisitante(String nombreVisitante) { this.nombreVisitante = nombreVisitante; }
    public String getDniVisitante() { return dniVisitante; }
    public void setDniVisitante(String dniVisitante) { this.dniVisitante = dniVisitante; }
    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }
    public String getTipoIngreso() { return tipoIngreso; }
    public void setTipoIngreso(String tipoIngreso) { this.tipoIngreso = tipoIngreso; }
    public Timestamp getFechaHoraIngreso() { return fechaHoraIngreso; }
    public void setFechaHoraIngreso(Timestamp fechaHoraIngreso) { this.fechaHoraIngreso = fechaHoraIngreso; }
    public Timestamp getFechaHoraSalida() { return fechaHoraSalida; }
    public void setFechaHoraSalida(Timestamp fechaHoraSalida) { this.fechaHoraSalida = fechaHoraSalida; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public InmuebleModel getInmueble() { return inmueble; }
    public void setInmueble(InmuebleModel inmueble) { this.inmueble = inmueble; }
}