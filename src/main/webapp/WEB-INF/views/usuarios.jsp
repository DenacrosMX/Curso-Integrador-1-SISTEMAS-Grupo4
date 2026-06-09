<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Usuario" %>
<%@ page import="java.util.List" %>
<%
    // Detectamos si el controlador nos envió un usuario para editar
    Usuario usuarioEdit = (Usuario) request.getAttribute("usuarioSeleccionado");
    boolean esEdicion = (usuarioEdit != null);

    // Obtenemos la lista de usuarios para el historial
    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("usuarios");
%>

<div class="modulo-container">

    <div class="card-formulario">
        <h2><%= esEdicion ? "Modificar Usuario" : "Registrar Nuevo Usuario" %></h2>

        <form action="${pageContext.request.contextPath}/usuarios" method="POST">
            <% if(esEdicion) { %>
                <input type="hidden" name="id" value="<%= usuarioEdit.getId() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-group">
                    <label>Nombre de Usuario (Username)</label>
                    <input type="text" name="username" required value="<%= esEdicion ? usuarioEdit.getUsername() : "" %>">
                </div>

                <div class="form-group">
                    <label>Nombres</label>
                    <input type="text" name="nombres" required value="<%= esEdicion ? usuarioEdit.getNombres() : "" %>">
                </div>

                <div class="form-group">
                    <label>Apellidos</label>
                    <input type="text" name="apellidos" required value="<%= esEdicion ? usuarioEdit.getApellidos() : "" %>">
                </div>

                <div class="form-group">
                    <label>Correo Electrónico</label>
                    <input type="email" name="email" required value="<%= esEdicion ? usuarioEdit.getEmail() : "" %>">
                </div>

                <div class="form-group">
                    <label>Teléfono</label>
                    <input type="text" name="telefono" value="<%= (esEdicion && usuarioEdit.getTelefono() != null) ? usuarioEdit.getTelefono() : "" %>">
                </div>

                <div class="form-group">
                    <label>Rol de Usuario</label>
                    <select name="rol" required>
                        <option value="RESIDENTE" <%= (esEdicion && "RESIDENTE".equals(usuarioEdit.getRol())) ? "selected" : "" %>>Residente</option>
                        <option value="CONSERJE" <%= (esEdicion && "CONSERJE".equals(usuarioEdit.getRol())) ? "selected" : "" %>>Conserje</option>
                        <option value="ADMIN_SISTEMA" <%= (esEdicion && "ADMIN_SISTEMA".equals(usuarioEdit.getRol())) ? "selected" : "" %>>Administrador de Sistema</option>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar"><%= esEdicion ? "Actualizar" : "Guardar Registro" %></button>
                <% if(esEdicion) { %>
                    <a href="${pageContext.request.contextPath}/usuarios" class="btn-cancelar">Cancelar Edición</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="card-historial">
        <h2>Historial de Usuarios Registrados</h2>

        <div class="table-responsive">
            <table class="tabla-usuarios">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Nombre Completo</th>
                        <th>Email</th>
                        <th>Teléfono</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                        for (Usuario u : listaUsuarios) { %>
                            <tr>
                                <td><%= u.getId() %></td>
                                <td><strong><%= u.getUsername() %></strong></td>
                                <td><%= u.getNombres() + " " + u.getApellidos() %></td>
                                <td><%= u.getEmail() %></td>
                                <td><%= u.getTelefono() != null ? u.getTelefono() : "-" %></td>
                                <td><span class="badge-rol <%= u.getRol().toLowerCase() %>"><%= u.getRol() %></span></td>
                                <td><span class="badge-estado activo"><%= u.getEstado() %></span></td>
                                <td>
                                    <div class="acciones-flex">
                                        <a href="${pageContext.request.contextPath}/dashboard?modulo=usuarios&accion=editar&id=<%= u.getId() %>" class="btn-accion edit" title="Editar">✏️</a>
                                        <a href="${pageContext.request.contextPath}/usuarios?accion=eliminar&id=<%= u.getId() %>" class="btn-accion delete" title="Desactivar" onclick="return confirm('¿Seguro que deseas desactivar este usuario?');">🗑️</a>
                                    </div>
                                </td>
                            </tr>
                        <% }
                    } else { %>
                        <tr>
                            <td colspan="8" class="text-center">No hay usuarios activos registrados actualmente.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>