<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.habitech.model.UsuarioModel" %>

<%
    // 1. Recuperamos la vista solicitada por parámetro
    String view = request.getParameter("view");
    if (view == null) view = "home";

    // 2. Recuperamos el objeto usuario de la sesión para la validación RBAC
    UsuarioModel usuarioSesion = (UsuarioModel) session.getAttribute("usuarioSesion");
    String nombreUsuario = (usuarioSesion != null) ? usuarioSesion.getNombres() + " " + usuarioSesion.getApellidos() : "Invitado";
    String rolActual = (usuarioSesion != null) ? usuarioSesion.getRol() : "";
%>

<html>
<head>
    <title>Habitech Sistema</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

    <%-- Estilo aislado exclusivo para el Módulo Maestro --%>
    <% if ("maestro".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/maestro.css">
    <% } %>

    <%-- Estilo aislado exclusivo para el Módulo de Inmuebles --%>
    <% if ("inmuebles".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inmuebles.css">
    <% } %>

    <%-- Estilo aislado exclusivo para el Módulo de Asignación de Viviendas --%>
    <% if ("asignaciones".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/asignaciones.css">
    <% } %>

    <%-- Estilo aislado exclusivo para el Módulo de Recibos y Pagos --%>
    <% if ("recibos".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recibos.css">
    <% } %>

    <%-- INTEGRACIÓN MÓDULO 5: Estilo aislado exclusivo para el Control de Visitas --%>
    <% if ("visitas".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/visitas.css">
    <% } %>

    <%-- INTEGRACIÓN MÓDULO 6: Estilo aislado exclusivo para la Mesa de Ayuda --%>
    <% if ("mesa_ayuda".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mesa_ayuda.css">
    <% } %>

    <%-- INTEGRACIÓN MÓDULO 8: Estilo aislado exclusivo para Reservas de Áreas Comunes --%>
    <% if ("reservas".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reservas.css">
    <% } %>

    <%-- INTEGRACIÓN MÓDULO 3: Estilo aislado exclusivo para la Gestión de Usuarios Seguro --%>
    <% if ("usuarios".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuarios.css">
    <% } %>
</head>

<body>

<div class="header" style="display: flex; justify-content: space-between; align-items: center; padding: 0 20px;">
    <h2>Habitech - Sistema de Gestión</h2>
    <div class="user-info" style="color: white; font-size: 14px;">
        <span>👤 Bienvenido, <strong><%= nombreUsuario %></strong></span>
        <a href="${pageContext.request.contextPath}/auth" style="color: #f87171; margin-left: 15px; text-decoration: none; font-weight: 600;">Automático 🚪 Cerrar Sesión</a>
    </div>
</div>

<div class="layout">

    <div class="sidebar">
        <a href="dashboard?view=home" class="<%= "home".equals(view) ? "active" : "" %>">🏠 Dashboard</a>

        <%
            // Control de visibilidad estricto exclusivo para ADMINISTRADORES
            if ("ADMIN_SISTEMA".equals(rolActual)) {
        %>
            <a href="dashboard?view=maestro" class="<%= "maestro".equals(view) ? "active" : "" %>">⚙️ Maestro</a>
            <a href="dashboard?view=usuarios" class="<%= "usuarios".equals(view) ? "active" : "" %>">👤 Usuarios</a>
        <%
            }
        %>

        <%
            // Control de visibilidad para Personal Operativo (Administradores y Conserjes)
            if ("ADMIN_SISTEMA".equals(rolActual) || "CONSERJE".equals(rolActual)) {
        %>
            <a href="dashboard?view=inmuebles" class="<%= "inmuebles".equals(view) ? "active" : "" %>">🏢 Inmuebles</a>
            <a href="dashboard?view=asignaciones" class="<%= "asignaciones".equals(view) ? "active" : "" %>">🔑 Asignación de viviendas</a>
            <a href="dashboard?view=recibos" class="<%= "recibos".equals(view) ? "active" : "" %>">💵 Recibo y estado de pago</a>
            <a href="dashboard?view=visitas" class="<%= "visitas".equals(view) ? "active" : "" %>">🛂 Control de visitas</a>
        <%
            }
        %>

        <a href="dashboard?view=mesa_ayuda" class="<%= "mesa_ayuda".equals(view) ? "active" : "" %>">🔧 Mesa de ayuda</a>
        <a href="dashboard?view=reservas" class="<%= "reservas".equals(view) ? "active" : "" %>">📆 Reservas de áreas comunes</a>
    </div>

    <div class="content">

        <%
            if ("maestro".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/maestro.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Acceso Restringido: Se requieren privilegios de Administrador del Sistema.</h2>");
                }
            } else if ("inmuebles".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual) || "CONSERJE".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/inmuebles.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Acceso Restringido: Vista exclusiva para personal operativo autorizado.</h2>");
                }
            } else if ("asignaciones".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual) || "CONSERJE".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/asignaciones.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Acceso Restringido: Vista exclusiva para personal operativo autorizado.</h2>");
                }
            } else if ("recibos".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual) || "CONSERJE".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/recibos.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Acceso Restringido: Vista exclusiva para personal operativo autorizado.</h2>");
                }
            } else if ("visitas".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual) || "CONSERJE".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/visitas.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Acceso Restringido: Vista exclusiva para personal operativo autorizado.</h2>");
                }
            } else if ("mesa_ayuda".equals(view)) {
        %>
                <jsp:include page="/WEB-INF/views/mesa_ayuda.jsp" />
        <%
            } else if ("reservas".equals(view)) {
        %>
                <jsp:include page="/WEB-INF/views/reservas.jsp" />
        <%
            } else if ("usuarios".equals(view)) {
                if ("ADMIN_SISTEMA".equals(rolActual)) {
        %>
                    <jsp:include page="/WEB-INF/views/usuarios.jsp" />
        <%
                } else {
                    out.println("<h2 style='color:#991b1b; padding:20px;'>⚠️ Seguridad RBAC: No cuenta con permisos de auditoría sobre credenciales de usuarios.</h2>");
                }
            } else {
        %>
            <h1>Dashboard</h1>

            <div class="card">
                <p>Bienvenido al sistema Habitech, <strong><%= nombreUsuario %></strong>. Su sesión ha sido iniciada con el rol: <code><%= rolActual %></code> ✅</p>
            </div>
        <%
            }
        %>

    </div>

</div>

<div class="footer">
    <p>© 2026 Habitech - Todos los derechos reservados</p>
</div>

</body>
</html>