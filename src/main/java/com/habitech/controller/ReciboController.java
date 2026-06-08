package com.habitech.controller;

import com.habitech.dao.ReciboDAO;
import com.habitech.dao.impl.ReciboDAOImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/recibos")
public class ReciboController extends HttpServlet {
    private final ReciboDAO reciboDAO = new ReciboDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("generarFacturacion".equals(action)) {
            try {
                int mes = Integer.parseInt(request.getParameter("mes"));
                int anio = Integer.parseInt(request.getParameter("anio"));
                BigDecimal monto = new BigDecimal(request.getParameter("monto"));

                int creados = reciboDAO.emitirRecibosMasivos(mes, anio, monto);
                if (creados > 0) {
                    request.getSession().setAttribute("mensajeExito", "💰 Facturación procesada. Se emitieron " + creados + " recibos de mantenimiento.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se generaron recibos nuevos. Es posible que el periodo ya esté facturado.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error al procesar los datos de facturación.");
            }
        } else if ("pagar".equals(action)) {
            try {
                int idRecibo = Integer.parseInt(request.getParameter("idRecibo"));
                if (reciboDAO.cambiarEstadoPago(idRecibo, "PAGADO")) {
                    request.getSession().setAttribute("mensajeExito", "✅ Pago procesado y conciliado con éxito.");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=recibos");
    }
}