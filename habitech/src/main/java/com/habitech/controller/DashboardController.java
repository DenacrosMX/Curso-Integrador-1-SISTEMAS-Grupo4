package com.habitech.controller;

import com.habitech.dao.AsignacionDAO;
import com.habitech.dao.InmuebleDAO;
import com.habitech.dao.MaestroDAO;
import com.habitech.dao.ReciboDAO;
import com.habitech.dao.VisitaDAO;
import com.habitech.dao.IncidenciaDAO;
import com.habitech.dao.ReservaDAO; // Importación Módulo 8: Reservas
import com.habitech.dao.impl.AsignacionDAOImpl;
import com.habitech.dao.impl.InmuebleDAOImpl;
import com.habitech.dao.impl.MaestroDAOImpl;
import com.habitech.dao.impl.ReciboDAOImpl;
import com.habitech.dao.impl.VisitaDAOImpl;
import com.habitech.dao.impl.IncidenciaDAOImpl;
import com.habitech.dao.impl.ReservaDAOImpl; // Importación Módulo 8: Reservas
import com.habitech.model.InmuebleModel;
import com.habitech.model.MaestroModel;
import com.habitech.model.AsignacionModel;
import com.habitech.model.ReciboModel;
import com.habitech.model.VisitaModel;
import com.habitech.model.IncidenciaModel;
import com.habitech.model.ReservaModel; // Importación Módulo 8: Reservas
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
    private final ReciboDAO reciboDAO = new ReciboDAOImpl();
    private final VisitaDAO visitaDAO = new VisitaDAOImpl();
    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAOImpl();
    private final ReservaDAO reservaDAO = new ReservaDAOImpl(); // Instancia de Reservas (Módulo 8)

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

        } else if ("visitas".equals(view)) {
            // INTEGRACIÓN MÓDULO 5: CONTROL DE VISITAS Y PERMANENCIA (SEGURIDAD AUDITABLE)
            // 1. Cargar la bitácora histórica de ingresos (tanto EN_CURSO como FINALIZADOS)
            List<VisitaModel> listaVisitas = visitaDAO.listarVisitasRecientes();
            request.setAttribute("listaVisitas", listaVisitas);

            // 2. Cargar el mapa de inmuebles registrados para que sirvan como unidades destino en el selector
            List<InmuebleModel> listaInmueblesDestino = visitaDAO.listarTodosLosInmuebles();
            request.setAttribute("listaInmueblesDestino", listaInmueblesDestino);

        } else if ("mesa_ayuda".equals(view)) {
            // INTEGRACIÓN MÓDULO 6: MESA DE AYUDA / INCIDENCIAS
            // 1. Cargar la lista de tickets reportados con prioridades y estados
            List<IncidenciaModel> listaIncidencias = incidenciaDAO.listarIncidencias();
            request.setAttribute("listaIncidencias", listaIncidencias);

            // 2. Cargar los inmuebles para mapear la unidad que reporta el desperfecto
            List<InmuebleModel> listaInmueblesOrigen = visitaDAO.listarTodosLosInmuebles();
            request.setAttribute("listaInmueblesOrigen", listaInmueblesOrigen);

        } else if ("reservas".equals(view)) {
            // INTEGRACIÓN MÓDULO 8: RESERVAS DE ÁREAS COMUNES
            // 1. Cargar la agenda consolidada de separaciones de espacios
            List<ReservaModel> listaReservas = reservaDAO.listarReservas();
            request.setAttribute("listaReservas", listaReservas);

            // 2. Cargar el padrón de inmuebles para identificar a la unidad solicitante
            List<InmuebleModel> listaInmueblesSolicitantes = visitaDAO.listarTodosLosInmuebles();
            request.setAttribute("listaInmueblesSolicitantes", listaInmueblesSolicitantes);
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}