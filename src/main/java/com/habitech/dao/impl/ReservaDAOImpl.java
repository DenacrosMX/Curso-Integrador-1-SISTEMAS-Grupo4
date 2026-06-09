package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ReservaDao;
import com.habitech.model.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ReservaDaoImpl implements ReservaDao {

    @Override
    public boolean insertar(Reserva reserva) {
        String sql = "INSERT INTO reservas (usuario_id, inventario_maestro_id, fecha_reserva, turno, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reserva.getUsuarioId());
            ps.setInt(2, reserva.getInventarioMaestroId());
            ps.setObject(3, reserva.getFechaReserva()); // Inyección directa de LocalDate
            ps.setString(4, reserva.getTurno());
            ps.setString(5, reserva.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        // Query avanzada que cruza tablas para armar la grilla visual informativa
        // CORRECCIÓN EXACTA: Se cambia i.nombre_item por i.tipo_elemento basado en el DDL real
        String sql = "SELECT r.id, r.usuario_id, r.inventario_maestro_id, r.fecha_reserva, r.turno, r.estado, r.fecha_registro, "
                + "       (u.nombres || ' ' || u.apellidos) AS nombre_completo, i.tipo_elemento AS area_comun "
                + "FROM reservas r "
                + "LEFT JOIN usuarios u ON r.usuario_id = u.id "
                + "LEFT JOIN inventario_maestro_infraestructura i ON r.inventario_maestro_id = i.id "
                + "ORDER BY r.fecha_reserva DESC, r.turno ASC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setUsuarioId(rs.getInt("usuario_id"));
                r.setInventarioMaestroId(rs.getInt("inventario_maestro_id"));
                r.setFechaReserva(rs.getObject("fecha_reserva", LocalDate.class));
                r.setTurno(rs.getString("turno"));
                r.setEstado(rs.getString("estado"));

                Timestamp regTime = rs.getTimestamp("fecha_registro");
                if (regTime != null) {
                    r.setFechaRegistro(regTime.toInstant().atOffset(ZoneOffset.UTC));
                }

                // Seteamos los campos adicionales del JOIN
                r.setNombreUsuario(rs.getString("nombre_completo"));
                r.setNombreAreaComun(rs.getString("area_comun"));

                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Reserva obtenerPorId(int id) {
        String sql = "SELECT id, usuario_id, inventario_maestro_id, fecha_reserva, turno, estado, fecha_registro FROM reservas WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reserva r = new Reserva();
                    r.setId(rs.getInt("id"));
                    r.setUsuarioId(rs.getInt("usuario_id"));
                    r.setInventarioMaestroId(rs.getInt("inventario_maestro_id"));
                    r.setFechaReserva(rs.getObject("fecha_reserva", LocalDate.class));
                    r.setTurno(rs.getString("turno"));
                    r.setEstado(rs.getString("estado"));

                    Timestamp regTime = rs.getTimestamp("fecha_registro");
                    if (regTime != null) {
                        r.setFechaRegistro(regTime.toInstant().atOffset(ZoneOffset.UTC));
                    }
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean cancelarReserva(int id) {
        String sql = "UPDATE reservas SET estado = 'CANCELADA' WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existeReserva(int inventarioMaestroId, LocalDate fecha, String turno) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE inventario_maestro_id = ? AND fecha_reserva = ? AND turno = ? AND estado != 'CANCELADA'";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, inventarioMaestroId);
            ps.setObject(2, fecha);
            ps.setString(3, turno);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}