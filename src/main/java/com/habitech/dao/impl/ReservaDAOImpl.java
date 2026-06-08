package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ReservaDAO;
import com.habitech.model.InmuebleModel;
import com.habitech.model.ReservaModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {

    @Override
    public List<ReservaModel> listarReservas() {
        List<ReservaModel> lista = new ArrayList<>();
        String sql = "SELECT r.*, i.bloque_torre, i.nro_unidad FROM reservas r " +
                "INNER JOIN inmuebles i ON r.inmueble_id = i.id " +
                "ORDER BY r.fecha_reserva ASC, r.turno DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservaModel res = new ReservaModel(
                        rs.getInt("id"), rs.getInt("inmueble_id"), rs.getString("area_comun"),
                        rs.getDate("fecha_reserva"), rs.getString("turno"), rs.getTimestamp("fecha_registro")
                );
                InmuebleModel inm = new InmuebleModel();
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                inm.setNroUnidad(rs.getString("nro_unidad"));
                res.setInmueble(inm);
                lista.add(res);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean verificarDisponibilidad(String area, Date fecha, String turno) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE area_comun = ? AND fecha_reserva = ? AND turno = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, area);
            ps.setDate(2, fecha);
            ps.setString(3, turno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) == 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean registrarReserva(ReservaModel reserva) {
        String sql = "INSERT INTO reservas (inmueble_id, area_comun, fecha_reserva, turno) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reserva.getInmuebleId());
            ps.setString(2, reserva.getAreaComun());
            ps.setDate(3, reserva.getFechaReserva());
            ps.setString(4, reserva.getTurno());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminarReserva(int idReserva) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}