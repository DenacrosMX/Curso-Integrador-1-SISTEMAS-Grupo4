package com.habitech.controller;

import com.habitech.dao.InmuebleDAO;
import com.habitech.dao.MaestroDAO;
import com.habitech.dao.impl.InmuebleDAOImpl;
import com.habitech.dao.impl.MaestroDAOImpl;
import com.habitech.model.MaestroModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/inmuebles")
public class InmuebleController extends HttpServlet {
    private final InmuebleDAO inmuebleDAO = new InmuebleDAOImpl();
    private final MaestroDAO maestroDAO = new MaestroDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("generar".equals(action)) {
            String idMaestroStr = request.getParameter("idMaestro");
            if (idMaestroStr != null && !idMaestroStr.isEmpty()) {
                int idMaestro = Integer.parseInt(idMaestroStr);
                MaestroModel config = maestroDAO.obtenerPorId(idMaestro);

                if (config != null) {
                    int creados = inmuebleDAO.generarInventarioAutomatico(config);
                    if (creados > 0) {
                        request.getSession().setAttribute("mensajeExito", "¡Asistente Completado! Se han autogenerado " + creados + " unidades físicas con éxito.");
                    } else {
                        request.getSession().setAttribute("mensajeError", "El inventario para esta configuración ya existía o el proceso fue rechazado.");
                    }
                }
            }
        } else if ("limpiar".equals(action)) {
            if (inmuebleDAO.limpiarTodoElInventario()) {
                request.getSession().setAttribute("mensajeExito", "Todo el inventario físico ha sido removido. El sistema quedó limpio.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=inmuebles");
    }
}