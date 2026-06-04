-- ===========================================================================
-- SCRIPT DE CREACIÓN DE TABLAS: GESTIÓN DE CONDOMINIOS
-- SGBD: PostgreSQL
-- ===========================================================================

-- Tabla: configuracion_maestra
CREATE TABLE configuracion_maestra (
    id SERIAL PRIMARY KEY,
    nombre_condominio VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL,
    cantidad_torres INT NOT NULL,
    pisos_por_torre INT NOT NULL,
    dptos_por_piso INT NOT NULL,
    total_cocheras INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: inmuebles
CREATE TABLE inmuebles (
    id SERIAL PRIMARY KEY,
    nro_unidad VARCHAR(10) NOT NULL,
    bloque_torre VARCHAR(30) NOT NULL,
    piso INT NOT NULL,
    tipo_unidad VARCHAR(20) NOT NULL CHECK (tipo_unidad IN ('DEPARTAMENTO', 'COCHERA')),
    estado_ocupacion VARCHAR(20) NOT NULL DEFAULT 'VACANTE' CHECK (estado_ocupacion IN ('VACANTE', 'OCUPADO')),
    
    -- Evita duplicidad física exacta de un departamento o cochera
    CONSTRAINT uq_inmueble_unidad UNIQUE (bloque_torre, piso, nro_unidad)
);

-- Tabla: asignaciones
CREATE TABLE asignaciones (
    id SERIAL PRIMARY KEY,
    inmueble_id INT NOT NULL,
    nombre_residente VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(15) NOT NULL,
    tipo_adquisicion VARCHAR(20) NOT NULL CHECK (tipo_adquisicion IN ('PROPIETARIO', 'INQUILINO')),
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    
    CONSTRAINT fk_asignacion_inmueble FOREIGN KEY (inmueble_id) REFERENCES inmuebles(id) ON DELETE CASCADE,
    -- Un inmueble solo puede tener una asignación activa
    CONSTRAINT uq_inmueble_asignado UNIQUE (inmueble_id) 
);

-- Tabla: recibos
CREATE TABLE recibos (
    id SERIAL PRIMARY KEY,
    asignacion_id INT NOT NULL,
    mes_facturado INT NOT NULL CHECK (mes_facturado BETWEEN 1 AND 12),
    anio_facturado INT NOT NULL,
    monto_mantenimiento NUMERIC(10,2) NOT NULL CHECK (monto_mantenimiento > 0),
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE', 'PAGADO')),
    
    CONSTRAINT fk_recibo_asignacion FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE CASCADE,
    -- Evita duplicar el cobro del mismo mes
    CONSTRAINT uq_recibo_periodo UNIQUE (asignacion_id, mes_facturado, anio_facturado) 
);