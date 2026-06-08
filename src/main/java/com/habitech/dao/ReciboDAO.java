package com.habitech.dao;

import com.habitech.model.ReciboModel;
import java.math.BigDecimal;
import java.util.List;

public interface ReciboDAO {
    List<ReciboModel> listarRecibos();
    int emitirRecibosMasivos(int mes, int anio, BigDecimal montoEstandar);
    boolean cambiarEstadoPago(int idRecibo, String nuevoEstado);
}