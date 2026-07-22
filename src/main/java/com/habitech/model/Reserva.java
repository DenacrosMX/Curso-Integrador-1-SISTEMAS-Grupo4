package com.habitech.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Reserva {

    private Integer id;
    private Integer usuarioId;
    private Integer inventarioMaestroId;
    private LocalDate fechaReserva;
    private String turno;
    private String estado;
    private OffsetDateTime fechaRegistro;
    private String nombreUsuario;
    private String nombreAreaComun;

    public Reserva() {
    }

    public Reserva(Integer id, Integer usuarioId, Integer inventarioMaestroId, LocalDate fechaReserva,
                   String turno, String estado, OffsetDateTime fechaRegistro) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.inventarioMaestroId = inventarioMaestroId;
        this.fechaReserva = fechaReserva;
        this.turno = turno;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getInventarioMaestroId() {
        return inventarioMaestroId;
    }

    public void setInventarioMaestroId(Integer inventarioMaestroId) {
        this.inventarioMaestroId = inventarioMaestroId;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(OffsetDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreAreaComun() {
        return nombreAreaComun;
    }

    public void setNombreAreaComun(String nombreAreaComun) {
        this.nombreAreaComun = nombreAreaComun;
    }
}