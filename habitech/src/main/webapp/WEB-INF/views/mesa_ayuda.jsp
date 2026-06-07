<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.IncidenciaModel" %>
<%@ page import="com.habitech.model.InmuebleModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<IncidenciaModel> listaIncidencias = (List<IncidenciaModel>) request.getAttribute("listaIncidencias");
    List<InmuebleModel> listaInmueblesOrigen = (List<InmuebleModel>) request.getAttribute("listaInmueblesOrigen");
    String mensajeExito = (String) session.getAttribute("mensajeExito");
    String mensajeError = (String) session.getAttribute("mensajeError");
    session.removeAttribute("mensajeExito");
    session.removeAttribute("mensajeError");

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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

<div class="modulo-incidencias">
    <div class="header-modulo">
        <h2 class="titulo-modulo">🔧 Mesa de Ayuda y Control de Incidencias</h2>
        <p class="descripcion-modulo">Canal operativo enfocado en resolver fallas mecánicas, eléctricas o de infraestructura mediante asignación de prioridades.</p>
    </div>

    <div class="bloque-split">
        <div class="card-formulario">
            <h3>📝 Registrar Nuevo Ticket</h3>
            <form action="${pageContext.request.contextPath}/incidencias" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="reportar">

                <div class="grupo-campo">
                    <label>Unidad / Departamento Afectado:</label>
                    <select name="inmuebleId" required class="input-control">
                        <option value="">-- Seleccionar Unidad --</option>
                        <% if (listaInmueblesOrigen != null) {
                            for (InmuebleModel inm : listaInmueblesOrigen) { %>
                                <option value="<%= inm.getId() %>">
                                    <%= inm.getBloqueTorre() %> - <%= inm.getNroUnidad() %>
                                </option>
                            <% }
                        } %>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Asunto / Título Breve del Desperfecto:</label>
                    <input type="text" name="titulo" placeholder="Ej. Fuga de agua en sótano 2" maxlength="100" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Descripción Detallada del Problema:</label>
                    <textarea name="descripcion" placeholder="Describa pormenorizadamente el desperfecto detectado..." rows="4" required class="input-control text-area-control"></textarea>
                </div>

                <div class="grupo-campo">
                    <label>Prioridad Asignada:</label>
                    <select name="prioridad" required class="input-control">
                        <option value="BAJA">BAJA</option>
                        <option value="MEDIA">MEDIA</option>
                        <option value="ALTA">ALTA</option>
                    </select>
                </div>

                <button type="submit" class="btn-registrar-ticket">Abrir Ticket de Soporte</button>
            </form>
        </div>

        <div class="card-tabla-datos">
            <h3>📋 Historial de Tickets y Estado de Atención</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>ID Ticket</th>
                            <th>Ubicación</th>
                            <th>Desperfecto</th>
                            <th>Prioridad</th>
                            <th>Registro / Cierre</th>
                            <th>Estado</th>
                            <th>Acciones de Control</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaIncidencias != null && !listaIncidencias.isEmpty()) {
                            for (IncidenciaModel inc : listaIncidencias) {
                        %>
                                <tr>
                                    <td><code>#TK-<%= inc.getId() %></code></td>
                                    <td><strong><%= inc.getInmueble().getBloqueTorre() %> - <%= inc.getInmueble().getNroUnidad() %></strong></td>
                                    <td>
                                        <div class="ticket-info">
                                            <span class="tk-titulo"><%= inc.getTitulo() %></span>
                                            <span class="tk-desc"><%= inc.getDescripcion() %></span>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge-prioridad prio-<%= inc.getPrioridad().toLowerCase() %>">
                                            <%= inc.getPrioridad() %>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="fechas-info">
                                            <span>📅 In: <%= sdf.format(inc.getFechaReporte()) %></span>
                                            <span>🔒 Fin: <%= (inc.getFechaCierre() != null) ? sdf.format(inc.getFechaCierre()) : "<i class='pendiente'>No resuelto</i>" %></span>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge-estado-ticket est-<%= inc.getEstado().toLowerCase().replace("_", "") %>">
                                            <%= inc.getEstado() %>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="acciones-flujo">
                                            <% if ("ABIERTO".equals(inc.getEstado())) { %>
                                                <form action="${pageContext.request.contextPath}/incidencias" method="POST" style="margin:0;">
                                                    <input type="hidden" name="action" value="cambiarEstado">
                                                    <input type="hidden" name="idIncidencia" value="<%= inc.getId() %>">
                                                    <input type="hidden" name="nuevoEstado" value="EN_PROCESO">
                                                    <button type="submit" class="btn-flujo procesar">🛠️ Atender</button>
                                                </form>
                                            <% } else if ("EN_PROCESO".equals(inc.getEstado())) { %>
                                                <form action="${pageContext.request.contextPath}/incidencias" method="POST" style="margin:0;">
                                                    <input type="hidden" name="action" value="cambiarEstado">
                                                    <input type="hidden" name="idIncidencia" value="<%= inc.getId() %>">
                                                    <input type="hidden" name="nuevoEstado" value="RESUELTO">
                                                    <button type="submit" class="btn-flujo resolver">✅ Resolver</button>
                                                </form>
                                            <% } else { %>
                                                <span class="archivado-txt">🔒 Archivado</span>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="7" style="text-align: center; color: #64748b; padding: 30px;">No se registran tickets de incidencias en la mesa de ayuda.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>