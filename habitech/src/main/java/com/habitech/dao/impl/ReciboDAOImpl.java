package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ReciboDAO;
import com.habitech.model.AsignacionModel;
import com.habitech.model.InmuebleModel;
import com.habitech.model.ReciboModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReciboDAOImpl implements ReciboDAO {

    @Override
    public List<ReciboModel> listarRecibos() {
        List<ReciboModel> lista = new ArrayList<>();
        String sql = "SELECT r.*, a.nombre_residente, i.nro_unidad, i.bloque_torre " +
                "FROM recibos r " +
                "INNER JOIN asignaciones a ON r.asignacion_id = a.id " +
                "INNER JOIN inmuebles i ON a.inmueble_id = i.id " +
                "ORDER BY r.anio_facturado DESC, r.mes_facturado DESC, r.id DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReciboModel rec = new ReciboModel(
                        rs.getInt("id"), rs.getInt("asignacion_id"), rs.getInt("mes_facturado"),
                        rs.getInt("anio_facturado"), rs.getBigDecimal("monto_mantenimiento"),
                        rs.getDate("fecha_emision"), rs.getString("estado_pago")
                );

                AsignacionModel asig = new AsignacionModel();
                asig.setNombreResidente(rs.getString("nombre_residente"));

                InmuebleModel inm = new InmuebleModel();
                inm.setNroUnidad(rs.getString("nro_unidad"));
                inm.setBloqueTorre(rs.getString("bloque_torre"));
                asig.setInmueble(inm);

                rec.setAsignacion(asig);
                lista.add(rec);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public int emitirRecibosMasivos(int mes, int anio, BigDecimal montoEstandar) {
        String sqlAsignados = "SELECT id FROM asignaciones";
        String sqlInsertRecibo = "INSERT INTO recibos (asignacion_id, mes_facturado, anio_facturado, monto_mantenimiento) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        int generados = 0;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement psSel = conn.prepareStatement(sqlAsignados);
             ResultSet rs = psSel.executeQuery();
             PreparedStatement psIns = conn.prepareStatement(sqlInsertRecibo)) {

            conn.setAutoCommit(false);
            while (rs.next()) {
                psIns.setInt(1, rs.getInt("id"));
                psIns.setInt(2, mes);
                psIns.setInt(3, anio);
                psIns.setBigDecimal(4, montoEstandar);
                psIns.addBatch();
            }

            int[] resultados = psIns.executeBatch();
            for (int res : resultados) {
                if (res >= 0 || res == Statement.SUCCESS_NO_INFO) generados++;
            }
            conn.commit();
        } catch (SQLException e) { e.printStackTrace(); }
        return generados;
    }

    @Override
    public boolean cambiarEstadoPago(int idRecibo, String nuevoEstado) {
        String sql = "UPDATE recibos SET estado_pago = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idRecibo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}