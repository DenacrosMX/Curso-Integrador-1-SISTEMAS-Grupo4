package com.habitech.model;

import java.util.ArrayList;
import java.util.List;

public class InventarioInfraestructura {

    private int id;
    private int configuracionMaestraId;
    private String tipoElemento;
    private String torre;
    private int nroPiso;
    private int cantidadRegistrada;
    private String estado;
    private String nombreCondominio;

    public InventarioInfraestructura() {
    }

    public List<String> getCodigosGenerados() {
        List<String> codigos = new ArrayList<>();
        int cantidad = this.cantidadRegistrada;
        int piso = this.nroPiso;
        String tipo = this.tipoElemento;

        if (tipo == null) {
            return codigos;
        }

        for (int i = 1; i <= cantidad; i++) {
            String codigoGenerado = "";

            if ("DEPARTAMENTO".equals(tipo)) {
                if (piso < 0) {
                    codigoGenerado = "S" + Math.abs(piso) + String.format("%02d", i);
                } else if (piso == 0) {
                    codigoGenerado = "PB-" + String.format("%02d", i);
                } else {
                    codigoGenerado = "DPTO-" + (piso * 100 + i);
                }
            } else if ("COCHERA".equals(tipo)) {
                String prefijoPiso = (piso < 0) ? "S" + Math.abs(piso) : "P" + piso;
                codigoGenerado = "COCHERA-" + prefijoPiso + "-" + String.format("%02d", i);
            } else {
                String prefijoPiso = (piso < 0) ? "S" + Math.abs(piso) : "P" + piso;
                codigoGenerado = tipo.substring(0, Math.min(tipo.length(), 5)) + "-" + prefijoPiso + "-" + String.format("%02d", i);
            }
            codigos.add(codigoGenerado.toUpperCase());
        }
        return codigos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfiguracionMaestraId() {
        return configuracionMaestraId;
    }

    public void setConfiguracionMaestraId(int configuracionMaestraId) {
        this.configuracionMaestraId = configuracionMaestraId;
    }

    public String getTipoElemento() {
        return tipoElemento;
    }

    public void setTipoElemento(String tipoElemento) {
        this.tipoElemento = tipoElemento;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public int getNroPiso() {
        return nroPiso;
    }

    public void setNroPiso(int nroPiso) {
        this.nroPiso = nroPiso;
    }

    public int getCantidadRegistrada() {
        return cantidadRegistrada;
    }

    public void setCantidadRegistrada(int cantidadRegistrada) {
        this.cantidadRegistrada = cantidadRegistrada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }
}