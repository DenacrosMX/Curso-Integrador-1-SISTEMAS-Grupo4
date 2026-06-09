package com.habitech.dao;

import com.habitech.model.Reserva;
import java.util.List;
import java.time.LocalDate; // <-- ¡FALTABA ESTA IMPORTACIÓN CRUCIAL!

public interface ReservaDao {

    // C - Crear una reserva
    boolean insertar(Reserva reserva);

    // R - Listar todas las reservas con JOINS para la grilla administrativa
    List<Reserva> listarTodas();

    // R - Obtener una sola reserva por ID
    Reserva obtenerPorId(int id);

    // U - Cancelar reserva (Cambiar estado a 'CANCELADA' de forma atómica)
    boolean cancelarReserva(int id);

    // Validación Crucial: Verificar si ya existe una reserva para evitar romper el constraint UNIQUE
    // El error en la línea 21 ocurría exactamente aquí por el parámetro LocalDate
    boolean existeReserva(int inventarioMaestroId, LocalDate fecha, String turno);
}