-- ==========================================================================
-- 1. ENTIDAD CENTRAL: USUARIOS (Solo desactivación)
-- ==========================================================================
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

-- 1. Eliminar al usuario con el hash defectuoso
DELETE FROM usuarios WHERE username = 'carlos_admin';

-- 2. Insertarlo limpiamente con el hash exacto verificado para "admin123"
INSERT INTO usuarios (username, password, nombres, apellidos, email, telefono, rol, estado)
VALUES (
    'carlos_admin', 
    '$2a$12$g.V33Z8U0yv5uXw7I1Z6e.pCFrrJq03jE1xIHeoIqO7U6p9k4qP2i', -- Hash exacto para admin123
    'Carlos', 
    'Mendoza', 
    'carlos@habitech.com', 
    '987654321', 
    'ADMIN_SISTEMA', 
    'ACTIVO'
);

-- 3. Confirmar que se guardó correctamente
SELECT username, password, estado FROM usuarios WHERE username = 'carlos_admin';
-- 2. INSERTAR CONSERJE / PERSONAL DE SEGURIDAD
-- Username: luis_conserje  |  Contraseña: 123456 (La que genera tu UsuarioController por defecto)
INSERT INTO usuarios (username, password, nombres, apellidos, email, telefono, rol, estado)
VALUES (
    'luis_conserje', 
    '$2a$12$L7R2QoWhXWzNlQ2bVb6.5uA7V4gV9/NkWpGfZ3R1vExvVb4B5ZKyO', 
    'Luis', 
    'Gomez', 
    'luis.seguridad@habitech.com', 
    '955443322', 
    'CONSERJE', 
    'ACTIVO'
);

-- 3. INSERTAR RESIDENTE / INQUILINO DE PRUEBA
-- Username: ana_residente  |  Contraseña: 123456
INSERT INTO usuarios (username, password, nombres, apellidos, email, telefono, rol, estado)
VALUES (
    'ana_residente', 
    '$2a$12$L7R2QoWhXWzNlQ2bVb6.5uA7V4gV9/NkWpGfZ3R1vExvVb4B5ZKyO', 
    'Ana', 
    'Silva', 
    'ana.silva@outlook.com', 
    '911223344', 
    'RESIDENTE', 
    'ACTIVO'
);

-- =========================================================================
-- VERIFICACIÓN
-- =========================================================================
SELECT id, username, nombres, rol, estado FROM usuarios;

-- ==========================================================================
-- 2. CONFIGURACIÓN MAESTRA LEGAL Y DATOS GENERALES DEL CONDOMINIO
-- ==========================================================================
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

-- ==========================================================================
-- 3. INVENTARIO MAESTRO DE INFRAESTRUCTURA (Bloques y Niveles)
-- ==========================================================================
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


-- ==========================================================================
-- 4. CONTROL DE ASIGNACIONES MÚLTIPLES Y FLEXIBLES (Reestructurada)
-- ==========================================================================
CREATE TABLE asignaciones (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,
    inventario_maestro_id INT NOT NULL, -- Apunta directo al ID del Bloque/Piso (Ej: 18, 19, 20)
    codigo_unidad_especifica VARCHAR(30) NOT NULL, -- Guarda el String generado en el aire (Ej: 'DPTO-505')
    tipo_adquisicion VARCHAR(20) NOT NULL CHECK (tipo_adquisicion IN ('PROPIETARIO', 'INQUILINO', 'RESERVA')),
    precio_mensual_pactado NUMERIC(10,2) NOT NULL DEFAULT 0.00 CHECK (precio_mensual_pactado >= 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'FINALIZADO')), 
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_salida DATE NULL,
    
    -- Llaves foráneas reales y directas
    CONSTRAINT fk_asignacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_maestro FOREIGN KEY (inventario_maestro_id) REFERENCES inventario_maestro_infraestructura(id) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX uq_unidad_real_activa 
ON asignaciones (inventario_maestro_id, codigo_unidad_especifica) 
WHERE estado = 'ACTIVO';

-- ==========================================================================
-- 5. CONTROL FINANCIERO INTEGRADO
-- ==========================================================================
CREATE TABLE recibos (
    id SERIAL PRIMARY KEY,
    nro_comprobante VARCHAR(30) UNIQUE NULL, -- Para el formato #BP-2026-XXXX del PDF
    usuario_id INT NOT NULL, -- El residente (propietario/inquilino) a quien se le cobra
    usuario_responsable_id INT NOT NULL, -- El administrador que emite/valida
    mes_facturado INT NOT NULL CHECK (mes_facturado BETWEEN 1 AND 12),
    anio_facturado INT NOT NULL,
    total_a_pagar NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    fecha_emision DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE', 'VALIDANDO', 'PAGADO', 'ANULADO')), 
    
    -- Datos que registrará el inquilino al pagar
    nro_operacion VARCHAR(50) NULL,
    medio_pago VARCHAR(50) NULL,       
    archivo_voucher VARCHAR(255) NULL, 
    fecha_pago DATE NULL,
    
    -- Claves foráneas
    CONSTRAINT fk_recibo_residente FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_recibo_admin FOREIGN KEY (usuario_responsable_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    -- Restricción: Un residente solo puede tener un único recibo consolidado por mes y año
    CONSTRAINT uq_usuario_periodo UNIQUE (usuario_id, mes_facturado, anio_facturado)
);

CREATE TABLE detalle_recibos (
    id SERIAL PRIMARY KEY,
    recibo_id INT NOT NULL,
    concepto_descripcion VARCHAR(255) NOT NULL, -- Ej: "Alquiler DPTO-802", "Cochera-05", "Agua"
    monto_individual NUMERIC(10,2) NOT NULL,
    
    CONSTRAINT fk_detalle_recibo FOREIGN KEY (recibo_id) REFERENCES recibos(id) ON DELETE CASCADE
);

-- ==========================================================================
-- 6. MÓDULO DE CONTROL DE ACCESOS Y SEGURIDAD (VISITAS)
-- ==========================================================================
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


-- ==========================================================================
-- 7. MÓDULO DE INCIDENCIAS MANTENIMIENTO / OPERATIVAS
-- ==========================================================================
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

-- ==========================================================================
-- 8. MÓDULO DE RESERVAS DE ÁREAS COMUNES
-- ==========================================================================
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

-- ==========================================================================
-- 9. MÓDULO DE COMUNICACIONES
-- ==========================================================================
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

-- ==========================================================================
-- ÍNDICES PARA MÁXIMA VELOCIDAD EN CONSULTAS
-- ==========================================================================
CREATE INDEX idx_recibos_periodo ON recibos(mes_facturado, anio_facturado);
CREATE INDEX idx_recibos_asignacion ON recibos(asignacion_id);
CREATE INDEX idx_asignaciones_usuario ON asignaciones(usuario_id);
CREATE INDEX idx_visitas_asignacion ON visitas(asignacion_id);
CREATE INDEX idx_incidencias_asignacion ON incidencias(asignacion_id);
CREATE INDEX idx_reservas_fecha ON reservas(fecha_reserva);
CREATE INDEX idx_unidades_maestro ON unidades_especificas(inventario_maestro_id);

