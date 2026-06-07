package com.habitech.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ReservaModel {
    private int id;
    private int inmuebleId;
    private String areaComun;
    private Date fechaReserva;
    private String turno;
    private Timestamp fechaRegistro;

    // Relación para renderizar qué unidad ocupa el espacio
    private InmuebleModel inmueble;

    public ReservaModel() {}

    public ReservaModel(int id, int inmuebleId, String areaComun, Date fechaReserva, String turno, Timestamp fechaRegistro) {
        this.id = id;
        this.inmuebleId = inmuebleId;
        this.areaComun = areaComun;
        this.fechaReserva = fechaReserva;
        this.turno = turno;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getInmuebleId() { return inmuebleId; }
    public void setInmuebleId(int inmuebleId) { this.inmuebleId = inmuebleId; }
    public String getAreaComun() { return areaComun; }
    public void setAreaComun(String areaComun) { this.areaComun = areaComun; }
    public Date getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(Date fechaReserva) { this.fechaReserva = fechaReserva; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public InmuebleModel getInmueble() { return inmueble; }
    public void setInmueble(InmuebleModel inmueble) { this.inmueble = inmueble; }
}