<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.habitech.model.Comunicado" %>
<%@ page import="com.habitech.model.InventarioInfraestructura" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    Comunicado comunicadoEdit = (Comunicado) request.getAttribute("comunicadoSeleccionado");
    boolean esEdicion = (comunicadoEdit != null);
    List<Comunicado> listaComunicados = (List<Comunicado>) request.getAttribute("comunicados");
    List<InventarioInfraestructura> listaInfra = (List<InventarioInfraestructura>) request.getAttribute("inventario");

    DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    DateTimeFormatter formatterTabla = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Filtrado directo en servidor para la alerta del Residente
    Comunicado alertaUrgenteParaModal = null;
    if (listaComunicados != null && !listaComunicados.isEmpty()) {
        for (Comunicado com : listaComunicados) {
            if ("URGENTE".equalsIgnoreCase(com.getCategoria())) {
                alertaUrgenteParaModal = com;
                break;
            }
        }
    }
%>

<c:choose>
    <%-- ======================================================================== --%>
    <%-- 1. VISTA EXCLUSIVA PARA EL ADMINISTRADOR (ADMIN_SISTEMA)                 --%>
    <%-- ======================================================================== --%>
    <c:when test="${usuarioLogueado.rol == 'ADMIN_SISTEMA'}">
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
    </c:when>

    <%-- ======================================================================== --%>
    <%-- 2. VISTA EXCLUSIVA PARA EL RESIDENTE / INQUILINO                         --%>
    <%-- ======================================================================== --%>
    <c:otherwise>
        <div class="modulo-container">
            <div class="card-historial">
                <h2>📢 Panel de Comunicados Oficiales</h2>
                <%
                    if (listaComunicados != null && !listaComunicados.isEmpty()) {
                        for (Comunicado com : listaComunicados) {
                %>
                            <div class="comunicado-tarjeta-lectura">
                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;">
                                    <h3><%= com.getTitulo() %></h3>
                                    <span class="badge-categoria <%= com.getCategoria().toLowerCase() %>">
                                        <%= com.getCategoria() %>
                                    </span>
                                </div>
                                <p><%= com.getContenido() %></p>
                                <div class="comunicado-meta-footer">
                                    <span>📅 Emitido: <%= com.getFechaPublicacion() != null ? com.getFechaPublicacion().format(formatterTabla) : "-" %></span>
                                    <span>📌 Alcance: <%= "GLOBAL".equals(com.getAlcance()) ? "Todo el condominio" : com.getTorreDestino() %></span>
                                </div>
                            </div>
                <%
                        }
                    } else {
                %>
                    <div class="text-center" style="padding: 20px 0;">
                        No hay comunicados vigentes en este momento para tu residencia.
                    </div>
                <% } %>
            </div>
        </div>

        <%-- VENTANA MODAL EMITIDA DIRECTAMENTE --%>
        <% if (alertaUrgenteParaModal != null) { %>
            <div id="modalComunicadoUrgente" style="position: fixed !important; top: 0 !important; left: 0 !important; width: 100vw !important; height: 100vh !important; background: rgba(15, 23, 42, 0.9) !important; display: flex !important; justify-content: center !important; align-items: center !important; z-index: 999999 !important;">
                <div style="background-color: #1e293b; border: 2px solid #ef4444; width: 90%; max-width: 500px; border-radius: 8px; padding: 24px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.7); box-sizing: border-box; margin: auto;">
                    <h2 style="color: #ef4444; border-bottom: 1px solid rgba(239, 68, 68, 0.3); margin: 0 0 15px 0; font-size: 1.3rem; padding-bottom: 10px; font-weight: 600; font-family: sans-serif;">
                        🚨 Alerta Urgente: <%= alertaUrgenteParaModal.getTitulo() %>
                    </h2>
                    <p style="background-color: #0f172a; border: 1px solid #334155; padding: 15px; border-radius: 6px; color: #cbd5e1; font-size: 0.95rem; line-height: 1.6; white-space: pre-line; margin: 0 0 20px 0; font-family: sans-serif;">
                        <%= alertaUrgenteParaModal.getContenido() %>
                    </p>
                    <div style="text-align: right;">
                        <button onclick="document.getElementById('modalComunicadoUrgente').style.display='none';" style="background-color: #ef4444; color: #ffffff; font-weight: 600; padding: 10px 24px; border: none; border-radius: 6px; cursor: pointer; font-size: 0.95rem;">
                            Entendido / Cerrar
                        </button>
                    </div>
                </div>
            </div>
        <% } %>
    </c:otherwise>
</c:choose>