package com.habitech.controller;

import com.habitech.dao.AsignacionDAO;
import com.habitech.dao.InmuebleDAO;
import com.habitech.dao.MaestroDAO;
import com.habitech.dao.ReciboDAO;
import com.habitech.dao.impl.AsignacionDAOImpl;
import com.habitech.dao.impl.InmuebleDAOImpl;
import com.habitech.dao.impl.MaestroDAOImpl;
import com.habitech.dao.impl.ReciboDAOImpl;
import com.habitech.model.InmuebleModel;
import com.habitech.model.MaestroModel;
import com.habitech.model.AsignacionModel;
import com.habitech.model.ReciboModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
    private final MaestroDAO maestroDAO = new MaestroDAOImpl();
    private final InmuebleDAO inmuebleDAO = new InmuebleDAOImpl();
    private final AsignacionDAO asignacionDAO = new AsignacionDAOImpl();
    private final ReciboDAO reciboDAO = new ReciboDAOImpl(); // Instancia del DAO de Recibos (Módulo 4)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String view = request.getParameter("view");

        if ("maestro".equals(view)) {
            // 1. Cargar el historial completo multi-registro
            List<MaestroModel> historial = maestroDAO.listarHistorial();
            request.setAttribute("historialMaestro", historial);

            // 2. Gestionar el Modo Edición (CRUD): Si viene un idEdit, precargar. Si no, mandar vacío para limpiar.
            String idEdit = request.getParameter("idEdit");
            MaestroModel formularioObjeto = new MaestroModel();
            if (idEdit != null) {
                try {
                    formularioObjeto = maestroDAO.obtenerPorId(Integer.parseInt(idEdit));
                } catch (NumberFormatException e) { e.printStackTrace(); }
            }
            request.setAttribute("configMaestra", formularioObjeto);

        } else if ("inmuebles".equals(view)) {
            // INTEGRACIÓN MÓDULO 2
            // 1. Cargar las configuraciones maestras para que el usuario elija con cuál generar el inventario
            List<MaestroModel> maestrosDisponibles = maestroDAO.listarHistorial();
            request.setAttribute("listaMaestrosDisponibles", maestrosDisponibles);

            // 2. Cargar todo el inventario físico generado hasta el momento
            List<InmuebleModel> listaInmuebles = inmuebleDAO.listarInmuebles();
            request.setAttribute("listaInmuebles", listaInmuebles);

        } else if ("asignaciones".equals(view)) {
            // INTEGRACIÓN MÓDULO 3
            // 1. Cargar el padrón histórico de asignaciones vigentes (con los nombres de torre y unidad)
            List<AsignacionModel> listaAsignaciones = asignacionDAO.listarAsignaciones();
            request.setAttribute("listaAsignaciones", listaAsignaciones);

            // 2. Cargar únicamente los departamentos o cocheras que estén libres ('VACANTE') para el formulario
            List<InmuebleModel> vacantes = asignacionDAO.listarInmueblesVacantes();
            request.setAttribute("listaInmueblesVacantes", vacantes);

        } else if ("recibos".equals(view)) {
            // INTEGRACIÓN MÓDULO 4: RECIBOS Y ESTADO DE PAGO
            // 1. Cargar el histórico completo de cuentas de mantenimiento generadas
            List<ReciboModel> listaRecibos = reciboDAO.listarRecibos();
            request.setAttribute("listaRecibos", listaRecibos);
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}