<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.InmuebleModel" %>
<%@ page import="com.habitech.model.MaestroModel" %>
<%@ page import="java.util.List" %>

<%
    List<MaestroModel> maestrosDisponibles = (List<MaestroModel>) request.getAttribute("listaMaestrosDisponibles");
    List<InmuebleModel> listaInmuebles = (List<InmuebleModel>) request.getAttribute("listaInmuebles");

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

<div class="modulo-inmuebles">
    <h1 class="titulo-modulo">🏢 Gestión de Inmuebles y Unidades</h1>
    <p class="descripcion-modulo">
        Administre la distribución física del condominio. Utilice el asistente automático basado en los parámetros maestros para poblar el inventario completo o remueva los registros para reconfigurar.
    </p>

    <div class="card-asistente">
        <div class="asistente-info">
            <h3>⚡ Asistente de Autogeneración de Inventario</h3>
            <p>Seleccione un registro del Historial Maestro. El sistema procesará iterativamente la creación secuencial de departamentos y cocheras sin registros manuales.</p>
        </div>

        <div class="asistente-acciones">
            <form action="${pageContext.request.contextPath}/inmuebles" method="POST" class="form-inline-asistente">
                <input type="hidden" name="action" value="generar">
                <select name="idMaestro" required class="select-maestro">
                    <option value="">-- Seleccionar Configuración Maestra --</option>
                    <% if (maestrosDisponibles != null && !maestrosDisponibles.isEmpty()) {
                        for (MaestroModel m : maestrosDisponibles) { %>
                            <option value="<%= m.getId() %>">
                                ID #<%= m.getId() %> - <%= m.getNombreCondominio() %> (<%= m.getCantidadTorres() %>T / <%= m.getPisosPorTorre() %>P)
                            </option>
                        <% }
                    } %>
                </select>
                <button type="submit" class="btn-asistente-generar">🚀 Ejecutar Asistente</button>
            </form>

            <% if (listaInmuebles != null && !listaInmuebles.isEmpty()) { %>
                <form action="${pageContext.request.contextPath}/inmuebles" method="POST" onsubmit="return confirm('¿ATENCIÓN!\nEsta acción eliminará todas las unidades físicas del condominio de manera irreversible.\n¿Desea continuar?');">
                    <input type="hidden" name="action" value="limpiar">
                    <button type="submit" class="btn-asistente-limpiar">🗑️ Vaciar Inventario</button>
                </form>
            <% } %>
        </div>
    </div>

    <div class="card-inventario">
        <h3>📊 Mapa Físico del Condominio (<%= listaInmuebles != null ? listaInmuebles.size() : 0 %> Unidades Registradas)</h3>

        <div class="grid-inmuebles-panoramico">
            <%
                if (listaInmuebles != null && !listaInmuebles.isEmpty()) {
                    for (InmuebleModel inmueble : listaInmuebles) {
                        boolean esDpto = "DEPARTAMENTO".equals(inmueble.getTipoUnidad());
                        boolean esOcupado = "OCUPADO".equals(inmueble.getEstadoOcupacion());
            %>
                <div class="tarjeta-inmueble <%= esDpto ? "tipo-dpto" : "tipo-cochera" %>">
                    <div class="tarjeta-encabezado">
                        <span class="badge-torre"><%= inmueble.getBloqueTorre() %></span>
                        <span class="badge-estado <%= esOcupado ? "estado-ocupado" : "estado-vacante" %>">
                            <%= inmueble.getEstadoOcupacion() %>
                        </span>
                    </div>

                    <div class="tarjeta-cuerpo">
                        <h2 class="nro-unidad"><%= inmueble.getNroUnidad() %></h2>
                        <p class="detalle-unidad">
                            <%= esDpto ? "↕️ Nivel / Piso " + inmueble.getPiso() : "🚗 Estacionamiento" %>
                        </p>
                    </div>

                    <div class="tarjeta-pie">
                        <span class="tipo-texto"><%= inmueble.getTipoUnidad() %></span>
                    </div>
                </div>
            <%
                    }
                } else {
            %>
                <div class="inventario-vacio-box">
                    <div class="icono-vacio">🏢</div>
                    <h4>El inventario físico está vacío</h4>
                    <p>No se encontraron departamentos ni cocheras mapeadas en el sistema. Elija una configuración en el asistente superior y ejecútelo para poblar el mapa inmobiliario.</p>
                </div>
            <% } %>
        </div>
    </div>
</div>