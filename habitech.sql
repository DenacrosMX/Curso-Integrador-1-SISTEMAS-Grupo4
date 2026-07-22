CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20) NULL, 
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('ADMIN_SISTEMA', 'CONSERJE', 'RESIDENTE')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')) 
);

INSERT INTO usuarios (username, password, nombres, apellidos, email, telefono, rol, estado)
VALUES (
    'carlos_admin', 
    '$2a$12$3JzOYiqouXSu2m5GbVcct.KAmt0KJQGq75eO66P6RM7nkIlPYSidm',
    'Carlos', 
    'Mendoza', 
    'carlos@habitech.com', 
    '987654321', 
    'ADMIN_SISTEMA', 
    'ACTIVO'
);

CREATE TABLE configuracion_maestra (
    id SERIAL PRIMARY KEY,
    nombre_condominio VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    cuenta_bancaria VARCHAR(30) NULL,
    dia_vencimiento_recibo INT NOT NULL DEFAULT 5 CHECK (dia_vencimiento_recibo BETWEEN 1 AND 28),
    fecha_registro TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP, 
    estado VARCHAR(20) DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE inventario_maestro_infraestructura (
    id SERIAL PRIMARY KEY,
    configuracion_maestra_id INT NOT NULL,
    tipo_elemento VARCHAR(50) NOT NULL CHECK (tipo_elemento IN ('DEPARTAMENTO', 'COCHERA', 'SALON_RECEPCION', 'GIMNASIO', 'PISCINA', 'DEPOSITO')),
    torre VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    nro_piso INT NOT NULL DEFAULT 1, 
    cantidad_registrada INT NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')), 
    CONSTRAINT fk_inventario_maestra FOREIGN KEY (configuracion_maestra_id) REFERENCES configuracion_maestra(id) ON DELETE RESTRICT,
    CONSTRAINT uq_elemento_estructural UNIQUE (configuracion_maestra_id, tipo_elemento, torre, nro_piso)
);

CREATE TABLE asignaciones (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,
    inventario_maestro_id INT NOT NULL,
    codigo_unidad VARCHAR(50) NOT NULL,
    tipo_adquisicion VARCHAR(20) NOT NULL CHECK (tipo_adquisicion IN ('PROPIETARIO', 'INQUILINO', 'RESERVA')),
    precio_mensual_pactado NUMERIC(10,2) NOT NULL DEFAULT 0.00 CHECK (precio_mensual_pactado >= 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'FINALIZADO')), 
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_salida DATE NULL,
    CONSTRAINT fk_asignacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_maestro FOREIGN KEY (inventario_maestro_id) REFERENCES inventario_maestro_infraestructura(id) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX uq_unidad_real_activa 
ON asignaciones (codigo_unidad) 
WHERE estado = 'ACTIVO';

CREATE TABLE recibos (
    id SERIAL PRIMARY KEY,
    nro_comprobante VARCHAR(30) UNIQUE NULL,
    usuario_id INT NOT NULL,
    usuario_responsable_id INT NOT NULL,
    mes_facturado INT NOT NULL CHECK (mes_facturado BETWEEN 1 AND 12),
    anio_facturado INT NOT NULL,
    total_a_pagar NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE', 'VALIDANDO', 'PAGADO', 'ANULADO')), 
    nro_operacion VARCHAR(50) NULL,
    medio_pago VARCHAR(50) NULL,       
    archivo_voucher VARCHAR(255) NULL, 
    fecha_pago DATE NULL,
    CONSTRAINT fk_recibo_residente FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_recibo_admin FOREIGN KEY (usuario_responsable_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT uq_usuario_periodo UNIQUE (usuario_id, mes_facturado, anio_facturado)
);

CREATE TABLE detalle_recibos (
    id SERIAL PRIMARY KEY,
    recibo_id INT NOT NULL,
    concepto_descripcion VARCHAR(255) NOT NULL,
    monto_individual NUMERIC(10,2) NOT NULL,
    CONSTRAINT fk_detalle_recibo FOREIGN KEY (recibo_id) REFERENCES recibos(id) ON DELETE CASCADE
);

CREATE TABLE visitas (
    id SERIAL PRIMARY KEY,
    asignacion_id INT NOT NULL,       
    conserje_id INT NULL,              
    nombre_visitante VARCHAR(100) NOT NULL,
    dni_visitante VARCHAR(15) NOT NULL,
    placa_vehiculo VARCHAR(15) NULL,
    tipo_ingreso VARCHAR(30) NOT NULL CHECK (tipo_ingreso IN ('VISITA', 'DELIVERY', 'SERVICIO_TECNICO')),
    fecha_hora_ingreso TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_hora_out TIMESTAMPTZ NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'EN_CURSO' CHECK (estado IN ('EN_CURSO', 'FINALIZADO', 'ANULADO')),
    CONSTRAINT fk_visitas_asignacion FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE RESTRICT,
    CONSTRAINT fk_visitas_conserje FOREIGN KEY (conserje_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE TABLE incidencias (
    id SERIAL PRIMARY KEY,
    asignacion_id INT NOT NULL, 
    conserje_id INT NULL,              
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    prioridad VARCHAR(20) NOT NULL CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO' CHECK (estado IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO', 'ANULADO')),
    fecha_reporte TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMPTZ NULL,
    CONSTRAINT fk_incidencias_asignacion FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE RESTRICT,
    CONSTRAINT fk_incidencias_conserje FOREIGN KEY (conserje_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE TABLE reservas (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,            
    inventario_maestro_id INT NOT NULL, 
    fecha_reserva DATE NOT NULL,
    turno VARCHAR(20) NOT NULL CHECK (turno IN ('MAÑANA', 'TARDE', 'NOCHE')),
    estado VARCHAR(20) NOT NULL DEFAULT 'APROBADA' CHECK (estado IN ('APROBADA', 'CANCELADA')), 
    fecha_registro TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservas_inventario FOREIGN KEY (inventario_maestro_id) REFERENCES inventario_maestro_infraestructura(id) ON DELETE RESTRICT,
    CONSTRAINT uq_reserva_agenda UNIQUE (inventario_maestro_id, fecha_reserva, turno)
);

CREATE TABLE comunicados (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,            
    titulo VARCHAR(150) NOT NULL,
    contenido TEXT NOT NULL,
    alcance VARCHAR(30) NOT NULL DEFAULT 'GLOBAL' CHECK (alcance IN ('GLOBAL', 'TORRE_ESPECIFICA')),
    torre_destino VARCHAR(50) NULL,     
    categoria VARCHAR(30) NOT NULL DEFAULT 'INFORMATIVO' CHECK (categoria IN ('INFORMATIVO', 'URGENTE', 'MANTENIMIENTO', 'ASAMBLEA')),
    estado VARCHAR(20) NOT NULL DEFAULT 'PUBLICADO' CHECK (estado IN ('PUBLICADO', 'OCULTO')), 
    fecha_publicacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMPTZ NULL,    
    CONSTRAINT fk_comunicado_emisor FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

CREATE INDEX idx_recibos_periodo ON recibos(mes_facturado, anio_facturado);
CREATE INDEX idx_recibos_asignacion ON recibos(id);
CREATE INDEX idx_asignaciones_usuario ON asignaciones(usuario_id);
CREATE INDEX idx_visitas_asignacion ON visitas(asignacion_id);
CREATE INDEX idx_incidencias_asignacion ON incidencias(asignacion_id);
CREATE INDEX idx_reservas_fecha ON reservas(fecha_reserva);

CREATE USER habitech_user WITH PASSWORD 'Una_Contra_Muy_Segura_2026';
GRANT ALL PRIVILEGES ON DATABASE habitech TO habitech_user;