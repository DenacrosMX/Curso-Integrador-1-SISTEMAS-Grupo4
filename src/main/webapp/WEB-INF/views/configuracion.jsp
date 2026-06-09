<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Configuracion" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    // Detectamos si el controlador nos envió una configuración para editar
    Configuracion configEdit = (Configuracion) request.getAttribute("configSeleccionada");
    boolean esEdicion = (configEdit != null);

    // Obtenemos la lista histórica de configuraciones
    List<Configuracion> listaConfig = (List<Configuracion>) request.getAttribute("configuraciones");

    // Formateador para mostrar la fecha de forma limpia y elegante (Día/Mes/Año Hora:Minutos)
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<div class="modulo-container">

    <div class="card-formulario">
        <h2><%= esEdicion ? "Modificar Configuración Legal" : "Registrar Nueva Configuración Maestra" %></h2>

        <form action="${pageContext.request.contextPath}/configuracion" method="POST">
            <% if(esEdicion) { %>
                <input type="hidden" name="id" value="<%= configEdit.getId() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-group">
                    <label>Nombre del Condominio</label>
                    <input type="text" name="nombreCondominio" required value="<%= esEdicion ? configEdit.getNombreCondominio() : "" %>" placeholder="Ej: Altos de la Ensenada">
                </div>

                <div class="form-group">
                    <label>Dirección Física</label>
                    <input type="text" name="direccion" required value="<%= esEdicion ? configEdit.getDireccion() : "" %>" placeholder="Ej: Av. Las Flores 123">
                </div>

                <div class="form-group">
                    <label>RUC (Identificación Fiscal)</label>
                    <input type="text" name="ruc" required maxlength="11" value="<%= esEdicion ? configEdit.getRuc() : "" %>" placeholder="11 dígitos">
                </div>

                <div class="form-group">
                    <label>Cuenta Bancaria del Complejo</label>
                    <input type="text" name="cuentaBancaria" value="<%= (esEdicion && configEdit.getCuentaBancaria() != null) ? configEdit.getCuentaBancaria() : "" %>" placeholder="Nro de cuenta o CCI">
                </div>

                <div class="form-group">
                    <label>Día de Vencimiento de Recibos</label>
                    <input type="number" name="diaVencimientoRecibo" min="1" max="28" required value="<%= esEdicion ? configEdit.getDiaVencimientoRecibo() : "5" %>">
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar"><%= esEdicion ? "Actualizar Parámetros" : "Guardar Configuración" %></button>
                <% if(esEdicion) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?modulo=configuracion" class="btn-cancelar">Cancelar Edición</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="card-historial">
        <h2>Historial de Configuraciones Maestras</h2>

        <div class="table-responsive">
            <table class="tabla-usuarios">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Condominio</th>
                        <th>Dirección</th>
                        <th>RUC</th>
                        <th>Cuenta Bancaria</th>
                        <th>Día Venc.</th>
                        <th>Fecha Registro</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (listaConfig != null && !listaConfig.isEmpty()) {
                        for (Configuracion c : listaConfig) { %>
                            <tr>
                                <td><%= c.getId() %></td>
                                <td><strong><%= c.getNombreCondominio() %></strong></td>
                                <td><%= c.getDireccion() %></td>
                                <td><%= c.getRuc() %></td>
                                <td><%= c.getCuentaBancaria() != null ? c.getCuentaBancaria() : "-" %></td>
                                <td><span class="badge-vencimiento"><%= c.getDiaVencimientoRecibo() %></span></td>
                                <td><%= c.getFechaRegistro() != null ? c.getFechaRegistro().format(formateador) : "-" %></td>
                                <td><span class="badge-estado activo"><%= c.getEstado() %></span></td>
                                <td>
                                    <div class="acciones-flex">
                                        <a href="${pageContext.request.contextPath}/dashboard?modulo=configuracion&accion=editar&id=<%= c.getId() %>" class="btn-accion edit" title="Editar">✏️</a>
                                        <a href="${pageContext.request.contextPath}/configuracion?accion=eliminar&id=<%= c.getId() %>" class="btn-accion delete" title="Dar de Baja" onclick="return confirm('¿Seguro que deseas pasar a INACTIVO esta configuración?');">🗑️</a>
                                    </div>
                                </td>
                            </tr>
                        <% }
                    } else { %>
                        <tr>
                            <td colspan="9" class="text-center">No hay registros de configuración activos en el sistema.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>