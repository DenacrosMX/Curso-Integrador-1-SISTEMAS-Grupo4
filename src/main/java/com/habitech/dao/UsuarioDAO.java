package com.habitech.dao;

import com.habitech.model.Usuario;
import java.util.List;

public interface UsuarioDao {
    boolean insertar(Usuario usuario);
    List<Usuario> listarTodos();
    Usuario obtenerPorId(int id);
    boolean actualizar(Usuario usuario);
    boolean eliminarLogico(int id);
    Usuario validarLogin(String username, String passwordPlano);
}