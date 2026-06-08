<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.MaestroModel" %>
<%@ page import="java.util.List" %>

<%
    // Recuperar modelo para el formulario (edición o limpio)
    MaestroModel configMaestra = (MaestroModel) request.getAttribute("configMaestra");
    if (configMaestra == null) {
        configMaestra = new MaestroModel();
    }

    // Recuperar la lista completa para la tabla del historial
    List<MaestroModel> historial = (List<MaestroModel>) request.getAttribute("historialMaestro");

    // Gestión de mensajes de feedback de sesión
    String mensajeExito = (String) session.getAttribute("mensajeExito");
    String mensajeError = (String) session.getAttribute("mensajeError");
    session.removeAttribute("mensajeExito");
    session.removeAttribute("mensajeError");
%>

<% if (mensajeExito != null) { %>
    <div class="alert alert-success"><%= mensajeExito %></div>
<% } %>
<% if (mensajeError != null) { %>
    <div class="alert alert-danger"><%= mensajeError %></div>
<% } %>

<div class="modulo-maestro">
    <h1 class="titulo-modulo">⚙️ Configuración Maestra del Condominio</h1>
    <p class="descripcion-modulo">
        Gestione la identidad legal y las variables estructurales base del complejo residencial. Use la tabla inferior para auditar, editar o remover registros del historial.
    </p>

    <div class="card-formulario-ancho">
        <h3><%= (configMaestra.getId() > 0) ? "📝 Editando Registro #" + configMaestra.getId() : "🚀 Insertar Nuevo Registro" %></h3>

        <form action="${pageContext.request.contextPath}/maestro" method="POST">
            <input type="hidden" name="id" value="<%= configMaestra.getId() %>">

            <div class="form-row-triple">
                <div class="form-group">
                    <label>Nombre del Condominio:</label>
                    <input type="text" name="nombreCondominio" value="<%= configMaestra.getNombreCondominio() != null ? configMaestra.getNombreCondominio() : "" %>" required maxlength="100">
                </div>
                <div class="form-group">
                    <label>Dirección Física:</label>
                    <input type="text" name="direccion" value="<%= configMaestra.getDireccion() != null ? configMaestra.getDireccion() : "" %>" required maxlength="150">
                </div>
                <div class="form-group">
                    <label>RUC (11 dígitos):</label>
                    <input type="text" name="ruc" value="<%= configMaestra.getRuc() != null ? configMaestra.getRuc() : "" %>" required maxlength="11" pattern="\d{11}">
                </div>
            </div>

            <div class="form-row-cuadruple">
                <div class="form-group">
                    <label>Cantidad de Torres:</label>
                    <input type="number" name="cantidadTorres" value="<%= configMaestra.getCantidadTorres() > 0 ? configMaestra.getCantidadTorres() : "" %>" required min="1">
                </div>
                <div class="form-group">
                    <label>Pisos por Torre:</label>
                    <input type="number" name="pisosPorTorre" value="<%= configMaestra.getPisosPorTorre() > 0 ? configMaestra.getPisosPorTorre() : "" %>" required min="1">
                </div>
                <div class="form-group">
                    <label>Dptos por Piso:</label>
                    <input type="number" name="dptosPorPiso" value="<%= configMaestra.getDptosPorPiso() > 0 ? configMaestra.getDptosPorPiso() : "" %>" required min="1">
                </div>
                <div class="form-group">
                    <label>Total Cocheras:</label>
                    <input type="number" name="totalCocheras" value="<%= configMaestra.getTotalCocheras() > 0 ? configMaestra.getTotalCocheras() : "" %>" required min="0">
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar">
                    <%= (configMaestra.getId() > 0) ? "💾 Actualizar Cambios" : "➕ Registrar Parámetro" %>
                </button>
                <% if (configMaestra.getId() > 0) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?view=maestro" class="btn-cancelar">Cancelar Edición</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="card-historial-abajo">
        <h3>📊 Historial General de Parámetros de Configuración</h3>
        <div class="tabla-responsive-container">
            <table class="tabla-maestra-panoramica">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Nombre del Condominio</th>
                        <th>RUC</th>
                        <th>Dirección Registrada</th>
                        <th style="text-align: center;">Estructura Física Base</th>
                        <th style="text-align: center; width: 120px;">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (historial != null && !historial.isEmpty()) {
                            for (MaestroModel item : historial) {
                    %>
                        <tr class="<%= (item.getId() == configMaestra.getId()) ? "fila-activo-edit" : "" %>">
                            <td><b class="id-resaltado">#<%= item.getId() %></b></td>
                            <td><span class="nombre-principal"><%= item.getNombreCondominio() %></span></td>
                            <td><code class="ruc-codigo"><%= item.getRuc() %></code></td>
                            <td><span class="direccion-texto"><%= item.getDireccion() != null ? item.getDireccion() : "" %></span></td>
                            <td style="text-align: center;">
                                <span class="tag-metrica">🏢 <%= item.getCantidadTorres() %> Torres</span>
                                <span class="tag-metrica">↕️ <%= item.getPisosPorTorre() %> Pisos</span>
                                <span class="tag-metrica">🚪 <%= item.getDptosPorPiso() %> Dptos/Piso</span>
                                <span class="tag-metrica">🚗 <%= item.getTotalCocheras() %> Cocheras</span>
                            </td>
                            <td class="celda-operaciones">
                                <a href="${pageContext.request.contextPath}/dashboard?view=maestro&idEdit=<%= item.getId() %>" class="btn-op editar" title="Editar parámetros">✏️</a>
                                <a href="${pageContext.request.contextPath}/maestro?action=delete&id=<%= item.getId() %>" class="btn-op eliminar" onclick="return confirm('¿Está seguro de que desea eliminar permanentemente el registro #<%= item.getId() %>?');" title="Eliminar del historial">❌</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="6" class="tabla-sin-datos">No existen configuraciones previas almacenadas. Use el formulario superior para registrar la primera.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>