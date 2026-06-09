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
            int idCancelar = Integer.parseInt(request.getParameter("id"));
            reservaDao.cancelarReserva(idCancelar);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=reservas");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int usuarioId = Integer.parseInt(request.getParameter("usuario_id"));
        int inventarioId = Integer.parseInt(request.getParameter("inventario_maestro_id"));
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha_reserva"));
        String turno = request.getParameter("turno");
        String estado = request.getParameter("estado");

        // Validar duplicado antes de insertar
        if (reservaDao.existeReserva(inventarioId, fecha, turno)) {
            // Redireccionamos enviando una alerta de agenda ocupada
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

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=reservas");
    }
}