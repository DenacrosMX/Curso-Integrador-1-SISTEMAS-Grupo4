<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Habitech - Cambio de Contraseña Obligatorio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/configuracion.css">
    <style>
        .error-box { color: #ff6b6b; background: rgba(255,107,107,0.1); padding: 10px; border-radius: 5px; margin-bottom: 15px; font-size: 14px; }
        .instructions { font-size: 13px; color: #aaa; margin-top: 5px; }
    </style>
</head>
<body style="background-color: #0f172a; color: white; font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;">

    <div style="background: #1e293b; padding: 30px; border-radius: 8px; width: 380px; box-shadow: 0 4px 6px rgba(0,0,0,0.3);">
        <h2 style="margin-top: 0; color: #38bdf8; text-align: center;">Actualiza tu Contraseña</h2>
        <p style="font-size: 14px; color: #cbd5e1; text-align: center;">Por seguridad, debes cambiar la clave asignada por defecto antes de continuar.</p>

        <% if (request.getAttribute("errorValidacion") != null) { %>
            <div class="error-box"><%= request.getAttribute("errorValidacion") %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/resetPassword" method="POST">
            <div style="margin-bottom: 20px;">
                <label style="display: block; margin-bottom: 8px; font-size: 14px;">Nueva Contraseña:</label>
                <input type="password" name="txtNuevaPass" required style="width: 100%; padding: 10px; border-radius: 4px; border: 1px solid #475569; background: #0f172a; color: white; box-sizing: border-box;">
                <div class="instructions">
                    Debe tener mínimo 8 caracteres, incluir mayúscula, minúscula, un número y un símbolo (ej: @, $, !, #).
                </div>
            </div>

            <button type="submit" style="width: 100%; padding: 12px; background: #0284c7; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer;">
                Guardar y Entrar al Sistema
            </button>
        </form>
    </div>

</body>
</html>