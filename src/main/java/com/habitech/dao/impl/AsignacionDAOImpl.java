package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.AsignacionDao;
import com.habitech.model.Asignacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsignacionDaoImpl implements AsignacionDao {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionDaoImpl.class);

    @Override
    public boolean insertar(Asignacion asignacion) {
        String sql = "INSERT INTO asignaciones (usuario_id, inventario_maestro_id, codigo_unidad, "
                + "tipo_adquisicion, precio_mensual_pactado, estado, fecha_ingreso, fecha_salida) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getUsuarioId());
            ps.setInt(2, asignacion.getInventarioMaestroId());
            ps.setString(3, asignacion.getCodigoUnidad());
            ps.setString(4, asignacion.getTipoAdquisicion());
            ps.setBigDecimal(5, asignacion.getPrecioMensualPactado());
            ps.setString(6, asignacion.getEstado() != null ? asignacion.getEstado() : "ACTIVO");
            ps.setDate(7, asignacion.getFechaIngreso() != null ? asignacion.getFechaIngreso() : new java.sql.Date(System.currentTimeMillis()));
            ps.setDate(8, asignacion.getFechaSalida());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error al insertar asignación", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Asignacion> listarTodas() {
        List<Asignacion> lista = new ArrayList<>();
        String sql = "SELECT a.*, u.nombres AS nombre_usuario, i.tipo_elemento, i.torre, i.nro_piso "
                + "FROM asignaciones a "
                + "INNER JOIN usuarios u ON a.usuario_id = u.id "
                + "INNER JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id "
                + "ORDER BY a.estado ASC, a.id DESC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mappearAsignacion(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar asignaciones", e);
        }
        return lista;
    }

    @Override
    public Asignacion obtenerPorId(int id) {
        String sql = "SELECT a.*, u.nombres AS nombre_usuario, i.tipo_elemento, i.torre, i.nro_piso "
                + "FROM asignaciones a "
                + "INNER JOIN usuarios u ON a.usuario_id = u.id "
                + "INNER JOIN inventario_maestro_infraestructura i ON a.inventario_maestro_id = i.id "
                + "WHERE a.id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappearAsignacion(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener asignación por ID", e);
        }
        return null;
    }

    @Override
    public boolean actualizar(Asignacion asignacion) {
        String sql = "UPDATE asignaciones SET usuario_id = ?, inventario_maestro_id = ?, "
                + "codigo_unidad = ?, tipo_adquisicion = ?, precio_mensual_pactado = ?, "
                + "estado = ?, fecha_ingreso = ?, fecha_salida = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getUsuarioId());
            ps.setInt(2, asignacion.getInventarioMaestroId());
            ps.setString(3, asignacion.getCodigoUnidad()); // Actualización manual directa
            ps.setString(4, asignacion.getTipoAdquisicion());
            ps.setBigDecimal(5, asignacion.getPrecioMensualPactado());
            ps.setString(6, asignacion.getEstado());
            ps.setDate(7, asignacion.getFechaIngreso());
            ps.setDate(8, asignacion.getFechaSalida());
            ps.setInt(9, asignacion.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error al actualizar asignación", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean finalizarAsignacion(int id) {
        String sql = "UPDATE asignaciones SET estado = 'FINALIZADO', fecha_salida = CURRENT_DATE WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error al finalizar lógicamente la asignación con ID: {}", id, e);
            return false;
        }
    }

    private Asignacion mappearAsignacion(ResultSet rs) throws SQLException {
        Asignacion a = new Asignacion();
        a.setId(rs.getInt("id"));
        a.setUsuarioId(rs.getInt("usuario_id"));
        a.setInventarioMaestroId(rs.getInt("inventario_maestro_id"));
        a.setCodigoUnidad(rs.getString("codigo_unidad"));
        a.setTipoAdquisicion(rs.getString("tipo_adquisicion"));
        a.setPrecioMensualPactado(rs.getBigDecimal("precio_mensual_pactado"));
        a.setEstado(rs.getString("estado"));
        a.setFechaIngreso(rs.getDate("fecha_ingreso"));
        a.setFechaSalida(rs.getDate("fecha_salida"));
        a.setNombreUsuario(rs.getString("nombre_usuario"));

        int piso = rs.getInt("nro_piso");
        String textPiso = (piso < 0) ? "SÓTANO " + (piso * -1) : (piso == 0) ? "PLANTA BAJA" : "PISO " + piso;
        String detalle = rs.getString("torre") + " - " + textPiso + " (" + rs.getString("tipo_elemento") + ")";
        a.setDetalleInfraestructura(detalle);

        return a;
    }
}