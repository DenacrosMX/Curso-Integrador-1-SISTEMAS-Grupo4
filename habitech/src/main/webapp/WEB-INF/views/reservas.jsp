<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.ReservaModel" %>
<%@ page import="com.habitech.model.InmuebleModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<ReservaModel> listaReservas = (List<ReservaModel>) request.getAttribute("listaReservas");
    List<InmuebleModel> listaInmueblesSolicitantes = (List<InmuebleModel>) request.getAttribute("listaInmueblesSolicitantes");
    String mensajeExito = (String) session.getAttribute("mensajeExito");
    String mensajeError = (String) session.getAttribute("mensajeError");
    session.removeAttribute("mensajeExito");
    session.removeAttribute("mensajeError");

    SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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

<div class="modulo-reservas">
    <div class="header-modulo">
        <h2 class="titulo-modulo">📆 Agenda y Reservas de Áreas Comunes</h2>
        <p class="descripcion-modulo">Evite colisiones de horarios controlando el usufructo de los espacios comunes del condominio bajo bloques de tiempo estrictos.</p>
    </div>

    <div class="bloque-split">
        <div class="card-formulario">
            <h3>📝 Programar Nueva Separación</h3>
            <form action="${pageContext.request.contextPath}/reservas" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="reservar">

                <div class="grupo-campo">
                    <label>Unidad Habitacional del Solicitante:</label>
                    <select name="inmuebleId" required class="input-control">
                        <option value="">-- Seleccionar Vivienda --</option>
                        <% if (listaInmueblesSolicitantes != null) {
                            for (InmuebleModel inm : listaInmueblesSolicitantes) { %>
                                <option value="<%= inm.getId() %>">
                                    <%= inm.getBloqueTorre() %> - <%= inm.getNroUnidad() %>
                                </option>
                            <% }
                        } %>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Espacio Común a Solicitar:</label>
                    <select name="areaComun" required class="input-control">
                        <option value="PARRILLA">🍖 PARRILLA</option>
                        <option value="SALON_EVENTOS">🎉 SALÓN DE EVENTOS</option>
                        <option value="GIMNASIO">🏋️ GIMNASIO</option>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Fecha del Evento / Uso:</label>
                    <input type="date" name="fechaReserva" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Bloque Horario / Turno:</label>
                    <select name="turno" required class="input-control">
                        <option value="MAÑANA">🌅 MAÑANA (08:00 AM - 02:00 PM)</option>
                        <option value="NOCHE">🌃 NOCHE (04:00 PM - 10:00 PM)</option>
                    </select>
                </div>

                <button type="submit" class="btn-registrar-reserva">Confirmar Separación</button>
            </form>
        </div>

        <div class="card-tabla-datos">
            <h3>📋 Agenda Consolidada de Eventos</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Espacio Común</th>
                            <th>Residente Solicitante</th>
                            <th>Fecha Evento</th>
                            <th>Turno Asignado</th>
                            <th>Registro Auditoría</th>
                            <th>Operación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaReservas != null && !listaReservas.isEmpty()) {
                            for (ReservaModel r : listaReservas) {
                        %>
                                <tr>
                                    <td><code>#RES-<%= r.getId() %></code></td>
                                    <td>
                                        <span class="badge-area area-<%= r.getAreaComun().toLowerCase() %>">
                                            <%= r.getAreaComun().replace("_", " ") %>
                                        </span>
                                    </td>
                                    <td><strong><%= r.getInmueble().getBloqueTorre() %> - <%= r.getInmueble().getNroUnidad() %></strong></td>
                                    <td><span class="fecha-destacada">📅 <%= sdfFecha.format(r.getFechaReserva()) %></span></td>
                                    <td>
                                        <span class="badge-turno turno-<%= r.getTurno().toLowerCase() %>">
                                            <%= r.getTurno() %>
                                        </span>
                                    </td>
                                    <td><small class="text-muted"><%= sdfRegistro.format(r.getFechaRegistro()) %></small></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/reservas" method="POST" style="margin:0;" onsubmit="return confirm('¿Está seguro de que desea liberar esta fecha en la agenda?');">
                                            <input type="hidden" name="action" value="cancelar">
                                            <input type="hidden" name="idReserva" value="<%= r.getId() %>">
                                            <button type="submit" class="btn-cancelar-reserva">Anular</button>
                                        </form>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="7" style="text-align: center; color: #64748b; padding: 30px;">No existen separaciones registradas en la agenda de áreas comunes.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>