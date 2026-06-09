package com.habitech.controller;

import com.google.common.collect.ImmutableList;
import com.habitech.dao.UsuarioDao;
import com.habitech.dao.impl.UsuarioDaoImpl;
import com.habitech.dao.AsignacionDao;
import com.habitech.dao.impl.AsignacionDaoImpl;
import com.habitech.model.Usuario;
import com.habitech.model.Asignacion;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet(name = "ExportarExcelController", urlPatterns = {"/exportarExcel"})
public class ExportarExcelController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ExportarExcelController.class);
    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();
    private final AsignacionDao asignacionDao = new AsignacionDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        logger.info("Auditoría Logback: Solicitud de exportación Excel de tipo: {}", tipo);

        if (StringUtils.isBlank(tipo)) {
            logger.warn("Se intentó invocar el servlet de exportación sin un parámetro de tipo válido.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro 'tipo' faltante.");
            return;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try (Workbook workbook = new XSSFWorkbook(); OutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Datos Habitech Real");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            if ("usuarios".equalsIgnoreCase(tipo)) {
                response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.xlsx");

                ImmutableList<String> cabeceras = ImmutableList.of("ID Usuario", "Username", "Nombres", "Apellidos", "Rol", "Estado");
                Row rowCabecera = sheet.createRow(0);
                for (int i = 0; i < cabeceras.size(); i++) {
                    Cell celda = rowCabecera.createCell(i);
                    celda.setCellValue(cabeceras.get(i));
                    celda.setCellStyle(headerStyle);
                }

                List<Usuario> listaUsuarios = usuarioDao.listarTodos();
                int numFila = 1;
                if (listaUsuarios != null) {
                    for (Usuario u : listaUsuarios) {
                        Row fila = sheet.createRow(numFila++);
                        fila.createCell(0).setCellValue(u.getId());
                        fila.createCell(1).setCellValue(u.getUsername());
                        fila.createCell(2).setCellValue(u.getNombres());
                        fila.createCell(3).setCellValue(u.getApellidos());
                        fila.createCell(4).setCellValue(u.getRol());
                        fila.createCell(5).setCellValue(u.getEstado());
                    }
                }
                logger.info("Reporte POI finalizado: Se exportaron {} registros de usuarios.", numFila - 1);

            } else if ("asignaciones".equalsIgnoreCase(tipo)) {
                response.setHeader("Content-Disposition", "attachment; filename=reporte_asignaciones.xlsx");

                // Rediseño de cabeceras basadas en tu BD real (AsignacionDaoImpl)
                ImmutableList<String> cabeceras = ImmutableList.of("ID Asignación", "Usuario Residente", "Infraestructura Base", "Código Unidad", "Tipo Adquisición", "Precio Pactado", "Estado");
                Row rowCabecera = sheet.createRow(0);
                for (int i = 0; i < cabeceras.size(); i++) {
                    Cell celda = rowCabecera.createCell(i);
                    celda.setCellValue(cabeceras.get(i));
                    celda.setCellStyle(headerStyle);
                }

                List<Asignacion> listaAsignaciones = asignacionDao.listarTodas();
                int numFila = 1;
                if (listaAsignaciones != null) {
                    for (Asignacion a : listaAsignaciones) {
                        Row fila = sheet.createRow(numFila++);

                        // Inyección basada estrictamente en las columnas de tu ResultSet mapeado
                        fila.createCell(0).setCellValue(a.getId());
                        fila.createCell(1).setCellValue(a.getNombreUsuario());
                        fila.createCell(2).setCellValue(a.getDetalleInfraestructura());
                        fila.createCell(3).setCellValue(a.getCodigoUnidadEspecifica()); // Código interno (Ej: "DEP-402")
                        fila.createCell(4).setCellValue(a.getTipoAdquisicion());

                        // Conversión segura para BigDecimals en celdas numéricas de Excel
                        double precio = (a.getPrecioMensualPactado() != null) ? a.getPrecioMensualPactado().doubleValue() : 0.0;
                        fila.createCell(5).setCellValue(precio);

                        fila.createCell(6).setCellValue(a.getEstado());
                    }
                }
                logger.info("Reporte POI finalizado: Se exportaron {} registros de asignaciones.", numFila - 1);
            }

            // Autoajustar el tamaño dinámico de las columnas (hasta 7 columnas para el módulo de asignaciones)
            for (int col = 0; col < 7; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(out);
            out.flush();

        } catch (Exception e) {
            logger.error("Error crítico durante la generación del libro binario Apache POI", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Excepción interna al procesar la descarga.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}