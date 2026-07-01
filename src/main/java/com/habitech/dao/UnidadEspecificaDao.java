package com.habitech.dao;

import com.habitech.model.UnidadEspecifica;
import java.util.List;

public interface UnidadEspecificaDao {
    // Nos servirá para filtrar las unidades disponibles de una estructura elegida
    List<UnidadEspecifica> listarDisponiblesPorInfraestructura(int infraestructuraId);

    // Nos servirá para cambiar el estado a 'OCUPADO' al asignar, o 'DISPONIBLE' al dar de baja
    boolean actualizarEstadoOcupacion(int id, String nuevoEstado);
}