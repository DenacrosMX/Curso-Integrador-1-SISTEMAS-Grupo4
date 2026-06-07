package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.IncidenciaDAO;
import com.habitech.model.InmuebleModel;
import com.habitech.model.IncidenciaModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAOImpl implements IncidenciaDAO {

    @Override
    public List<IncidenciaModel> listarIncidencias() {
        List<IncidenciaModel> lista = new ArrayList<>();
        String sql = "SELECT inc.*, inm.bloque_torre, inm.nro_unidad " +
                "FROM incidencias inc " +
                "INNER JOIN inmuebles inm ON inc.inmueble_id = inm.id " +
                "ORDER BY CASE inc.estado WHEN 'ABIERTO' THEN 1 WHEN 'EN_PROCESO' THEN 2 ELSE 3 END, " +
                "CASE inc.prioridad WHEN 'ALTA' THEN 1 WHEN 'MEDIA' THEN 2 ELSE 3 END, inc.fecha_reporte DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                IncidenciaModel model = new IncidenciaModel(
                        rs.getInt("id"), rs.getInt("inmueble_id"), rs.getString("titulo"),
                        rs.getString("descripcion"), rs.getString("prioridad"), rs.getString("estado"),
                        rs.getTimestamp("fecha_reporte"), rs.getTimestamp("fecha_cierre"),
                        (Integer) rs.getObject("conserje_id")
                );

                InmuebleModel inm = new InmuebleModel();
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                inm.setNroUnidad(rs.getString("nro_unidad"));
                model.setInmueble(inm);

                lista.add(model);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean registrarIncidencia(IncidenciaModel incidencia) {
        String sql = "INSERT INTO incidencias (inmueble_id, titulo, descripcion, prioridad, estado) VALUES (?, ?, ?, ?, 'ABIERTO')";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, incidencia.getInmuebleId());
            ps.setString(2, incidencia.getTitulo());
            ps.setString(3, incidencia.getDescripcion());
            ps.setString(4, incidencia.getPrioridad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizarEstado(int idIncidencia, String nuevoEstado) {
        String sql = "UPDATE incidencias SET estado = ?, fecha_cierre = ? WHERE id = ?";
        Timestamp fechaCierre = "RESUELTO".equals(nuevoEstado) ? new Timestamp(System.currentTimeMillis()) : null;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setTimestamp(2, fechaCierre);
            ps.setInt(3, idIncidencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}