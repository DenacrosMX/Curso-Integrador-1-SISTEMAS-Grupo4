<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Incidencia" %>
<%@ page import="com.habitech.model.Asignacion" %>
<%@ page import="com.habitech.model.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    List<Incidencia> listaIncidencias = (List<Incidencia>) request.getAttribute("incidencias");
    List<Asignacion> listaAsignaciones = (List<Asignacion>) request.getAttribute("asignaciones");
    String alertaError = (String) request.getAttribute("alertaError");

    // Formateador estricto para fechas con estampa de tiempo
    DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Capturamos el usuario en sesión para validar permisos de atención
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
    String rolUsuario = (usuarioSesion != null && usuarioSesion.getRol() != null) ? usuarioSesion.getRol().trim().toUpperCase() : "";
%>

<div class="modulo-container">

    <% if (alertaError != null) { %>
        <div class="alerta-error">
            ⚠️ <strong>Error en Operación:</strong> <%= alertaError %>
        </div>
    <% } %>

    <div class="card-formulario">
        <h2>Reportar Nueva Incidencia o Avería</h2>
        <form action="${pageContext.request.contextPath}/incidencias" method="POST">
            <div class="form-grid">
                <div class="form-group full-width">
                    <label>Título Corto del Problema</label>
                    <input type="text" name="titulo" placeholder="Ej. Filtración de agua en sótano / Ascensor averiado" required maxlength="100">
                </div>

                <div class="form-group">
                    <label>Ubicación / Unidad Afectada</label>
                    <select name="asignacion_id" required>
                        <option value="">-- Seleccione la Ubicación --</option>
                        <%
                            if (listaAsignaciones != null) {
                                for (Asignacion asig : listaAsignaciones) {
                        %>
                                    <option value="<%= asig.getId() %>">
                                        <%= asig.getDetalleInfraestructura() != null ? asig.getDetalleInfraestructura() : "Unidad #" + asig.getId() %>
                                        <%-- CORREGIDO: Se reemplaza getCodigoUnidadEspecifica() por getCodigoUnidad() --%>
                                        <% if (asig.getCodigoUnidad() != null && !asig.getCodigoUnidad().isEmpty()) { %>
                                            (<%= asig.getCodigoUnidad() %>)
                                        <% } %>
                                        [<%= asig.getNombreUsuario() != null ? asig.getNombreUsuario() : "Sin Residente" %>]
                                    </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Prioridad Inicial</label>
                    <select name="prioridad" required>
                        <option value="BAJA">Baja (Mantenimiento rutinario)</option>
                        <option value="MEDIA" selected>Media (Afecta confort)</option>
                        <option value="ALTA">Alta (Urgencia técnica / Seguridad)</option>
                    </select>
                </div>

                <div class="form-group full-width">
                    <label>Descripción Detallada del Suceso</label>
                    <textarea name="descripcion" rows="4" placeholder="Describa claramente los hechos observados para que el personal pueda atenderlo..." required></textarea>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar">Emitir Reporte de Incidencia</button>
            </div>
        </form>
    </div>

    <div class="card-historial">
        <h2>Panel de Monitoreo y Seguimiento Técnico</h2>
        <div class="tabla-responsive">
            <table class="tabla-incidencias">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha Reporte</th>
                        <th>Incidencia</th>
                        <th>Prioridad</th>
                        <th>Estado</th>
                        <th>Encargado (Conserje)</th>
                        <th>Fecha Cierre</th>
                        <th>Acciones de Control</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaIncidencias != null && !listaIncidencias.isEmpty()) {
                            for (Incidencia inc : listaIncidencias) {
                                String estNorm = inc.getEstado() != null ? inc.getEstado().trim().toUpperCase() : "ABIERTO";
                                String priNorm = inc.getPrioridad() != null ? inc.getPrioridad().trim().toUpperCase() : "MEDIA";
                    %>
                            <tr>
                                <td>#<%= inc.getId() %></td>
                                <td><%= inc.getFechaReporte() != null ? inc.getFechaReporte().format(formFecha) : "-" %></td>
                                <td>
                                    <div class="detalle-incidencia">
                                        <strong><%= inc.getTitulo() %></strong>
                                        <p><%= inc.getDescription() %></p>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge-prioridad <%= priNorm.toLowerCase() %>">
                                        <%= priNorm %>
                                    </span>
                                </td>
                                <td>
                                    <span class="badge-estado <%= estNorm.toLowerCase() %>">
                                        <%= estNorm.replace("_", " ") %>
                                    </span>
                                </td>
                                <td>
                                    <span class="nombre-conserje">
                                        👨‍✈️ <%= inc.getNombreConserje() != null && !inc.getNombreConserje().isEmpty() ? inc.getNombreConserje() : "Sin Asignar" %>
                                    </span>
                                </td>
                                <td><%= inc.getFechaCierre() != null ? inc.getFechaCierre().format(formFecha) : "<em class='abierto'>Activa</em>" %></td>
                                <td>
                                    <div class="acciones-flex">
                                        <%
                                            // Flexibilización de roles: Verifica si contiene ADMIN (ej: ADMIN_SISTEMA) o es CONSERJE
                                            boolean esAdmin = rolUsuario.contains("ADMIN");
                                            boolean esConserje = rolUsuario.equals("CONSERJE");

                                            if ("ABIERTO".equals(estNorm) && (esAdmin || esConserje)) {
                                        %>
                                            <a href="${pageContext.request.contextPath}/incidencias?accion=atender&id=<%= inc.getId() %>"
                                               class="btn-accion btn-atender" title="Tomar Incidencia">🛠️ Atender</a>
                                        <% } %>

                                        <% if ("EN_PROCESO".equals(estNorm) && (esAdmin || esConserje)) { %>
                                            <a href="${pageContext.request.contextPath}/incidencias?accion=resolver&id=<%= inc.getId() %>"
                                               class="btn-accion btn-resolver" title="Marcar como Resuelto">✅ Resolver</a>
                                        <% } %>

                                        <% if (("ABIERTO".equals(estNorm) || "EN_PROCESO".equals(estNorm)) && esAdmin) { %>
                                            <a href="${pageContext.request.contextPath}/incidencias?accion=anular&id=<%= inc.getId() %>"
                                               class="btn-accion btn-anular" title="Anular"
                                               onclick="return confirm('¿Seguro que deseas anular este reporte por completo?');">🚫 Anular</a>
                                        <% } %>

                                        <% if ("RESUELTO".equals(estNorm) || "ANULADO".equals(estNorm)) { %>
                                            <span class="texto-bloqueado">Bloqueado</span>
                                        <% } %>

                                        <%-- Soporte UI: Si no posee un rol de gestión, se le avisa explícitamente --%>
                                        <% if (!esAdmin && !esConserje) { %>
                                            <span class="texto-bloqueado">Solo Lectura</span>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="8" class="text-center">No hay incidencias reportadas en el sistema. Todo se encuentra en orden.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>