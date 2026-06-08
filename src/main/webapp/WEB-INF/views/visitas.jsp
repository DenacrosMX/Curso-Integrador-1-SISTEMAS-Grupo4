<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.VisitaModel" %>
<%@ page import="com.habitech.model.InmuebleModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<VisitaModel> listaVisitas = (List<VisitaModel>) request.getAttribute("listaVisitas");
    List<InmuebleModel> listaInmueblesDestino = (List<InmuebleModel>) request.getAttribute("listaInmueblesDestino");
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

<div class="modulo-visitas">
    <div class="header-modulo">
        <h2 class="titulo-modulo">🛂 Control de Visitas e Ingresos (Seguridad Auditable)</h2>
        <p class="descripcion-modulo">Módulo crítico de garita. Controle la permanencia temporal de personas externas asociando obligatoriamente su destino.</p>
    </div>

    <div class="bloque-split">
        <!-- FORMULARIO DE INGRESO -->
        <div class="card-formulario">
            <h3>📝 Registrar Entrada en Garita</h3>
            <form action="${pageContext.request.contextPath}/visitas" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="ingreso">

                <div class="grupo-campo">
                    <label>Departamento / Unidad de Destino:</label>
                    <select name="inmuebleId" required class="input-control">
                        <option value="">-- Seleccionar Destino --</option>
                        <% if (listaInmueblesDestino != null) {
                            for (InmuebleModel inm : listaInmueblesDestino) { %>
                                <option value="<%= inm.getId() %>">
                                    <%= inm.getBloqueTorre() %> - <%= inm.getNroUnidad() %> (<%= inm.getTipoUnidad() %>)
                                </option>
                            <% }
                        } %>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Nombre Completo del Visitante:</label>
                    <input type="text" name="nombreVisitante" placeholder="Ej. Carlos Mendoza" maxlength="100" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>DNI / Documento de Identidad:</label>
                    <input type="text" name="dniVisitante" placeholder="Ej. 70554128" maxlength="15" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Placa del Vehículo (Si ingresa a Estacionamiento):</label>
                    <input type="text" name="placaVehiculo" placeholder="Ej. F3W-415 (Opcional)" maxlength="15" class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Motivo catalogado / Tipo de Ingreso:</label>
                    <select name="tipoIngreso" required class="input-control">
                        <option value="VISITA">VISITA</option>
                        <option value="DELIVERY">DELIVERY</option>
                        <option value="SERVICIO_TECNICO">SERVICIO TÉCNICO</option>
                    </select>
                </div>

                <button type="submit" class="btn-registrar-ingreso">Conceder y Registrar Entrada</button>
            </form>
        </div>

        <!-- BITÁCORA DE CONTROL -->
        <div class="card-tabla-datos">
            <h3>📋 Bitácora Operacional en Tiempo Real</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Destino</th>
                            <th>Visitante</th>
                            <th>Motivo</th>
                            <th>Vehículo</th>
                            <th>Tiempos de Permanencia</th>
                            <th>Estado</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaVisitas != null && !listaVisitas.isEmpty()) {
                            for (VisitaModel v : listaVisitas) {
                                boolean enCurso = "EN_CURSO".equals(v.getEstado());
                        %>
                                <tr>
                                    <td><code>#V-<%= v.getId() %></code></td>
                                    <td><strong><%= v.getInmueble().getBloqueTorre() %> - <%= v.getInmueble().getNroUnidad() %></strong></td>
                                    <td>
                                        <div class="visitante-info">
                                            <span class="v-name"><%= v.getNombreVisitante() %></span>
                                            <span class="v-doc">DNI: <%= v.getDniVisitante() %></span>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge-tipo-ingreso tipo-<%= v.getTipoIngreso().toLowerCase() %>">
                                            <%= v.getTipoIngreso() %>
                                        </span>
                                    </td>
                                    <td><%= (v.getPlacaVehiculo() != null) ? "🚘 " + v.getPlacaVehiculo() : "<i>Peatonal</i>" %></td>
                                    <td>
                                        <div class="fechas-info">
                                            <span>🟢 In: <%= sdf.format(v.getFechaHoraIngreso()) %></span>
                                            <span>🔴 Out: <%= (v.getFechaHoraSalida() != null) ? sdf.format(v.getFechaHoraSalida()) : "En el complejo" %></span>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge-estado-visita <%= enCurso ? "vis-curso" : "vis-fin" %>">
                                            <%= v.getEstado() %>
                                        </span>
                                    </td>
                                    <td>
                                        <% if (enCurso) { %>
                                            <form action="${pageContext.request.contextPath}/visitas" method="POST" style="margin:0;">
                                                <input type="hidden" name="action" value="darSalida">
                                                <input type="hidden" name="idVisita" value="<%= v.getId() %>">
                                                <button type="submit" class="btn-tabla-salida">Marcar Salida</button>
                                            </form>
                                        <% } else { %>
                                            <span class="text-finalizado">🔒 Concluido</span>
                                        <% } %>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="8" style="text-align: center; color: #64748b; padding: 20px;">No hay movimientos en la bitácora de control el día de hoy.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>