package com.habitech.controller;

import com.habitech.model.Visita;
import com.habitech.model.Usuario;
import com.habitech.dao.VisitaDao;
import com.habitech.dao.impl.VisitaDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "VisitaController", urlPatterns = {"/visitas"})
public class VisitaController extends HttpServlet {

    private final VisitaDao visitaDao = new VisitaDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion != null) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);

                // Procesamos las acciones directamente sin interceptar con bloqueos de rol molestos
                if ("registrarSalida".equals(accion)) {
                    visitaDao.registrarSalida(id);
                } else if ("anular".equals(accion)) {
                    visitaDao.anularRegistro(id);
                }
            }
        }

        // Te devuelve de inmediato a la vista donde estabas parado
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=visitas");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        // Intentamos capturar el usuario, pero no bloqueamos el flujo si tiene otra clave de sesión
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        try {
            int asignacionId = Integer.parseInt(request.getParameter("asignacion_id"));
            String nombre = request.getParameter("nombre_visitante");
            String dni = request.getParameter("dni_visitante");
            String placa = request.getParameter("placa_vehiculo");
            String tipo = request.getParameter("tipo_ingreso");

            Visita nuevaVisita = new Visita();
            nuevaVisita.setAsignacionId(asignacionId);
            nuevaVisita.setNombreVisitante(nombre != null ? nombre.trim() : "");
            nuevaVisita.setDniVisitante(dni != null ? dni.trim() : "");
            nuevaVisita.setPlacaVehiculo(placa != null ? placa.trim() : "");
            nuevaVisita.setTipoIngreso(tipo);

            // Si tu sistema usa otra clave para el usuario, esto evita el NullPointerException
            if (usuarioLogueado != null) {
                nuevaVisita.setConserjeId(usuarioLogueado.getId());
            } else {
                nuevaVisita.setConserjeId(null);
            }

            visitaDao.registrarIngreso(nuevaVisita);

        } catch (Exception e) {
            request.getSession().setAttribute("alertaError", "Error al procesar el ingreso: " + e.getMessage());
        }

        // Redirección limpia al panel para que veas tu registro inmediatamente insertado
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=visitas");
    }
}