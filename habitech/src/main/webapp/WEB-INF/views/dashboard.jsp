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
        <a href="dashboard?view=recibos" class="<%= "recibos".equals(view) ? "active" : "" %>">💵 Recibo y estado de pago</a>
        <a href="dashboard?view=visitas" class="<%= "visitas".equals(view) ? "active" : "" %>">🛂 Control de visitas</a>
        <a href="dashboard?view=mesa_ayuda" class="<%= "mesa_ayuda".equals(view) ? "active" : "" %>">🔧 Mesa de ayuda</a>

        <%-- INTEGRACIÓN MÓDULO 8: Enlace del menú activado para las Reservas de Áreas Comunes --%>
        <a href="dashboard?view=reservas" class="<%= "reservas".equals(view) ? "active" : "" %>">📆 Reservas de áreas comunes</a>

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
            <%-- Inclusión aislada del fragmento visual del Módulo Recibos y Pagos --%>
            <jsp:include page="/WEB-INF/views/recibos.jsp" />
        <%
            } else if ("visitas".equals(view)) {
        %>
            <%-- Inclusión aislada del fragmento visual de Control de Visitas --%>
            <jsp:include page="/WEB-INF/views/visitas.jsp" />
        <%
            } else if ("mesa_ayuda".equals(view)) {
        %>
            <%-- Inclusión aislada del fragmento visual de la Mesa de Ayuda --%>
            <jsp:include page="/WEB-INF/views/mesa_ayuda.jsp" />
        <%
            } else if ("reservas".equals(view)) {
        %>
            <%-- INTEGRACIÓN MÓDULO 8: Inclusión aislada del fragmento visual de Reservas de Áreas Comunes --%>
            <jsp:include page="/WEB-INF/views/reservas.jsp" />
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