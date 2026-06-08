package com.habitech.model;

public class MaestroModel {
    private int id;
    private String nombreCondominio;
    private String direccion;
    private String ruc;
    private int cantidadTorres;
    private int pisosPorTorre;
    private int dptosPorPiso;
    private int totalCocheras;

    // Constructor vacío obligatorio para inicializar formularios limpios
    public MaestroModel() {
    }

    // Constructor completo para mapear los registros de la base de datos
    public MaestroModel(int id, String nombreCondominio, String direccion, String ruc,
                        int cantidadTorres, int pisosPorTorre, int dptosPorPiso, int totalCocheras) {
        this.id = id;
        this.nombreCondominio = nombreCondominio;
        this.direccion = direccion;
        this.ruc = ruc;
        this.cantidadTorres = cantidadTorres;
        this.pisosPorTorre = pisosPorTorre;
        this.dptosPorPiso = dptosPorPiso;
        this.totalCocheras = totalCocheras;
    }

    // Métodos de acceso encapsulados (Getters y Setters)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCondominio() { return nombreCondominio; }
    public void setNombreCondominio(String nombreCondominio) { this.nombreCondominio = nombreCondominio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public int getCantidadTorres() { return cantidadTorres; }
    public void setCantidadTorres(int cantidadTorres) { this.cantidadTorres = cantidadTorres; }

    public int getPisosPorTorre() { return pisosPorTorre; }
    public void setPisosPorTorre(int pisosPorTorre) { this.pisosPorTorre = pisosPorTorre; }

    public int getDptosPorPiso() { return dptosPorPiso; }
    public void setDptosPorPiso(int dptosPorPiso) { this.dptosPorPiso = dptosPorPiso; }

    public int getTotalCocheras() { return totalCocheras; }
    public void setTotalCocheras(int totalCocheras) { this.totalCocheras = totalCocheras; }
}