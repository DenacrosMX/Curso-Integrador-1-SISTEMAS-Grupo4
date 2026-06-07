CREATE TABLE configuracion_maestra (
    id SERIAL PRIMARY KEY,
    nombre_condominio VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL,
    cantidad_torres INT NOT NULL,
    pisos_por_torre INT NOT NULL,
    dptos_por_piso INT NOT NULL,
    total_cocheras INT NOT NULL
);

ALTER TABLE configuracion_maestra ADD COLUMN fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE inmuebles (
    id SERIAL PRIMARY KEY,
    nro_unidad VARCHAR(10) NOT NULL,
    bloque_torre VARCHAR(30) NOT NULL,
    piso INT NOT NULL,
    tipo_unidad VARCHAR(20) NOT NULL CHECK (tipo_unidad IN ('DEPARTAMENTO', 'COCHERA')),
    estado_ocupacion VARCHAR(20) NOT NULL DEFAULT 'VACANTE' CHECK (estado_ocupacion IN ('VACANTE', 'OCUPADO'))
);

-- Evita duplicidad física exacta de un departamento o cochera
ALTER TABLE inmuebles ADD CONSTRAINT uq_inmueble_unidad UNIQUE (bloque_torre, piso, nro_unidad);

CREATE TABLE asignaciones (
    id SERIAL PRIMARY KEY,
    inmueble_id INT NOT NULL,
    nombre_residente VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(15) NOT NULL,
    tipo_adquisicion VARCHAR(20) NOT NULL CHECK (tipo_adquisicion IN ('PROPIETARIO', 'INQUILINO')),
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_asignacion_inmueble FOREIGN KEY (inmueble_id) REFERENCES inmuebles(id) ON DELETE CASCADE,
    CONSTRAINT uq_inmueble_asignado UNIQUE (inmueble_id) -- Un inmueble solo puede tener una asignación activa
);

CREATE TABLE recibos (
    id SERIAL PRIMARY KEY,
    asignacion_id INT NOT NULL,
    mes_facturado INT NOT NULL CHECK (mes_facturado BETWEEN 1 AND 12),
    anio_facturado INT NOT NULL,
    monto_mantenimiento NUMERIC(10,2) NOT NULL CHECK (monto_mantenimiento > 0),
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE', 'PAGADO')),
    CONSTRAINT fk_recibo_asignacion FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE CASCADE,
    CONSTRAINT uq_recibo_periodo UNIQUE (asignacion_id, mes_facturado, anio_facturado) -- Evita duplicar el cobro del mismo mes
);

CREATE TABLE visitas (
    id SERIAL PRIMARY KEY,
    inmueble_id INT NOT NULL,
    conserje_id INT NULL, -- Se deja NULL temporalmente hasta implementar el Módulo de Usuarios
    nombre_visitante VARCHAR(100) NOT NULL,
    dni_visitante VARCHAR(15) NOT NULL,
    placa_vehiculo VARCHAR(15) NULL,
    tipo_ingreso VARCHAR(30) NOT NULL CHECK (tipo_ingreso IN ('VISITA', 'DELIVERY', 'SERVICIO_TECNICO')),
    fecha_hora_ingreso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_hora_out TIMESTAMP NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'EN_CURSO' CHECK (estado IN ('EN_CURSO', 'FINALIZADO')),
    CONSTRAINT fk_visitas_inmueble FOREIGN KEY (inmueble_id) REFERENCES inmuebles(id) ON DELETE CASCADE
);

CREATE TABLE incidencias (
    id SERIAL PRIMARY KEY,
    inmueble_id INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20) NOT NULL CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO' CHECK (estado IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO')),
    fecha_reporte TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP NULL,
    conserje_id INT NULL, -- Se habilitará completamente al implementar el Módulo de Usuarios
    CONSTRAINT fk_incidencias_inmuebles FOREIGN KEY (inmueble_id) REFERENCES inmuebles(id) ON DELETE CASCADE
);

CREATE TABLE reservas (
    id SERIAL PRIMARY KEY,
    inmueble_id INT NOT NULL,
    area_comun VARCHAR(50) NOT NULL CHECK (area_comun IN ('PARRILLA', 'SALON_EVENTOS', 'GIMNASIO')),
    fecha_reserva DATE NOT NULL,
    turno VARCHAR(20) NOT NULL CHECK (turno IN ('MAÑANA', 'NOCHE')),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservas_inmuebles FOREIGN KEY (inmueble_id) REFERENCES inmuebles(id) ON DELETE CASCADE,
    -- Clave única compuesta para evitar que se reserve la misma área en la misma fecha y turno
    CONSTRAINT uq_reserva_agenda UNIQUE (area_comun, fecha_reserva, turno)
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE, -- Documento Nacional de Identidad (DNI)
    password VARCHAR(100) NOT NULL,       -- Cadena encriptada con BCrypt
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('ADMIN_SISTEMA', 'CONSERJE', 'RESIDENTE')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

SELECT * FROM inmuebles;
