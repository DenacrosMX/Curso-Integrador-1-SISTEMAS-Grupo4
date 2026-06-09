package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.ComunicadoDao;
import com.habitech.model.Comunicado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ComunicadoDaoImpl implements ComunicadoDao {

    @Override
    public boolean insertar(Comunicado comunicado) {
        String sql = "INSERT INTO comunicados (usuario_id, titulo, contenido, alcance, torre_destino, categoria, estado, fecha_expiracion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, comunicado.getUsuarioId());
            ps.setString(2, comunicado.getTitulo());
            ps.setString(3, comunicado.getContenido());
            ps.setString(4, comunicado.getAlcance());
            ps.setString(5, comunicado.getTorreDestino()); // Puede ser null si el alcance es GLOBAL
            ps.setString(6, comunicado.getCategoria());
            ps.setString(7, comunicado.getEstado());

            if (comunicado.getFechaExpiracion() != null) {
                ps.setTimestamp(8, Timestamp.from(comunicado.getFechaExpiracion().toInstant()));
            } else {
                ps.setNull(8, java.sql.Types.TIMESTAMP_WITH_TIMEZONE);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comunicado> listarTodos() {
        List<Comunicado> lista = new ArrayList<>();
        String sql = "SELECT id, usuario_id, titulo, contenido, alcance, torre_destino, categoria, estado, fecha_publicacion, fecha_expiracion "
                + "FROM comunicados ORDER BY fecha_publicacion DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearComunicado(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Comunicado obtenerPorId(int id) {
        String sql = "SELECT id, usuario_id, titulo, contenido, alcance, torre_destino, categoria, estado, fecha_publicacion, fecha_expiracion "
                + "FROM comunicados WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComunicado(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean actualizar(Comunicado comunicado) {
        String sql = "UPDATE comunicados SET titulo = ?, contenido = ?, alcance = ?, torre_destino = ?, categoria = ?, estado = ?, fecha_expiracion = ? "
                + "WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, comunicado.getTitulo());
            ps.setString(2, comunicado.getContenido());
            ps.setString(3, comunicado.getAlcance());
            ps.setString(4, comunicado.getTorreDestino());
            ps.setString(5, comunicado.getCategoria());
            ps.setString(6, comunicado.getEstado());

            if (comunicado.getFechaExpiracion() != null) {
                ps.setTimestamp(7, Timestamp.from(comunicado.getFechaExpiracion().toInstant()));
            } else {
                ps.setNull(7, java.sql.Types.TIMESTAMP_WITH_TIMEZONE);
            }
            ps.setInt(8, comunicado.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE comunicados SET estado = 'OCULTO' WHERE id = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comunicado> listarActivosParaResidentes(String torreResidente) {
        List<Comunicado> lista = new ArrayList<>();
        // Query optimizada: Trae comunicados PUBLICADOS cuya fecha de expiración sea nula o futura,
        // y que tengan alcance GLOBAL o coincidan con la torre específica del residente.
        String sql = "SELECT id, usuario_id, titulo, contenido, alcance, torre_destino, categoria, estado, fecha_publicacion, fecha_expiracion "
                + "FROM comunicados "
                + "WHERE estado = 'PUBLICADO' "
                + "AND (fecha_expiracion IS NULL OR fecha_expiracion > CURRENT_TIMESTAMP) "
                + "AND (alcance = 'GLOBAL' OR torre_destino = ?) "
                + "ORDER BY fecha_publicacion DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, torreResidente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearComunicado(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método auxiliar privado para no repetir mapeo de columnas con tipos OffsetDateTime modernos
    private Comunicado mapearComunicado(ResultSet rs) throws SQLException {
        Timestamp pubTime = rs.getTimestamp("fecha_publicacion");
        Timestamp expTime = rs.getTimestamp("fecha_expiracion");

        OffsetDateTime fechaPub = pubTime != null ? pubTime.toInstant().atOffset(ZoneOffset.UTC) : null;
        OffsetDateTime fechaExp = expTime != null ? expTime.toInstant().atOffset(ZoneOffset.UTC) : null;

        return new Comunicado(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getString("titulo"),
                rs.getString("contenido"),
                rs.getString("alcance"),
                rs.getString("torre_destino"),
                rs.getString("categoria"),
                rs.getString("estado"),
                fechaPub,
                fechaExp
        );
    }
}