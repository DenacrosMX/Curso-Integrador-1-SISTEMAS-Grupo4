<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.ReciboModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%
    List<ReciboModel> listaRecibos = (List<ReciboModel>) request.getAttribute("listaRecibos");
    String mensajeExito = (String) session.getAttribute("mensajeExito");
    String mensajeError = (String) session.getAttribute("mensajeError");
    session.removeAttribute("mensajeExito");
    session.removeAttribute("mensajeError");

    String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
%>

<% if (mensajeExito != null) { %>
    <div class="alert alert-success"><%= mensajeExito %></div>
<% } %>
<% if (mensajeError != null) { %>
    <div class="alert alert-danger"><%= mensajeError %></div>
<% } %>

<div class="modulo-recibos">
    <div class="header-modulo">
        <h1 class="titulo-modulo">💰 Recibos y Estados de Pago</h1>
        <p class="descripcion-modulo">Genere el cobro de la cuota de mantenimiento mensual para todos los residentes ocupantes y procese las cobranzas.</p>
    </div>

    <div class="bloque-split">
        <div class="card-formulario">
            <h3>⚡ Facturación Mensual Masiva</h3>
            <form action="${pageContext.request.contextPath}/recibos" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="generarFacturacion">

                <div class="grupo-campo">
                    <label>Mes a Facturar:</label>
                    <select name="mes" required class="input-control">
                        <option value="6">Junio</option>
                        <option value="7">Julio</option>
                        <option value="8">Agosto</option>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Año Fiscal:</label>
                    <input type="number" name="anio" value="2026" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Cuota de Mantenimiento (S/.):</label>
                    <input type="number" name="monto" step="0.01" value="250.00" required class="input-control">
                </div>

                <button type="submit" class="btn-procesar-cobro">🚀 Emitir Recibos del Mes</button>
            </form>
        </div>

        <div class="card-tabla-datos">
            <h3>📊 Historial General de Cobros Emitidos</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>ID Recibo</th>
                            <th>Inmueble</th>
                            <th>Residente</th>
                            <th>Periodo</th>
                            <th>Monto Cobrado</th>
                            <th>Estado</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaRecibos != null && !listaRecibos.isEmpty()) {
                            for (ReciboModel r : listaRecibos) {
                                boolean pendiente = "PENDIENTE".equals(r.getEstadoPago());
                        %>
                                <tr>
                                    <td><code>#REC-<%= r.getId() %></code></td>
                                    <td><strong><%= r.getAsignacion().getInmueble().getBloqueTorre() %> - <%= r.getAsignacion().getInmueble().getNroUnidad() %></strong></td>
                                    <td><%= r.getAsignacion().getNombreResidente() %></td>
                                    <td><%= meses[r.getMesFacturado()] %> / <%= r.getAnioFacturado() %></td>
                                    <td><strong>S/. <%= r.getMontoMantenimiento() %></strong></td>
                                    <td>
                                        <span class="badge-estado-pago <%= pendiente ? "pago-pen" : "pago-pag" %>">
                                            <%= r.getEstadoPago() %>
                                        </span>
                                    </td>
                                    <td>
                                        <% if (pendiente) { %>
                                            <form action="${pageContext.request.contextPath}/recibos" method="POST" style="margin:0;">
                                                <input type="hidden" name="action" value="pagar">
                                                <input type="hidden" name="idRecibo" value="<%= r.getId() %>">
                                                <button type="submit" class="btn-tabla-pagar">💳 Registrar Pago</button>
                                            </form>
                                        <% } else { %>
                                            <span class="text-conciliado">🔒 Conciliado</span>
                                        <% } %>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="7" class="text-center">No se registran cobros ni recibos emitidos para este año fiscal.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>