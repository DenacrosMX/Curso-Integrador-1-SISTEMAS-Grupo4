package com.habitech.model;

public class InmuebleModel {
    private int id;
    private String nroUnidad;
    private String bloqueTorre;
    private int piso;
    private String tipoUnidad;
    private String estadoOcupacion;

    public InmuebleModel() {
    }

    public InmuebleModel(int id, String nroUnidad, String bloqueTorre, int piso, String tipoUnidad, String estadoOcupacion) {
        this.id = id;
        this.nroUnidad = nroUnidad;
        this.bloqueTorre = bloqueTorre;
        this.piso = piso;
        this.tipoUnidad = tipoUnidad;
        this.estadoOcupacion = estadoOcupacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNroUnidad() { return nroUnidad; }
    public void setNroUnidad(String nroUnidad) { this.nroUnidad = nroUnidad; }

    public String getBloqueTorre() { return bloqueTorre; }
    public void setBloqueTorre(String bloqueTorre) { this.bloqueTorre = bloqueTorre; }

    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }

    public String getTipoUnidad() { return tipoUnidad; }
    public void setTipoUnidad(String tipoUnidad) { this.tipoUnidad = tipoUnidad; }

    public String getEstadoOcupacion() { return estadoOcupacion; }
    public void setEstadoOcupacion(String estadoOcupacion) { this.estadoOcupacion = estadoOcupacion; }
}