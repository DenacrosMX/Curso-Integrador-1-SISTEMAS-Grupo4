package com.habitech.dao;

import com.habitech.model.Comunicado;
import java.util.List;

public interface ComunicadoDao {

    boolean insertar(Comunicado comunicado);

    List<Comunicado> listarTodos();

    Comunicado obtenerPorId(int id);

    boolean actualizar(Comunicado comunicado);

    boolean eliminarLogico(int id);

    List<Comunicado> listarActivosParaResidentes(String torreResidente);
}