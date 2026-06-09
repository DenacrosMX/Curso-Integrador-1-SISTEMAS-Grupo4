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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "BoletaController", urlPatterns = {"/boletas"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10) // Soporte nativo de subida de vouchers
public class BoletaController extends HttpServlet {

    private final ReciboDao reciboDao = new ReciboDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("descargarPdf".equals(accion)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Recibo recibo = reciboDao.obtenerPorId(id);

                // Jalar datos simulación de configuración global
                Configuracion conf = new Configuracion();
                conf.setNombreCondominio("Torres del Sol");
                conf.setRuc("20123456789");
                conf.setDireccion("Av. Principal 123, Lima");

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=Boleta_" + recibo.getNroComprobante() + ".pdf");

                GeneradorPdfBoleta.generarBoleta(recibo, conf, response.getOutputStream());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas&error=pdf");
                return;
            }
        }

        if ("validar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            HttpSession session = request.getSession();
            Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
            int adminId = (admin != null) ? admin.getId() : 1; // Respaldo por si navegas en local sin login estricto

            reciboDao.cambiarEstadoPago(id, "PAGADO", adminId);
            response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        int responsableId = (usuarioLogueado != null) ? usuarioLogueado.getId() : 1;

        if ("emitir".equals(accion)) {
            // Flujo del Admin: Crear el cobro base
            int residenteId = Integer.parseInt(request.getParameter("usuario_id"));
            int mes = Integer.parseInt(request.getParameter("mes"));
            int anio = Integer.parseInt(request.getParameter("anio"));
            BigDecimal montoMantenimiento = new BigDecimal(request.getParameter("monto_base"));

            Recibo r = new Recibo();
            r.setUsuarioId(residenteId);
            r.setUsuarioResponsableId(responsableId);
            r.setMesFacturado(mes);
            r.setAnioFacturado(anio);
            r.setTotalAPagar(montoMantenimiento);

            // Primer concepto obligatorio del detalle
            r.getDetalles().add(new DetalleRecibo("Cuota Mensual de Mantenimiento Estándar", montoMantenimiento));

            // Concepto opcional si el admin añade un extra (Áreas comunes / Multas)
            String extraDesc = request.getParameter("concepto_extra");
            String extraMontoStr = request.getParameter("monto_extra");
            if (extraDesc != null && !extraDesc.trim().isEmpty() && extraMontoStr != null) {
                BigDecimal montoExtra = new BigDecimal(extraMontoStr);
                if (montoExtra.compareTo(BigDecimal.ZERO) > 0) {
                    r.getDetalles().add(new DetalleRecibo(extraDesc.trim(), montoExtra));
                    r.setTotalAPagar(r.getTotalAPagar().add(montoExtra)); // Sumar al gran total del maestro
                }
            }

            reciboDao.insertarConDetalles(r);

        } else if ("declararPago".equals(accion)) {
            // Flujo del Inquilino: Subir voucher y registrar transferencia
            int reciboId = Integer.parseInt(request.getParameter("recibo_id"));
            String nroOp = request.getParameter("nro_operacion");
            String medio = request.getParameter("medio_pago");

            // Subida física del archivo voucher usando las librerías del POM
            Part filePart = request.getPart("voucher_file");
            String fileName = "voucher_" + reciboId + "_" + System.currentTimeMillis() + ".jpg";
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            filePart.write(uploadPath + File.separator + fileName);
            String rutaRelativaVoucher = "uploads/" + fileName;

            java.sql.Date fechaHoy = new java.sql.Date(System.currentTimeMillis());
            reciboDao.declararPago(reciboId, nroOp, medio, rutaRelativaVoucher, fechaHoy);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=boletas");
    }
}