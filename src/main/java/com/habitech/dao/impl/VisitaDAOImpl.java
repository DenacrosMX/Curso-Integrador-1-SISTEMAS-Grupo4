package com.habitech.dao.impl;

import com.habitech.dao.VisitaDao;
import com.habitech.model.Visita;
import com.habitech.config.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitaDaoImpl implements VisitaDao {

    @Override
    public boolean registrarIngreso(Visita visita) {
        String sql = "INSERT INTO visitas (asignacion_id, conserje_id, nombre_visitante, dni_visitante, placa_vehiculo, tipo_ingreso) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, visita.getAsignacionId());
            if (visita.getConserjeId() != null) {
                ps.setInt(2, visita.getConserjeId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, visita.getNombreVisitante());
            ps.setString(4, visita.getDniVisitante());
            ps.setString(5, visita.getPlacaVehiculo());
            ps.setString(6, visita.getTipoIngreso());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registrarSalida(int id) {
        String sql = "UPDATE visitas SET fecha_hora_out = CURRENT_TIMESTAMP, estado = 'FINALIZADO' WHERE id = ? AND estado = 'EN_CURSO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean anularRegistro(int id) {
        String sql = "UPDATE visitas SET estado = 'ANULADO' WHERE id = ? AND estado = 'EN_CURSO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Visita> listarTodos() {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT v.*, " +
                "       a.codigo_unidad AS codigo_unidad_especifica, " +
                "       i.tipo_elemento, i.torre, i.nro_piso, " +
                "       u.nombres AS nombre_conserje " +
                "FROM visitas v " +
                "INNER JOIN asignaciones a ON v.asignacion_id = a.id " +
                "INNER JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id " +
                "LEFT JOIN usuarios u ON v.conserje_id = u.id " +
                "ORDER BY v.fecha_hora_ingreso DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Visita v = new Visita();
                v.setId(rs.getInt("id"));
                v.setAsignacionId(rs.getInt("asignacion_id"));

                int consId = rs.getInt("conserje_id");
                v.setConserjeId(rs.wasNull() ? null : consId);

                v.setNombreVisitante(rs.getString("nombre_visitante"));
                v.setDniVisitante(rs.getString("dni_visitante"));
                v.setPlacaVehiculo(rs.getString("placa_vehiculo"));
                v.setTipoIngreso(rs.getString("tipo_ingreso"));

                Timestamp ingreso = rs.getTimestamp("fecha_hora_ingreso");
                if (ingreso != null) {
                    v.setFechaHoraIngreso(ingreso.toLocalDateTime());
                }

                Timestamp salida = rs.getTimestamp("fecha_hora_out");
                if (salida != null) {
                    v.setFechaHoraOut(salida.toLocalDateTime());
                }

                v.setEstado(rs.getString("estado"));
                v.setCodigoUnidadEspecifica(rs.getString("codigo_unidad_especifica"));
                v.setNombreConserje(rs.getString("nombre_conserje"));

                int piso = rs.getInt("nro_piso");
                String textPiso = (piso < 0) ? "SÓTANO " + (piso * -1) : (piso == 0) ? "PLANTA BAJA" : "PISO " + piso;
                String detalle = rs.getString("torre") + " - " + textPiso + " (" + rs.getString("tipo_elemento") + ")";
                v.setDetalleInfraestructura(detalle);

                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}