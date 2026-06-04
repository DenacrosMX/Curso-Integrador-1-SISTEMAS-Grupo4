package com.habitech.model;

import java.math.BigDecimal;
import java.sql.Date;

public class ReciboModel {
    private int id;
    private int asignacionId;
    private int mesFacturado;
    private int anioFacturado;
    private BigDecimal montoMantenimiento;
    private Date fechaEmision;
    private String estadoPago;

    private AsignacionModel asignacion; // JOIN lógico embebido

    public ReciboModel() {}

    public ReciboModel(int id, int asignacionId, int mesFacturado, int anioFacturado,
                       BigDecimal montoMantenimiento, Date fechaEmision, String estadoPago) {
        this.id = id;
        this.asignacionId = asignacionId;
        this.mesFacturado = mesFacturado;
        this.anioFacturado = anioFacturado;
        this.montoMantenimiento = montoMantenimiento;
        this.fechaEmision = fechaEmision;
        this.estadoPago = estadoPago;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAsignacionId() { return asignacionId; }
    public void setAsignacionId(int asignacionId) { this.asignacionId = asignacionId; }
    public int getMesFacturado() { return mesFacturado; }
    public void setMesFacturado(int mesFacturado) { this.mesFacturado = mesFacturado; }
    public int getAnioFacturado() { return anioFacturado; }
    public void setAnioFacturado(int anioFacturado) { this.anioFacturado = anioFacturado; }
    public BigDecimal getMontoMantenimiento() { return montoMantenimiento; }
    public void setMontoMantenimiento(BigDecimal montoMantenimiento) { this.montoMantenimiento = montoMantenimiento; }
    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public AsignacionModel getAsignacion() { return asignacion; }
    public void setAsignacion(AsignacionModel asignacion) { this.asignacion = asignacion; }
}