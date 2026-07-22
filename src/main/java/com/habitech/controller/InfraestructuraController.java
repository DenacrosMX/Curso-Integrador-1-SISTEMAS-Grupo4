package com.habitech.controller;

import com.habitech.dao.InventarioInfraestructuraDao;
import com.habitech.dao.impl.InventarioInfraestructuraDaoImpl;
import com.habitech.model.InventarioInfraestructura;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "InfraestructuraController", urlPatterns = {"/infraestructura"})
public class InfraestructuraController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(InfraestructuraController.class);
    private final InventarioInfraestructuraDao infraestructuraDao = new InventarioInfraestructuraDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String idParam = request.getParameter("id");

        try {
            if ("eliminar".equals(accion) && idParam != null) {
                int id = Integer.parseInt(idParam);
                logger.info("Procesando baja lógica del elemento estructural con ID: {}", id);

                boolean eliminado = infraestructuraDao.eliminarLogico(id);
                if (eliminado) {
                    logger.info("El elemento con ID {} fue dado de baja lógicamente.", id);
                } else {
                    logger.warn("No se pudo dar de baja el elemento con ID {}.", id);
                }
            }
        } catch (Exception e) {
            logger.error("Error en la operación GET del módulo de infraestructura", e);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=infraestructura");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String configIdParam = request.getParameter("configuracionMaestraId");
        String tipoElemento = request.getParameter("tipoElemento");
        String torre = request.getParameter("torre");
        String pisoInicioParam = request.getParameter("nroPisoInicio");
        String pisoFinParam = request.getParameter("nroPisoFin");
        String cantidadParam = request.getParameter("cantidadRegistrada");

        try {
            int configId = Integer.parseInt(configIdParam);
            int cantidadPorPiso = Integer.parseInt(cantidadParam);
            String torreFinal = (torre == null || torre.trim().isEmpty()) ? "" : torre.toUpperCase().trim();

            if (idParam == null || idParam.trim().isEmpty()) {
                int pisoInicio = Integer.parseInt(pisoInicioParam);
                int pisoFin = (pisoFinParam == null || pisoFinParam.trim().isEmpty())
                        ? pisoInicio : Integer.parseInt(pisoFinParam);

                logger.info("Iniciando generación masiva de infraestructura. Rango: {} al {}. Tipo: {}. Bloque: '{}'",
                        pisoInicio, pisoFin, tipoElemento, torreFinal);

                int menor = Math.min(pisoInicio, pisoFin);
                int mayor = Math.max(pisoInicio, pisoFin);

                for (int i = menor; i <= mayor; i++) {
                    InventarioInfraestructura nuevoElemento = new InventarioInfraestructura();
                    nuevoElemento.setConfiguracionMaestraId(configId);
                    nuevoElemento.setTipoElemento(tipoElemento);
                    nuevoElemento.setTorre(torreFinal);
                    nuevoElemento.setNroPiso(i);
                    nuevoElemento.setCantidadRegistrada(cantidadPorPiso);

                    boolean insertado = infraestructuraDao.insertar(nuevoElemento);
                    if (!insertado) {
                        logger.error("Fallo crítico insertando el piso {} en la generación masiva.", i);
                    }
                }
                logger.info("Procesamiento masivo de infraestructura completado de forma exitosa.");

            } else {
                InventarioInfraestructura elemento = new InventarioInfraestructura();
                elemento.setId(Integer.parseInt(idParam));
                elemento.setConfiguracionMaestraId(configId);
                elemento.setTipoElemento(tipoElemento);
                elemento.setTorre(torreFinal);
                elemento.setNroPiso(Integer.parseInt(pisoInicioParam));
                elemento.setCantidadRegistrada(cantidadPorPiso);

                logger.info("Actualizando registro de infraestructura individual. ID: {}", elemento.getId());
                boolean actualizado = infraestructuraDao.actualizar(elemento);

                if (actualizado) {
                    logger.info("Fila con ID {} actualizada correctamente.", elemento.getId());
                } else {
                    logger.warn("No se modificó ninguna fila para el ID {}.", elemento.getId());
                }
            }

        } catch (Exception e) {
            logger.error("Error grave en el hilo de procesamiento del InfraestructuraController", e);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=infraestructura");
    }
}