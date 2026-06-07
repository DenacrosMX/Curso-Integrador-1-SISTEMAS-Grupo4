package com.habitech.controller;

import com.habitech.dao.VisitaDAO;
import com.habitech.dao.impl.VisitaDAOImpl;
import com.habitech.model.VisitaModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/visitas")
public class VisitaController extends HttpServlet {
    private final VisitaDAO visitaDAO = new VisitaDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("ingreso".equals(action)) {
            try {
                int inmuebleId = Integer.parseInt(request.getParameter("inmuebleId"));
                String nombre = request.getParameter("nombreVisitante");
                String dni = request.getParameter("dniVisitante");
                String placa = request.getParameter("placaVehiculo");
                String tipo = request.getParameter("tipoIngreso");

                VisitaModel v = new VisitaModel(0, inmuebleId, null, nombre, dni, placa, tipo, null, null, "EN_CURSO");

                if (visitaDAO.registrarIngreso(v)) {
                    request.getSession().setAttribute("mensajeExito", "✅ Ingreso registrado y marcado EN CURSO.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ Error al insertar el registro de entrada.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("mensajeError", "❌ Datos inválidos enviados al control de accesos.");
            }
        } else if ("darSalida".equals(action)) {
            try {
                int idVisita = Integer.parseInt(request.getParameter("idVisita"));
                if (visitaDAO.registrarSalida(idVisita)) {
                    request.getSession().setAttribute("mensajeExito", "🕒 Salida confirmada. Permanencia finalizada.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo registrar la salida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("mensajeError", "❌ Error al procesar la salida.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=visitas");
    }
}