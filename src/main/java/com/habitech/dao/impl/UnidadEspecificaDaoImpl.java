package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.UnidadEspecificaDao;
import com.habitech.model.UnidadEspecifica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnidadEspecificaDaoImpl implements UnidadEspecificaDao {

    @Override
    public List<UnidadEspecifica> listarDisponiblesPorInfraestructura(int infraestructuraId) {
        List<UnidadEspecifica> lista = new ArrayList<>();
        String sql = "SELECT * FROM unidades_especificas " +
                "WHERE infraestructura_id = ? AND estado_ocupacion = 'DISPONIBLE' AND estado = 'ACTIVO' " +
                "ORDER BY codigo_unidad ASC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, infraestructuraId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UnidadEspecifica u = new UnidadEspecifica();
                    u.setId(rs.getInt("id"));
                    u.setInfraestructuraId(rs.getInt("infraestructura_id"));
                    u.setCodigoUnidad(rs.getString("codigo_unidad"));
                    u.setEstadoOcupacion(rs.getString("estado_ocupacion"));
                    u.setEstado(rs.getString("estado"));
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizarEstadoOcupacion(int id, String nuevoEstado) {
        String sql = "UPDATE unidades_especificas SET estado_ocupacion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}