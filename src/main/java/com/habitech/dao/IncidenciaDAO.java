package com.habitech.dao;

import com.habitech.model.Incidencia;
import java.util.List;

public interface IncidenciaDao {
    boolean insertar(Incidencia incidencia);
    List<Incidencia> listarTodas();
    Incidencia obtenerPorId(int id);
    boolean actualizarEstado(int id, String nuevoEstado, Integer conserjeId);
    boolean asignarConserje(int id, int conserjeId);
}