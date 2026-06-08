<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.AsignacionModel" %>
<%@ page import="com.habitech.model.InmuebleModel" %>
<%@ page import="java.util.List" %>

<%
    List<AsignacionModel> listaAsignaciones = (List<AsignacionModel>) request.getAttribute("listaAsignaciones");
    List<InmuebleModel> vacantes = (List<InmuebleModel>) request.getAttribute("listaInmueblesVacantes");

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

<div class="modulo-asignaciones">
    <div class="header-modulo">
        <h1 class="titulo-modulo">🔑 Asignación de Viviendas e Inmuebles</h1>
        <p class="descripcion-modulo">Vincule los departamentos y cocheras autogenerados con sus respectivos propietarios o inquilinos encargados.</p>
    </div>

    <div class="bloque-split">
        <div class="card-formulario">
            <h3>📝 Registrar Nueva Ocupación</h3>
            <form action="${pageContext.request.contextPath}/asignaciones" method="POST" class="form-habitech">
                <input type="hidden" name="action" value="registrar">

                <div class="grupo-campo">
                    <label>Unidad Inmobiliaria Libre:</label>
                    <select name="inmuebleId" required class="input-control">
                        <option value="">-- Seleccionar Unidad Vacante --</option>
                        <% if (vacantes != null && !vacantes.isEmpty()) {
                            for (InmuebleModel v : vacantes) { %>
                                <option value="<%= v.getId() %>">
                                    <%= v.getBloqueTorre() %> - <%= v.getTipoUnidad() %> Nro <%= v.getNroUnidad() %>
                                </option>
                            <% }
                        } else { %>
                            <option value="" disabled>No hay unidades vacantes disponibles</option>
                        <% } %>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Nombre Completo del Residente:</label>
                    <input type="text" name="nombreResidente" placeholder="Ej: Juan Pérez Guerrero" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Documento de Identidad (DNI/CE):</label>
                    <input type="text" name="documentoIdentidad" placeholder="Ej: 74859612" required class="input-control">
                </div>

                <div class="grupo-campo">
                    <label>Condición Legal de Ocupación:</label>
                    <select name="tipoAdquisicion" required class="input-control">
                        <option value="PROPIETARIO">PROPIETARIO</option>
                        <option value="INQUILINO">INQUILINO</option>
                    </select>
                </div>

                <div class="grupo-campo">
                    <label>Fecha de Entrega de Llaves / Ingreso:</label>
                    <input type="date" name="fechaIngreso" value="<%= new java.sql.Date(System.currentTimeMillis()) %>" required class="input-control">
                </div>

                <button type="submit" class="btn-guardar-asignacion">💾 Consolidar Ocupación</button>
            </form>
        </div>

        <div class="card-tabla-datos">
            <h3>👥 Padrón Actual de Residentes Activos</h3>
            <div class="table-container">
                <table class="tabla-habitech">
                    <thead>
                        <tr>
                            <th>Ubicación</th>
                            <th>Tipo</th>
                            <th>Residente Responsable</th>
                            <th>Documento</th>
                            <th>Régimen</th>
                            <th>Fecha Ingreso</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaAsignaciones != null && !listaAsignaciones.isEmpty()) {
                            for (AsignacionModel a : listaAsignaciones) { %>
                                <tr>
                                    <td><strong><%= a.getInmueble().getBloqueTorre() %> - <%= a.getInmueble().getNroUnidad() %></strong></td>
                                    <td><span class="badge-tipo-un"><%= a.getInmueble().getTipoUnidad() %></span></td>
                                    <td><%= a.getNombreResidente() %></td>
                                    <td><code><%= a.getDocumentoIdentidad() %></code></td>
                                    <td>
                                        <span class="badge-regimen <%= "PROPIETARIO".equals(a.getTipoAdquisicion()) ? "reg-prop" : "reg-inq" %>">
                                            <%= a.getTipoAdquisicion() %>
                                        </span>
                                    </td>
                                    <td><%= a.getFechaIngreso() %></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/asignaciones" method="POST" style="margin:0;" onsubmit="return confirm('¿Liberar inmueble? El estado de la unidad cambiará a VACANTE.');">
                                            <input type="hidden" name="action" value="eliminar">
                                            <input type="hidden" name="idAsignacion" value="<%= a.getId() %>">
                                            <input type="hidden" name="inmuebleId" value="<%= a.getInmuebleId() %>">
                                            <button type="submit" class="btn-tabla-eliminar">❌ Desalojar</button>
                                        </form>
                                    </td>
                                </tr>
                            <% }
                        } else { %>
                            <tr>
                                <td colspan="7" class="text-center">No existen viviendas asignadas en este momento. El condominio se encuentra desocupado.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>