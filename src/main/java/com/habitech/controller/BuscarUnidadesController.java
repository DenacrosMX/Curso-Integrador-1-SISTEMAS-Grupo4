package com.habitech.controller;

import com.habitech.dao.UnidadEspecificaDao;
import com.habitech.dao.impl.UnidadEspecificaDaoImpl;
import com.habitech.model.UnidadEspecifica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BuscarUnidadesController", urlPatterns = {"/api/unidades-disponibles"})
public class BuscarUnidadesController extends HttpServlet {

    private final UnidadEspecificaDao unidadEspecificaDao = new UnidadEspecificaDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuramos la respuesta como JSON en formato UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String infraIdParam = request.getParameter("infraestructuraId");
        PrintWriter out = response.getWriter();

        try {
            if (infraIdParam != null && !infraIdParam.trim().isEmpty()) {
                int infraestructuraId = Integer.parseInt(infraIdParam);

                // Consultamos a la base de datos las sub-unidades que estén libres
                List<UnidadEspecifica> unidades = unidadEspecificaDao.listarDisponiblesPorInfraestructura(infraestructuraId);

                // Construimos manualmente una cadena JSON ligera para no depender de librerías externas (como Gson/Jackson)
                StringBuilder json = new StringBuilder();
                json.append("[");
                for (int i = 0; i < unidades.size(); i++) {
                    UnidadEspecifica u = unidades.get(i);
                    json.append("{");
                    json.append("\"id\":").append(u.getId()).append(",");
                    json.append("\"codigoUnidad\":\"").append(u.getCodigoUnidad()).append("\"");
                    json.append("}");
                    if (i < unidades.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");

                out.print(json.toString());
            } else {
                out.print("[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Error al procesar los identificadores de infraestructura\"}");
        } finally {
            out.flush();
        }
    }
}