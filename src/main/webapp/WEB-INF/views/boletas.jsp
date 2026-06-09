<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container-finance">
    <div class="module-header">
        <h2><i class="fa fa-money"></i> Gestión de Recibos y Boletas de Pago</h2>
        <p>Módulo de facturación, control de deudas, recaudación e historial de comprobantes.</p>
    </div>

    <c:if test="${not empty alertaSuccess}">
        <div class="alert alert-success">
            <i class="fa fa-check-circle"></i> ${alertaSuccess}
        </div>
    </c:if>

    <c:if test="${sessionScope.usuarioLogueado.rol eq 'ADMINISTRADOR' || sessionScope.usuarioLogueado.rol eq 'SUPERADMIN'}">
        <div class="card card-emission">
            <div class="card-header">
                <h3><i class="fa fa-plus-circle"></i> Emitir Nuevo Recibo Mensual</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/boletas?accion=emitir" method="POST">
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Seleccionar Residente / Unidad:</label>
                            <select name="usuario_id" required class="form-control">
                                <option value="">-- Seleccione un residente activo --</option>
                                <c:forEach var="asig" items="${asignaciones}">
                                    <option value="${asig.usuarioId}">
                                        <%-- CORREGIDO: de codigoUnidadSpecifica a codigoUnidadEspecifica para cuadrar con el Modelo Java --%>
                                        ${asig.nombreUsuario} - ${asig.codigoUnidadEspecifica}
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
                            <input type="number" step="0.01" name="monto_base" placeholder="350.00" required class="form-control">
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
                        <tr>
                            <td><strong>${recibo.nroComprobante}</strong></td>
                            <td>${recibo.nombreResidente}</td>
                            <td><span class="badge badge-info">${recibo.detalleUnidad}</span></td>
                            <td>${recibo.mesFacturado}/2026</td>
                            <td><mark class="monto-destacado">S/ ${recibo.totalAPagar}</mark></td>
                            <td>
                                <span class="status-badge status-${recibo.estadoPago.toLowerCase()}">
                                    ${recibo.estadoPago}
                                </span>
                            </td>
                            <td>
                                <c:if test="${recibo.estadoPago eq 'PENDIENTE' && sessionScope.usuarioLogueado.rol eq 'RESIDENTE'}">
                                    <button onclick="abrirModalPago(${recibo.id}, '${recibo.nroComprobante}', ${recibo.totalAPagar})" class="btn-action btn-pay">
                                        <i class="fa fa-upload"></i> Declarar Pago
                                    </button>
                                </c:if>

                                <c:if test="${recibo.estadoPago eq 'VALIDANDO' && (sessionScope.usuarioLogueado.rol eq 'ADMINISTRADOR' || sessionScope.usuarioLogueado.rol eq 'SUPERADMIN')}">
                                    <div class="admin-validation-box">
                                        <a href="${pageContext.request.contextPath}/${recibo.archivoVoucher}" target="_blank" class="btn-action btn-view-voucher">
                                            <i class="fa fa-image"></i> Ver Voucher
                                        </a>
                                        <a href="${pageContext.request.contextPath}/boletas?accion=validar&id=${recibo.id}" class="btn-action btn-approve" onclick="return confirm('¿Confirmas que el dinero está en la cuenta del condominio?')">
                                            <i class="fa fa-check"></i> Validar
                                        </a>
                                    </div>
                                </c:if>

                                <c:if test="${recibo.estadoPago eq 'PAGADO'}">
                                    <a href="${pageContext.request.contextPath}/boletas?accion=descargarPdf&id=${recibo.id}" class="btn-action btn-pdf">
                                        <i class="fa fa-file-pdf-o"></i> Descargar Boleta PDF
                                    </a>
                                </c:if>

                                <c:if test="${recibo.estadoPago eq 'PENDIENTE' && sessionScope.usuarioLogueado.rol ne 'RESIDENTE'}">
                                    <span class="text-muted"><i class="fa fa-clock-o"></i> Esperando al residente</span>
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

<div id="modalPago" class="modal-finance">
    <div class="modal-finance-content">
        <div class="modal-finance-header">
            <h3>Declarar Transacción Bancaria</h3>
            <span class="close-modal" onclick="cerrarModalPago()">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/boletas?accion=declararPago" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="recibo_id" id="modal_recibo_id">

            <p>Vas a registrar el pago para el comprobante: <strong id="modal_nro_comprobante"></strong></p>
            <p>Monto Requerido: <strong style="color: green;" id="modal_monto"></strong></p>

            <div class="form-group">
                <label>Número de Operación Bancaria:</label>
                <input type="text" name="nro_operacion" placeholder="Ej: 05421984" required class="form-control">
            </div>

            <div class="form-group">
                <label>Medio de Pago Utilizado:</label>
                <select name="medio_pago" required class="form-control">
                    <option value="Transferencia Interbancaria">Transferencia Bancaria BCP/BBVA</option>
                    <option value="Yape">Yape</option>
                    <option value="Plin">Plin</option>
                </select>
            </div>

            <div class="form-group">
                <label>Subir Foto del Voucher / Captura de Pantalla:</label>
                <input type="file" name="voucher_file" accept="image/*" required class="form-control">
            </div>

            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="cerrarModalPago()">Cancelar</button>
                <button type="submit" class="btn btn-success"><i class="fa fa-save"></i> Enviar Declaración</button>
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
</script>