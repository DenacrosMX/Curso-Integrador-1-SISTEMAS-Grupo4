package com.habitech.dao;

import com.habitech.model.Configuracion;
import java.util.List;

public interface ConfiguracionDao {

    boolean insertar(Configuracion config);

    List<Configuracion> listarTodas();

    Configuracion obtenerPorId(int id);

    boolean actualizar(Configuracion config);

    boolean eliminarLogico(int id);
}