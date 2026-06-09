package com.habitech.dao;

import com.habitech.model.Usuario;
import java.util.List;

public interface UsuarioDao {

    // C - Create: Guardar un nuevo usuario desde el formulario
    boolean insertar(Usuario usuario);

    // R - Read: Obtener todos los usuarios activos para el Historial (Tabla)
    List<Usuario> listarTodos();

    // R - Read: Buscar un usuario por ID (para cargar sus datos en el formulario al editar)
    Usuario obtenerPorId(int id);

    // U - Update: Modificar los datos del usuario modificado en el formulario
    boolean actualizar(Usuario usuario);

    // D - Delete: Borrado lógico (cambiar estado a 'INACTIVO' en lugar de borrar la fila)
    boolean eliminarLogico(int id);
}