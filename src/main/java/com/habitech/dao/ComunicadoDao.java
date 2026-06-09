package com.habitech.dao;

import com.habitech.model.Comunicado;
import java.util.List;

public interface ComunicadoDao {

    // C - Create: Registrar un nuevo anuncio desde el formulario administrativo
    boolean insertar(Comunicado comunicado);

    // R - Read: Obtener todos los comunicados para el historial completo del administrador
    List<Comunicado> listarTodos();

    // R - Read: Obtener un comunicado por ID para precargar el formulario al editar
    Comunicado obtenerPorId(int id);

    // U - Update: Actualizar el título, contenido, alcance, torre y categoría del comunicado
    boolean actualizar(Comunicado comunicado);

    // D - Delete: Cambiar el estado a 'OCULTO' (Borrado lógico) para sacarlo de cartelera rápido
    boolean eliminarLogico(int id);

    // Extra R - Read: Listar los anuncios vigentes que deben visualizar los residentes en su panel
    List<Comunicado> listarActivosParaResidentes(String torreResidente);
}