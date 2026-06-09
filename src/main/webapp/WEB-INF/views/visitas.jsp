<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Visita" %>
<%@ page import="com.habitech.model.Asignacion" %>
<%@ page import="com.habitech.model.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    List<Visita> listaVisitas = (List<Visita>) request.getAttribute("visitas");
    List<Asignacion> listaAsignaciones = (List<Asignacion>) request.getAttribute("asignaciones");
    String alertaError = (String) request.getAttribute("alertaError");

    DateTimeFormatter formFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
    String rolUsuario = (usuarioSesion != null && usuarioSesion.getRol() != null) ? usuarioSesion.getRol().trim().toUpperCase() : "";
%>

<div class="modulo-container animate-fade-in">

    <% if (alertaError != null) { %>
        <div class="alerta-error">
            <span class="icono-alerta">⚠️</span>
            <div class="alerta-contenido">
                <strong>Error en Operación:</strong>
                <p><%= alertaError %></p>
            </div>
        </div>
    <% } %>

    <%-- Formulario de Registro de Entrada --%>
    <div class="card-habitech">
        <div class="card-header-habitech">
            <div class="icono-titulo">🪪</div>
            <div>
                <h2>Registrar Control de Acceso</h2>
                <p class="subtitulo">Bitácora de ingresos peatonales y vehiculares en tiempo real</p>
            </div>
        </div>

        <%-- Envía los datos directamente al controlador transaccional VisitaController --%>
        <form action="${pageContext.request.contextPath}/visitas" method="POST" class="form-habitech">
            <div class="form-grid-habitech">

                <div class="form-group-habitech">
                    <label for="nombre_visitante">Nombre Completo del Visitante</label>
                    <input type="text" id="nombre_visitante" name="nombre_visitante" placeholder="Ej. Carlos Mendoza Ramos" required maxlength="100">
                </div>

                <div class="form-group-habitech">
                    <label for="dni_visitante">Documento de Identidad (DNI / CE)</label>
                    <input type="text" id="dni_visitante" name="dni_visitante" placeholder="Número de documento" required maxlength="15">
                </div>

                <div class="form-group-habitech">
                    <label for="asignacion_id">Unidad de Destino</label>
                    <select id="asignacion_id" name="asignacion_id" required>
                        <option value="">-- Seleccione Ubicación --</option>
                        <%
                            if (listaAsignaciones != null) {
                                for (Asignacion asig : listaAsignaciones) {
                        %>
                                    <option value="<%= asig.getId() %>">
                                        <%= asig.getDetalleInfraestructura() != null ? asig.getDetalleInfraestructura() : "Unidad #" + asig.getId() %>
                                        <% if (asig.getCodigoUnidadEspecifica() != null && !asig.getCodigoUnidadEspecifica().isEmpty()) { %>
                                            (<%= asig.getCodigoUnidadEspecifica() %>)
                                        <% } %>
                                        — <%= asig.getNombreUsuario() != null ? asig.getNombreUsuario() : "Residente" %>
                                    </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group-habitech">
                    <label for="tipo_ingreso">Tipo de Ingreso</label>
                    <select id="tipo_ingreso" name="tipo_ingreso" required>
                        <option value="VISITA" selected>📌 Visita Familiar / Personal</option>
                        <option value="DELIVERY">🛵 Delivery / Reparto Rápido</option>
                        <option value="SERVICIO_TECNICO">🛠️ Servicio Técnico / Contratista</option>
                    </select>
                </div>

                <div class="form-group-habitech full-width-mobile">
                    <label for="placa_vehiculo">Placa Vehicular (Opcional)</label>
                    <input type="text" id="placa_vehiculo" name="placa_vehiculo" placeholder="Ej. ABC-123" maxlength="15">
                </div>
            </div>

            <div class="form-acciones-habitech">
                <button type="submit" class="btn-primario-habitech">Conceder Ingreso e Iniciar Estadía</button>
            </div>
        </form>
    </div>

    <%-- Panel de Monitoreo de Estadías Activas e Historial --%>
    <div class="card-habitech tabla-margin-top">
        <div class="card-header-habitech">
            <div class="icono-titulo">📺</div>
            <div>
                <h2>Monitoreo de Movimiento y Estadías</h2>
                <p class="subtitulo">Panel de seguimiento para control de seguridad y salidas</p>
            </div>
        </div>

        <div class="tabla-responsive-habitech">
            <table class="tabla-maestra-habitech">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Visitante</th>
                        <th>Destino Interno</th>
                        <th>Tipo</th>
                        <th>Vehículo</th>
                        <th>Ingreso</th>
                        <th>Salida</th>
                        <th>Estado</th>
                        <th class="text-center">Acciones de Control</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaVisitas != null && !listaVisitas.isEmpty()) {
                            for (Visita vis : listaVisitas) {
                                String estNorm = vis.getEstado() != null ? vis.getEstado().trim().toUpperCase() : "EN_CURSO";
                                String tipoNorm = vis.getTipoIngreso() != null ? vis.getTipoIngreso().trim().toUpperCase() : "VISITA";
                    %>
                            <tr>
                                <td class="col-id">#<%= vis.getId() %></td>
                                <td>
                                    <div class="nombre-principal"><%= vis.getNombreVisitante() %></div>
                                    <div class="subtexto-tabla">🪪 <%= vis.getDniVisitante() %></div>
                                </td>
                                <td>
                                    <div class="nombre-principal">
                                        <%= vis.getDetalleInfraestructura() != null ? vis.getDetalleInfraestructura() : "Unidad #" + vis.getAsignacionId() %>
                                    </div>
                                    <% if (vis.getCodigoUnidadEspecifica() != null && !vis.getCodigoUnidadEspecifica().isEmpty()) { %>
                                        <div class="badge-unidad"><%= vis.getCodigoUnidadEspecifica() %></div>
                                    <% } %>
                                </td>
                                <td>
                                    <% if ("VISITA".equals(tipoNorm)) { %>
                                        <span class="badge-tipo tipo-visita">Personal</span>
                                    <% } else if ("DELIVERY".equals(tipoNorm)) { %>
                                        <span class="badge-tipo tipo-delivery">Delivery</span>
                                    <% } else { %>
                                        <span class="badge-tipo tipo-servicio">Servicio</span>
                                    <% } %>
                                </td>
                                <td>
                                    <%= (vis.getPlacaVehiculo() != null && !vis.getPlacaVehiculo().isEmpty()) ? "🚘 " + vis.getPlacaVehiculo() : "<span class='texto-vacio'>-</span>" %>
                                </td>
                                <td class="texto-tiempo"><%= vis.getFechaHoraIngreso() != null ? vis.getFechaHoraIngreso().format(formFecha) : "-" %></td>
                                <td class="texto-tiempo">
                                    <%= vis.getFechaHoraOut() != null ? vis.getFechaHoraOut().format(formFecha) : "<span class='estado-dentro'>Dentro del Complejo</span>" %>
                                </td>
                                <td>
                                    <% if ("EN_CURSO".equals(estNorm)) { %>
                                        <span class="badge-estado est-progreso">En Curso</span>
                                    <% } else if ("FINALIZADO".equals(estNorm)) { %>
                                        <span class="badge-estado est-completado">Finalizado</span>
                                    <% } else { %>
                                        <span class="badge-estado est-anulado">Anulado</span>
                                    <% } %>
                                </td>
                                <td>
                                    <div class="acciones-celda">
                                        <%
                                            boolean esOperador = rolUsuario.contains("ADMIN") || "CONSERJE".equals(rolUsuario);
                                            if ("EN_CURSO".equals(estNorm) && esOperador) {
                                        %>
                                            <a href="${pageContext.request.contextPath}/visitas?accion=registrarSalida&id=<%= vis.getId() %>"
                                               class="btn-tabla btn-salida"
                                               onclick="return confirm('¿Confirmar salida del visitante?');">
                                               ✓ Marcar Salida
                                            </a>

                                            <% if (rolUsuario.contains("ADMIN")) { %>
                                                <a href="${pageContext.request.contextPath}/visitas?accion=anular&id=<%= vis.getId() %>"
                                                   class="btn-tabla btn-anular"
                                                   onclick="return confirm('¿Seguro que deseas anular este registro?');">
                                                   ✕ Anular
                                                </a>
                                            <% } %>
                                        <% } else { %>
                                            <span class="bloqueado-indicador">Registro cerrado</span>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="9" class="tabla-vacia">No hay movimientos registrados en las últimas 24 horas.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>