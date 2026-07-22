--
-- PostgreSQL database dump
--

\restrict esh8TUHSuxjfJhfCurLG4LBlLdGrSBLZ5qmwdn8y07fXocAvf1CQUWHd9cUtoOr

-- Dumped from database version 16.14
-- Dumped by pg_dump version 16.14

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: asignaciones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.asignaciones (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    inventario_maestro_id integer NOT NULL,
    codigo_unidad character varying(50) NOT NULL,
    tipo_adquisicion character varying(20) NOT NULL,
    precio_mensual_pactado numeric(10,2) DEFAULT 0.00 NOT NULL,
    estado character varying(20) DEFAULT 'ACTIVO'::character varying NOT NULL,
    fecha_ingreso date DEFAULT CURRENT_DATE NOT NULL,
    fecha_salida date,
    CONSTRAINT asignaciones_estado_check CHECK (((estado)::text = ANY (ARRAY[('ACTIVO'::character varying)::text, ('FINALIZADO'::character varying)::text]))),
    CONSTRAINT asignaciones_precio_mensual_pactado_check CHECK ((precio_mensual_pactado >= (0)::numeric)),
    CONSTRAINT asignaciones_tipo_adquisicion_check CHECK (((tipo_adquisicion)::text = ANY (ARRAY[('PROPIETARIO'::character varying)::text, ('INQUILINO'::character varying)::text, ('RESERVA'::character varying)::text])))
);


ALTER TABLE public.asignaciones OWNER TO postgres;

--
-- Name: asignaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.asignaciones_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.asignaciones_id_seq OWNER TO postgres;

--
-- Name: asignaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.asignaciones_id_seq OWNED BY public.asignaciones.id;


--
-- Name: comunicados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comunicados (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    titulo character varying(150) NOT NULL,
    contenido text NOT NULL,
    alcance character varying(30) DEFAULT 'GLOBAL'::character varying NOT NULL,
    torre_destino character varying(50),
    categoria character varying(30) DEFAULT 'INFORMATIVO'::character varying NOT NULL,
    estado character varying(20) DEFAULT 'PUBLICADO'::character varying NOT NULL,
    fecha_publicacion timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_expiracion timestamp with time zone,
    CONSTRAINT comunicados_alcance_check CHECK (((alcance)::text = ANY (ARRAY[('GLOBAL'::character varying)::text, ('TORRE_ESPECIFICA'::character varying)::text]))),
    CONSTRAINT comunicados_categoria_check CHECK (((categoria)::text = ANY (ARRAY[('INFORMATIVO'::character varying)::text, ('URGENTE'::character varying)::text, ('MANTENIMIENTO'::character varying)::text, ('ASAMBLEA'::character varying)::text]))),
    CONSTRAINT comunicados_estado_check CHECK (((estado)::text = ANY (ARRAY[('PUBLICADO'::character varying)::text, ('OCULTO'::character varying)::text])))
);


ALTER TABLE public.comunicados OWNER TO postgres;

--
-- Name: comunicados_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comunicados_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comunicados_id_seq OWNER TO postgres;

--
-- Name: comunicados_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comunicados_id_seq OWNED BY public.comunicados.id;


--
-- Name: configuracion_maestra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.configuracion_maestra (
    id integer NOT NULL,
    nombre_condominio character varying(100) NOT NULL,
    direccion character varying(150) NOT NULL,
    ruc character varying(11) NOT NULL,
    cuenta_bancaria character varying(30),
    dia_vencimiento_recibo integer DEFAULT 5 NOT NULL,
    fecha_registro timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    estado character varying(20) DEFAULT 'ACTIVO'::character varying,
    CONSTRAINT configuracion_maestra_dia_vencimiento_recibo_check CHECK (((dia_vencimiento_recibo >= 1) AND (dia_vencimiento_recibo <= 28))),
    CONSTRAINT configuracion_maestra_estado_check CHECK (((estado)::text = ANY (ARRAY[('ACTIVO'::character varying)::text, ('INACTIVO'::character varying)::text])))
);


ALTER TABLE public.configuracion_maestra OWNER TO postgres;

--
-- Name: configuracion_maestra_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.configuracion_maestra_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.configuracion_maestra_id_seq OWNER TO postgres;

--
-- Name: configuracion_maestra_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.configuracion_maestra_id_seq OWNED BY public.configuracion_maestra.id;


--
-- Name: detalle_recibos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_recibos (
    id integer NOT NULL,
    recibo_id integer NOT NULL,
    concepto_descripcion character varying(255) NOT NULL,
    monto_individual numeric(10,2) NOT NULL
);


ALTER TABLE public.detalle_recibos OWNER TO postgres;

--
-- Name: detalle_recibos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detalle_recibos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.detalle_recibos_id_seq OWNER TO postgres;

--
-- Name: detalle_recibos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_recibos_id_seq OWNED BY public.detalle_recibos.id;


--
-- Name: incidencias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.incidencias (
    id integer NOT NULL,
    asignacion_id integer NOT NULL,
    conserje_id integer,
    titulo character varying(100) NOT NULL,
    descripcion text NOT NULL,
    prioridad character varying(20) NOT NULL,
    estado character varying(20) DEFAULT 'ABIERTO'::character varying NOT NULL,
    fecha_reporte timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_cierre timestamp with time zone,
    CONSTRAINT incidencias_estado_check CHECK (((estado)::text = ANY (ARRAY[('ABIERTO'::character varying)::text, ('EN_PROCESO'::character varying)::text, ('RESUELTO'::character varying)::text, ('ANULADO'::character varying)::text]))),
    CONSTRAINT incidencias_prioridad_check CHECK (((prioridad)::text = ANY (ARRAY[('BAJA'::character varying)::text, ('MEDIA'::character varying)::text, ('ALTA'::character varying)::text])))
);


ALTER TABLE public.incidencias OWNER TO postgres;

--
-- Name: incidencias_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.incidencias_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.incidencias_id_seq OWNER TO postgres;

--
-- Name: incidencias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.incidencias_id_seq OWNED BY public.incidencias.id;


--
-- Name: inventario_maestro_infraestructura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventario_maestro_infraestructura (
    id integer NOT NULL,
    configuracion_maestra_id integer NOT NULL,
    tipo_elemento character varying(50) NOT NULL,
    torre character varying(50) DEFAULT 'GENERAL'::character varying NOT NULL,
    nro_piso integer DEFAULT 1 NOT NULL,
    cantidad_registrada integer DEFAULT 0 NOT NULL,
    estado character varying(20) DEFAULT 'ACTIVO'::character varying NOT NULL,
    CONSTRAINT inventario_maestro_infraestructura_estado_check CHECK (((estado)::text = ANY (ARRAY[('ACTIVO'::character varying)::text, ('INACTIVO'::character varying)::text]))),
    CONSTRAINT inventario_maestro_infraestructura_tipo_elemento_check CHECK (((tipo_elemento)::text = ANY (ARRAY[('DEPARTAMENTO'::character varying)::text, ('COCHERA'::character varying)::text, ('SALON_RECEPCION'::character varying)::text, ('GIMNASIO'::character varying)::text, ('PISCINA'::character varying)::text, ('DEPOSITO'::character varying)::text])))
);


ALTER TABLE public.inventario_maestro_infraestructura OWNER TO postgres;

--
-- Name: inventario_maestro_infraestructura_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inventario_maestro_infraestructura_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inventario_maestro_infraestructura_id_seq OWNER TO postgres;

--
-- Name: inventario_maestro_infraestructura_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inventario_maestro_infraestructura_id_seq OWNED BY public.inventario_maestro_infraestructura.id;


--
-- Name: recibos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recibos (
    id integer NOT NULL,
    nro_comprobante character varying(30),
    usuario_id integer NOT NULL,
    usuario_responsable_id integer NOT NULL,
    mes_facturado integer NOT NULL,
    anio_facturado integer NOT NULL,
    total_a_pagar numeric(10,2) DEFAULT 0.00 NOT NULL,
    fecha_emision date DEFAULT CURRENT_DATE NOT NULL,
    estado_pago character varying(20) DEFAULT 'PENDIENTE'::character varying NOT NULL,
    nro_operacion character varying(50),
    medio_pago character varying(50),
    archivo_voucher character varying(255),
    fecha_pago date,
    CONSTRAINT recibos_estado_pago_check CHECK (((estado_pago)::text = ANY (ARRAY[('PENDIENTE'::character varying)::text, ('VALIDANDO'::character varying)::text, ('PAGADO'::character varying)::text, ('ANULADO'::character varying)::text]))),
    CONSTRAINT recibos_mes_facturado_check CHECK (((mes_facturado >= 1) AND (mes_facturado <= 12)))
);


ALTER TABLE public.recibos OWNER TO postgres;

--
-- Name: recibos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.recibos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.recibos_id_seq OWNER TO postgres;

--
-- Name: recibos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.recibos_id_seq OWNED BY public.recibos.id;


--
-- Name: reservas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservas (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    inventario_maestro_id integer NOT NULL,
    fecha_reserva date NOT NULL,
    turno character varying(20) NOT NULL,
    estado character varying(20) DEFAULT 'APROBADA'::character varying NOT NULL,
    fecha_registro timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT reservas_estado_check CHECK (((estado)::text = ANY (ARRAY[('APROBADA'::character varying)::text, ('CANCELADA'::character varying)::text]))),
    CONSTRAINT reservas_turno_check CHECK (((turno)::text = ANY (ARRAY[('MA??ANA'::character varying)::text, ('TARDE'::character varying)::text, ('NOCHE'::character varying)::text])))
);


ALTER TABLE public.reservas OWNER TO postgres;

--
-- Name: reservas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reservas_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reservas_id_seq OWNER TO postgres;

--
-- Name: reservas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reservas_id_seq OWNED BY public.reservas.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id integer NOT NULL,
    username character varying(20) NOT NULL,
    password character varying(255) NOT NULL,
    nombres character varying(50) NOT NULL,
    apellidos character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    telefono character varying(20),
    rol character varying(30) NOT NULL,
    estado character varying(20) DEFAULT 'ACTIVO'::character varying NOT NULL,
    CONSTRAINT usuarios_estado_check CHECK (((estado)::text = ANY (ARRAY[('ACTIVO'::character varying)::text, ('INACTIVO'::character varying)::text]))),
    CONSTRAINT usuarios_rol_check CHECK (((rol)::text = ANY (ARRAY[('ADMIN_SISTEMA'::character varying)::text, ('CONSERJE'::character varying)::text, ('RESIDENTE'::character varying)::text])))
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: visitas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.visitas (
    id integer NOT NULL,
    asignacion_id integer NOT NULL,
    conserje_id integer,
    nombre_visitante character varying(100) NOT NULL,
    dni_visitante character varying(15) NOT NULL,
    placa_vehiculo character varying(15),
    tipo_ingreso character varying(30) NOT NULL,
    fecha_hora_ingreso timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_hora_out timestamp with time zone,
    estado character varying(20) DEFAULT 'EN_CURSO'::character varying NOT NULL,
    CONSTRAINT visitas_estado_check CHECK (((estado)::text = ANY (ARRAY[('EN_CURSO'::character varying)::text, ('FINALIZADO'::character varying)::text, ('ANULADO'::character varying)::text]))),
    CONSTRAINT visitas_tipo_ingreso_check CHECK (((tipo_ingreso)::text = ANY (ARRAY[('VISITA'::character varying)::text, ('DELIVERY'::character varying)::text, ('SERVICIO_TECNICO'::character varying)::text])))
);


ALTER TABLE public.visitas OWNER TO postgres;

--
-- Name: visitas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.visitas_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.visitas_id_seq OWNER TO postgres;

--
-- Name: visitas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.visitas_id_seq OWNED BY public.visitas.id;


--
-- Name: asignaciones id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asignaciones ALTER COLUMN id SET DEFAULT nextval('public.asignaciones_id_seq'::regclass);


--
-- Name: comunicados id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comunicados ALTER COLUMN id SET DEFAULT nextval('public.comunicados_id_seq'::regclass);


--
-- Name: configuracion_maestra id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.configuracion_maestra ALTER COLUMN id SET DEFAULT nextval('public.configuracion_maestra_id_seq'::regclass);


--
-- Name: detalle_recibos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_recibos ALTER COLUMN id SET DEFAULT nextval('public.detalle_recibos_id_seq'::regclass);


--
-- Name: incidencias id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.incidencias ALTER COLUMN id SET DEFAULT nextval('public.incidencias_id_seq'::regclass);


--
-- Name: inventario_maestro_infraestructura id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_maestro_infraestructura ALTER COLUMN id SET DEFAULT nextval('public.inventario_maestro_infraestructura_id_seq'::regclass);


--
-- Name: recibos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos ALTER COLUMN id SET DEFAULT nextval('public.recibos_id_seq'::regclass);


--
-- Name: reservas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservas ALTER COLUMN id SET DEFAULT nextval('public.reservas_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Name: visitas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visitas ALTER COLUMN id SET DEFAULT nextval('public.visitas_id_seq'::regclass);


--
-- Data for Name: asignaciones; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.asignaciones (id, usuario_id, inventario_maestro_id, codigo_unidad, tipo_adquisicion, precio_mensual_pactado, estado, fecha_ingreso, fecha_salida) FROM stdin;
1	5	1	Dpt 101	PROPIETARIO	3000.00	ACTIVO	2026-07-14	\N
2	4	4	Dpt 405	PROPIETARIO	2000.00	ACTIVO	2026-07-15	\N
\.


--
-- Data for Name: comunicados; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comunicados (id, usuario_id, titulo, contenido, alcance, torre_destino, categoria, estado, fecha_publicacion, fecha_expiracion) FROM stdin;
1	2	Asamble general 	El dia Viernes habra una asamblea general para toda la Torre A	TORRE_ESPECIFICA	TORRE A	ASAMBLEA	PUBLICADO	2026-07-20 09:02:17.171188+00	\N
\.


--
-- Data for Name: configuracion_maestra; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.configuracion_maestra (id, nombre_condominio, direccion, ruc, cuenta_bancaria, dia_vencimiento_recibo, fecha_registro, estado) FROM stdin;
1	Los Parques de Villa el Salvador	Av 1ro de Mayo 1945	10728118224	1241241241254	5	2026-07-20 08:51:48.041821+00	ACTIVO
\.


--
-- Data for Name: detalle_recibos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_recibos (id, recibo_id, concepto_descripcion, monto_individual) FROM stdin;
1	1	Mantenimiento Mensual Est??ndar Base	3000.00
2	2	Mantenimiento Mensual Est??ndar Base	2000.00
\.


--
-- Data for Name: incidencias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.incidencias (id, asignacion_id, conserje_id, titulo, descripcion, prioridad, estado, fecha_reporte, fecha_cierre) FROM stdin;
1	1	2	Mantenimiento General	Mantenimiento General	ALTA	RESUELTO	2026-07-20 08:59:05.967266+00	2026-07-20 08:59:13.306413+00
3	2	6	FUGA DE GAS	Hay olor a gas saliendo del departamento 405	ALTA	EN_PROCESO	2026-07-20 09:08:00.802579+00	\N
2	1	6	Derrame de agua	Hay un derrame de agua en la puerta del departamento 101	MEDIA	EN_PROCESO	2026-07-20 09:03:44.078054+00	\N
\.


--
-- Data for Name: inventario_maestro_infraestructura; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventario_maestro_infraestructura (id, configuracion_maestra_id, tipo_elemento, torre, nro_piso, cantidad_registrada, estado) FROM stdin;
1	1	DEPARTAMENTO	TORRE A	1	4	ACTIVO
2	1	DEPARTAMENTO	TORRE A	2	4	ACTIVO
3	1	DEPARTAMENTO	TORRE A	3	4	ACTIVO
4	1	DEPARTAMENTO	TORRE A	4	4	ACTIVO
5	1	DEPARTAMENTO	TORRE A	5	4	ACTIVO
6	1	PISCINA	BLOQUE A	1	1	ACTIVO
\.


--
-- Data for Name: recibos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.recibos (id, nro_comprobante, usuario_id, usuario_responsable_id, mes_facturado, anio_facturado, total_a_pagar, fecha_emision, estado_pago, nro_operacion, medio_pago, archivo_voucher, fecha_pago) FROM stdin;
1	#BP-2026-0001	5	2	8	2026	3000.00	2026-07-20	PAGADO	dasdas	Transferencia Interbancaria	uploads/voucher_1_1784537994304.jpg	2026-07-20
2	#BP-2026-0002	4	2	7	2026	2000.00	2026-07-20	PENDIENTE	\N	\N	\N	\N
\.


--
-- Data for Name: reservas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reservas (id, usuario_id, inventario_maestro_id, fecha_reserva, turno, estado, fecha_registro) FROM stdin;
1	5	5	2026-07-22	MA??ANA	APROBADA	2026-07-20 09:02:46.140771+00
2	4	6	2026-07-30	NOCHE	APROBADA	2026-07-20 09:07:15.64154+00
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id, username, password, nombres, apellidos, email, telefono, rol, estado) FROM stdin;
1	carlos_admin	$2a$12$3JzOYiqouXSu2m5GbVcct.KAmt0KJQGq75eO66P6RM7nkIlPYSidm	Carlos	Mendoza	carlos@habitech.com	987654321	ADMIN_SISTEMA	ACTIVO
2	javier_admin	$2a$10$einRa8VVRzGF.aZI5VroXOBfS/TtJeahQob63C6cgZgtO1HZZ4ZPS	Javier	Mejia	javier@habitech.com	999999999	ADMIN_SISTEMA	ACTIVO
3	luis_conserje	$2a$10$einRa8VVRzGF.aZI5VroXOBfS/TtJeahQob63C6cgZgtO1HZZ4ZPS	Luis	Gomez	luis@habitech.com	988888888	CONSERJE	ACTIVO
4	ana_residente	$2a$10$einRa8VVRzGF.aZI5VroXOBfS/TtJeahQob63C6cgZgtO1HZZ4ZPS	Ana	Silva	ana@habitech.com	977777777	RESIDENTE	ACTIVO
5	jose_residente	$2a$12$WK26qCRLfoLjaydimIWpBu8sgLqHs52RVlza8PK9gyFsjqi1MD/k2	Jose	Mejia	jose@gmail.com	982664646	RESIDENTE	ACTIVO
6	enrique_conserje	$2a$12$EpKe4ySGyt2ipcHkxR9Nd.N1x3Yo73kXDDvsp2BmEKzTnlyg0iNB.	Enrique	Lopez	enrique@gmail.com	963258755	CONSERJE	ACTIVO
\.


--
-- Data for Name: visitas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.visitas (id, asignacion_id, conserje_id, nombre_visitante, dni_visitante, placa_vehiculo, tipo_ingreso, fecha_hora_ingreso, fecha_hora_out, estado) FROM stdin;
1	1	2	Jorge	123123123123		VISITA	2026-07-20 08:58:51.526266+00	\N	EN_CURSO
\.


--
-- Name: asignaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.asignaciones_id_seq', 2, true);


--
-- Name: comunicados_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comunicados_id_seq', 1, true);


--
-- Name: configuracion_maestra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.configuracion_maestra_id_seq', 1, true);


--
-- Name: detalle_recibos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_recibos_id_seq', 2, true);


--
-- Name: incidencias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.incidencias_id_seq', 3, true);


--
-- Name: inventario_maestro_infraestructura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.inventario_maestro_infraestructura_id_seq', 6, true);


--
-- Name: recibos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.recibos_id_seq', 2, true);


--
-- Name: reservas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reservas_id_seq', 2, true);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 6, true);


--
-- Name: visitas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.visitas_id_seq', 1, true);


--
-- Name: asignaciones asignaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asignaciones
    ADD CONSTRAINT asignaciones_pkey PRIMARY KEY (id);


--
-- Name: comunicados comunicados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comunicados
    ADD CONSTRAINT comunicados_pkey PRIMARY KEY (id);


--
-- Name: configuracion_maestra configuracion_maestra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.configuracion_maestra
    ADD CONSTRAINT configuracion_maestra_pkey PRIMARY KEY (id);


--
-- Name: configuracion_maestra configuracion_maestra_ruc_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.configuracion_maestra
    ADD CONSTRAINT configuracion_maestra_ruc_key UNIQUE (ruc);


--
-- Name: detalle_recibos detalle_recibos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_recibos
    ADD CONSTRAINT detalle_recibos_pkey PRIMARY KEY (id);


--
-- Name: incidencias incidencias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.incidencias
    ADD CONSTRAINT incidencias_pkey PRIMARY KEY (id);


--
-- Name: inventario_maestro_infraestructura inventario_maestro_infraestructura_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_maestro_infraestructura
    ADD CONSTRAINT inventario_maestro_infraestructura_pkey PRIMARY KEY (id);


--
-- Name: recibos recibos_nro_comprobante_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_nro_comprobante_key UNIQUE (nro_comprobante);


--
-- Name: recibos recibos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_pkey PRIMARY KEY (id);


--
-- Name: reservas reservas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservas
    ADD CONSTRAINT reservas_pkey PRIMARY KEY (id);


--
-- Name: inventario_maestro_infraestructura uq_elemento_estructural; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_maestro_infraestructura
    ADD CONSTRAINT uq_elemento_estructural UNIQUE (configuracion_maestra_id, tipo_elemento, torre, nro_piso);


--
-- Name: reservas uq_reserva_agenda; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservas
    ADD CONSTRAINT uq_reserva_agenda UNIQUE (inventario_maestro_id, fecha_reserva, turno);


--
-- Name: recibos uq_usuario_periodo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT uq_usuario_periodo UNIQUE (usuario_id, mes_facturado, anio_facturado);


--
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_username_key UNIQUE (username);


--
-- Name: visitas visitas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visitas
    ADD CONSTRAINT visitas_pkey PRIMARY KEY (id);


--
-- Name: idx_asignaciones_usuario; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_asignaciones_usuario ON public.asignaciones USING btree (usuario_id);


--
-- Name: idx_incidencias_asignacion; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_incidencias_asignacion ON public.incidencias USING btree (asignacion_id);


--
-- Name: idx_recibos_asignacion; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recibos_asignacion ON public.recibos USING btree (id);


--
-- Name: idx_recibos_periodo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_recibos_periodo ON public.recibos USING btree (mes_facturado, anio_facturado);


--
-- Name: idx_reservas_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_reservas_fecha ON public.reservas USING btree (fecha_reserva);


--
-- Name: idx_visitas_asignacion; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_visitas_asignacion ON public.visitas USING btree (asignacion_id);


--
-- Name: uq_unidad_real_activa; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uq_unidad_real_activa ON public.asignaciones USING btree (codigo_unidad) WHERE ((estado)::text = 'ACTIVO'::text);


--
-- Name: asignaciones fk_asignacion_maestro; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asignaciones
    ADD CONSTRAINT fk_asignacion_maestro FOREIGN KEY (inventario_maestro_id) REFERENCES public.inventario_maestro_infraestructura(id) ON DELETE RESTRICT;


--
-- Name: asignaciones fk_asignacion_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asignaciones
    ADD CONSTRAINT fk_asignacion_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE RESTRICT;


--
-- Name: comunicados fk_comunicado_emisor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comunicados
    ADD CONSTRAINT fk_comunicado_emisor FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE RESTRICT;


--
-- Name: detalle_recibos fk_detalle_recibo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_recibos
    ADD CONSTRAINT fk_detalle_recibo FOREIGN KEY (recibo_id) REFERENCES public.recibos(id) ON DELETE CASCADE;


--
-- Name: incidencias fk_incidencias_asignacion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.incidencias
    ADD CONSTRAINT fk_incidencias_asignacion FOREIGN KEY (asignacion_id) REFERENCES public.asignaciones(id) ON DELETE RESTRICT;


--
-- Name: incidencias fk_incidencias_conserje; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.incidencias
    ADD CONSTRAINT fk_incidencias_conserje FOREIGN KEY (conserje_id) REFERENCES public.usuarios(id) ON DELETE SET NULL;


--
-- Name: inventario_maestro_infraestructura fk_inventario_maestra; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_maestro_infraestructura
    ADD CONSTRAINT fk_inventario_maestra FOREIGN KEY (configuracion_maestra_id) REFERENCES public.configuracion_maestra(id) ON DELETE RESTRICT;


--
-- Name: recibos fk_recibo_admin; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT fk_recibo_admin FOREIGN KEY (usuario_responsable_id) REFERENCES public.usuarios(id) ON DELETE RESTRICT;


--
-- Name: recibos fk_recibo_residente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT fk_recibo_residente FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE RESTRICT;


--
-- Name: reservas fk_reservas_inventario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservas
    ADD CONSTRAINT fk_reservas_inventario FOREIGN KEY (inventario_maestro_id) REFERENCES public.inventario_maestro_infraestructura(id) ON DELETE RESTRICT;


--
-- Name: reservas fk_reservas_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservas
    ADD CONSTRAINT fk_reservas_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE RESTRICT;


--
-- Name: visitas fk_visitas_asignacion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visitas
    ADD CONSTRAINT fk_visitas_asignacion FOREIGN KEY (asignacion_id) REFERENCES public.asignaciones(id) ON DELETE RESTRICT;


--
-- Name: visitas fk_visitas_conserje; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.visitas
    ADD CONSTRAINT fk_visitas_conserje FOREIGN KEY (conserje_id) REFERENCES public.usuarios(id) ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

\unrestrict esh8TUHSuxjfJhfCurLG4LBlLdGrSBLZ5qmwdn8y07fXocAvf1CQUWHd9cUtoOr

