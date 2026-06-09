package com.habitech.controller;

import com.habitech.dao.UsuarioDao;
import com.habitech.dao.impl.UsuarioDaoImpl;
import com.habitech.dao.ConfiguracionDao;
import com.habitech.dao.impl.ConfiguracionDaoImpl;
import com.habitech.dao.InventarioInfraestructuraDao;
import com.habitech.dao.impl.InventarioInfraestructuraDaoImpl;
import com.habitech.dao.AsignacionDao;
import com.habitech.dao.impl.AsignacionDaoImpl;
import com.habitech.dao.ComunicadoDao;
import com.habitech.dao.impl.ComunicadoDaoImpl;
import com.habitech.dao.ReservaDao;
import com.habitech.dao.impl.ReservaDaoImpl;
import com.habitech.dao.IncidenciaDao;
import com.habitech.dao.impl.IncidenciaDaoImpl;
import com.habitech.dao.VisitaDao;                // Import limpio
import com.habitech.dao.impl.VisitaDaoImpl;        // Import limpio

import com.habitech.model.Usuario;
import com.habitech.model.Configuracion;
import com.habitech.model.InventarioInfraestructura;
import com.habitech.model.Asignacion;
import com.habitech.model.Comunicado;
import com.habitech.model.Reserva;
import com.habitech.model.Incidencia;
import com.habitech.model.Visita;                  // Import limpio

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();
    private final ConfiguracionDao configuracionDao = new ConfiguracionDaoImpl();
    private final InventarioInfraestructuraDao infraestructuraDao = new InventarioInfraestructuraDaoImpl();
    private final AsignacionDao asignacionDao = new AsignacionDaoImpl();
    private final ComunicadoDao comunicadoDao = new ComunicadoDaoImpl();
    private final ReservaDao reservaDao = new ReservaDaoImpl();
    private final IncidenciaDao incidenciaDao = new IncidenciaDaoImpl();
    private final VisitaDao visitaDao = new VisitaDaoImpl(); // Instancia única como los otros DAOs

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String modulo = request.getParameter("modulo");
            String accion = request.getParameter("accion");

            if (modulo == null) {
                modulo = "inicio";
            }

            logger.info("Cargando el panel general. Módulo solicitado: {}", modulo);

            // MÓDULO 1: GESTIÓN DE USUARIOS
            if ("usuarios".equals(modulo)) {
                request.setAttribute("moduloActivo", "usuarios");
                request.setAttribute("cssModulo", "usuarios.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Usuario u = usuarioDao.obtenerPorId(idEditar);
                    request.setAttribute("usuarioSelected", u);
                }

                List<Usuario> lista = usuarioDao.listarTodos();
                request.setAttribute("usuarios", lista);

                // MÓDULO 2: CONFIGURACIÓN MAESTRA DEL CONDOMINIO
            } else if ("configuracion".equals(modulo)) {
                request.setAttribute("moduloActivo", "configuracion");
                request.setAttribute("cssModulo", "configuracion.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Configuracion c = configuracionDao.obtenerPorId(idEditar);
                    request.setAttribute("configSeleccionada", c);
                }

                List<Configuracion> listaConfig = configuracionDao.listarTodas();
                request.setAttribute("configuraciones", listaConfig);

                // MÓDULO 3: INVENTARIO MAESTRO DE INFRAESTRUCTURA
            } else if ("infraestructura".equals(modulo)) {
                request.setAttribute("moduloActivo", "infraestructura");
                request.setAttribute("cssModulo", "infraestructura.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    InventarioInfraestructura inv = infraestructuraDao.obtenerPorId(idEditar);
                    request.setAttribute("infraSeleccionada", inv);
                }

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

                List<Configuracion> listaConfig = configuracionDao.listarTodas();
                request.setAttribute("configuraciones", listaConfig);

                // MÓDULO 4: ASIGNACIONES DE UNIDADES (PROPIETARIOS / INQUILINOS)
            } else if ("asignaciones".equals(modulo)) {
                request.setAttribute("moduloActivo", "asignaciones");
                request.setAttribute("cssModulo", "asignaciones.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Asignacion asig = asignacionDao.obtenerPorId(idEditar);
                    request.setAttribute("asignacionSeleccionada", asig);
                }

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

                List<Usuario> listaUsuarios = usuarioDao.listarTodos();
                request.setAttribute("usuarios", listaUsuarios);

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

                // MÓDULO 5: COMUNICADOS E INFORMES DEL CONDOMINIO
            } else if ("comunicados".equals(modulo)) {
                request.setAttribute("moduloActivo", "comunicados");
                request.setAttribute("cssModulo", "comunicados.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Comunicado com = comunicadoDao.obtenerPorId(idEditar);
                    request.setAttribute("comunicadoSeleccionado", com);
                }

                List<Comunicado> listaComunicados = comunicadoDao.listarTodos();
                request.setAttribute("comunicados", listaComunicados);

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

                // MÓDULO 6: RESERVAS DE ÁREAS COMUNES
            } else if ("reservas".equals(modulo)) {
                request.setAttribute("moduloActivo", "reservas");
                request.setAttribute("cssModulo", "reservas.css");

                List<Reserva> listaReservas = reservaDao.listarTodas();
                request.setAttribute("reservas", listaReservas);

                List<Usuario> listaUsuarios = usuarioDao.listarTodos();
                request.setAttribute("usuarios", listaUsuarios);

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

                if ("duplicado".equals(request.getParameter("error"))) {
                    request.setAttribute("alertaError", "El área seleccionada ya se encuentra reservada en esa fecha y turno.");
                }

                // MÓDULO 7: CONTROL DE INCIDENCIAS
            } else if ("incidencias".equals(modulo)) {
                request.setAttribute("moduloActivo", "incidencias");
                request.setAttribute("cssModulo", "incidencias.css");

                List<Incidencia> listaIncidencias = incidenciaDao.listarTodas();
                request.setAttribute("incidencias", listaIncidencias);

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

                // MÓDULO 8: CONTROL DE VISITAS E INGRESOS (CORREGIDO Y ULTRA-LIGERO)
            } else if ("visitas".equals(modulo)) {
                request.setAttribute("moduloActivo", "visitas");
                request.setAttribute("cssModulo", "visitas.css");

                // Solo actuamos como puente: cargamos la data requerida por la vista y listo
                List<Visita> listaVisitas = visitaDao.listarTodos();
                request.setAttribute("visitas", listaVisitas);

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

                // VISTA POR DEFECTO
            } else {
                request.setAttribute("moduloActivo", "dashboard");
                request.setAttribute("cssModulo", null);
            }

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error al renderizar el esqueleto del Dashboard", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar el esqueleto del panel.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}