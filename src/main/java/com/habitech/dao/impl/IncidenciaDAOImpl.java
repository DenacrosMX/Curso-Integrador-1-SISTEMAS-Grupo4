package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.IncidenciaDao;
import com.habitech.model.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDaoImpl implements IncidenciaDao {

    @Override
    public boolean insertar(Incidencia incidencia) {
        String sql = "INSERT INTO incidencias (asignacion_id, conserje_id, titulo, descripcion, prioridad, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, incidencia.getAsignacionId());
            if (incidencia.getConserjeId() != null) {
                ps.setInt(2, incidencia.getConserjeId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, incidencia.getTitulo());
            ps.setString(4, incidencia.getDescription());
            ps.setString(5, incidencia.getPrioridad().toUpperCase().trim());
            ps.setString(6, incidencia.getEstado() != null ? incidencia.getEstado().toUpperCase().trim() : "ABIERTO");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Incidencia> listarTodas() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.asignacion_id, i.conserje_id, i.titulo, i.descripcion, i.prioridad, i.estado, i.fecha_reporte, i.fecha_cierre, "
                + "       (u.nombres || ' ' || u.apellidos) AS nombre_conserje "
                + "FROM incidencias i "
                + "LEFT JOIN usuarios u ON i.conserje_id = u.id "
                + "ORDER BY i.fecha_reporte DESC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId(rs.getInt("id"));
                i.setAsignacionId(rs.getInt("asignacion_id"));

                int conserjeId = rs.getInt("conserje_id");
                i.setConserjeId(rs.wasNull() ? null : conserjeId);

                i.setTitulo(rs.getString("titulo"));
                i.setDescription(rs.getString("descripcion"));
                i.setPrioridad(rs.getString("prioridad"));
                i.setEstado(rs.getString("estado"));

                Timestamp repTime = rs.getTimestamp("fecha_reporte");
                if (repTime != null) {
                    i.setFechaReporte(repTime.toInstant().atOffset(ZoneOffset.UTC));
                }

                Timestamp cieTime = rs.getTimestamp("fecha_cierre");
                if (cieTime != null) {
                    i.setFechaCierre(cieTime.toInstant().atOffset(ZoneOffset.UTC));
                }

                i.setNombreConserje(rs.getString("nombre_conserje") != null ? rs.getString("nombre_conserje") : "Sin Asignar");
                lista.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Incidencia obtenerPorId(int id) {
        String sql = "SELECT id, asignacion_id, conserje_id, titulo, descripcion, prioridad, estado, fecha_reporte, fecha_cierre FROM incidencias WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Incidencia i = new Incidencia();
                    i.setId(rs.getInt("id"));
                    i.setAsignacionId(rs.getInt("asignacion_id"));

                    int conserjeId = rs.getInt("conserje_id");
                    i.setConserjeId(rs.wasNull() ? null : conserjeId);

                    i.setTitulo(rs.getString("titulo"));
                    i.setDescription(rs.getString("descripcion"));
                    i.setPrioridad(rs.getString("prioridad"));
                    i.setEstado(rs.getString("estado"));

                    Timestamp repTime = rs.getTimestamp("fecha_reporte");
                    if (repTime != null) {
                        i.setFechaReporte(repTime.toInstant().atOffset(ZoneOffset.UTC));
                    }

                    Timestamp cieTime = rs.getTimestamp("fecha_cierre");
                    if (cieTime != null) {
                        i.setFechaCierre(cieTime.toInstant().atOffset(ZoneOffset.UTC));
                    }
                    return i;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean actualizarEstado(int id, String nuevoEstado, Integer conserjeId) {
        String estadoNorm = nuevoEstado.toUpperCase().trim();
        String sql;

        if ("RESUELTO".equals(estadoNorm) || "ANULADO".equals(estadoNorm)) {
            sql = "UPDATE incidencias SET estado = ?, conserje_id = ?, fecha_cierre = CURRENT_TIMESTAMP WHERE id = ?";
        } else {
            sql = "UPDATE incidencias SET estado = ?, conserje_id = ?, fecha_cierre = NULL WHERE id = ?";
        }

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estadoNorm);
            if (conserjeId != null) {
                ps.setInt(2, conserjeId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean asignarConserje(int id, int conserjeId) {
        String sql = "UPDATE incidencias SET conserje_id = ?, estado = 'EN_PROCESO' WHERE id = ? AND estado = 'ABIERTO'";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, conserjeId);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}