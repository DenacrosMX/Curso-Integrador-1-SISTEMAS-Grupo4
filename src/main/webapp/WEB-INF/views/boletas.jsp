<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container-finance">
    <div class="module-header">
        <h2><i class="fa fa-money"></i> Gestión de Recibos y Boletas de Pago</h2>
        <p>Módulo de facturación, control de deudas, recaudación e historial de comprobantes.</p>
    </div>

    <%-- Mensajes de Retroalimentación del Sistema --%>
    <c:if test="${not empty alertaSuccess}">
        <div class="alert alert-success">
            <i class="fa fa-check-circle"></i> ${alertaSuccess}
        </div>
        <c:remove var="alertaSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty alertaError}">
        <div class="alert alert-danger" style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 4px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
            <i class="fa fa-exclamation-triangle"></i> ${alertaError}
        </div>
        <c:remove var="alertaError" scope="session"/>
    </c:if>

    <%-- Formulario de Emisión (Solo Administrador) --%>
    <c:if test="${sessionScope.usuarioLogueado.rol eq 'ADMIN_SISTEMA'}">
        <div class="card card-emission">
            <div class="card-header">
                <h3><i class="fa fa-plus-circle"></i> Emitir Nuevo Recibo Mensual</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/boletas?accion=emitir" method="POST">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Seleccionar Residente / Unidad:</label>
                            <select name="usuario_id" id="selectResidente" required class="form-control">
                                <option value="" data-monto="">-- Seleccione un residente activo --</option>
                                <c:forEach var="asig" items="${asignaciones}">
                                    <%-- CORREGIDO: Se reemplazó codigoUnidadEspecifica por codigoUnidad --%>
                                    <option value="${asig.usuarioId}" data-monto="${asig.precioMensualPactado}">
                                        ${asig.nombreUsuario} - ${asig.codigoUnidad}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group-row">
                            <div class="form-group">
                                <label>Mes Facturado:</label>
                                <select name="mes" required class="form-control">
                                    <option value="1">Enero</option>
                                    <option value="2">Febrero</option>
                                    <option value="3">Marzo</option>
                                    <option value="4">Abril</option>
                                    <option value="5">Mayo</option>
                                    <option value="6" selected>Junio</option>
                                    <option value="7">Julio</option>
                                    <option value="8">Agosto</option>
                                    <option value="9">Septiembre</option>
                                    <option value="10">Octubre</option>
                                    <option value="11">Noviembre</option>
                                    <option value="12">Diciembre</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Año:</label>
                                <input type="number" name="anio" value="2026" required class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Monto Mantenimiento Estándar (S/):</label>
                            <input type="number" step="0.01" name="monto_base" id="montoBase" placeholder="350.00" required class="form-control">
                        </div>

                        <div class="form-group">
                            <label>Concepto Adicional / Penalidad (Opcional):</label>
                            <input type="text" name="concepto_extra" placeholder="Ej: Uso de Zona de Parrillas" class="form-control">
                        </div>

                        <div class="form-group">
                            <label>Monto Adicional (S/):</label>
                            <input type="number" step="0.01" name="monto_extra" value="0.00" class="form-control">
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary"><i class="fa fa-paper-plane"></i> Generar y Notificar Cobro</button>
                </form>
            </div>
        </div>
    </c:if>

    <div class="card">
        <div class="card-header">
            <h3><i class="fa fa-list"></i> Historial de Cuentas y Comprobantes</h3>
        </div>
        <div class="card-body table-responsive">
            <table class="table-finance">
                <thead>
                    <tr>
                        <th>Nro Comprobante</th>
                        <th>Residente</th>
                        <th>Unidad Física</th>
                        <th>Periodo</th>
                        <th>Total a Pagar</th>
                        <th>Estado</th>
                        <th>Acciones / Flujo</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="recibo" items="${recibos}">
                        <c:set var="estadoUpper" value="${fn:toUpperCase(recibo.estadoPago)}" />

                        <tr>
                            <td><strong>${recibo.nroComprobante}</strong></td>
                            <td>${recibo.nombreResidente}</td>
                            <td><span class="badge badge-info">${recibo.detalleUnidad}</span></td>
                            <td>${recibo.mesFacturado}/2026</td>
                            <td><mark class="monto-destacado">S/ ${recibo.totalAPagar}</mark></td>
                            <td>
                                <%-- CORRECCIÓN: Alineado con los estados reales de la Base de Datos ('PENDIENTE', 'VALIDANDO', 'PAGADO') --%>
                                <c:choose>
                                    <c:when test="${estadoUpper eq 'VALIDANDO'}">
                                        <span class="status-badge status-validando" style="background-color: #ffc107; color: #000; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px;">VALIDANDO</span>
                                    </c:when>
                                    <c:when test="${estadoUpper eq 'PAGADO'}">
                                        <span class="status-badge status-pagado" style="background-color: #28a745; color: #fff; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px;">PAGADO</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-pendiente" style="background-color: #dc3545; color: #fff; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px;">${estadoUpper}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <%-- Flujo del Residente: Declarar Pago --%>
                                <c:if test="${estadoUpper eq 'PENDIENTE' && sessionScope.usuarioLogueado.rol eq 'RESIDENTE'}">
                                    <button onclick="abrirModalPago(${recibo.id}, '${recibo.nroComprobante}', ${recibo.totalAPagar})" class="btn-action btn-pay" style="background-color: #007bff; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;">
                                        <i class="fa fa-upload"></i> Declarar Pago
                                    </button>
                                </c:if>

                                <%-- Flujo del Administrador: Validar e inspeccionar Voucher --%>
                                <c:if test="${estadoUpper eq 'VALIDANDO' && sessionScope.usuarioLogueado.rol eq 'ADMIN_SISTEMA'}">
                                    <div class="admin-validation-box" style="display: flex; gap: 8px; align-items: center;">

                                        <a href="${pageContext.request.contextPath}/${recibo.archivoVoucher}"
                                           target="_blank"
                                           class="btn-action btn-view-voucher"
                                           style="background-color: #17a2b8; color: #fff; padding: 6px 12px; border-radius: 4px; text-decoration: none; font-size: 13px; display: inline-flex; align-items: center; gap: 5px;">
                                            <i class="fa fa-image"></i> Ver Voucher
                                        </a>

                                        <form action="${pageContext.request.contextPath}/boletas"
                                              method="GET"
                                              style="margin: 0; display: inline-block;"
                                              onsubmit="return confirm('¿Confirmas que el depósito de este residente ya se encuentra verificado en la cuenta del condominio?')">

                                            <input type="hidden" name="accion" value="validar">
                                            <input type="hidden" name="id" value="${recibo.id}">

                                            <button type="submit" class="btn-action btn-approve"
                                                    style="background-color: #28a745; color: #fff; padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; font-size: 13px; display: inline-flex; align-items: center; gap: 5px;">
                                                <i class="fa fa-check"></i> Validar Pago
                                            </button>
                                        </form>
                                    </div>
                                </c:if>

                                <%-- Descarga del Comprobante PDF (Se activa con el estado real PAGADO) --%>
                                <c:if test="${estadoUpper eq 'PAGADO'}">
                                    <a href="${pageContext.request.contextPath}/boletas?accion=descargarPdf&id=${recibo.id}"
                                       class="btn-action btn-pdf"
                                       target="_blank"
                                       style="background-color: #dc3545; color: #fff; padding: 6px 12px; border-radius: 4px; text-decoration: none; font-size: 13px; display: inline-flex; align-items: center; gap: 5px;">
                                        <i class="fa fa-file-pdf-o"></i> Descargar Boleta PDF
                                    </a>
                                </c:if>

                                <%-- Indicador pasivo para cobros pendientes cuando no eres el residente --%>
                                <c:if test="${estadoUpper eq 'PENDIENTE' && sessionScope.usuarioLogueado.rol ne 'RESIDENTE'}">
                                    <span class="text-muted" style="color: #a0a0a0; font-size: 13px;"><i class="fa fa-clock-o"></i> Esperando al residente</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recibos}">
                        <tr>
                            <td colspan="7" style="text-align: center; padding: 20px; color: gray;">No se registran recibos vinculados a esta cuenta.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%-- Ventana modal para la declaración de voucher bancario --%>
<div id="modalPago" class="modal-finance" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); justify-content: center; align-items: center; z-index: 9999;">
    <div class="modal-finance-content" style="background: white; padding: 25px; border-radius: 8px; width: 450px; max-width: 90%;">
        <div class="modal-finance-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
            <h3 style="margin: 0;">Declarar Transacción Bancaria</h3>
            <span class="close-modal" onclick="cerrarModalPago()" style="cursor: pointer; font-size: 24px; font-weight: bold;">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/boletas?accion=declararPago" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="recibo_id" id="modal_recibo_id">

            <p>Vas a registrar el pago para el comprobante: <strong id="modal_nro_comprobante"></strong></p>
            <p>Monto Requerido: <strong style="color: green;" id="modal_monto"></strong></p>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px;">Número de Operación Bancaria:</label>
                <input type="text" name="nro_operacion" placeholder="Ej: 05421984" required class="form-control" style="width: 100%; padding: 8px; box-sizing: border-box;">
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px;">Medio de Pago Utilizado:</label>
                <select name="medio_pago" required class="form-control" style="width: 100%; padding: 8px; box-sizing: border-box;">
                    <option value="Transferencia Interbancaria">Transferencia Bancaria BCP/BBVA</option>
                    <option value="Yape">Yape</option>
                    <option value="Plin">Plin</option>
                </select>
            </div>

            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display: block; margin-bottom: 5px;">Subir Foto del Voucher / Captura de Pantalla:</label>
                <input type="file" name="voucher_file" accept="image/*" required class="form-control" style="width: 100%; padding: 5px; box-sizing: border-box;">
            </div>

            <div class="modal-actions" style="display: flex; justify-content: flex-end; gap: 10px;">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalPago()" style="padding: 8px 16px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;">Cancelar</button>
                <button type="submit" class="btn btn-success" style="padding: 8px 16px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"><i class="fa fa-save"></i> Enviar Declaración</button>
            </div>
        </form>
    </div>
</div>

<script>
function abrirModalPago(id, comprobante, monto) {
    document.getElementById('modal_recibo_id').value = id;
    document.getElementById('modal_nro_comprobante').innerText = comprobante;
    document.getElementById('modal_monto').innerText = 'S/ ' + monto.toFixed(2);
    document.getElementById('modalPago').style.display = 'flex';
}
function cerrarModalPago() {
    document.getElementById('modalPago').style.display = 'none';
}

var selectResidente = document.getElementById('selectResidente');
if (selectResidente) {
    selectResidente.addEventListener('change', function() {
        var option = this.options[this.selectedIndex];
        var monto = option.getAttribute('data-monto');
        var inputMonto = document.getElementById('montoBase');

        if (inputMonto) {
            if (monto && monto.trim() !== "") {
                inputMonto.value = parseFloat(monto).toFixed(2);
            } else {
                inputMonto.value = "";
            }
        }
    });
}
</script>