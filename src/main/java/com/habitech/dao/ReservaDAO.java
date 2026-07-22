package com.habitech.dao;

import com.habitech.model.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface ReservaDao {

    boolean insertar(Reserva reserva);

    List<Reserva> listarTodas();

    Reserva obtenerPorId(int id);

    boolean cancelarReserva(int id);

    boolean existeReserva(int inventarioMaestroId, LocalDate fecha, String turno);
}