<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Habitech - Panel de Control</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">

    <%-- Carga dinámica del CSS según lo que indique el controlador --%>
    <c:if test="${not empty cssModulo}">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/${cssModulo}">
    </c:if>
</head>
<body>

    <header class="dashboard-header">
        <div class="header-logo">
            <h1>Habitech</h1>
        </div>
        <div class="header-info">
            <span class="condominio-nombre">Condominio Altos de la Ensenada</span>

            <div class="usuario-perfil">
                <span class="usuario-nombre">Carlos Mendoza</span>
                <span class="usuario-rol">ADMIN_SISTEMA</span>
            </div>
            <a href="#" class="btn-logout">Cerrar Sesión</a>
        </div>
    </header>

    <div class="dashboard-container">

        <aside class="dashboard-sidebar">
            <nav class="sidebar-nav">
                <ul>
                    <%-- Enlaces del ecosistema Habitech pasando por el DashboardController --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=usuarios" class="nav-link">Usuarios</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=configuracion" class="nav-link">Configuración</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=infraestructura" class="nav-link">Infraestructura</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=asignaciones" class="nav-link">Asignaciones</a></li>
                    <li><a href="#" class="nav-link">Recibos</a></li>
                    <li><a href="#" class="nav-link">Visitas</a></li>
                    <%-- ENLACE INTEGRADO: Módulo de Incidencias --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=incidencias" class="nav-link">Incidencias</a></li>
                    <%-- ENLACE INTEGRADO: Módulo de Reservas --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=reservas" class="nav-link">Reservas</a></li>
                    <%-- ENLACE INTEGRADO: Módulo de Comunicados --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=comunicados" class="nav-link">Comunicados</a></li>
                </ul>
            </nav>
        </aside>

        <main class="dashboard-content">
            <c:choose>
                <%-- Evaluamos el atributo moduloActivo del controlador --%>
                <c:when test="${moduloActivo == 'usuarios'}">
                    <jsp:include page="usuarios.jsp" />
                </c:when>

                <%-- Renderizado dinámico del módulo de Configuración Maestra --%>
                <c:when test="${moduloActivo == 'configuracion'}">
                    <jsp:include page="configuracion.jsp" />
                </c:when>

                <%-- Renderizado dinámico del módulo de Inventario Maestro de Infraestructura --%>
                <c:when test="${moduloActivo == 'infraestructura'}">
                    <jsp:include page="infraestructura.jsp" />
                </c:when>

                <%-- Renderizado dinámico del módulo de Asignaciones (Contratos/Ocupación) --%>
                <c:when test="${moduloActivo == 'asignaciones'}">
                    <jsp:include page="asignaciones.jsp" />
                </c:when>

                <%-- INTEGRACIÓN: Renderizado del módulo de Incidencias --%>
                <c:when test="${moduloActivo == 'incidencias'}">
                    <jsp:include page="incidencias.jsp" />
                </c:when>

                <%-- INTEGRACIÓN: Renderizado del módulo de Reservas --%>
                <c:when test="${moduloActivo == 'reservas'}">
                    <jsp:include page="reservas.jsp" />
                </c:when>

                <%-- INTEGRACIÓN: Renderizado del módulo de Comunicados --%>
                <c:when test="${moduloActivo == 'comunicados'}">
                    <jsp:include page="comunicados.jsp" />
                </c:when>

                <%-- VISTA POR DEFECTO --%>
                <c:otherwise>
                    <div style="padding: 40px; text-align: center; color: #94a3b8;">
                        <h2 style="color: #38bdf8; margin-bottom: 10px;">¡Bienvenido al Panel Administrativo!</h2>
                        <p>Selecciona una opción del menú lateral para comenzar la gestión de Habitech.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

    </div>

    <footer class="dashboard-footer">
        <p>&copy; 2026 Habitech - Sistema de Gestión de Condominios. Todos los derechos reservados.</p>
    </footer>

</body>
</html>