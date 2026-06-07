# Manual de Desarrollador - Habitech

## 1. Descripcion tecnica

Habitech es una aplicacion web Java para gestion de condominios. La aplicacion principal se encuentra en la carpeta `habitech` y utiliza Servlets, JSP, DAO, modelos Java, Maven, Tomcat embebido mediante Cargo y PostgreSQL.

Tambien existe una estructura React en la raiz del repositorio, pero la funcionalidad operativa documentada corresponde al proyecto Java web.

## 2. Tecnologias principales

- Java 21.
- Maven.
- Jakarta Servlet 6.
- JSP y JSTL.
- PostgreSQL.
- JDBC.
- Tomcat 10 embebido con Cargo Maven Plugin.
- HTML y CSS en vistas JSP.
- Logback para logging.

## 3. Estructura del proyecto

```text
.
|-- Base de Datos/
|   `-- habitech.sql
|-- habitech/
|   |-- pom.xml
|   |-- logs/
|   |-- src/main/java/com/habitech/
|   |   |-- config/
|   |   |-- controller/
|   |   |-- dao/
|   |   |-- dao/impl/
|   |   `-- model/
|   `-- src/main/webapp/
|       |-- css/
|       `-- WEB-INF/views/
|-- public/
|-- src/
|-- package.json
`-- README.md
```

## 4. Arquitectura

La aplicacion sigue una arquitectura MVC simple:

- Model: clases de datos ubicadas en `com.habitech.model`.
- View: paginas JSP ubicadas en `src/main/webapp/WEB-INF/views`.
- Controller: servlets ubicados en `com.habitech.controller`.
- DAO: interfaces e implementaciones para acceso a datos.
- Config: conexion JDBC a PostgreSQL.

## 5. Modulos implementados

### Dashboard

Controlador:

- `DashboardController`

Ruta:

- `GET /dashboard`

Funcion:

- Recibe el parametro `view`.
- Carga datos segun el modulo seleccionado.
- Redirige internamente a `dashboard.jsp`.

Valores principales de `view`:

- `home`
- `maestro`
- `inmuebles`
- `asignaciones`
- `recibos`
- `visitas`
- `mesa_ayuda`
- `reservas`

### Maestro

Controlador:

- `MaestroController`

Rutas:

- `POST /maestro`: insertar o actualizar configuracion.
- `GET /maestro?action=delete&id=...`: eliminar configuracion.

DAO:

- `MaestroDAO`
- `MaestroDAOImpl`

Tabla:

- `configuracion_maestra`

### Inmuebles

Controlador:

- `InmuebleController`

Ruta:

- `POST /inmuebles`

Acciones:

- `action=generar`: genera inventario desde una configuracion maestra.
- `action=limpiar`: elimina todo el inventario.

DAO:

- `InmuebleDAO`
- `InmuebleDAOImpl`

Tabla:

- `inmuebles`

### Asignaciones

Controlador:

- `AsignacionController`

Ruta:

- `POST /asignaciones`

Acciones:

- `action=registrar`: asigna un residente a un inmueble vacante.
- `action=eliminar`: elimina la asignacion y libera el inmueble.

DAO:

- `AsignacionDAO`
- `AsignacionDAOImpl`

Tabla:

- `asignaciones`

### Recibos

Controlador:

- `ReciboController`

Ruta:

- `POST /recibos`

Acciones:

- `action=generarFacturacion`: genera recibos masivos.
- `action=pagar`: marca un recibo como pagado.

DAO:

- `ReciboDAO`
- `ReciboDAOImpl`

Tabla:

- `recibos`

### Control de visitas

Controlador:

- `VisitaController`

Ruta:

- `POST /visitas`

Acciones:

- `action=ingreso`: registra el ingreso de un visitante.
- `action=darSalida`: registra la salida y finaliza la visita.

DAO:

- `VisitaDAO`
- `VisitaDAOImpl`

Tabla:

- `visitas`

### Mesa de ayuda

Controlador:

- `IncidenciaController`

Ruta:

- `POST /incidencias`

Acciones:

- `action=reportar`: registra una nueva incidencia.
- `action=cambiarEstado`: actualiza el estado del ticket.

DAO:

- `IncidenciaDAO`
- `IncidenciaDAOImpl`

Tabla:

- `incidencias`

### Reservas

Controlador:

- `ReservaController`

Ruta:

- `POST /reservas`

Acciones:

- `action=reservar`: registra una reserva de area comun.
- `action=cancelar`: elimina una reserva.

DAO:

- `ReservaDAO`
- `ReservaDAOImpl`

Tabla:

- `reservas`

## 6. Base de datos

El script principal se encuentra en:

```text
Base de Datos/habitech.sql
```

Nombre de base de datos usado por la aplicacion:

```text
habitech_db
```

Tablas:

- `configuracion_maestra`
- `inmuebles`
- `asignaciones`
- `recibos`
- `visitas`
- `incidencias`
- `reservas`

### Relaciones

- `asignaciones.inmueble_id` referencia a `inmuebles.id`.
- `recibos.asignacion_id` referencia a `asignaciones.id`.
- `visitas.inmueble_id` referencia a `inmuebles.id`.
- `incidencias.inmueble_id` referencia a `inmuebles.id`.
- `reservas.inmueble_id` referencia a `inmuebles.id`.

### Restricciones importantes

- `inmuebles.tipo_unidad`: solo permite `DEPARTAMENTO` o `COCHERA`.
- `inmuebles.estado_ocupacion`: solo permite `VACANTE` u `OCUPADO`.
- `asignaciones.tipo_adquisicion`: solo permite `PROPIETARIO` o `INQUILINO`.
- `recibos.estado_pago`: solo permite `PENDIENTE` o `PAGADO`.
- `visitas.tipo_ingreso`: solo permite `VISITA`, `DELIVERY` o `SERVICIO_TECNICO`.
- `visitas.estado`: solo permite `EN_CURSO` o `FINALIZADO`.
- `incidencias.prioridad`: solo permite `BAJA`, `MEDIA` o `ALTA`.
- `incidencias.estado`: solo permite `ABIERTO`, `EN_PROCESO` o `RESUELTO`.
- `reservas.area_comun`: solo permite `PARRILLA`, `SALON_EVENTOS` o `GIMNASIO`.
- Un inmueble no puede tener mas de una asignacion activa.
- Un recibo no puede duplicarse para la misma asignacion, mes y anio.
- Una reserva no puede duplicarse para la misma area comun, fecha y turno.

## 7. Configuracion de conexion

Archivo:

```text
habitech/src/main/java/com/habitech/config/ConexionDB.java
```

Valores actuales:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/habitech_db";
private static final String USER = "postgres";
private static final String PASS = "123456";
```

Para otro entorno, cambiar host, puerto, nombre de base de datos, usuario o clave segun corresponda.

Recomendacion tecnica: en una version productiva, mover estas credenciales a variables de entorno o a un archivo de configuracion externo.

## 8. Instalacion y ejecucion local

### 8.1 Requisitos

- JDK 21 instalado.
- Maven instalado.
- PostgreSQL instalado y en ejecucion.
- Puerto 8080 disponible.

### 8.2 Crear base de datos

Desde PostgreSQL o terminal:

```bash
createdb -U postgres habitech_db
```

Luego ejecutar el script:

```bash
psql -U postgres -d habitech_db -f "Base de Datos/habitech.sql"
```

Si se usa pgAdmin:

1. Crear una base llamada `habitech_db`.
2. Abrir Query Tool.
3. Cargar y ejecutar el contenido de `Base de Datos/habitech.sql`.

### 8.3 Compilar

Desde la carpeta del proyecto Java:

```bash
cd habitech
mvn clean package
```

El WAR generado queda en:

```text
habitech/target/habitech.war
```

### 8.4 Ejecutar con Tomcat embebido

Desde `habitech`:

```bash
mvn cargo:run
```

Abrir:

```text
http://localhost:8080/habitech/dashboard
```

## 9. Flujo de datos

### Configuracion maestra

1. El usuario envia el formulario `POST /maestro`.
2. `MaestroController` crea un `MaestroModel`.
3. `MaestroDAOImpl` inserta o actualiza en `configuracion_maestra`.
4. El controlador redirige a `/dashboard?view=maestro`.

### Generacion de inventario

1. El usuario selecciona una configuracion maestra.
2. `InmuebleController` obtiene el registro maestro.
3. `InmuebleDAOImpl.generarInventarioAutomatico` crea departamentos y cocheras.
4. Se usa transaccion JDBC con `commit` y `rollback`.
5. El inventario queda en estado `VACANTE`.

### Asignacion de residentes

1. El usuario selecciona un inmueble vacante.
2. `AsignacionController` crea una `AsignacionModel`.
3. `AsignacionDAOImpl.registrarAsignacion` inserta la asignacion.
4. En la misma transaccion actualiza el inmueble a `OCUPADO`.

### Emision de recibos

1. El usuario selecciona mes, anio y monto.
2. `ReciboController` llama a `emitirRecibosMasivos`.
3. El DAO recorre las asignaciones activas.
4. Inserta recibos con estado inicial `PENDIENTE`.
5. La restriccion unica evita recibos duplicados por periodo.

### Pago de recibos

1. El usuario presiona `Registrar Pago`.
2. `ReciboController` recibe el `idRecibo`.
3. `ReciboDAOImpl.cambiarEstadoPago` actualiza el estado a `PAGADO`.

### Control de visitas

1. El usuario registra un visitante desde `dashboard?view=visitas`.
2. `VisitaController` recibe la accion `ingreso`.
3. `VisitaDAOImpl.registrarIngreso` inserta la visita con estado inicial `EN_CURSO`.
4. Cuando se registra la salida, `registrarSalida` actualiza `fecha_hora_out` y estado `FINALIZADO`.

### Mesa de ayuda

1. El usuario reporta una incidencia desde `dashboard?view=mesa_ayuda`.
2. `IncidenciaController` recibe la accion `reportar`.
3. `IncidenciaDAOImpl.registrarIncidencia` inserta el ticket con estado `ABIERTO`.
4. La accion `cambiarEstado` permite pasar el ticket a `EN_PROCESO` o `RESUELTO`.

### Reservas

1. El usuario registra una reserva desde `dashboard?view=reservas`.
2. `ReservaController` valida disponibilidad con `verificarDisponibilidad`.
3. `ReservaDAOImpl.registrarReserva` inserta la reserva.
4. La accion `cancelar` elimina la reserva seleccionada.

## 10. Vistas y estilos

Vistas:

- `dashboard.jsp`
- `maestro.jsp`
- `inmuebles.jsp`
- `asignaciones.jsp`
- `recibos.jsp`
- `visitas.jsp`
- `mesa_ayuda.jsp`
- `reservas.jsp`

Estilos:

- `dashboard.css`
- `maestro.css`
- `inmuebles.css`
- `asignaciones.css`
- `recibos.css`
- `visitas.css`
- `mesa_ayuda.css`
- `reservas.css`

`dashboard.jsp` carga estilos por modulo segun el valor del parametro `view`.

## 11. Dependencias Maven destacadas

- `jakarta.servlet-api`: API Servlet.
- `jakarta.servlet.jsp.jstl-api` y `jakarta.servlet.jsp.jstl`: soporte JSTL.
- `postgresql`: driver JDBC.
- `jbcrypt`: hashing de contrasenias, disponible pero no usado en la funcionalidad actual.
- `poi` y `poi-ooxml`: soporte para documentos Excel, disponibles pero no usados en la funcionalidad actual.
- `logback-classic` y `logback-core`: logging.
- `cargo-maven3-plugin`: ejecucion con Tomcat embebido.

## 12. Pruebas manuales sugeridas

1. Crear una configuracion maestra valida.
2. Editar la configuracion y verificar el cambio en la tabla.
3. Generar inventario desde la configuracion.
4. Confirmar que las unidades aparecen como `VACANTE`.
5. Registrar un residente en una unidad.
6. Confirmar que la unidad cambia a `OCUPADO`.
7. Emitir recibos para un mes y anio.
8. Intentar emitir el mismo periodo de nuevo y verificar que no se duplique.
9. Registrar pago de un recibo pendiente.
10. Verificar que el estado cambia a `PAGADO`.
11. Liberar una asignacion y confirmar que el inmueble vuelve a `VACANTE`.
12. Registrar una visita y luego marcar su salida.
13. Reportar una incidencia y cambiar su estado a `EN_PROCESO` y `RESUELTO`.
14. Registrar una reserva y verificar que no se duplique en la misma area, fecha y turno.

## 13. Mantenimiento y recomendaciones

- No dejar credenciales hardcodeadas en produccion.
- Agregar validaciones adicionales en backend, no solo en formularios HTML.
- Corregir caracteres mal codificados visibles en algunos JSP y mensajes.
- Implementar autenticacion si se requiere acceso por roles.
- Agregar pruebas unitarias para DAO y pruebas de integracion para controladores.
- Evitar usar `TRUNCATE ... CASCADE` en produccion sin respaldo previo.
- Registrar errores con logger en lugar de usar solamente `e.printStackTrace()`.

## 14. Posibles mejoras futuras

- Login y gestion de usuarios.
- Roles de administrador, residente y operador.
- Modulo de incidencias o mesa de ayuda.
- Exportacion de recibos a PDF o Excel.
- Filtros de busqueda en padrones y recibos.
- Configuracion externa de conexion a base de datos.

