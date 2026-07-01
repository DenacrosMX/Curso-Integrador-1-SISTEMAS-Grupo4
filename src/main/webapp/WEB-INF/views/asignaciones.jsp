<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modulo-contenedor">

    <%-- FORMULARIO DE ASIGNACIÓN / CONTRATO --%>
    <div class="card-formulario">
        <h2>
            <c:choose>
                <c:when test="${not empty asignacionSeleccionada}">Modificar Contrato de Asignación</c:when>
                <c:otherwise>Registrar Nueva Asignación de Unidad</c:otherwise>
            </c:choose>
        </h2>

        <form action="${pageContext.request.contextPath}/asignaciones" method="POST" class="form-habitech">
            <%-- ID oculto para actualización --%>
            <input type="hidden" name="id" value="${asignacionSeleccionada.id}">
            <input type="hidden" name="estado" value="${not empty asignacionSeleccionada ? asignacionSeleccionada.estado : 'ACTIVO'}">

            <div class="form-row">
                <%-- Selector de Usuarios --%>
                <div class="form-group col-4">
                    <label for="usuarioId">Usuario / Residente</label>
                    <select name="usuarioId" id="usuarioId" required class="form-control">
                        <option value="">-- Seleccione Habitante --</option>
                        <c:forEach var="usr" items="${usuarios}">
                            <option value="${usr.id}" <c:if test="${asignacionSeleccionada.usuarioId == usr.id}">selected</c:if>>
                                ${usr.nombres} (${usr.rol})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <%-- Selector de Infraestructura Base --%>
                <div class="form-group col-4">
                    <label for="inventarioMaestroId">Estructura Base</label>
                    <select name="inventarioMaestroId" id="inventarioMaestroId" required class="form-control" onchange="cargarUnidadesEspecificas(this.value)">
                        <option value="">-- Seleccione Bloque --</option>
                        <c:forEach var="inf" items="${inventario}">
                            <option value="${inf.id}" <c:if test="${asignacionSeleccionada.inventarioMaestroId == inf.id}">selected</c:if>>
                                ${inf.torre} -
                                <c:choose>
                                    <c:when test="${inf.nroPiso < 0}">Sótano ${inf.nroPiso * -1}</c:when>
                                    <c:when test="${inf.nroPiso == 0}">Planta Baja</c:when>
                                    <c:otherwise>Piso ${inf.nroPiso}</c:otherwise>
                                </c:choose>
                                (${inf.tipoElemento})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <%-- Código Específico de Unidad (MODIFICADO A ID) --%>
                <div class="form-group col-4">
                    <label for="codigoUnidadSpecificas">Código de Unidad Específica</label>
                    <select name="unidadEspecificaId" id="codigoUnidadSpecificas" required class="form-control">
                        <option value="">-- Seleccione Código --</option>
                        <c:if test="${not empty asignacionSeleccionada}">
                            <option value="${asignacionSeleccionada.unidadEspecificaId}" selected>
                                ${asignacionSeleccionada.codigoUnidadEspecifica}
                            </option>
                        </c:if>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <%-- Tipo de Adquisición --%>
                <div class="form-group col-4">
                    <label for="tipoAdquisicion">Tipo de Adquisición</label>
                    <select name="tipoAdquisicion" id="tipoAdquisicion" required class="form-control">
                        <option value="PROPIETARIO" <c:if test="${asignacionSeleccionada.tipoAdquisicion == 'PROPIETARIO'}">selected</c:if>>PROPIETARIO</option>
                        <option value="INQUILINO" <c:if test="${asignacionSeleccionada.tipoAdquisicion == 'INQUILINO'}">selected</c:if>>INQUILINO</option>
                        <option value="RESERVA" <c:if test="${asignacionSeleccionada.tipoAdquisicion == 'RESERVA'}">selected</c:if>>RESERVA</option>
                    </select>
                </div>

                <%-- Precio Pactado --%>
                <div class="form-group col-4">
                    <label for="precioMensualPactado">Precio Mensual Pactado</label>
                    <input type="number" name="precioMensualPactado" id="precioMensualPactado" step="0.01" min="0" placeholder="0.00"
                           value="${not empty asignacionSeleccionada ? asignacionSeleccionada.precioMensualPactado : '0.00'}" required class="form-control">
                </div>

                <%-- Fecha de Ingreso --%>
                <div class="form-group col-4">
                    <label for="fechaIngreso">Fecha de Ingreso / Contrato</label>
                    <input type="date" name="fechaIngreso" id="fechaIngreso"
                           value="${not empty asignacionSeleccionada ? asignacionSeleccionada.fechaIngreso : '2026-06-07'}" required class="form-control">
                </div>
            </div>

            <div class="form-actions">
                <c:if test="${not empty asignacionSeleccionada}">
                    <a href="${pageContext.request.contextPath}/dashboard?modulo=asignaciones" class="btn-secondary">Cancelar Edición</a>
                </c:if>
                <button type="submit" class="btn-primary">
                    <c:choose>
                        <c:when test="${not empty asignacionSeleccionada}">Actualizar Contrato</c:when>
                        <c:otherwise>📋 Confirmar Asignación</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <%-- TABLA DE ASIGNACIONES HISTÓRICAS Y ACTIVAS --%>
    <div class="card-tabla">
        <h2>Historial de Asignaciones y Ocupación</h2>
        <table class="tabla-habitech">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Residente / Usuario</th>
                    <th>Estructura General</th>
                    <th>Unidad Real</th>
                    <th>Adquisición</th>
                    <th>Precio Pactado</th>
                    <th>Ingreso</th>
                    <th>Salida</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty asignaciones}">
                        <c:forEach var="asig" items="${asignaciones}">
                            <tr>
                                <td>${asig.id}</td>
                                <td class="text-bold">${asig.nombreUsuario}</td>
                                <td>${asig.detalleInfraestructura}</td>
                                <td><span class="badge-cantidad">${asig.codigoUnidadEspecifica}</span></td>
                                <td><span class="badge-tipo">${asig.tipoAdquisicion}</span></td>
                                <td class="text-bold">S/. ${asig.precioMensualPactado}</td>
                                <td>${asig.fechaIngreso}</td>
                                <td>${not empty asig.fechaSalida ? asig.fechaSalida : '-'}</td>
                                <td>
                                    <span class="badge-estado ${asig.estado == 'ACTIVO' ? 'activo' : 'inactivo'}">
                                        ${asig.estado}
                                    </span>
                                </td>
                                <td>
                                    <div class="acciones-iconos">
                                        <c:if var="esActivo" test="${asig.estado == 'ACTIVO'}">
                                            <a href="${pageContext.request.contextPath}/dashboard?modulo=asignaciones&accion=editar&id=${asig.id}" class="icon-edit" title="Modificar Datos">
                                                ✏️
                                            </a>
                                            <a href="${pageContext.request.contextPath}/asignaciones?accion=finalizar&id=${asig.id}"
                                               class="icon-delete" title="Finalizar Contrato / Desocupar"
                                               onclick="return confirm('¿Está seguro de finalizar este contrato de asignación? Se registrará la fecha de salida de hoy de forma automática.');">
                                                🛑
                                            </a>
                                        </c:if>
                                        <c:if test="${!esActivo}">
                                            <span style="font-size: 0.85em; color: #64748b; font-style: italic;">Histórico</span>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="tabla-vacia">No existen asignaciones de unidades registradas in el sistema.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

</div>

<%-- SCRIPT AJAX ASÍNCRONO PARA LA INTERACTIVIDAD --%>
<script>
function cargarUnidadesEspecificas(infraestructuraId) {
    const selectUnidad = document.getElementById("codigoUnidadSpecificas");

    selectUnidad.innerHTML = '<option value="">-- Seleccione Código --</option>';

    if (!infraestructuraId) return;

    const url = `${pageContext.request.contextPath}/api/unidades-disponibles?infraestructuraId=${infraestructuraId}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error("Error en la respuesta de red");
            return response.json();
        })
        .then(data => {
            if (data && data.length > 0) {
                data.forEach(unidad => {
                    const option = document.createElement("option");
                    option.value = unidad.id; // Modificado: Envía el ID numérico
                    option.textContent = unidad.codigoUnidad; // Muestra la etiqueta de texto legible
                    selectUnidad.appendChild(option);
                });
            } else {
                const option = document.createElement("option");
                option.value = "";
                option.textContent = "⚠️ No hay unidades disponibles";
                option.disabled = true;
                selectUnidad.appendChild(option);
            }
        })
        .catch(error => {
            console.error("Error cargando unidades específicas:", error);
            selectUnidad.innerHTML = '<option value="">⚠️ Error al cargar elementos</option>';
        });
}
</script>