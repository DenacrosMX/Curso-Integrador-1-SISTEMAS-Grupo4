package com.habitech.controller;

import com.habitech.dao.ReservaDao;
import com.habitech.dao.impl.ReservaDaoImpl;
import com.habitech.model.Reserva;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/reservas")
public class ReservaController extends HttpServlet {

    private final ReservaDao reservaDao = new ReservaDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("cancelar".equals(accion) && request.getParameter("id") != null) {
            try {
                int idCancelar = Integer.parseInt(request.getParameter("id"));
                reservaDao.cancelarReserva(idCancelar);
            } catch (NumberFormatException e) {
                System.err.println("[Error] ID inválido para cancelación.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=reservas");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int usuarioId = Integer.parseInt(request.getParameter("usuario_id"));
            int inventarioId = Integer.parseInt(request.getParameter("inventario_maestro_id"));
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha_reserva"));
            String turno = request.getParameter("turno");
            String estado = request.getParameter("estado");

            if (reservaDao.existeReserva(inventarioId, fecha, turno)) {
                response.sendRedirect(request.getContextPath() + "/dashboard?modulo=reservas&error=duplicado");
                return;
            }

            Reserva r = new Reserva();
            r.setUsuarioId(usuarioId);
            r.setInventarioMaestroId(inventarioId);
            r.setFechaReserva(fecha);
            r.setTurno(turno);
            r.setEstado(estado);

            reservaDao.insertar(r);

        } catch (Exception e) {
            System.err.println("[Error] Error al procesar la reserva.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=reservas");
    }
}