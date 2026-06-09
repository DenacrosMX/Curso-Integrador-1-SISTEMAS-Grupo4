package com.habitech.controller;

import com.habitech.dao.AsignacionDao;
import com.habitech.dao.impl.AsignacionDaoImpl;
import com.habitech.model.Asignacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "AsignacionController", urlPatterns = {"/asignaciones"})
public class AsignacionController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionController.class);
    private final AsignacionDao asignacionDao = new AsignacionDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String idParam = request.getParameter("id");

        try {
            if ("finalizar".equals(accion) && idParam != null) {
                int id = Integer.parseInt(idParam);
                logger.info("Solicitud para dar de baja/finalizar contrato de asignación ID: {}", id);

                boolean finalizado = asignacionDao.finalizarAsignacion(id);
                if (finalizado) {
                    logger.info("Asignación ID {} finalizada con éxito estableciendo fecha de salida.", id);
                } else {
                    logger.warn("No se pudo finalizar la asignación con ID: {}", id);
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Error al parsear el ID de asignación para la baja contractual", e);
        }

        // Redirección de control al dashboard apuntando al módulo de asignaciones
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=asignaciones");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String usuarioIdParam = request.getParameter("usuarioId");
        String inventarioMaestroIdParam = request.getParameter("inventarioMaestroId");
        String codigoUnidadEspecifica = request.getParameter("codigoUnidadEspecifica");
        String tipoAdquisicion = request.getParameter("tipoAdquisicion");
        String precioParam = request.getParameter("precioMensualPactado");
        String estado = request.getParameter("estado");
        String fechaIngresoParam = request.getParameter("fechaIngreso");
        String fechaSalidaParam = request.getParameter("fechaSalida");

        try {
            Asignacion asignacion = new Asignacion();
            asignacion.setUsuarioId(Integer.parseInt(usuarioIdParam));
            asignacion.setInventarioMaestroId(Integer.parseInt(inventarioMaestroIdParam));
            asignacion.setCodigoUnidadEspecifica(codigoUnidadEspecifica);
            asignacion.setTipoAdquisicion(tipoAdquisicion);

            // Conversión segura a BigDecimal para el precio pactado
            BigDecimal precio = (precioParam != null && !precioParam.trim().isEmpty())
                    ? new BigDecimal(precioParam.trim())
                    : BigDecimal.ZERO;
            asignacion.setPrecioMensualPactado(precio);

            asignacion.setEstado(estado != null ? estado : "ACTIVO");

            // Mapeo seguro de fechas SQL DATE
            if (fechaIngresoParam != null && !fechaIngresoParam.trim().isEmpty()) {
                asignacion.setFechaIngreso(Date.valueOf(fechaIngresoParam));
            } else {
                asignacion.setFechaIngreso(new Date(System.currentTimeMillis()));
            }

            if (fechaSalidaParam != null && !fechaSalidaParam.trim().isEmpty()) {
                asignacion.setFechaSalida(Date.valueOf(fechaSalidaParam));
            }

            if (idParam == null || idParam.trim().isEmpty()) {
                // OPERACIÓN: NUEVO REGISTRO
                logger.info("Intentando registrar nueva asignación para la unidad {} de tipo {}.",
                        codigoUnidadEspecifica, tipoAdquisicion);
                asignacionDao.insertar(asignacion);
            } else {
                // OPERACIÓN: EDICIÓN / ACTUALIZACIÓN
                asignacion.setId(Integer.parseInt(idParam));
                logger.info("Intentando actualizar asignación existente con ID: {}", asignacion.getId());
                asignacionDao.actualizar(asignacion);
            }

        } catch (Exception e) {
            logger.error("Error crítico al procesar la asignación contractual en doPost", e);
        }

        // Redirección limpia para refrescar la grilla del dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=asignaciones");
    }
}