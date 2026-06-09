package com.habitech.model;

public class InventarioInfraestructura {
    private int id;
    private int configuracionMaestraId;
    private String tipoElemento;
    private String torre;
    private int nroPiso;
    private int cantidadRegistrada;
    private String estado;

    // Variables de apoyo para mostrar información en las vistas
    private String nombreCondominio;

    public InventarioInfraestructura() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getConfiguracionMaestraId() { return configuracionMaestraId; }
    public void setConfiguracionMaestraId(int configuracionMaestraId) { this.configuracionMaestraId = configuracionMaestraId; }

    public String getTipoElemento() { return tipoElemento; }
    public void setTipoElemento(String tipoElemento) { this.tipoElemento = tipoElemento; }

    public String getTorre() { return torre; }
    public void setTorre(String torre) { this.torre = torre; }

    public int getNroPiso() { return nroPiso; }
    public void setNroPiso(int nroPiso) { this.nroPiso = nroPiso; }

    public int getCantidadRegistrada() { return cantidadRegistrada; }
    public void setCantidadRegistrada(int cantidadRegistrada) { this.cantidadRegistrada = cantidadRegistrada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreCondominio() { return nombreCondominio; }
    public void setNombreCondominio(String nombreCondominio) { this.nombreCondominio = nombreCondominio; }
}