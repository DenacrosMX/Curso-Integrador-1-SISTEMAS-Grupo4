package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.VisitaDAO;
import com.habitech.model.InmuebleModel;
import com.habitech.model.VisitaModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitaDAOImpl implements VisitaDAO {

    @Override
    public List<VisitaModel> listarVisitasRecientes() {
        List<VisitaModel> lista = new ArrayList<>();
        String sql = "SELECT v.*, i.nro_unidad, i.bloque_torre, i.tipo_unidad " +
                "FROM visitas v " +
                "INNER JOIN inmuebles i ON v.inmueble_id = i.id " +
                "ORDER BY v.estado DESC, v.fecha_hora_ingreso DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VisitaModel vis = new VisitaModel(
                        rs.getInt("id"), rs.getInt("inmueble_id"), (Integer) rs.getObject("conserje_id"),
                        rs.getString("nombre_visitante"), rs.getString("dni_visitante"),
                        rs.getString("placa_vehiculo"), rs.getString("tipo_ingreso"),
                        rs.getTimestamp("fecha_hora_ingreso"), rs.getTimestamp("fecha_hora_out"),
                        rs.getString("estado")
                );

                InmuebleModel inm = new InmuebleModel();
                inm.setNroUnidad(rs.getString("nro_unidad"));
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                inm.setTipoUnidad(rs.getString("tipo_unidad"));
                vis.setInmueble(inm);

                lista.add(vis);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public List<InmuebleModel> listarTodosLosInmuebles() {
        List<InmuebleModel> lista = new ArrayList<>();
        String sql = "SELECT id, bloque_torre, nro_unidad, tipo_unidad FROM inmuebles ORDER BY bloque_torre, nro_unidad";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InmuebleModel i = new InmuebleModel();
                i.setId(rs.getInt("id"));
                i.setBloqueTorre(rs.getString("bloque_torre"));
                i.setNroUnidad(rs.getString("nro_unidad"));
                i.setTipoUnidad(rs.getString("tipo_unidad"));
                lista.add(i);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean registrarIngreso(VisitaModel visita) {
        String sql = "INSERT INTO visitas (inmueble_id, nombre_visitante, dni_visitante, placa_vehiculo, tipo_ingreso) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, visita.getInmuebleId());
            ps.setString(2, visita.getNombreVisitante());
            ps.setString(3, visita.getDniVisitante());
            ps.setString(4, (visita.getPlacaVehiculo() == null || visita.getPlacaVehiculo().isEmpty()) ? null : visita.getPlacaVehiculo());
            ps.setString(5, visita.getTipoIngreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean registrarSalida(int idVisita) {
        String sql = "UPDATE visitas SET fecha_hora_out = CURRENT_TIMESTAMP, estado = 'FINALIZADO' WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVisita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}