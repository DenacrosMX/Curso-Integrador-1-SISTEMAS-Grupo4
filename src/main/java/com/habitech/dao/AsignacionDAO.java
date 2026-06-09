package com.habitech.dao;

import com.habitech.model.Asignacion;
import java.util.List;

public interface AsignacionDao {
    boolean insertar(Asignacion asignacion);
    List<Asignacion> listarTodas();
    Asignacion obtenerPorId(int id);
    boolean actualizar(Asignacion asignacion);
    boolean finalizarAsignacion(int id); // Para el botón de dar de baja / salida
}