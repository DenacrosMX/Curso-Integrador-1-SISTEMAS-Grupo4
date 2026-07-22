package com.habitech.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Recibo {

    private int id;
    private String nroComprobante;
    private int usuarioId;
    private int usuarioResponsableId;
    private int mesFacturado;
    private int anioFacturado;
    private BigDecimal totalAPagar;
    private Date fechaEmision;
    private String estadoPago;
    private String nroOperacion;
    private String medioPago;
    private String archivoVoucher;
    private Date fechaPago;
    private String nombreResidente;
    private String dniResidente;
    private String detalleUnidad;
    private String nombreAdmin;
    private List<DetalleRecibo> detalles = new ArrayList<>();

    public Recibo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getUsuarioResponsableId() {
        return usuarioResponsableId;
    }

    public void setUsuarioResponsableId(int usuarioResponsableId) {
        this.usuarioResponsableId = usuarioResponsableId;
    }

    public int getMesFacturado() {
        return mesFacturado;
    }

    public void setMesFacturado(int mesFacturado) {
        this.mesFacturado = mesFacturado;
    }

    public int getAnioFacturado() {
        return anioFacturado;
    }

    public void setAnioFacturado(int anioFacturado) {
        this.anioFacturado = anioFacturado;
    }

    public BigDecimal getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(BigDecimal totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getNroOperacion() {
        return nroOperacion;
    }

    public void setNroOperacion(String nroOperacion) {
        this.nroOperacion = nroOperacion;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getArchivoVoucher() {
        return archivoVoucher;
    }

    public void setArchivoVoucher(String archivoVoucher) {
        this.archivoVoucher = archivoVoucher;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getDniResidente() {
        return dniResidente;
    }

    public void setDniResidente(String dniResidente) {
        this.dniResidente = dniResidente;
    }

    public String getDetalleUnidad() {
        return detalleUnidad;
    }

    public void setDetalleUnidad(String detalleUnidad) {
        this.detalleUnidad = detalleUnidad;
    }

    public String getNombreAdmin() {
        return nombreAdmin;
    }

    public void setNombreAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
    }

    public List<DetalleRecibo> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRecibo> detalles) {
        this.detalles = detalles;
    }
}