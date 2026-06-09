<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.habitech.model.Comunicado" %>
<%@ page import="com.habitech.model.InventarioInfraestructura" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    // Detectamos si el controlador nos envió un comunicado para editar
    Comunicado comunicadoEdit = (Comunicado) request.getAttribute("comunicadoSeleccionado");
    boolean esEdicion = (comunicadoEdit != null);

    // Obtenemos la lista de todos los comunicados para la grilla
    List<Comunicado> listaComunicados = (List<Comunicado>) request.getAttribute("comunicados");

    // Obtenemos la lista de la infraestructura base para sacar las torres únicas
    List<InventarioInfraestructura> listaInfra = (List<InventarioInfraestructura>) request.getAttribute("inventario");

    // Formateador de fecha para los inputs de tipo datetime-local (YYYY-MM-DDTHH:mm)
    DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    // Formateador para mostrar visualmente en la tabla
    DateTimeFormatter formatterTabla = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<div class="modulo-container">

    <div class="card-formulario">
        <h2><%= esEdicion ? "Modificar Comunicado" : "Publicar Nuevo Comunicado" %></h2>

        <form action="${pageContext.request.contextPath}/comunicados" method="POST">
            <% if(esEdicion) { %>
                <input type="hidden" name="id" value="<%= comunicadoEdit.getId() %>">
            <% } %>

            <div class="form-grid">
                <div class="form-group full-width">
                    <label>Título del Anuncio</label>
                    <input type="text" name="titulo" required maxlength="150" placeholder="Ej: Mantenimiento de Ascensores" value="<%= esEdicion ? comunicadoEdit.getTitulo() : "" %>">
                </div>

                <div class="form-group full-width">
                    <label>Contenido / Cuerpo del Mensaje</label>
                    <textarea name="contenido" required rows="6" placeholder="Escriba los detalles del comunicado aquí..."><%= esEdicion ? comunicadoEdit.getContenido() : "" %></textarea>
                </div>

                <div class="form-group">
                    <label>Categoría</label>
                    <select name="categoria" required>
                        <option value="INFORMATIVO" <%= esEdicion && "INFORMATIVO".equals(comunicadoEdit.getCategoria()) ? "selected" : "" %>>Informativo</option>
                        <option value="URGENTE" <%= esEdicion && "URGENTE".equals(comunicadoEdit.getCategoria()) ? "selected" : "" %>>Urgente 🚨</option>
                        <option value="MANTENIMIENTO" <%= esEdicion && "MANTENIMIENTO".equals(comunicadoEdit.getCategoria()) ? "selected" : "" %>>Mantenimiento 🛠️</option>
                        <option value="ASAMBLEA" <%= esEdicion && "ASAMBLEA".equals(comunicadoEdit.getCategoria()) ? "selected" : "" %>>Asamblea 👥</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Estado Inicial</label>
                    <select name="estado" required>
                        <option value="PUBLICADO" <%= esEdicion && "PUBLICADO".equals(comunicadoEdit.getEstado()) ? "selected" : "" %>>Publicado (Visible)</option>
                        <option value="OCULTO" <%= esEdicion && "OCULTO".equals(comunicadoEdit.getEstado()) ? "selected" : "" %>>Oculto (Borrador)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Alcance de Difusión</label>
                    <select name="alcance" id="select-alcance" required onchange="evaluarAlcance()">
                        <option value="GLOBAL" <%= esEdicion && "GLOBAL".equals(comunicadoEdit.getAlcance()) ? "selected" : "" %>>Global (Todo el Condominio)</option>
                        <option value="TORRE_ESPECIFICA" <%= esEdicion && "TORRE_ESPECIFICA".equals(comunicadoEdit.getAlcance()) ? "selected" : "" %>>Torre Específica</option>
                    </select>
                </div>

                <div class="form-group" id="grupo-torre" style="<%= esEdicion && "TORRE_ESPECIFICA".equals(comunicadoEdit.getAlcance()) ? "" : "display: none;" %>">
                    <label>Seleccionar Torre Destino</label>
                    <select name="torre_destino">
                        <option value="">-- Seleccione una Torre --</option>
                        <%
                            if (listaInfra != null) {
                                java.util.Set<String> torresProcesadas = new java.util.HashSet<>();
                                for (InventarioInfraestructura infra : listaInfra) {
                                    if (infra.getTorre() != null && !infra.getTorre().trim().isEmpty() && torresProcesadas.add(infra.getTorre())) {
                        %>
                                    <option value="<%= infra.getTorre() %>" <%= esEdicion && infra.getTorre().equals(comunicadoEdit.getTorreDestino()) ? "selected" : "" %>><%= infra.getTorre() %></option>
                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Fecha de Expiración (Opcional)</label>
                    <input type="datetime-local" name="fecha_expiration" value="<%= esEdicion && comunicadoEdit.getFechaExpiracion() != null ? comunicadoEdit.getFechaExpiracion().format(formatterInput) : "" %>">
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn-guardar"><%= esEdicion ? "Actualizar Anuncio" : "Emitir Comunicado" %></button>
                <% if(esEdicion) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?modulo=comunicados" class="btn-cancelar">Cancelar</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="card-historial">
        <h2>Historial de Comunicados</h2>
        <div class="tabla-responsive">
            <table class="tabla-comunicados">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Categoría</th>
                        <th>Alcance</th>
                        <th>Destino</th>
                        <th>Publicado</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (listaComunicados != null && !listaComunicados.isEmpty()) {
                            for (Comunicado com : listaComunicados) {
                    %>
                            <tr>
                                <td><%= com.getId() %></td>
                                <td class="titulo-recortado" title="<%= com.getContenido() %>">
                                    <strong><%= com.getTitulo() %></strong>
                                </td>
                                <td><span class="badge-categoria <%= com.getCategoria().toLowerCase() %>"><%= com.getCategoria() %></span></td>
                                <td><%= com.getAlcance() %></td>
                                <td><%= com.getTorreDestino() != null ? com.getTorreDestino() : "TODOS" %></td>
                                <td><%= com.getFechaPublicacion() != null ? com.getFechaPublicacion().format(formatterTabla) : "-" %></td>
                                <td><span class="badge-estado <%= com.getEstado().toLowerCase() %>"><%= com.getEstado() %></span></td>
                                <td>
                                    <div class="acciones-flex">
                                        <a href="${pageContext.request.contextPath}/dashboard?modulo=comunicados&accion=editar&id=<%= com.getId() %>" class="btn-accion edit" title="Editar">✏️</a>
                                        <a href="${pageContext.request.contextPath}/comunicados?accion=eliminar&id=<%= com.getId() %>" class="btn-accion delete" title="Ocultar" onclick="return confirm('¿Seguro que deseas sacar este comunicado de la cartelera?');">🗑️</a>
                                    </div>
                                </td>
                            </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="8" class="text-center">No se han registrado ni publicado comunicados en la plataforma.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
function evaluarAlcance() {
    var alcance = document.getElementById("select-alcance").value;
    var grupoTorre = document.getElementById("grupo-torre");
    if(alcance === "TORRE_ESPECIFICA") {
        grupoTorre.style.display = "block";
    } else {
        grupoTorre.style.display = "none";
        document.getElementsByName("torre_destino")[0].value = "";
    }
}
</script>