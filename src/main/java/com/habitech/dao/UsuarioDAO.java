package com.habitech.dao;

import com.habitech.model.UsuarioModel;
import java.util.List;

public interface UsuarioDAO {
    List<UsuarioModel> listarUsuarios();
    boolean registrarUsuario(UsuarioModel usuario);
    boolean cambiarEstado(int idUsuario, String nuevoEstado);

    // INTEGRACIÓN LOGIN: Valida credenciales contra firmas BCrypt y estado activo
    UsuarioModel autenticar(String username, String password);
}