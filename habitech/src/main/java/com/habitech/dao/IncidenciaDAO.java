package com.habitech.dao;

import com.habitech.model.IncidenciaModel;
import java.util.List;

public interface IncidenciaDAO {
    List<IncidenciaModel> listarIncidencias();
    boolean registrarIncidencia(IncidenciaModel incidencia);
    boolean actualizarEstado(int idIncidencia, String nuevoEstado);
}