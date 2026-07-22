package com.habitech.model;

import java.time.ZonedDateTime;

public class Configuracion {

    private Integer id;
    private String nombreCondominio;
    private String direccion;
    private String ruc;
    private String cuentaBancaria;
    private int diaVencimientoRecibo;
    private ZonedDateTime fechaRegistro;
    private String estado;

    public Configuracion() {
    }

    public Configuracion(Integer id, String nombreCondominio, String direccion, String ruc,
                         String cuentaBancaria, int diaVencimientoRecibo, ZonedDateTime fechaRegistro, String estado) {
        this.id = id;
        this.nombreCondominio = nombreCondominio;
        this.direccion = direccion;
        this.ruc = ruc;
        this.cuentaBancaria = cuentaBancaria;
        this.diaVencimientoRecibo = diaVencimientoRecibo;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public int getDiaVencimientoRecibo() {
        return diaVencimientoRecibo;
    }

    public void setDiaVencimientoRecibo(int diaVencimientoRecibo) {
        this.diaVencimientoRecibo = diaVencimientoRecibo;
    }

    public ZonedDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(ZonedDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}