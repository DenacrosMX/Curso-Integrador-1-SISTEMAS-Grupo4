<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String error = (String) request.getAttribute("errorLogin");
%>
<html>
<head>
    <title>Habitech - Ingreso al Sistema</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<div class="login-wrapper">
    <div class="login-container">
        <div class="login-header">
            <h1>🏢 Habitech</h1>
            <p>Ecosistema Integral de Gestión Residencial</p>
        </div>

        <% if (error != null) { %>
            <div class="alert-error">
                <%= error %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/auth" method="POST" class="login-form">
            <div class="form-group">
                <label for="username">DNI / Usuario de Acceso</label>
                <input type="text" id="username" name="username" placeholder="Ingrese su documento" required autocomplete="off">
            </div>

            <div class="form-group">
                <label for="password">Contraseña Criptográfica</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-login">Iniciar Sesión Seguro</button>
        </form>

        <div class="login-footer">
            <p>© 2026 Habitech. Autenticación protegida mediante firmas adaptativas BCrypt.</p>
        </div>
    </div>
</div>

</body>
</html>