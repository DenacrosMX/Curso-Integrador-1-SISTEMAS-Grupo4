package com.habitech.dao;

import com.habitech.model.MaestroModel;
import java.util.List;

public interface MaestroDAO {
    List<MaestroModel> listarHistorial();
    MaestroModel obtenerPorId(int id);
    boolean insertar(MaestroModel maestro);
    boolean actualizar(MaestroModel maestro);
    boolean eliminar(int id);
}