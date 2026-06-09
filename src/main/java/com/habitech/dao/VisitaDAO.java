package com.habitech.dao;

import com.habitech.model.Visita;
import java.util.List;

public interface VisitaDao {
    boolean registrarIngreso(Visita visita);
    boolean registrarSalida(int id);
    boolean anularRegistro(int id);
    List<Visita> listarTodos();
}