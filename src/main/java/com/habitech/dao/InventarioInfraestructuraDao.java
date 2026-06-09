package com.habitech.dao;

import com.habitech.model.InventarioInfraestructura;
import java.util.List;

public interface InventarioInfraestructuraDao {
    boolean insertar(InventarioInfraestructura elemento);
    List<InventarioInfraestructura> listarTodo();
    InventarioInfraestructura obtenerPorId(int id);
    boolean actualizar(InventarioInfraestructura elemento);
    boolean eliminarLogico(int id);
}