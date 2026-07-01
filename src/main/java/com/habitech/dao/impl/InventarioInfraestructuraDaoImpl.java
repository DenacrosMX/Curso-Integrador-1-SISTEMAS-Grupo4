package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.InventarioInfraestructuraDao;
import com.habitech.model.InventarioInfraestructura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventarioInfraestructuraDaoImpl implements InventarioInfraestructuraDao {

    @Override
    public boolean insertar(InventarioInfraestructura elemento) {
        String sqlInfra = "INSERT INTO inventario_maestro_infraestructura (configuracion_maestra_id, tipo_elemento, torre, nro_piso, cantidad_registrada) VALUES (?, ?, ?, ?, ?)";
        String sqlUnidad = "INSERT INTO unidades_especificas (infraestructura_id, codigo_unidad) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement psInfra = null;
        PreparedStatement psUnidad = null;
        ResultSet rsKeys = null;

        try {
            con = ConexionDB.getConnection();
            con.setAutoCommit(false); // Iniciamos transacción manual por seguridad

            // Usamos Statement.RETURN_GENERATED_KEYS para capturar el ID que cree PostgreSQL
            psInfra = con.prepareStatement(sqlInfra, Statement.RETURN_GENERATED_KEYS);
            psInfra.setInt(1, elemento.getConfiguracionMaestraId());
            psInfra.setString(2, elemento.getTipoElemento());
            psInfra.setString(3, elemento.getTorre());
            psInfra.setInt(4, elemento.getNroPiso());
            psInfra.setInt(5, elemento.getCantidadRegistrada());

            int filasAfectadas = psInfra.executeUpdate();

            if (filasAfectadas > 0) {
                rsKeys = psInfra.getGeneratedKeys();
                if (rsKeys.next()) {
                    int idInfraestructuraGenerado = rsKeys.getInt(1);

                    psUnidad = con.prepareStatement(sqlUnidad);
                    int cantidad = elemento.getCantidadRegistrada();
                    int piso = elemento.getNroPiso();
                    String tipo = elemento.getTipoElemento();

                    // Generación automatizada de la nomenclatura por cada unidad individual
                    for (int i = 1; i <= cantidad; i++) {
                        String codigoGenerado = "";

                        if ("DEPARTAMENTO".equals(tipo)) {
                            if (piso < 0) {
                                // Sótanos: S101, S102, S201...
                                codigoGenerado = "S" + Math.abs(piso) + String.format("%02d", i);
                            } else if (piso == 0) {
                                // Planta baja: PB-01, PB-02...
                                codigoGenerado = "PB-" + String.format("%02d", i);
                            } else {
                                // Pisos estándar: DPTO-101, DPTO-102, DPTO-201...
                                codigoGenerado = "DPTO-" + (piso * 100 + i);
                            }
                        } else if ("COCHERA".equals(tipo)) {
                            String prefijoPiso = (piso < 0) ? "S" + Math.abs(piso) : "P" + piso;
                            codigoGenerado = "COCHERA-" + prefijoPiso + "-" + String.format("%02d", i);
                        } else {
                            // Demás estructuras: SALON_RECEPCION, GIMNASIO, PISCINA, DEPOSITO
                            String prefijoPiso = (piso < 0) ? "S" + Math.abs(piso) : "P" + piso;
                            codigoGenerado = tipo.substring(0, Math.min(tipo.length(), 5)) + "-" + prefijoPiso + "-" + String.format("%02d", i);
                        }

                        psUnidad.setInt(1, idInfraestructuraGenerado);
                        psUnidad.setString(2, codigoGenerado.toUpperCase());
                        psUnidad.addBatch(); // Se agrega al lote masivo
                    }

                    psUnidad.executeBatch(); // Se insertan todas las unidades hijas juntas
                }
            }

            con.commit(); // Confirmamos los cambios permanentemente en la Base de Datos
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Deshacemos todo si ocurre algún error para evitar datos corruptos
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Cierre seguro de recursos en orden inverso
            try {
                if (rsKeys != null) rsKeys.close();
                if (psInfra != null) psInfra.close();
                if (psUnidad != null) psUnidad.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<InventarioInfraestructura> listarTodo() {
        List<InventarioInfraestructura> lista = new ArrayList<>();
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
                i.setNombreCondominio(rs.getString("nombre_condominio"));
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