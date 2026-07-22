package com.habitech.controller;

import com.habitech.dao.ReciboDao;
import com.habitech.dao.impl.ReciboDaoImpl;
import com.habitech.model.Recibo;
import com.habitech.model.DetalleRecibo;
import com.habitech.model.Usuario;
import com.habitech.model.Configuracion;
import com.habitech.util.GeneradorPdfBoleta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "BoletaController", urlPatterns = {"/boletas"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10)
public class BoletaController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(BoletaController.class);
    private final ReciboDao reciboDao = new ReciboDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("descargarPdf".equals(accion)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Recibo recibo = reciboDao.obtenerPorId(id);

                if (recibo != null) {
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "inline; filename=Boleta_" + recibo.getNroComprobante() + ".pdf");

                    Configuracion conf = obtenerConfiguracionEmpresa();
                    GeneradorPdfBoleta.generarBoleta(recibo, conf, response.getOutputStream());
                    return;
                }
            } catch (Exception e) {
                logger.error("Error al generar o descargar el PDF de la boleta", e);
            }
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");

        } else if ("validar".equals(accion)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuarioLogueado");
                int adminId = (usuarioLogueado != null) ? usuarioLogueado.getId() : 1;

                boolean actualizado = reciboDao.cambiarEstadoPago(id, "PAGADO", adminId);

                if (actualizado) {
                    try {
                        Recibo recibo = reciboDao.obtenerPorId(id);
                        if (recibo != null) {
                            String uploadPath = obtenerRutaAlmacenamiento();
                            String nombrePdf = "boleta_" + recibo.getId() + "_" + recibo.getNroComprobante().replace("#", "").replace("-", "_") + ".pdf";
                            File pdfFile = new File(uploadPath + File.separator + nombrePdf);

                            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                                Configuracion conf = obtenerConfiguracionEmpresa();
                                GeneradorPdfBoleta.generarBoleta(recibo, conf, fos);
                            }

                            request.getSession().setAttribute("alertaSuccess", "El pago ha sido aprobado y la boleta PDF fue generada con éxito.");
                        }
                    } catch (Exception e) {
                        logger.error("Error al escribir el archivo PDF en almacenamiento", e);
                        request.getSession().setAttribute("alertaSuccess", "El pago se aprobó, pero hubo un percance al compilar el PDF físico.");
                    }
                } else {
                    request.getSession().setAttribute("alertaError", "No se pudo actualizar el estado de la boleta.");
                }
            } catch (NumberFormatException e) {
                logger.error("ID de boleta inválido para validación");
                request.getSession().setAttribute("alertaError", "ID de boleta incorrecto.");
            }

            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        if ("emitir".equals(accion)) {
            try {
                int usuarioId = Integer.parseInt(request.getParameter("usuario_id"));
                int mes = Integer.parseInt(request.getParameter("mes"));
                int anio = Integer.parseInt(request.getParameter("anio"));
                BigDecimal montoBase = new BigDecimal(request.getParameter("monto_base"));

                String extraDesc = request.getParameter("concepto_extra");
                String extraMontoStr = request.getParameter("monto_extra");

                Recibo r = new Recibo();
                r.setUsuarioId(usuarioId);
                r.setUsuarioResponsableId(usuarioLogueado != null ? usuarioLogueado.getId() : 1);
                r.setMesFacturado(mes);
                r.setAnioFacturado(anio);
                r.setTotalAPagar(montoBase);
                r.setFechaEmision(new java.sql.Date(System.currentTimeMillis()));
                r.setEstadoPago("PENDIENTE");

                r.getDetalles().add(new DetalleRecibo("Mantenimiento Mensual Estándar Base", montoBase));

                if (extraMontoStr != null && !extraMontoStr.trim().isEmpty()) {
                    BigDecimal montoExtra = new BigDecimal(extraMontoStr);
                    if (montoExtra.compareTo(BigDecimal.ZERO) > 0) {
                        if (extraDesc == null || extraDesc.trim().isEmpty()) {
                            extraDesc = "Concepto Adicional / Penalidad de Convivencia";
                        }
                        r.getDetalles().add(new DetalleRecibo(extraDesc.trim(), montoExtra));
                        r.setTotalAPagar(r.getTotalAPagar().add(montoExtra));
                    }
                }

                reciboDao.insertarConDetalles(r);
                request.getSession().setAttribute("alertaSuccess", "El recibo mensual se emitió y notificó correctamente.");

            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : "";
                if (errorMsg.contains("uq_usuario_periodo") || errorMsg.contains("violates unique constraint") || errorMsg.contains("restricción de unicidad")) {
                    request.getSession().setAttribute("alertaError", "Error: Ya existe un recibo de mantenimiento emitido para este residente en el periodo seleccionado.");
                } else {
                    request.getSession().setAttribute("alertaError", "No se pudo procesar la emisión: " + errorMsg);
                }
            }
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");

        } else if ("declararPago".equals(accion)) {
            try {
                int reciboId = Integer.parseInt(request.getParameter("recibo_id"));
                String nroOp = request.getParameter("nro_operacion");
                String medio = request.getParameter("medio_pago");

                Part filePart = request.getPart("voucher_file");
                String fileName = "voucher_" + reciboId + "_" + System.currentTimeMillis() + ".jpg";

                String uploadPath = obtenerRutaAlmacenamiento();

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + fileName);

                String serverBackupPath = getServletContext().getRealPath("") + File.separator + "uploads";
                File serverBackupDir = new File(serverBackupPath);
                if (!serverBackupDir.exists()) {
                    serverBackupDir.mkdirs();
                }
                try {
                    filePart.write(serverBackupPath + File.separator + fileName);
                } catch (Exception ignored) {}

                String rutaRelativaVoucher = "uploads/" + fileName;
                java.sql.Date fechaHoy = new java.sql.Date(System.currentTimeMillis());

                reciboDao.declararPago(reciboId, nroOp, medio, rutaRelativaVoucher, fechaHoy);
                request.getSession().setAttribute("alertaSuccess", "Tu pago ha sido declarado con éxito. Esperando validación administrativa.");

            } catch (Exception e) {
                logger.error("Error al procesar la declaración de pago", e);
                request.getSession().setAttribute("alertaError", "Hubo un error al procesar el archivo del voucher.");
            }
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
        }
    }

    private Configuracion obtenerConfiguracionEmpresa() {
        Configuracion conf = new Configuracion();
        conf.setNombreCondominio("CONDOMINIO HABITECH SMART");
        conf.setRuc("20601234567");
        conf.setDireccion("Av. El Sol 1450, Lima");
        conf.setCuentaBancaria("BCP - Cta: 191-9843210-0-54 / CCI: 002-191009843210054032");
        return conf;
    }

    private String obtenerRutaAlmacenamiento() {
        String realPath = getServletContext().getRealPath("/");
        String uploadPath = "";

        if (realPath.contains("target")) {
            uploadPath = realPath.split("target")[0] + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads";
        } else if (realPath.contains("out")) {
            uploadPath = realPath.split("out")[0] + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads";
        } else if (realPath.contains(".metadata")) {
            uploadPath = realPath.split(".metadata")[0] + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads";
        } else {
            uploadPath = realPath + File.separator + "uploads";
        }
        return uploadPath;
    }
}