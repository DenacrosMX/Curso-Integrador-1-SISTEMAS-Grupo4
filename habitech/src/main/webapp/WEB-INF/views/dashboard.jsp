<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String view = request.getParameter("view");
    if (view == null) view = "home";
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

    <%-- CAMBIO 1: Estilo aislado exclusivo para el Módulo de Recibos y Pagos --%>
    <% if ("recibos".equals(view)) { %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recibos.css">
    <% } %>
</head>

<body>

<div class="header">
    <h2>Habitech - Sistema de Gestión</h2>
</div>

<div class="layout">

    <div class="sidebar">
        <a href="dashboard?view=home" class="<%= "home".equals(view) ? "active" : "" %>">🏠 Dashboard</a>
        <a href="dashboard?view=maestro" class="<%= "maestro".equals(view) ? "active" : "" %>">⚙️ Maestro</a>
        <a href="dashboard?view=inmuebles" class="<%= "inmuebles".equals(view) ? "active" : "" %>">🏢 Inmuebles</a>
        <a href="dashboard?view=asignaciones" class="<%= "asignaciones".equals(view) ? "active" : "" %>">🔑 Asignación de viviendas</a>

        <%-- CAMBIO 2: Enlace del menú actualizado para apuntar al módulo financiero de recibos --%>
        <a href="dashboard?view=recibos" class="<%= "recibos".equals(view) ? "active" : "" %>">💵 Recibo y estado de pago</a>

        <a href="#">Control de visitas</a>
        <a href="#">Mesa de ayuda</a>
        <a href="#">Reservas de áreas comunes</a>
        <a href="#">👤 Usuarios</a>
    </div>

    <div class="content">

        <%
            if ("maestro".equals(view)) {
        %>
            <%-- Inclusión aislada del fragmento visual del Módulo Maestro --%>
            <jsp:include page="/WEB-INF/views/maestro.jsp" />
        <%
            } else if ("inmuebles".equals(view)) {
        %>
            <%-- Inclusión aislada del fragmento visual del Módulo Inmuebles --%>
            <jsp:include page="/WEB-INF/views/inmuebles.jsp" />
        <%
            } else if ("asignaciones".equals(view)) {
        %>
            <%-- Inclusión aislada del fragmento visual del Módulo de Asignaciones --%>
            <jsp:include page="/WEB-INF/views/asignaciones.jsp" />
        <%
            } else if ("recibos".equals(view)) {
        %>
            <%-- CAMBIO 3: Inclusión aislada del fragmento visual del Módulo Recibos y Pagos --%>
            <jsp:include page="/WEB-INF/views/recibos.jsp" />
        <%
            } else {
        %>
            <h1>Dashboard</h1>

            <div class="card">
                <p>Bienvenido al sistema Habitech ✅</p>
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