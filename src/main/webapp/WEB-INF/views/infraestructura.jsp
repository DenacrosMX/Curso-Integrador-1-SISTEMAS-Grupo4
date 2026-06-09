<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modulo-contenedor">

    <%-- FORMULARIO DE REGISTRO / EDICIÓN GENERATIVA --%>
    <div class="card-formulario">
        <h2>
            <c:choose>
                <c:when test="${not empty infraSeleccionada}">Editar Elemento Estructural</c:when>
                <c:otherwise>Generador Masivo de Infraestructura</c:otherwise>
            </c:choose>
        </h2>

        <form action="${pageContext.request.contextPath}/infraestructura" method="POST" class="form-habitech">
            <%-- ID oculto para la operación de actualización --%>
            <input type="hidden" name="id" value="${infraSeleccionada.id}">

            <div class="form-row">
                <%-- Selector Dinámico de Condominios --%>
                <div class="form-group col-4">
                    <label for="configuracionMaestraId">Condominio Asociado</label>
                    <select name="configuracionMaestraId" id="configuracionMaestraId" required class="form-control">
                        <option value="">-- Seleccione Condominio --</option>
                        <c:forEach var="config" items="${configuraciones}">
                            <option value="${config.id}"
                                <c:if test="${infraSeleccionada.configuracionMaestraId == config.id}">selected</c:if>>
                                ${config.nombreCondominio}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <%-- Tipos definidos en el CHECK de la base de datos --%>
                <div class="form-group col-4">
                    <label for="tipoElemento">Tipo de Elemento</label>
                    <select name="tipoElemento" id="tipoElemento" required class="form-control">
                        <option value="DEPARTAMENTO" <c:if test="${infraSeleccionada.tipoElemento == 'DEPARTAMENTO'}">selected</c:if>>DEPARTAMENTO</option>
                        <option value="COCHERA" <c:if test="${infraSeleccionada.tipoElemento == 'COCHERA'}">selected</c:if>>COCHERA</option>
                        <option value="SALON_RECEPCION" <c:if test="${infraSeleccionada.tipoElemento == 'SALON_RECEPCION'}">selected</c:if>>SALÓN DE RECEPCIÓN</option>
                        <option value="GIMNASIO" <c:if test="${infraSeleccionada.tipoElemento == 'GIMNASIO'}">selected</c:if>>GIMNASIO</option>
                        <option value="PISCINA" <c:if test="${infraSeleccionada.tipoElemento == 'PISCINA'}">selected</c:if>>PISCINA</option>
                        <option value="DEPOSITO" <c:if test="${infraSeleccionada.tipoElemento == 'DEPOSITO'}">selected</c:if>>DEPÓSITO</option>
                    </select>
                </div>

                <div class="form-group col-4">
                    <label for="torre">Torre / Bloque</label>
                    <input type="text" name="torre" id="torre" placeholder="Ej: TORRE A, SÓTANOS"
                           value="${not empty infraSeleccionada ? infraSeleccionada.torre : 'GENERAL'}" required class="form-control">
                </div>
            </div>

            <div class="form-row">
                <%-- Sin min="1" para admitir sótanos (valores negativos como -1, -2) y planta baja (0) --%>
                <div class="form-group col-4">
                    <label for="nroPisoInicio" id="lblPisoInicio">
                        <c:choose>
                            <c:when test="${not empty infraSeleccionada}">Número de Piso / Sótano</c:when>
                            <c:otherwise>Desde el Piso / Sótano</c:otherwise>
                        </c:choose>
                    </label>
                    <input type="number" name="nroPisoInicio" id="nroPisoInicio" placeholder="Ej: -2, -1, 0, 1"
                           value="${not empty infraSeleccionada ? infraSeleccionada.nroPiso : 1}" required class="form-control">
                </div>

                <%-- Grupo dinámico que se oculta automáticamente al editar registros individuales --%>
                <div class="form-group col-4" id="grupoPisoFin">
                    <label for="nroPisoFin">Hasta el Piso</label>
                    <input type="number" name="nroPisoFin" id="nroPisoFin" placeholder="Ej: 10"
                           value="${not empty infraSeleccionada ? infraSeleccionada.nroPiso : 1}" class="form-control">
                </div>

                <div class="form-group col-4">
                    <label for="cantidadRegistrada" id="lblCantidad">
                        <c:choose>
                            <c:when test="${not empty infraSeleccionada}">Cantidad de Unidades</c:when>
                            <c:otherwise>Cantidad por cada Piso</c:otherwise>
                        </c:choose>
                    </label>
                    <input type="number" name="cantidadRegistrada" id="cantidadRegistrada" min="1" placeholder="Ej: 4"
                           value="${infraSeleccionada.cantidadRegistrada}" required class="form-control">
                </div>
            </div>

            <div class="form-actions">
                <c:if test="${not empty infraSeleccionada}">
                    <a href="${pageContext.request.contextPath}/dashboard?modulo=infraestructura" class="btn-secondary">Cancelar</a>
                </c:if>
                <button type="submit" class="btn-primary">
                    <c:choose>
                        <c:when test="${not empty infraSeleccionada}">Actualizar Registro</c:when>
                        <c:otherwise>🚀 Generar Estructuras Completas</c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>
    </div>

    <%-- TABLA HISTORIAL DEL INVENTARIO ESTRUCTURAL --%>
    <div class="card-tabla">
        <h2>Inventario Maestro de Estructuras</h2>
        <table class="tabla-habitech">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Condominio</th>
                    <th>Tipo de Elemento</th>
                    <th>Torre / Bloque</th>
                    <th>Nro. Piso / Sótano</th>
                    <th>Cantidad</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty inventario}">
                        <c:forEach var="item" items="${inventario}">
                            <tr>
                                <td>${item.id}</td>
                                <td class="text-bold">${item.nombreCondominio}</td>
                                <td><span class="badge-tipo">${item.tipoElemento}</span></td>
                                <td>${item.torre}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.nroPiso < 0}">Sótano ${item.nroPiso * -1}</c:when>
                                        <c:when test="${item.nroPiso == 0}">Planta Baja (0)</c:when>
                                        <c:otherwise>Piso ${item.nroPiso}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td><span class="badge-cantidad">${item.cantidadRegistrada}</span></td>
                                <td>
                                    <span class="badge-estado ${item.estado == 'ACTIVO' ? 'activo' : 'inactivo'}">
                                        ${item.estado}
                                    </span>
                                </td>
                                <td>
                                    <div class="acciones-iconos">
                                        <%-- Enlace Editar --%>
                                        <a href="${pageContext.request.contextPath}/dashboard?modulo=infraestructura&accion=editar&id=${item.id}" class="icon-edit" title="Editar">
                                            ✏️
                                        </a>
                                        <%-- Enlace Eliminar Lógico --%>
                                        <a href="${pageContext.request.contextPath}/infraestructura?accion=eliminar&id=${item.id}"
                                           class="icon-delete" title="Dar de baja"
                                           onclick="return confirm('¿Está seguro de dar de baja lógicamente este elemento de la infraestructura?');">
                                            🗑️
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="tabla-vacia">No hay elementos de infraestructura registrados en este momento.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

</div>

<%-- CONTROL VISUAL DINÁMICO PARA EDICIÓN VS GENERACIÓN MASIVA --%>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const urlParams = new URLSearchParams(window.location.search);
        const accion = urlParams.get('accion');
        const grupoPisoFin = document.getElementById("grupoPisoFin");

        if (accion === "editar") {
            if (grupoPisoFin) {
                grupoPisoFin.style.display = "none";
                const inputFin = document.getElementById("nroPisoFin");
                if (inputFin) inputFin.removeAttribute("required");
            }
        }
    });
</script>