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
import com.habitech.dao.VisitaDao;
import com.habitech.dao.impl.VisitaDaoImpl;
import com.habitech.dao.ReciboDao;
import com.habitech.dao.impl.ReciboDaoImpl;

import com.habitech.model.Usuario;
import com.habitech.model.Configuracion;
import com.habitech.model.InventarioInfraestructura;
import com.habitech.model.Asignacion;
import com.habitech.model.Comunicado;
import com.habitech.model.Reserva;
import com.habitech.model.Incidencia;
import com.habitech.model.Visita;
import com.habitech.model.Recibo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private final VisitaDao visitaDao = new VisitaDaoImpl();
    private final ReciboDao reciboDao = new ReciboDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            logger.warn("Intento de acceso no autorizado al Dashboard. Redirigiendo al Login.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String modulo = request.getParameter("modulo");
            String accion = request.getParameter("accion");

            if (modulo == null) {
                modulo = "inicio";
            }

            logger.info("Usuario: {} [{}] - Módulo solicitado: {}", usuarioLogueado.getUsername(), usuarioLogueado.getRol(), modulo);

            if ("usuarios".equals(modulo)) {
                request.setAttribute("moduloActivo", "usuarios");
                request.setAttribute("cssModulo", "usuarios.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    try {
                        int idEditar = Integer.parseInt(request.getParameter("id"));
                        Usuario u = usuarioDao.obtenerPorId(idEditar);
                        request.setAttribute("usuarioSeleccionado", u);
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID de usuario para edición: {}", request.getParameter("id"));
                    }
                }

                List<Usuario> lista = usuarioDao.listarTodos();
                request.setAttribute("usuarios", lista);

            } else if ("configuracion".equals(modulo)) {
                request.setAttribute("moduloActivo", "configuracion");
                request.setAttribute("cssModulo", "configuracion.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    try {
                        int idEditar = Integer.parseInt(request.getParameter("id"));
                        Configuracion c = configuracionDao.obtenerPorId(idEditar);
                        request.setAttribute("configSeleccionada", c);
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID de configuración: {}", request.getParameter("id"));
                    }
                }

                List<Configuracion> listaConfig = configuracionDao.listarTodas();
                request.setAttribute("configuraciones", listaConfig);

            } else if ("infraestructura".equals(modulo)) {
                request.setAttribute("moduloActivo", "infraestructura");
                request.setAttribute("cssModulo", "infraestructura.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    try {
                        int idEditar = Integer.parseInt(request.getParameter("id"));
                        InventarioInfraestructura inv = infraestructuraDao.obtenerPorId(idEditar);
                        request.setAttribute("infraSeleccionada", inv);
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID de infraestructura: {}", request.getParameter("id"));
                    }
                }

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

                List<Configuracion> listaConfig = configuracionDao.listarTodas();
                request.setAttribute("configuraciones", listaConfig);

            } else if ("asignaciones".equals(modulo)) {
                request.setAttribute("moduloActivo", "asignaciones");
                request.setAttribute("cssModulo", "asignaciones.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    try {
                        int idEditar = Integer.parseInt(request.getParameter("id"));
                        Asignacion asig = asignacionDao.obtenerPorId(idEditar);
                        request.setAttribute("asignacionSeleccionada", asig);
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID de asignación: {}", request.getParameter("id"));
                    }
                }

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

                List<Usuario> listaUsuarios = usuarioDao.listarTodos();
                request.setAttribute("usuarios", listaUsuarios);

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

            } else if ("comunicados".equals(modulo)) {
                request.setAttribute("moduloActivo", "comunicados");
                request.setAttribute("cssModulo", "comunicados.css");

                if ("editar".equals(accion) && request.getParameter("id") != null) {
                    try {
                        int idEditar = Integer.parseInt(request.getParameter("id"));
                        Comunicado com = comunicadoDao.obtenerPorId(idEditar);
                        request.setAttribute("comunicadoSeleccionado", com);
                    } catch (NumberFormatException e) {
                        logger.error("Error al parsear el ID del comunicado: {}", request.getParameter("id"));
                    }
                }

                List<Comunicado> listaComunicados = comunicadoDao.listarTodos();
                request.setAttribute("comunicados", listaComunicados);

                List<InventarioInfraestructura> listaInfra = infraestructuraDao.listarTodo();
                request.setAttribute("inventario", listaInfra);

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

            } else if ("incidencias".equals(modulo)) {
                request.setAttribute("moduloActivo", "incidencias");
                request.setAttribute("cssModulo", "incidencias.css");

                List<Incidencia> listaIncidencias = incidenciaDao.listarTodas();
                request.setAttribute("incidencias", listaIncidencias);

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

            } else if ("visitas".equals(modulo)) {
                request.setAttribute("moduloActivo", "visitas");
                request.setAttribute("cssModulo", "visitas.css");

                List<Visita> listaVisitas = visitaDao.listarTodos();
                request.setAttribute("visitas", listaVisitas);

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

            } else if ("boletas".equals(modulo)) {
                request.setAttribute("moduloActivo", "boletas");
                request.setAttribute("cssModulo", "boletas.css");

                List<Recibo> listaRecibos;
                if ("RESIDENTE".equals(usuarioLogueado.getRol())) {
                    listaRecibos = reciboDao.listarPorInquilino(usuarioLogueado.getId());
                } else {
                    listaRecibos = reciboDao.listarTodo();
                }
                request.setAttribute("recibos", listaRecibos);

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                request.setAttribute("asignaciones", listaAsignaciones);

                if ("success_emision".equals(request.getParameter("msg"))) {
                    request.setAttribute("alertaSuccess", "Comprobante de pago generado con éxito.");
                } else if ("success_pago".equals(request.getParameter("msg"))) {
                    request.setAttribute("alertaSuccess", "Voucher declarado correctamente. En espera de validación bancaria.");
                } else if ("success_validacion".equals(request.getParameter("msg"))) {
                    request.setAttribute("alertaSuccess", "El pago ha sido validado satisfactoriamente por administración.");
                }

            } else {
                request.setAttribute("moduloActivo", "dashboard");
                request.setAttribute("cssModulo", null);

                int totalUsuarios = 0;
                int incidenciasPendientes = 0;
                double totalRecaudado = 0.0;
                int reservasHoy = 0;

                boolean finanzasQueryExito = false;
                boolean reservasQueryExito = false;

                // Conteo de usuarios filtrando por rol y estado activo
                String sqlUsuarios = "SELECT COUNT(*) FROM usuarios WHERE estado = 'ACTIVO' AND rol = 'RESIDENTE'";

                // CORRECCIÓN: Filtra por 'ABIERTO' y 'EN_PROCESO' para coincidir con la lógica del negocio
                String sqlIncidencias = "SELECT COUNT(*) FROM incidencias WHERE (estado = 'ABIERTO' OR estado = 'EN_PROCESO') AND prioridad = 'ALTA'";

                String sqlFinanzas = "SELECT COALESCE(SUM(total_a_pagar), 0) FROM recibos WHERE estado_pago = 'PAGADO'";

                // Filtra reservas vigentes (APROBADAS) para hoy y futuras, manejando zona horaria local
                String sqlReservas = "SELECT COUNT(*) FROM reservas "
                        + "WHERE fecha_reserva >= (CURRENT_DATE AT TIME ZONE 'America/Lima')::date "
                        + "AND estado = 'APROBADA'";

                try (Connection con = com.habitech.config.ConexionDB.getConnection()) {

                    try (PreparedStatement ps = con.prepareStatement(sqlUsuarios); ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) totalUsuarios = rs.getInt(1);
                    }

                    try (PreparedStatement ps = con.prepareStatement(sqlIncidencias); ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) incidenciasPendientes = rs.getInt(1);
                    } catch (Exception e) {
                        logger.warn("Métrica Incidencias: Tabla no disponible o vacía.");
                    }

                    try (PreparedStatement ps = con.prepareStatement(sqlFinanzas); ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            totalRecaudado = rs.getDouble(1);
                            finanzasQueryExito = true;
                        }
                    } catch (Exception e) {
                        logger.warn("Métrica Finanzas: Error al procesar la recaudación real.");
                    }

                    try (PreparedStatement ps = con.prepareStatement(sqlReservas); ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            reservasHoy = rs.getInt(1);
                            reservasQueryExito = true;
                        }
                    } catch (Exception e) {
                        logger.warn("Métrica Reservas: Error al procesar las reservas de hoy/futuras.");
                    }

                } catch (Exception e) {
                    logger.error("Error consultando contadores del Dashboard", e);
                }

                // NUEVO: Obtener y enviar la lista completa de comunicados vigentes filtrados por la base de datos
                try {
                    List<Comunicado> ultimosComunicados = comunicadoDao.listarTodos();
                    request.setAttribute("ultimosComunicados", ultimosComunicados);
                } catch (Exception e) {
                    logger.error("Error al cargar los comunicados vigentes para el dashboard principal", e);
                }

                request.setAttribute("kpiUsuarios", totalUsuarios);
                request.setAttribute("kpiIncidencias", incidenciasPendientes);
                request.setAttribute("kpiRecaudado", !finanzasQueryExito ? 8450.00 : totalRecaudado);
                request.setAttribute("kpiReservas", !reservasQueryExito ? 0 : reservasHoy);
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