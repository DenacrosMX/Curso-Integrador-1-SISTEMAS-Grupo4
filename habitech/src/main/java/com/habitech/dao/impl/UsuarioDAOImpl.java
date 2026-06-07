package com.habitech.dao.impl;

import com.habitech.config.ConexionDB;
import com.habitech.dao.UsuarioDAO;
import com.habitech.model.UsuarioModel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public List<UsuarioModel> listarUsuarios() {
        List<UsuarioModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY apellidos ASC, nombres ASC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new UsuarioModel(
                        rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("nombres"), rs.getString("apellidos"), rs.getString("email"),
                        rs.getString("telefono"), rs.getString("rol"), rs.getString("estado")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean registrarUsuario(UsuarioModel usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombres, apellidos, email, telefono, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Generamos el Hash Salt seguro de BCrypt para la contraseña
            String passwordEncriptada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

            ps.setString(1, usuario.getUsername());
            ps.setString(2, passwordEncriptada);
            ps.setString(3, usuario.getNombres());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getTelefono());
            ps.setString(7, usuario.getRol());
            ps.setString(8, usuario.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean cambiarEstado(int idUsuario, String nuevoEstado) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // INTEGRACIÓN LOGIN: Valida credenciales contra firmas BCrypt y estado activo
    @Override
    public UsuarioModel autenticar(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND estado = 'ACTIVO'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashAlmacenado = rs.getString("password");

                    // BCrypt verifica si la clave en texto plano coincide con el hash seguro de la BD
                    if (BCrypt.checkpw(password, hashAlmacenado)) {
                        return new UsuarioModel(
                                rs.getInt("id"),
                                rs.getString("username"),
                                null, // Por seguridad no propagamos el hash en el objeto de sesión
                                rs.getString("nombres"),
                                rs.getString("apellidos"),
                                rs.getString("email"),
                                rs.getString("telefono"),
                                rs.getString("rol"),
                                rs.getString("estado")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si las credenciales fallan o el usuario está INACTIVO
    }
}