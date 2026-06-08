package com.habitech.controller;

import com.habitech.dao.MaestroDAO;
import com.habitech.dao.impl.MaestroDAOImpl;
import com.habitech.model.MaestroModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/maestro")
public class MaestroController extends HttpServlet {
    private final MaestroDAO maestroDAO = new MaestroDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Manejo de la acción ELIMINAR desde el Historial
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (maestroDAO.eliminar(id)) {
                request.getSession().setAttribute("mensajeExito", "Registro eliminado del historial con éxito.");
            } else {
                request.getSession().setAttribute("mensajeError", "No se pudo eliminar el registro.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/dashboard?view=maestro");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String idParam = request.getParameter("id");
            int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;

            String nombre = request.getParameter("nombreCondominio");
            String direccion = request.getParameter("direccion");
            String ruc = request.getParameter("ruc");
            int torres = Integer.parseInt(request.getParameter("cantidadTorres"));
            int pisos = Integer.parseInt(request.getParameter("pisosPorTorre"));
            int dptos = Integer.parseInt(request.getParameter("dptosPorPiso"));
            int cocheras = Integer.parseInt(request.getParameter("totalCocheras"));

            MaestroModel m = new MaestroModel(id, nombre, direccion, ruc, torres, pisos, dptos, cocheras);

            boolean exito;
            if (id > 0) {
                exito = maestroDAO.actualizar(m);
                request.getSession().setAttribute("mensajeExito", "¡Registro actualizado correctamente!");
            } else {
                exito = maestroDAO.insertar(m);
                request.getSession().setAttribute("mensajeExito", "¡Nuevo registro guardado con éxito! Cajas de texto limpias.");
            }

            if (!exito) {
                request.getSession().setAttribute("mensajeError", "Error procesando la transacción en la base de datos.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("mensajeError", "Datos incorrectos introducidos en el formulario.");
        }

        // Redirige de vuelta sin parámetros de edición (Garantiza limpiar cajas de texto tras guardar)
        response.sendRedirect(request.getContextPath() + "/dashboard?view=maestro");
    }
}