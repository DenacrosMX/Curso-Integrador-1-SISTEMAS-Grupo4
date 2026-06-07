package com.habitech.controller;

import com.habitech.dao.ReservaDAO;
import com.habitech.dao.impl.ReservaDAOImpl;
import com.habitech.model.ReservaModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/reservas")
public class ReservaController extends HttpServlet {
    private final ReservaDAO reservaDAO = new ReservaDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("reservar".equals(action)) {
            try {
                int inmuebleId = Integer.parseInt(request.getParameter("inmuebleId"));
                String areaComun = request.getParameter("areaComun");
                Date fechaReserva = Date.valueOf(request.getParameter("fechaReserva"));
                String turno = request.getParameter("turno");

                // Validación de colisión de agenda
                if (!reservaDAO.verificarDisponibilidad(areaComun, fechaReserva, turno)) {
                    request.getSession().setAttribute("mensajeError", "❌ Conflicto de Agenda: El espacio seleccionado ya cuenta con una reserva activa en esa fecha y turno.");
                } else {
                    ReservaModel reserva = new ReservaModel(0, inmuebleId, areaComun, fechaReserva, turno, null);
                    if (reservaDAO.registrarReserva(reserva)) {
                        request.getSession().setAttribute("mensajeExito", "📅 ¡Reserva confirmada y agendada exitosamente!");
                    } else {
                        request.getSession().setAttribute("mensajeError", "❌ No se pudo registrar la reserva.");
                    }
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error al procesar los datos de la reserva.");
            }
        } else if ("cancelar".equals(action)) {
            try {
                int idReserva = Integer.parseInt(request.getParameter("idReserva"));
                if (reservaDAO.eliminarReserva(idReserva)) {
                    request.getSession().setAttribute("mensajeExito", "🗑️ Reserva cancelada. El espacio ha quedado liberado.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo cancelar la reserva.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error al procesar la cancelación.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=reservas");
    }
}