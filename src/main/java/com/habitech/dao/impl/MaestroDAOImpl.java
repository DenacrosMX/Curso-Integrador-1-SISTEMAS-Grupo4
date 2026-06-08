package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.MaestroDAO;
import com.habitech.model.MaestroModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaestroDAOImpl implements MaestroDAO {

    @Override
    public List<MaestroModel> listarHistorial() {
        List<MaestroModel> lista = new ArrayList<>();
        String sql = "SELECT id, nombre_condominio, direccion, ruc, cantidad_torres, pisos_por_torre, dptos_por_piso, total_cocheras FROM configuracion_maestra ORDER BY id DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new MaestroModel(
                        rs.getInt("id"), rs.getString("nombre_condominio"), rs.getString("direccion"),
                        rs.getString("ruc"), rs.getInt("cantidad_torres"), rs.getInt("pisos_por_torre"),
                        rs.getInt("dptos_por_piso"), rs.getInt("total_cocheras")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public MaestroModel obtenerPorId(int id) {
        String sql = "SELECT * FROM configuracion_maestra WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MaestroModel(
                            rs.getInt("id"), rs.getString("nombre_condominio"), rs.getString("direccion"),
                            rs.getString("ruc"), rs.getInt("cantidad_torres"), rs.getInt("pisos_por_torre"),
                            rs.getInt("dptos_por_piso"), rs.getInt("total_cocheras")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean insertar(MaestroModel m) {
        String sql = "INSERT INTO configuracion_maestra(nombre_condominio, direccion, ruc, cantidad_torres, pisos_por_torre, dptos_por_piso, total_cocheras) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            mapearPs(ps, m);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(MaestroModel m) {
        String sql = "UPDATE configuracion_maestra SET nombre_condominio=?, direccion=?, ruc=?, cantidad_torres=?, pisos_por_torre=?, dptos_por_piso=?, total_cocheras=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            mapearPs(ps, m);
            ps.setInt(8, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM configuracion_maestra WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private void mapearPs(PreparedStatement ps, MaestroModel m) throws SQLException {
        ps.setString(1, m.getNombreCondominio());
        ps.setString(2, m.getDireccion());
        ps.setString(3, m.getRuc());
        ps.setInt(4, m.getCantidadTorres());
        ps.setInt(5, m.getPisosPorTorre());
        ps.setInt(6, m.getDptosPorPiso());
        ps.setInt(7, m.getTotalCocheras());
    }
}