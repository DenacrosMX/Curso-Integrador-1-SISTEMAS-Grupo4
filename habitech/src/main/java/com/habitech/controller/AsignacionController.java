package com.habitech.controller;

import com.habitech.dao.AsignacionDAO;
import com.habitech.dao.impl.AsignacionDAOImpl;
import com.habitech.model.AsignacionModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/asignaciones")
public class AsignacionController extends HttpServlet {
    private final AsignacionDAO asignacionDAO = new AsignacionDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("registrar".equals(action)) {
            try {
                int inmuebleId = Integer.parseInt(request.getParameter("inmuebleId"));
                String nombre = request.getParameter("nombreResidente");
                String documento = request.getParameter("documentoIdentidad");
                String tipo = request.getParameter("tipoAdquisicion");
                String fechaStr = request.getParameter("fechaIngreso");

                Date fecha = (fechaStr != null && !fechaStr.isEmpty()) ? Date.valueOf(fechaStr) : new Date(System.currentTimeMillis());

                AsignacionModel nuevaAsignacion = new AsignacionModel(0, inmuebleId, nombre, documento, tipo, fecha);

                if (asignacionDAO.registrarAsignacion(nuevaAsignacion)) {
                    request.getSession().setAttribute("mensajeExito", "✅ Residente asignado correctamente al inmueble.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ Error al procesar la asignación. Verifique los datos.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("mensajeError", "❌ Error en el formato de los parámetros enviados.");
            }

        } else if ("eliminar".equals(action)) {
            try {
                int idAsignacion = Integer.parseInt(request.getParameter("idAsignacion"));
                int inmuebleId = Integer.parseInt(request.getParameter("inmuebleId"));

                if (asignacionDAO.eliminarAsignacion(idAsignacion, inmuebleId)) {
                    request.getSession().setAttribute("mensajeExito", "🗑️ Asignación removida. El inmueble vuelve a estar VACANTE.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo eliminar la asignación.");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=asignaciones");
    }
}