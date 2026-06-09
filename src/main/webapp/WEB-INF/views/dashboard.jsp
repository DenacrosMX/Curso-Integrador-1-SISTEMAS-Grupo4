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
            <%-- SE ELIMINÓ LA ETIQUETA ESTÁTICA DEL CONDOMINIO ASIGNADO PARA EVITAR DATOS FALSOS --%>

            <div class="usuario-perfil">
                <%-- Datos dinámicos del usuario en sesión --%>
                <span class="usuario-nombre">${usuarioLogueado.nombres} ${usuarioLogueado.apellidos}</span>
                <span class="usuario-rol">${usuarioLogueado.rol}</span>
            </div>

            <%-- Enlace al servlet de cierre de sesión seguro --%>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </header>

    <div class="dashboard-container">

        <aside class="dashboard-sidebar">
            <nav class="sidebar-nav">
                <ul>
                    <%-- ENLACE INTEGRADO: REGRESAR AL HOME RESUMEN --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard" class="nav-link ${moduloActivo == 'inicio' ? 'active' : ''}">🏠 Resumen Inicial</a></li>

                    <%-- Enlaces del ecosistema Habitech pasando por el DashboardController --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=usuarios" class="nav-link ${moduloActivo == 'usuarios' ? 'active' : ''}">Usuarios</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=configuracion" class="nav-link ${moduloActivo == 'configuracion' ? 'active' : ''}">Configuración</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=infraestructura" class="nav-link ${moduloActivo == 'infraestructura' ? 'active' : ''}">Infraestructura</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=asignaciones" class="nav-link ${moduloActivo == 'asignaciones' ? 'active' : ''}">Asignaciones</a></li>

                    <%-- Enlace integrado: Módulo de Finanzas (Recibos y Boletas) --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=boletas" class="nav-link ${moduloActivo == 'boletas' ? 'active' : ''}">Recibos</a></li>

                    <%-- Enlaces del ecosistema Habitech --%>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=visitas" class="nav-link ${moduloActivo == 'visitas' ? 'active' : ''}">Visitas</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=incidencias" class="nav-link ${moduloActivo == 'incidencias' ? 'active' : ''}">Incidencias</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=reservas" class="nav-link ${moduloActivo == 'reservas' ? 'active' : ''}">Reservas</a></li>
                    <li><a href="${pageContext.request.contextPath}/dashboard?modulo=comunicados" class="nav-link ${moduloActivo == 'comunicados' ? 'active' : ''}">Comunicados</a></li>
                </ul>
            </nav>

            <div class="sidebar-tools" style="padding: 20px 15px; border-top: 1px solid rgba(255,255,255,0.1); margin-top: 20px;">
                <p style="color: #64748b; font-size: 11px; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; font-weight: bold;">Herramientas</p>
                <div class="dropdown-exportar" style="width: 100%;">
                    <button class="btn-dropdown" onclick="toggleMenuExcel()" style="width: 100%; text-align: left; display: flex; justify-content: space-between; align-items: center;">
                        <span>Exportar Reportes 📊</span>
                        <span>▼</span>
                    </button>
                    <div id="dropdownExcelMenu" class="dropdown-content" style="width: 100%; position: relative; box-shadow: none; margin-top: 5px; background: #1e293b; border: 1px solid rgba(255,255,255,0.1);">
                        <a href="${pageContext.request.contextPath}/exportarExcel?tipo=usuarios" style="color: #cbd5e1; border-bottom: 1px solid rgba(255,255,255,0.05);">👥 Descargar Usuarios</a>
                        <a href="${pageContext.request.contextPath}/exportarExcel?tipo=asignaciones" style="color: #cbd5e1;">🔑 Descargar Asignaciones</a>
                    </div>
                </div>
            </div>
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

                <%-- Core financiero (Boletas y Recibos) --%>
                <c:when test="${moduloActivo == 'boletas'}">
                    <jsp:include page="boletas.jsp" />
                </c:when>

                <%-- Renderizados de módulos del ecosistema --%>
                <c:when test="${moduloActivo == 'visitas'}">
                    <jsp:include page="visitas.jsp" />
                </c:when>

                <c:when test="${moduloActivo == 'incidencias'}">
                    <jsp:include page="incidencias.jsp" />
                </c:when>

                <c:when test="${moduloActivo == 'reservas'}">
                    <jsp:include page="reservas.jsp" />
                </c:when>

                <c:when test="${moduloActivo == 'comunicados'}">
                    <jsp:include page="comunicados.jsp" />
                </c:when>

                <%-- VISTA POR DEFECTO --%>
                <c:otherwise>
                    <div class="home-container">
                        <div class="home-welcome">
                            <h2>Resumen Ejecutivo</h2>
                            <p>Estatus general del ecosistema residencial Habitech en tiempo real.</p>
                        </div>

                        <div class="kpi-grid">
                            <div class="kpi-card">
                                <span class="kpi-category">Comunidad</span>
                                <h3 class="kpi-value">${kpiUsuarios}</h3>
                                <span class="kpi-status status-green">● Residentes Activos</span>
                            </div>

                            <div class="kpi-card">
                                <span class="kpi-category">Finanzas Matriz</span>
                                <h3 class="kpi-value">S/. ${kpiRecaudado}</h3>
                                <span class="kpi-status status-blue">Recaudación Total</span>
                            </div>

                            <div class="kpi-card">
                                <span class="kpi-category">Incidencias Críticas</span>
                                <h3 class="kpi-value value-red">${kpiIncidencias}</h3>
                                <span class="kpi-status status-orange">Requieren atención</span>
                            </div>

                            <div class="kpi-card">
                                <span class="kpi-category">Áreas Comunes</span>
                                <h3 class="kpi-value value-purple">${kpiReservas}</h3>
                                <span class="kpi-status status-gray">Reservadas hoy</span>
                            </div>
                        </div>

                        <div class="home-panels">
                            <div class="panel-main">
                                <h3>📢 Últimos Comunicados Oficiales</h3>

                                <div class="comunicado-item">
                                    <div class="comunicado-meta">
                                        <span class="comunicado-titulo">Mantenimiento de Ascensores</span>
                                        <span class="comunicado-fecha">Hoy, 08:30 AM</span>
                                    </div>
                                    <p>Se realizará la revisión técnica bimestral de los ascensores de la Torre A de 10:00 AM a 12:00 PM.</p>
                                </div>

                                <div class="comunicado-item">
                                    <div class="comunicado-meta">
                                        <span class="comunicado-titulo">Corte Programado de Agua</span>
                                        <span class="comunicado-fecha">Ayer</span>
                                    </div>
                                    <p>Sedapal informa trabajos en la red matriz de la avenida principal. Tomar previsiones del caso.</p>
                                </div>
                            </div>

                            <div class="panel-side">
                                <h3>⚡ Acceso Rápido</h3>
                                <a href="${pageContext.request.contextPath}/dashboard?modulo=usuarios" class="shortcut-btn btn-secondary">+ Registrar Usuario</a>
                                <a href="${pageContext.request.contextPath}/dashboard?modulo=boletas" class="shortcut-btn btn-primary">🧾 Emitir Recibo</a>
                                <a href="${pageContext.request.contextPath}/dashboard?modulo=visitas" class="shortcut-btn btn-secondary">🚗 Monitorear Accesos</a>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

    </div>

    <footer class="dashboard-footer">
        <p>&copy; 2026 Habitech - Sistema de Gestión de Condominios. Todos los derechos reservados.</p>
    </footer>

    <script>
        function toggleMenuExcel() {
            var menu = document.getElementById("dropdownExcelMenu");
            if (menu.style.display === "block") {
                menu.style.display = "none";
            } else {
                menu.style.display = "block";
            }
        }

        window.onclick = function(event) {
            if (!event.target.matches('.btn-dropdown') && !event.target.closest('.btn-dropdown')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.style.display === "block") {
                        openDropdown.style.display = "none";
                    }
                }
            }
        }
    </script>
</body>
</html>