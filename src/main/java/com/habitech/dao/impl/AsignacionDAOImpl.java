package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.AsignacionDAO;
import com.habitech.model.AsignacionModel;
import com.habitech.model.InmuebleModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAOImpl implements AsignacionDAO {

    @Override
    public List<AsignacionModel> listarAsignaciones() {
        List<AsignacionModel> lista = new ArrayList<>();
        String sql = "SELECT a.*, i.nro_unidad, i.bloque_torre, i.tipo_unidad " +
                "FROM asignaciones a INNER JOIN inmuebles i ON a.inmueble_id = i.id " +
                "ORDER BY a.id DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AsignacionModel asig = new AsignacionModel(
                        rs.getInt("id"), rs.getInt("inmueble_id"), rs.getString("nombre_residente"),
                        rs.getString("documento_identidad"), rs.getString("tipo_adquisicion"), rs.getDate("fecha_ingreso")
                );
                InmuebleModel inm = new InmuebleModel();
                inm.setNroUnidad(rs.getString("nro_unidad"));
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                inm.setTipoUnidad(rs.getString("tipo_unidad"));
                asig.setInmueble(inm);
                lista.add(asig);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public List<InmuebleModel> listarInmueblesVacantes() {
        List<InmuebleModel> lista = new ArrayList<>();
        String sql = "SELECT id, nro_unidad, bloque_torre, tipo_unidad FROM inmuebles WHERE estado_ocupacion = 'VACANTE' ORDER BY bloque_torre, nro_unidad";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InmuebleModel inm = new InmuebleModel();
                inm.setId(rs.getInt("id"));
                inm.setNroUnidad(rs.getString("nro_unidad"));
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                inm.setTipoUnidad(rs.getString("tipo_unidad"));
                lista.add(inm);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean registrarAsignacion(AsignacionModel asignacion) {
        String sqlInsert = "INSERT INTO asignaciones (inmueble_id, nombre_residente, documento_identidad, tipo_adquisicion, fecha_ingreso) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateInmueble = "UPDATE inmuebles SET estado_ocupacion = 'OCUPADO' WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                 PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateInmueble)) {

                psInsert.setInt(1, asignacion.getInmuebleId());
                psInsert.setString(2, asignacion.getNombreResidente());
                psInsert.setString(3, asignacion.getDocumentoIdentidad());
                psInsert.setString(4, asignacion.getTipoAdquisicion());
                psInsert.setDate(5, asignacion.getFechaIngreso());
                psInsert.executeUpdate();

                psUpdate.setInt(1, asignacion.getInmuebleId());
                psUpdate.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminarAsignacion(int idAsignacion, int inmuebleId) {
        String sqlDelete = "DELETE FROM asignaciones WHERE id = ?";
        String sqlUpdateInmueble = "UPDATE inmuebles SET estado_ocupacion = 'VACANTE' WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
                 PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateInmueble)) {

                psDelete.setInt(1, idAsignacion);
                psDelete.executeUpdate();

                psUpdate.setInt(1, inmuebleId);
                psUpdate.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}