package com.habitech.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Asignacion {
    private int id;
    private int usuarioId;
    private int inventarioMaestroId;
    private int unidadEspecificaId; // NUEVO: Para guardar el ID numérico en la BD
    private String codigoUnidadEspecifica; // Se mantiene para la UI y listados
    private String tipoAdquisicion;
    private BigDecimal precioMensualPactado;
    private String estado;
    private Date fechaIngreso;
    private Date fechaSalida;

    // Campos de apoyo para la UI (Joins)
    private String nombreUsuario;
    private String detalleInfraestructura;

    public Asignacion() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getInventarioMaestroId() { return inventarioMaestroId; }
    public void setInventarioMaestroId(int inventarioMaestroId) { this.inventarioMaestroId = inventarioMaestroId; }

    public int getUnidadEspecificaId() { return unidadEspecificaId; }
    public void setUnidadEspecificaId(int unidadEspecificaId) { this.unidadEspecificaId = unidadEspecificaId; }

    public String getCodigoUnidadEspecifica() { return codigoUnidadEspecifica; }
    public void setCodigoUnidadEspecifica(String codigoUnidadEspecifica) { this.codigoUnidadEspecifica = codigoUnidadEspecifica; }

    public String getTipoAdquisicion() { return tipoAdquisicion; }
    public void setTipoAdquisicion(String tipoAdquisicion) { this.tipoAdquisicion = tipoAdquisicion; }

    public BigDecimal getPrecioMensualPactado() { return precioMensualPactado; }
    public void setPrecioMensualPactado(BigDecimal precioMensualPactado) { this.precioMensualPactado = precioMensualPactado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public Date getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getDetalleInfraestructura() { return detalleInfraestructura; }
    public void setDetalleInfraestructura(String detalleInfraestructura) { this.detalleInfraestructura = detalleInfraestructura; }
}