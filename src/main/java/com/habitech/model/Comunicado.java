package com.habitech.model;

import java.time.OffsetDateTime;

public class Comunicado {

    private Integer id;
    private Integer usuarioId;
    private String titulo;
    private String contenido;
    private String alcance;
    private String torreDestino;
    private String categoria;
    private String estado;
    private OffsetDateTime fechaPublicacion;
    private OffsetDateTime fechaExpiracion;

    public Comunicado() {
    }

    public Comunicado(Integer id, Integer usuarioId, String titulo, String contenido, String alcance,
                      String torreDestino, String categoria, String estado,
                      OffsetDateTime fechaPublicacion, OffsetDateTime fechaExpiracion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.contenido = contenido;
        this.alcance = alcance;
        this.torreDestino = torreDestino;
        this.categoria = categoria;
        this.estado = estado;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaExpiracion = fechaExpiracion;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public String getTorreDestino() {
        return torreDestino;
    }

    public void setTorreDestino(String torreDestino) {
        this.torreDestino = torreDestino;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public OffsetDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(OffsetDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public OffsetDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(OffsetDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}