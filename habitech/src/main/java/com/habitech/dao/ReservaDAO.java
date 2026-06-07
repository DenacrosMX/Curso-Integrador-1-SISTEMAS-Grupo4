package com.habitech.dao;

import com.habitech.model.ReservaModel;
import java.util.List;

public interface ReservaDAO {
    List<ReservaModel> listarReservas();
    boolean verificarDisponibilidad(String area, java.sql.Date fecha, String turno);
    boolean registrarReserva(ReservaModel reserva);
    boolean eliminarReserva(int idReserva);
}