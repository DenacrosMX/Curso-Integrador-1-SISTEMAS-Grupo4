<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.UsuarioModel" %>
<%@ page import="java.util.List" %>

<%
    List<UsuarioModel> listaUsuarios = (List<UsuarioModel>) request.getAttribute("listaUsuarios");
    String mensajeExito = (String) session.getAttribute("mensajeExito");
    String mensajeError = (String) session.getAttribute("mensajeError");
    session.removeAttribute("mensajeExito");
    session.removeAttribute("mensajeError");
%>

<% if (mensajeExito != null) { %>
    <div class="alert alert-success" style="padding: 12px; margin-bottom: 15px; background-color: #d1e7dd; color: #0f5132; border-radius: 4px; font-weight: 600;">
        <%= mensajeExito %>
    </div>
<% } %>
<% if (mensajeError != null) { %>
    <div class="alert alert-danger" style="padding: 12px; margin-bottom: 15px; background-color: #f8d7da; color: #842029; border-radius: 4px; font-weight: 600;">
        <%= mensajeError %>
    </div>
<% } %>

<div class="modulo-usuarios">
    <div class="header-modulo">
        <h2 class="titulo-modulo">👤 Control de Personal, Residentes y Privilegios (RBAC)</h2>
        <p class="descripcion-modulo">Alta de operadores del sistema con encriptación adaptativa de credenciales y asignación de niveles de jerarquía.</p>
    </div>

    <div class="bloque-split">
        <div class="card-formulario">
            <h3>📝 Registrar Operador / Residente</h3>
            <form action="${pageContext.request.contextPath}/usuarios" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="registrar">

                <div class="grupo-campo">
                    <label>Nombre de Usuario (DNI de Identidad):</label>
                    <input type="text" name="username" placeholder="Ej. 47589632" required class="input-control" maxlength="20">
                </div>

                <div class="grupo-campo">
                    <label>Contraseña de Acceso:</label>
                    <input type="password" name="password" placeholder="••••••••" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Nombres Completos:</label>
                    <input type="text" name="nombres" placeholder="Ej. Carlos Juan" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Apellidos Completas:</label>
                    <input type="text" name="apellidos" placeholder="Ej. Mendoza Roca" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Correo Electrónico Corporativo/Personal:</label>
                    <input type="email" name="email" placeholder="carlos@habitech.com" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Número Telefónico:</label>
                    <input type="text" name="telefono" placeholder="+51 987654321" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Rol Funcional del Sistema:</label>
                    <select name="rol" required class="input-control">
                        <option value="ADMIN_SISTEMA">🔑 ADMINISTRADOR DEL SISTEMA</option>
                        <option value="CONSERJE">🛂 PERSONAL DE CONSERJERÍA</option>
                        <option value="RESIDENTE">🏠 RESIDENTE / PROPIETARIO</option>
                    </select>
                </div>

                <button type="submit" class="btn-registrar-usuario">Guardar Usuario Seguro</button>
            </form>
        </div>

        <div class="card-tabla-datos">
            <h3>📋 Padrón de Usuarios del Ecosistema</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>DNI / Usuario</th>
                            <th>Nombres y Apellidos</th>
                            <th>Contacto</th>
                            <th>Firma Password</th>
                            <th>Rol Asignado</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                            for (UsuarioModel u : listaUsuarios) {
                        %>
                                <tr>
                                    <td><code><%= u.getUsername() %></code></td>
                                    <td><strong><%= u.getApellidos() %>, <%= u.getNombres() %></strong></td>
                                    <td>
                                        <div class="contacto-box">
                                            <span>✉️ <%= u.getEmail() %></span>
                                            <span>📞 <%= u.getTelefono() %></span>
                                        </div>
                                    </td>
                                    <td><span class="badge-hash">🔒 BCrypt Hash</span></td>
                                    <td>
                                        <span class="badge-rol rol-<%= u.getRol().toLowerCase() %>">
                                            <%= u.getRol().replace("_", " ") %>
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge-estado est-<%= u.getEstado().toLowerCase() %>">
                                            <%= u.getEstado() %>
                                        </span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/usuarios" method="POST" style="margin:0;">
                                            <input type="hidden" name="idUsuario" value="<%= u.getId() %>">
                                            <% if ("ACTIVO".equals(u.getEstado())) { %>
                                                <input type="hidden" name="nuevoEstado" value="INACTIVO">
                                                <button type="submit" name="action" value="cambiarEstado" class="btn-toggle-status desactivar">Bloquear</button>
                                            <% } else { %>
                                                <input type="hidden" name="nuevoEstado" value="ACTIVO">
                                                <button type="submit" name="action" value="cambiarEstado" class="btn-toggle-status activar">Habilitar</button>
                                            <% } %>
                                        </form>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="7" style="text-align: center; color: #64748b; padding: 30px;">No se registran usuarios habilitados.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>