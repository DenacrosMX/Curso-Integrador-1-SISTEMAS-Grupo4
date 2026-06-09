package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.InventarioInfraestructuraDao;
import com.habitech.model.InventarioInfraestructura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioInfraestructuraDaoImpl implements InventarioInfraestructuraDao {

    @Override
    public boolean insertar(InventarioInfraestructura elemento) {
        String sql = "INSERT INTO inventario_maestro_infraestructura (configuracion_maestra_id, tipo_elemento, torre, nro_piso, cantidad_registrada) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, elemento.getConfiguracionMaestraId());
            ps.setString(2, elemento.getTipoElemento());
            ps.setString(3, elemento.getTorre());
            ps.setInt(4, elemento.getNroPiso());
            ps.setInt(5, elemento.getCantidadRegistrada());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<InventarioInfraestructura> listarTodo() {
        List<InventarioInfraestructura> lista = new ArrayList<>();
        // Hacemos JOIN para traer el nombre del condominio y mostrarlo en la tabla
        String sql = "SELECT i.*, c.nombre_condominio FROM inventario_maestro_infraestructura i " +
                "INNER JOIN configuracion_maestra c ON i.configuracion_maestra_id = c.id " +
                "WHERE i.estado = 'ACTIVO' ORDER BY i.id DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InventarioInfraestructura i = new InventarioInfraestructura();
                i.setId(rs.getInt("id"));
                i.setConfiguracionMaestraId(rs.getInt("configuracion_maestra_id"));
                i.setTipoElemento(rs.getString("tipo_elemento"));
                i.setTorre(rs.getString("torre"));
                i.setNroPiso(rs.getInt("nro_piso"));
                i.setCantidadRegistrada(rs.getInt("cantidad_registrada"));
                i.setEstado(rs.getString("estado"));
                i.setNombreCondominio(rs.getString("nombre_condominio")); // Atributo de apoyo
                lista.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public InventarioInfraestructura obtenerPorId(int id) {
        InventarioInfraestructura i = null;
        String sql = "SELECT * FROM inventario_maestro_infraestructura WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    i = new InventarioInfraestructura();
                    i.setId(rs.getInt("id"));
                    i.setConfiguracionMaestraId(rs.getInt("configuracion_maestra_id"));
                    i.setTipoElemento(rs.getString("tipo_elemento"));
                    i.setTorre(rs.getString("torre"));
                    i.setNroPiso(rs.getInt("nro_piso"));
                    i.setCantidadRegistrada(rs.getInt("cantidad_registrada"));
                    i.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public boolean actualizar(InventarioInfraestructura elemento) {
        String sql = "UPDATE inventario_maestro_infraestructura SET configuracion_maestra_id = ?, tipo_elemento = ?, torre = ?, nro_piso = ?, cantidad_registrada = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, elemento.getConfiguracionMaestraId());
            ps.setString(2, elemento.getTipoElemento());
            ps.setString(3, elemento.getTorre());
            ps.setInt(4, elemento.getNroPiso());
            ps.setInt(5, elemento.getCantidadRegistrada());
            ps.setInt(6, elemento.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE inventario_maestro_infraestructura SET estado = 'INACTIVO' WHERE id = ?";
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