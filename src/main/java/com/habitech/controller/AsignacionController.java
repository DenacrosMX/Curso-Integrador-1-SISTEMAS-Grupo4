package com.habitech.controller;

import com.habitech.dao.AsignacionDao;
import com.habitech.dao.impl.AsignacionDaoImpl;
import com.habitech.dao.InventarioInfraestructuraDao;
import com.habitech.dao.impl.InventarioInfraestructuraDaoImpl;
import com.habitech.model.Asignacion;
import com.habitech.model.InventarioInfraestructura;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "AsignacionController", urlPatterns = {"/asignaciones", "/api/unidades-disponibles"})
public class AsignacionController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionController.class);
    private final AsignacionDao asignacionDao = new AsignacionDaoImpl();
    private final InventarioInfraestructuraDao infraestructuraDao = new InventarioInfraestructuraDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/api/unidades-disponibles".equals(path)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String infraIdParam = request.getParameter("infraestructuraId");
            try (PrintWriter out = response.getWriter()) {
                if (infraIdParam == null || infraIdParam.trim().isEmpty()) {
                    out.print("[]");
                    return;
                }

                int infraId = Integer.parseInt(infraIdParam);
                InventarioInfraestructura maestro = infraestructuraDao.obtenerPorId(infraId);

                if (maestro != null) {
                    List<String> codigos = maestro.getCodigosGenerados();

                    StringBuilder json = new StringBuilder("[");
                    for (int i = 0; i < codigos.size(); i++) {
                        json.append("{\"codigoUnidad\":\"").append(codigos.get(i)).append("\"}");
                        if (i < codigos.size() - 1) {
                            json.append(",");
                        }
                    }
                    json.append("]");
                    out.print(json.toString());
                } else {
                    out.print("[]");
                }
            } catch (Exception e) {
                logger.error("Error generando unidades dinámicas para el select", e);
                response.getWriter().print("[]");
            }
            return;
        }

        String accion = request.getParameter("accion");
        String idParam = request.getParameter("id");

        try {
            if ("finalizar".equals(accion) && idParam != null) {
                int id = Integer.parseInt(idParam);
                asignacionDao.finalizarAsignacion(id);
            }
        } catch (NumberFormatException e) {
            logger.error("Error al parsear el ID de asignación para la finalización", e);
        }
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=asignaciones");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String usuarioIdParam = request.getParameter("usuarioId");
        String inventarioMaestroIdParam = request.getParameter("inventarioMaestroId");

        String codigoUnidadParam = request.getParameter("codigoUnidadEspecifica");
        if (codigoUnidadParam == null || codigoUnidadParam.trim().isEmpty()) {
            codigoUnidadParam = request.getParameter("codigoUnidad");
        }

        String tipoAdquisicion = request.getParameter("tipoAdquisicion");
        String precioParam = request.getParameter("precioMensualPactado");
        String estado = request.getParameter("estado");
        String fechaIngresoParam = request.getParameter("fechaIngreso");
        String fechaSalidaParam = request.getParameter("fechaSalida");

        try {
            Asignacion asignacion = new Asignacion();
            asignacion.setUsuarioId(Integer.parseInt(usuarioIdParam));
            asignacion.setInventarioMaestroId(Integer.parseInt(inventarioMaestroIdParam));

            if (codigoUnidadParam != null && !codigoUnidadParam.trim().isEmpty()) {
                asignacion.setCodigoUnidad(codigoUnidadParam.trim());
            }

            asignacion.setTipoAdquisicion(tipoAdquisicion);

            BigDecimal precio = (precioParam != null && !precioParam.trim().isEmpty())
                    ? new BigDecimal(precioParam.trim())
                    : BigDecimal.ZERO;
            asignacion.setPrecioMensualPactado(precio);
            asignacion.setEstado(estado != null ? estado : "ACTIVO");

            if (fechaIngresoParam != null && !fechaIngresoParam.trim().isEmpty()) {
                asignacion.setFechaIngreso(Date.valueOf(fechaIngresoParam));
            } else {
                asignacion.setFechaIngreso(new Date(System.currentTimeMillis()));
            }

            if (fechaSalidaParam != null && !fechaSalidaParam.trim().isEmpty()) {
                asignacion.setFechaSalida(Date.valueOf(fechaSalidaParam));
            }

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.info("Registrando nueva asignación para la unidad: '{}'", asignacion.getCodigoUnidad());
                asignacionDao.insertar(asignacion);
            } else {
                asignacion.setId(Integer.parseInt(idParam));
                asignacionDao.actualizar(asignacion);
            }

        } catch (Exception e) {
            logger.error("Error crítico al procesar la asignación contractual en doPost", e);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=asignaciones");
    }
}