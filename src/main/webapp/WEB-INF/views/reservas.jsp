<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Reserva" %>
<%@ page import="com.habitech.model.Usuario" %>
<%@ page import="com.habitech.model.InventarioInfraestructura" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    // Listas inyectadas por el DashboardController
    List<Reserva> listaReservas = (List<Reserva>) request.getAttribute("reservas");
    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("usuarios");
    List<InventarioInfraestructura> listaInfra = (List<InventarioInfraestructura>) request.getAttribute("inventario");

    // Captura de errores de cruce de agenda
    String alertaError = (String) request.getAttribute("alertaError");

    // Formateadores de fecha
    DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formRegistro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<div class="modulo-container">

    <% if (alertaError != null) { %>
        <div class="alerta-error">
            ⚠️ <strong>Error de Agenda:</strong> <%= alertaError %>
        </div>
    <% } %>

    <div class="card-formulario">
        <h2>Registrar Nueva Reserva de Área Común</h2>

        <form action="${pageContext.request.contextPath}/reservas" method="POST">
            <input type="hidden" name="estado" value="APROBADA">

            <div class="form-grid">
                <div class="form-group">
                    <label>Residente Solicitante</label>
                    <select name="usuario_id" required>
                        <option value="">-- Seleccione el Residente --</option>
                        <%
                            if (listaUsuarios != null) {
                                for (Usuario u : listaUsuarios) {
                        %>
                                    <option value="<%= u.getId() %>"><%= u.getNombres() %> <%= u.getApellidos() %> (<%= u.getRol() %>)</option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Área Común Requerida</label>
                    <select name="inventario_maestro_id" required>
                        <option value="">-- Seleccione el Área --</option>
                        <%
                            if (listaInfra != null) {
                                for (InventarioInfraestructura infra : listaInfra) {
                                    // Filtramos para listar solo items que correspondan a áreas comunes y no departamentos sueltos
                                    if ("ACTIVO".equals(infra.getEstado())) {
                        %>
                                        <option value="<%= infra.getId() %>"><%= infra.getTipoElemento() %> - <%= infra.getTorre() != null ? infra.getTorre() : "General" %></option>
                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Fecha del Evento</label>
                    <input type="date" name="fecha_reserva" required min="<%= java.time.LocalDate.now() %>">
                </div>

                <div class="form-group">
                    <label>Turno Solicitado</label>
                    <select name="turno" required>
                        <option value="">-- Seleccione Turno --</option>
                        <option value="MAÑANA">Mañana</option>
                        <option value="TARDE">Tarde</option>
                        <option value="NOCHE">Noche</option>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar">Agendar Espacio</button>
            </div>
        </form>
    </div>

    <div class="card-historial">
        <h2>Control de Agendas y Reservas realizadas</h2>
        <div class="tabla-responsive">
            <table class="tabla-reservas">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Residente</th>
                        <th>Área Común</th>
                        <th>Fecha Evento</th>
                        <th>Turno</th>
                        <th>Registrado el</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaReservas != null && !listaReservas.isEmpty()) {
                            for (Reserva r : listaReservas) {
                    %>
                            <tr>
                                <td><%= r.getId() %></td>
                                <td><strong><%= r.getNombreUsuario() != null ? r.getNombreUsuario() : "Desconocido" %></strong></td>
                                <td><span class="badge-area"><%= r.getNombreAreaComun() != null ? r.getNombreAreaComun() : "No especificado" %></span></td>
                                <td><%= r.getFechaReserva() != null ? r.getFechaReserva().format(formFecha) : "-" %></td>
                                <td><span class="badge-turno <%= r.getTurno().toLowerCase() %>"><%= r.getTurno() %></span></td>
                                <td><%= r.getFechaRegistro() != null ? r.getFechaRegistro().format(formRegistro) : "-" %></td>
                                <td><span class="badge-estado <%= r.getEstado().toLowerCase() %>"><%= r.getEstado() %></span></td>
                                <td>
                                    <div class="acciones-flex">
                                        <%
                                            // Normalizamos el texto quitando espacios fijos de la base de datos
                                            String est = (r.getEstado() != null) ? r.getEstado().trim().toUpperCase() : "";

                                            // Lógica exclusiva para APROBADA
                                            if ("APROBADA".equals(est)) {
                                        %>
                                            <a href="${pageContext.request.contextPath}/reservas?accion=cancelar&id=<%= r.getId() %>"
                                               class="btn-cancelar-reserva"
                                               title="Cancelar Reserva"
                                               onclick="return confirm('¿Seguro que deseas cancelar esta reserva por completo? Esto liberará el turno.');">❌ Cancelar</a>
                                        <% } else { %>
                                            <span class="texto-bloqueado">-</span>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="8" class="text-center">No existen solicitudes de separación de áreas registradas hoy.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>