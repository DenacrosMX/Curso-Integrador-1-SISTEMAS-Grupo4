package com.habitech.dao;

import com.habitech.model.Recibo;
import java.util.List;

public interface ReciboDao {
    boolean insertarConDetalles(Recibo recibo);
    List<Recibo> listarTodo();
    List<Recibo> listarPorInquilino(int usuarioId);
    Recibo obtenerPorId(int id);
    boolean declararPago(int reciboId, String nroOp, String medio, String rutaVoucher, java.sql.Date fechaPago);
    boolean cambiarEstadoPago(int reciboId, String nuevoEstado, int adminId);
}