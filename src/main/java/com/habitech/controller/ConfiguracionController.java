package com.habitech.controller;

import com.habitech.dao.ConfiguracionDao;
import com.habitech.dao.impl.ConfiguracionDaoImpl;
import com.habitech.model.Configuracion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/configuracion")
public class ConfiguracionController extends HttpServlet {

    private final ConfiguracionDao configuracionDao = new ConfiguracionDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Captura baja lógica si viene por GET de la tabla de historial
        String accion = request.getParameter("accion");
        if ("eliminar".equals(accion)) {
            int idEliminar = Integer.parseInt(request.getParameter("id"));
            configuracionDao.eliminarLogico(idEliminar);
        }
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=configuracion");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String nombreCondominio = request.getParameter("nombreCondominio");
        String direccion = request.getParameter("direccion");
        String ruc = request.getParameter("ruc");
        String cuentaBancaria = request.getParameter("cuentaBancaria");
        String diaVencimientoStr = request.getParameter("diaVencimientoRecibo");

        Configuracion config = new Configuracion();
        config.setNombreCondominio(nombreCondominio);
        config.setDireccion(direccion);
        config.setRuc(ruc);
        config.setCuentaBancaria(cuentaBancaria);
        config.setDiaVencimientoRecibo(Integer.parseInt(diaVencimientoStr));

        if (idStr == null || idStr.trim().isEmpty()) {
            // Nuevo Registro
            configuracionDao.insertar(config);
        } else {
            // Actualización de registro existente
            config.setId(Integer.parseInt(idStr));
            configuracionDao.actualizar(config);
        }

        // Redirección limpia al Dashboard para ver el historial actualizado
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=configuracion");
    }
}