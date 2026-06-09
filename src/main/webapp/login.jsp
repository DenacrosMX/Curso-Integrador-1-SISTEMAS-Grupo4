<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Habitech - Iniciar Sesión</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

    <div class="login-container">
        <div class="login-header">
            <h1>Habitech</h1>
            <p>Sistema de Control Inmobiliario y Condominios</p>
        </div>

        <%-- Alerta dinámica de control de accesos erróneos --%>
        <c:if test="${not empty errorLogin}">
            <div class="alert-danger">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="12"></line>
                    <line x1="12" y1="16" x2="12.01" y2="16"></line>
                </svg>
                <span>${errorLogin}</span>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth" method="POST" autocomplete="off">

            <div class="form-group">
                <label for="txtUser">Nombre de Usuario</label>
                <input type="text"
                       id="txtUser"
                       name="txtUser"
                       class="input-control"
                       placeholder="Ejm: carlos_admin"
                       required
                       autofocus>
            </div>

            <div class="form-group">
                <label for="txtPass">Contraseña de Acceso</label>
                <input type="password"
                       id="txtPass"
                       name="txtPass"
                       class="input-control"
                       placeholder="••••••••••••"
                       required>
            </div>

            <button type="submit" class="btn-submit">Ingresar al Panel</button>

        </form>

        <div class="login-footer">
            &copy; 2026 Habitech Enterprise. Todos los derechos reservados.
        </div>
    </div>

</body>
</html>