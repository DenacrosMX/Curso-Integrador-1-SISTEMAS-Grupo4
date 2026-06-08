package com.habitech.dao;

import com.habitech.model.AsignacionModel;
import com.habitech.model.InmuebleModel;
import java.util.List;

public interface AsignacionDAO {
    List<AsignacionModel> listarAsignaciones();
    List<InmuebleModel> listarInmueblesVacantes();
    boolean registrarAsignacion(AsignacionModel asignacion);
    boolean eliminarAsignacion(int idAsignacion, int inmuebleId);
}