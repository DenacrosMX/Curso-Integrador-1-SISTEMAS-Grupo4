package com.habitech.model;

import java.math.BigDecimal;

public class DetalleRecibo {

    private int id;
    private int reciboId;
    private String conceptoDescripcion;
    private BigDecimal montoIndividual;

    public DetalleRecibo() {
    }

    public DetalleRecibo(String conceptoDescripcion, BigDecimal montoIndividual) {
        this.conceptoDescripcion = conceptoDescripcion;
        this.montoIndividual = montoIndividual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReciboId() {
        return reciboId;
    }

    public void setReciboId(int reciboId) {
        this.reciboId = reciboId;
    }

    public String getConceptoDescripcion() {
        return conceptoDescripcion;
    }

    public void setConceptoDescripcion(String conceptoDescripcion) {
        this.conceptoDescripcion = conceptoDescripcion;
    }

    public BigDecimal getMontoIndividual() {
        return montoIndividual;
    }

    public void setMontoIndividual(BigDecimal montoIndividual) {
        this.montoIndividual = montoIndividual;
    }
}