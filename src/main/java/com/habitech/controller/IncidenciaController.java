package com.habitech.controller;

import com.habitech.dao.IncidenciaDao;
import com.habitech.dao.impl.IncidenciaDaoImpl;
import com.habitech.model.Incidencia;
import com.habitech.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/incidencias")
public class IncidenciaController extends HttpServlet {

    private final IncidenciaDao incidenciaDao = new IncidenciaDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // El DashboardController centraliza la carga de las listas en el panel general,
        // pero si se accede directamente o se requiere una acción específica (como cambiar estado):
        String accion = request.getParameter("accion");

        if (accion != null) {
            int id = Integer.parseInt(request.getParameter("id"));

            if ("atender".equals(accion)) {
                // El conserje en sesión toma la incidencia y pasa a EN_PROCESO
                HttpSession session = request.getSession();
                Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

                if (usuarioLogueado != null) {
                    incidenciaDao.asignarConserje(id, usuarioLogueado.getId());
                }
            } else if ("resolver".equals(accion)) {
                HttpSession session = request.getSession();
                Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
                Integer conserjeId = (usuarioLogueado != null) ? usuarioLogueado.getId() : null;

                incidenciaDao.actualizarEstado(id, "RESUELTO", conserjeId);
            } else if ("anular".equals(accion)) {
                incidenciaDao.actualizarEstado(id, "ANULADO", null);
            }
        }

        // Redirigimos de vuelta al esqueleto del Dashboard cargando el módulo de incidencias
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=incidencias");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Captura de parámetros desde el formulario de la vista
            int asignacionId = Integer.parseInt(request.getParameter("asignacion_id"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String prioridad = request.getParameter("prioridad");

            // Construcción del objeto modelo basado en las reglas del DDL
            Incidencia nuevaIncidencia = new Incidencia();
            nuevaIncidencia.setAsignacionId(asignacionId);
            nuevaIncidencia.setConserjeId(null); // Nulo al crearse, espera asignación o atención
            nuevaIncidencia.setTitulo(titulo);
            nuevaIncidencia.setDescription(descripcion);
            nuevaIncidencia.setPrioridad(prioridad);
            nuevaIncidencia.setEstado("ABIERTO"); // Estado por defecto dictado por el DDL

            boolean insertadoOk = incidenciaDao.insertar(nuevaIncidencia);

            if (!insertadoOk) {
                request.setAttribute("alertaError", "No se pudo registrar la incidencia. Verifique los datos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("alertaError", "Error crítico al procesar los datos: " + e.getMessage());
        }

        // Redirección limpia para evitar re-envíos duplicados de formulario al presionar F5
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=incidencias");
    }
}