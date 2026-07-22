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

        String accion = request.getParameter("accion");

        if (accion != null) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));

                if ("atender".equals(accion)) {
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
            } catch (NumberFormatException e) {
                System.err.println("[Error] ID inválido para la acción en incidencias.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=incidencias");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int asignacionId = Integer.parseInt(request.getParameter("asignacion_id"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String prioridad = request.getParameter("prioridad");

            Incidencia nuevaIncidencia = new Incidencia();
            nuevaIncidencia.setAsignacionId(asignacionId);
            nuevaIncidencia.setConserjeId(null);
            nuevaIncidencia.setTitulo(titulo);
            nuevaIncidencia.setDescription(descripcion);
            nuevaIncidencia.setPrioridad(prioridad);
            nuevaIncidencia.setEstado("ABIERTO");

            boolean insertadoOk = incidenciaDao.insertar(nuevaIncidencia);

            if (!insertadoOk) {
                request.getSession().setAttribute("alertaError", "No se pudo registrar la incidencia. Verifique los datos.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("alertaError", "Error crítico al procesar los datos: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=incidencias");
    }
}