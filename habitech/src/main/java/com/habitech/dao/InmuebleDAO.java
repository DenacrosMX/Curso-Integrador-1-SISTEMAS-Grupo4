package com.habitech.dao;

import com.habitech.model.InmuebleModel;
import com.habitech.model.MaestroModel;
import java.util.List;

public interface InmuebleDAO {
    List<InmuebleModel> listarInmuebles();
    int generarInventarioAutomatico(MaestroModel parametrosConfig);
    boolean limpiarTodoElInventario();
}