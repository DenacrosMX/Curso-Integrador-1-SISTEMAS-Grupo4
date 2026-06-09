package com.habitech.dao;

import com.habitech.model.Configuracion;
import java.util.List;

public interface ConfiguracionDao {

    // C - Create: Guardar una nueva configuración (ej: si cambia de administración o RUC)
    boolean insertar(Configuracion config);

    // R - Read: Listar todas las configuraciones activas para el Historial (Tabla)
    List<Configuracion> listarTodas();

    // R - Read: Obtener una configuración específica por su ID (para cargarla en el formulario al editar)
    Configuracion obtenerPorId(int id);

    // U - Update: Modificar los datos legales o bancarios de una configuración existente
    boolean actualizar(Configuracion config);

    // D - Delete: Borrado lógico (cambiar estado a 'INACTIVO' para que no afecte futuros módulos)
    boolean eliminarLogico(int id);
}