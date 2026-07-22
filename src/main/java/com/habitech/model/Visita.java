package com.habitech.model;

import java.time.LocalDateTime;

public class Visita {

    private int id;
    private int asignacionId;
    private Integer conserjeId;
    private String nombreVisitante;
    private String dniVisitante;
    private String placaVehiculo;
    private String tipoIngreso;
    private LocalDateTime fechaHoraIngreso;
    private LocalDateTime fechaHoraOut;
    private String estado;
    private String detalleInfraestructura;
    private String codigoUnidadEspecifica;
    private String nombreConserje;

    public Visita() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsignacionId() {
        return asignacionId;
    }

    public void setAsignacionId(int asignacionId) {
        this.asignacionId = asignacionId;
    }

    public Integer getConserjeId() {
        return conserjeId;
    }

    public void setConserjeId(Integer conserjeId) {
        this.conserjeId = conserjeId;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDniVisitante() {
        return dniVisitante;
    }

    public void setDniVisitante(String dniVisitante) {
        this.dniVisitante = dniVisitante;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(String tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public LocalDateTime getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public LocalDateTime getFechaHoraOut() {
        return fechaHoraOut;
    }

    public void setFechaHoraOut(LocalDateTime fechaHoraOut) {
        this.fechaHoraOut = fechaHoraOut;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalleInfraestructura() {
        return detalleInfraestructura;
    }

    public void setDetalleInfraestructura(String detalleInfraestructura) {
        this.detalleInfraestructura = detalleInfraestructura;
    }

    public String getCodigoUnidadEspecifica() {
        return codigoUnidadEspecifica;
    }

    public void setCodigoUnidadEspecifica(String codigoUnidadEspecifica) {
        this.codigoUnidadEspecifica = codigoUnidadEspecifica;
    }

    public String getNombreConserje() {
        return nombreConserje;
    }

    public void setNombreConserje(String nombreConserje) {
        this.nombreConserje = nombreConserje;
    }
}