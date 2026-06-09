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
                    logger.info("Elemento estructural desactivado con éxito de la base de datos.");
                } else {
                    logger.warn("No se pudo desactivar el elemento con ID: {}", id);
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Error al parsear el ID del elemento para eliminar", e);
        }

        // Redirección limpia al dashboard conservando el módulo activo
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=infraestructura");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String configuracionIdParam = request.getParameter("configuracionMaestraId");
        String tipoElemento = request.getParameter("tipoElemento");
        String torre = request.getParameter("torre");

        // Parámetros dinámicos para el generador inteligente por rangos
        String pisoInicioParam = request.getParameter("nroPisoInicio");
        String pisoFinParam = request.getParameter("nroPisoFin");
        String cantidadPorPisoParam = request.getParameter("cantidadRegistrada");

        try {
            int configId = Integer.parseInt(configuracionIdParam);
            String torreFinal = (torre == null || torre.trim().isEmpty()) ? "GENERAL" : torre.toUpperCase().trim();
            int cantidadPorPiso = Integer.parseInt(cantidadPorPisoParam);

            if (idParam == null || idParam.trim().isEmpty()) {
                // =================================================================
                // OPERACIÓN: GENERACIÓN MASIVA POR RANGO (DEPARTAMENTOS / SÓTANOS)
                // =================================================================
                int pisoInicio = Integer.parseInt(pisoInicioParam);

                // Si el piso final viene nulo o vacío por seguridad, lo igualamos al de inicio (inserta 1 solo piso)
                int pisoFin = (pisoFinParam == null || pisoFinParam.trim().isEmpty())
                        ? pisoInicio
                        : Integer.parseInt(pisoFinParam);

                logger.info("Iniciando motor de generación masiva de {} para la estructura {} desde el nivel {} al {}.",
                        tipoElemento, torreFinal, pisoInicio, pisoFin);

                int registrosExitosos = 0;

                // El ciclo for procesa de forma natural números negativos (sótanos), cero y positivos
                for (int piso = pisoInicio; piso <= pisoFin; piso++) {
                    InventarioInfraestructura elemento = new InventarioInfraestructura();
                    elemento.setConfiguracionMaestraId(configId);
                    elemento.setTipoElemento(tipoElemento);
                    elemento.setTorre(torreFinal);
                    elemento.setNroPiso(piso);
                    elemento.setCantidadRegistrada(cantidadPorPiso);

                    boolean insertado = infraestructuraDao.insertar(elemento);
                    if (insertado) {
                        registrosExitosos++;
                    }
                }
                logger.info("Motor masivo apagado. Se crearon exitosamente un total de {} registros de piso.", registrosExitosos);

            } else {
                // =================================================================
                // OPERACIÓN: ACTUALIZACIÓN INDIVIDUAL DE UN REGISTRO EXISTENTE
                // =================================================================
                InventarioInfraestructura elemento = new InventarioInfraestructura();
                elemento.setId(Integer.parseInt(idParam));
                elemento.setConfiguracionMaestraId(configId);
                elemento.setTipoElemento(tipoElemento);
                elemento.setTorre(torreFinal);
                // En modo edición individual, tomamos el valor del único input disponible (pisoInicio)
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

        // Redirección limpia de retorno al panel para refrescar la grilla de datos
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=infraestructura");
    }
}