package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ConfiguracionDao;
import com.habitech.model.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionDaoImpl implements ConfiguracionDao {

    @Override
    public boolean insertar(Configuracion config) {
        String sql = "INSERT INTO configuracion_maestra (nombre_condominio, direccion, ruc, cuenta_bancaria, dia_vencimiento_recibo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, config.getNombreCondominio());
            ps.setString(2, config.getDireccion());
            ps.setString(3, config.getRuc());
            ps.setString(4, config.getCuentaBancaria());
            ps.setInt(5, config.getDiaVencimientoRecibo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Configuracion> listarTodas() {
        List<Configuracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM configuracion_maestra WHERE estado = 'ACTIVO' ORDER BY id DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Configuracion c = new Configuracion();
                c.setId(rs.getInt("id"));
                c.setNombreCondominio(rs.getString("nombre_condominio"));
                c.setDireccion(rs.getString("direccion"));
                c.setRuc(rs.getString("ruc"));
                c.setCuentaBancaria(rs.getString("cuenta_bancaria"));
                c.setDiaVencimientoRecibo(rs.getInt("dia_vencimiento_recibo"));

                Timestamp ts = rs.getTimestamp("fecha_registro");
                if (ts != null) {
                    c.setFechaRegistro(ts.toInstant().atZone(ZoneId.systemDefault()));
                }

                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Configuracion obtenerPorId(int id) {
        Configuracion c = null;
        String sql = "SELECT * FROM configuracion_maestra WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Configuracion();
                    c.setId(rs.getInt("id"));
                    c.setNombreCondominio(rs.getString("nombre_condominio"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setRuc(rs.getString("ruc"));
                    c.setCuentaBancaria(rs.getString("cuenta_bancaria"));
                    c.setDiaVencimientoRecibo(rs.getInt("dia_vencimiento_recibo"));

                    Timestamp ts = rs.getTimestamp("fecha_registro");
                    if (ts != null) {
                        c.setFechaRegistro(ts.toInstant().atZone(ZoneId.systemDefault()));
                    }

                    c.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public boolean actualizar(Configuracion config) {
        String sql = "UPDATE configuracion_maestra SET nombre_condominio = ?, direccion = ?, ruc = ?, cuenta_bancaria = ?, dia_vencimiento_recibo = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, config.getNombreCondominio());
            ps.setString(2, config.getDireccion());
            ps.setString(3, config.getRuc());
            ps.setString(4, config.getCuentaBancaria());
            ps.setInt(5, config.getDiaVencimientoRecibo());
            ps.setInt(6, config.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE configuracion_maestra SET estado = 'INACTIVO' WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}