package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ReciboDao;
import com.habitech.model.Recibo;
import com.habitech.model.DetalleRecibo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReciboDaoImpl implements ReciboDao {

    @Override
    public boolean insertarConDetalles(Recibo recibo) {
        Connection conn = null;
        PreparedStatement psRecibo = null;
        PreparedStatement psDetalle = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            String sqlCorrelativo = "SELECT COALESCE(MAX(id), 0) + 1 FROM recibos";
            int siguienteId = 1;
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlCorrelativo)) {
                if (rs.next()) {
                    siguienteId = rs.getInt(1);
                }
            }
            String nroComprobante = String.format("#BP-%d-%04d", recibo.getAnioFacturado(), siguienteId);
            recibo.setNroComprobante(nroComprobante);

            String sqlMaestro = "INSERT INTO recibos (nro_comprobante, usuario_id, usuario_responsable_id, mes_facturado, anio_facturado, total_a_pagar, estado_pago) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'PENDIENTE')";
            psRecibo = conn.prepareStatement(sqlMaestro, Statement.RETURN_GENERATED_KEYS);
            psRecibo.setString(1, recibo.getNroComprobante());
            psRecibo.setInt(2, recibo.getUsuarioId());
            psRecibo.setInt(3, recibo.getUsuarioResponsableId());
            psRecibo.setInt(4, recibo.getMesFacturado());
            psRecibo.setInt(5, recibo.getAnioFacturado());
            psRecibo.setBigDecimal(6, recibo.getTotalAPagar());
            psRecibo.executeUpdate();

            int reciboId = 0;
            try (ResultSet rsKeys = psRecibo.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    reciboId = rsKeys.getInt(1);
                }
            }

            String sqlDetalle = "INSERT INTO detalle_recibos (recibo_id, concepto_descripcion, monto_individual) VALUES (?, ?, ?)";
            psDetalle = conn.prepareStatement(sqlDetalle);
            for (DetalleRecibo det : recibo.getDetalles()) {
                psDetalle.setInt(1, reciboId);
                psDetalle.setString(2, det.getConceptoDescripcion());
                psDetalle.setBigDecimal(3, det.getMontoIndividual());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psDetalle != null) {
                    psDetalle.close();
                }
                if (psRecibo != null) {
                    psRecibo.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Recibo> listarTodo() {
        return ejecutarListado("SELECT r.*, u.nombres AS nombre_residente, adm.nombres AS nombre_admin, " +
                "i.torre, i.nro_piso, i.tipo_elemento, a.codigo_unidad " +
                "FROM recibos r " +
                "INNER JOIN usuarios u ON r.usuario_id = u.id " +
                "INNER JOIN usuarios adm ON r.usuario_responsable_id = adm.id " +
                "LEFT JOIN asignaciones a ON a.usuario_id = u.id AND a.estado = 'ACTIVO' " +
                "LEFT JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id " +
                "ORDER BY r.anio_facturado DESC, r.mes_facturado DESC, r.id DESC");
    }

    @Override
    public List<Recibo> listarPorInquilino(int usuarioId) {
        return ejecutarListado("SELECT r.*, u.nombres AS nombre_residente, adm.nombres AS nombre_admin, " +
                "i.torre, i.nro_piso, i.tipo_elemento, a.codigo_unidad " +
                "FROM recibos r " +
                "INNER JOIN usuarios u ON r.usuario_id = u.id " +
                "INNER JOIN usuarios adm ON r.usuario_responsable_id = adm.id " +
                "LEFT JOIN asignaciones a ON a.usuario_id = u.id AND a.estado = 'ACTIVO' " +
                "LEFT JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id " +
                "WHERE r.usuario_id = " + usuarioId + " " +
                "ORDER BY r.anio_facturado DESC, r.mes_facturado DESC, r.id DESC");
    }

    private List<Recibo> ejecutarListado(String query) {
        List<Recibo> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Recibo r = mappearRecibo(rs);
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Recibo obtenerPorId(int id) {
        String sql = "SELECT r.*, u.nombres AS nombre_residente, adm.nombres AS nombre_admin, " +
                "i.torre, i.nro_piso, i.tipo_elemento, a.codigo_unidad " +
                "FROM recibos r " +
                "INNER JOIN usuarios u ON r.usuario_id = u.id " +
                "INNER JOIN usuarios adm ON r.usuario_responsable_id = adm.id " +
                "LEFT JOIN asignaciones a ON a.usuario_id = u.id AND a.estado = 'ACTIVO' " +
                "LEFT JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id " +
                "WHERE r.id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Recibo r = mappearRecibo(rs);
                    r.setDetalles(obtenerDetalles(r.getId()));
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DetalleRecibo> obtenerDetalles(int reciboId) throws SQLException {
        List<DetalleRecibo> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_recibos WHERE recibo_id = ? ORDER BY id ASC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reciboId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleRecibo d = new DetalleRecibo();
                    d.setId(rs.getInt("id"));
                    d.setReciboId(rs.getInt("recibo_id"));
                    d.setConceptoDescripcion(rs.getString("concepto_descripcion"));
                    d.setMontoIndividual(rs.getBigDecimal("monto_individual"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    @Override
    public boolean declararPago(int reciboId, String nroOp, String medio, String rutaVoucher, java.sql.Date fechaPago) {
        String sql = "UPDATE recibos SET nro_operacion = ?, medio_pago = ?, archivo_voucher = ?, fecha_pago = ?, estado_pago = 'VALIDANDO' WHERE id = ? AND estado_pago = 'PENDIENTE'";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nroOp);
            ps.setString(2, medio);
            ps.setString(3, rutaVoucher);
            ps.setDate(4, fechaPago);
            ps.setInt(5, reciboId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cambiarEstadoPago(int reciboId, String nuevoEstado, int adminId) {
        String sql = "UPDATE recibos SET estado_pago = ?, usuario_responsable_id = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, adminId);
            ps.setInt(3, reciboId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Recibo mappearRecibo(ResultSet rs) throws SQLException {
        Recibo r = new Recibo();
        r.setId(rs.getInt("id"));
        r.setNroComprobante(rs.getString("nro_comprobante"));
        r.setUsuarioId(rs.getInt("usuario_id"));
        r.setUsuarioResponsableId(rs.getInt("usuario_responsable_id"));
        r.setMesFacturado(rs.getInt("mes_facturado"));
        r.setAnioFacturado(rs.getInt("anio_facturado"));
        r.setTotalAPagar(rs.getBigDecimal("total_a_pagar"));
        r.setFechaEmision(rs.getDate("fecha_emision"));
        r.setEstadoPago(rs.getString("estado_pago"));
        r.setNroOperacion(rs.getString("nro_operacion"));
        r.setMedioPago(rs.getString("medio_pago"));
        r.setArchivoVoucher(rs.getString("archivo_voucher"));
        r.setFechaPago(rs.getDate("fecha_pago"));
        r.setNombreResidente(rs.getString("nombre_residente"));
        r.setNombreAdmin(rs.getString("nombre_admin"));

        String torre = rs.getString("torre");
        if (torre != null) {
            int piso = rs.getInt("nro_piso");
            String textPiso = (piso < 0) ? "SÓTANO " + (piso * -1) : (piso == 0) ? "PLANTA BAJA" : "PISO " + piso;
            r.setDetalleUnidad(rs.getString("codigo_unidad") + " (" + torre + " - " + textPiso + ")");
        } else {
            r.setDetalleUnidad("Sin Unidad Asignada");
        }
        return r;
    }
}