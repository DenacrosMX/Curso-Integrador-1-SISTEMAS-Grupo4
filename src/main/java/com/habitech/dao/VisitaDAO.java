package com.habitech.dao;

import com.habitech.model.VisitaModel;
import com.habitech.model.InmuebleModel;
import java.util.List;

public interface VisitaDAO {
    List<VisitaModel> listarVisitasRecientes();
    List<InmuebleModel> listarTodosLosInmuebles(); // Para llenar el selector de destino
    boolean registrarIngreso(VisitaModel visita);
    boolean registrarSalida(int idVisita);
}