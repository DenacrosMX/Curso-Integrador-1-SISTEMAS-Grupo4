package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.InmuebleDAO;
import com.habitech.model.InmuebleModel;
import com.habitech.model.MaestroModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InmuebleDAOImpl implements InmuebleDAO {

    @Override
    public List<InmuebleModel> listarInmuebles() {
        List<InmuebleModel> lista = new ArrayList<>();
        String sql = "SELECT id, nro_unidad, bloque_torre, piso, tipo_unidad, estado_ocupacion FROM inmuebles ORDER BY bloque_torre ASC, piso ASC, nro_unidad ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new InmuebleModel(
                        rs.getInt("id"), rs.getString("nro_unidad"), rs.getString("bloque_torre"),
                        rs.getInt("piso"), rs.getString("tipo_unidad"), rs.getString("estado_ocupacion")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public int generarInventarioAutomatico(MaestroModel config) {
        String sql = "INSERT INTO inmuebles (nro_unidad, bloque_torre, piso, tipo_unidad, estado_ocupacion) VALUES (?, ?, ?, ?, 'VACANTE') ON CONFLICT DO NOTHING";
        int registrosInsertados = 0;

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // Activamos control de transacciones

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                // 1. Algoritmo de Autogeneración de Departamentos
                for (int t = 1; t <= config.getCantidadTorres(); t++) {
                    String nombreTorre = "Torre " + String.format("%02d", t); // Genera 'Torre 01', 'Torre 02'

                    for (int p = 1; p <= config.getPisosPorTorre(); p++) {
                        for (int d = 1; d <= config.getDptosPorPiso(); d++) {
                            // Construye nomenclatura estándar: Piso 4 + correlativo 2 = '402'
                            String nroUnidad = p + String.format("%02d", d);

                            ps.setString(1, nroUnidad);
                            ps.setString(2, nombreTorre);
                            ps.setInt(3, p);
                            ps.setString(4, "DEPARTAMENTO");
                            ps.addBatch(); // Se añade al lote de ejecución
                        }
                    }
                }

                // 2. Algoritmo de Autogeneración de Cocheras (Si se definieron en el maestro)
                if (config.getTotalCocheras() > 0) {
                    for (int c = 1; c <= config.getTotalCocheras(); c++) {
                        String nroCochera = "C-" + String.format("%03d", c); // Genera 'C-001', 'C-002'

                        ps.setString(1, nroCochera);
                        ps.setString(2, "Área General Estacionamientos");
                        ps.setInt(3, 1); // Ubicadas por defecto en nivel 1
                        ps.setString(4, "COCHERA");
                        ps.addBatch();
                    }
                }

                int[] resultados = ps.executeBatch();
                for (int res : resultados) {
                    if (res >= 0 || res == Statement.SUCCESS_NO_INFO) {
                        registrosInsertados++;
                    }
                }

                conn.commit(); // Consolidar en BD
            } catch (SQLException e) {
                conn.rollback(); // Cancelar todo ante cualquier anomalía
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return registrosInsertados;
    }

    @Override
    public boolean limpiarTodoElInventario() {
        String sql = "TRUNCATE TABLE inmuebles RESTART IDENTITY CASCADE";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}