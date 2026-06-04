# Manual de Usuario - Habitech

## 1. Introduccion

Habitech es un sistema web para la gestion basica de condominios y edificios residenciales. Permite registrar la configuracion del condominio, generar el inventario de departamentos y cocheras, asignar residentes a inmuebles y emitir recibos de mantenimiento.

## 2. Requisitos para usar el sistema

- Navegador web actualizado: Chrome, Edge, Firefox u otro equivalente.
- Servidor del sistema en ejecucion.
- Base de datos PostgreSQL configurada.
- Acceso a la URL del sistema, por ejemplo: `http://localhost:8080/habitech/dashboard`.

## 3. Ingreso al sistema

1. Abrir el navegador.
2. Ingresar a la direccion del sistema:
   `http://localhost:8080/habitech/dashboard`
3. Se mostrara el panel principal de Habitech.

Nota: actualmente el proyecto no implementa una pantalla de login funcional dentro del modulo Java web. El acceso inicia directamente en el dashboard.

## 4. Menu principal

El sistema muestra una barra lateral con las siguientes opciones:

- Dashboard: pantalla inicial del sistema.
- Maestro: configuracion general del condominio.
- Inmuebles: generacion y visualizacion de departamentos y cocheras.
- Asignacion de viviendas: registro de residentes en unidades vacantes.
- Recibo y estado de pago: emision de recibos y registro de pagos.
- Control de visitas, Mesa de ayuda, Reservas de areas comunes y Usuarios: opciones visibles en el menu, pero sin funcionalidad implementada en esta version.

## 5. Modulo Maestro

Este modulo permite registrar los datos base del condominio.

### Registrar una configuracion

1. Entrar a `Maestro`.
2. Completar los campos:
   - Nombre del condominio.
   - Direccion fisica.
   - RUC de 11 digitos.
   - Cantidad de torres.
   - Pisos por torre.
   - Departamentos por piso.
   - Total de cocheras.
3. Presionar `Registrar Parametro`.
4. El sistema guardara el registro y mostrara un mensaje de confirmacion.

### Editar una configuracion

1. En el historial inferior, ubicar la configuracion deseada.
2. Presionar el boton de editar.
3. Modificar los datos en el formulario.
4. Presionar `Actualizar Cambios`.

### Eliminar una configuracion

1. En el historial inferior, ubicar el registro.
2. Presionar el boton de eliminar.
3. Confirmar la accion.

Advertencia: eliminar una configuracion maestra no elimina automaticamente los inmuebles ya generados desde ella. Para reiniciar los inmuebles se debe usar la opcion de vaciar inventario en el modulo Inmuebles.

## 6. Modulo Inmuebles

Este modulo genera automaticamente el inventario fisico del condominio.

### Generar inventario

1. Entrar a `Inmuebles`.
2. Seleccionar una configuracion maestra en la lista desplegable.
3. Presionar `Ejecutar Asistente`.
4. El sistema creara:
   - Departamentos por torre, piso y numero de unidad.
   - Cocheras con numeracion correlativa.
5. Las unidades se crean inicialmente con estado `VACANTE`.

Ejemplo de numeracion:

- Departamento del piso 1, unidad 1: `101`.
- Departamento del piso 4, unidad 2: `402`.
- Cochera 1: `C-001`.

### Revisar el inventario

En la parte inferior se muestra el mapa fisico del condominio con:

- Torre o area.
- Numero de unidad.
- Piso.
- Tipo de unidad: `DEPARTAMENTO` o `COCHERA`.
- Estado: `VACANTE` u `OCUPADO`.

### Vaciar inventario

1. Entrar a `Inmuebles`.
2. Presionar `Vaciar Inventario`.
3. Confirmar la accion.

Advertencia: esta accion elimina todos los inmuebles y, por cascada, tambien puede eliminar asignaciones y recibos relacionados.

## 7. Modulo Asignacion de Viviendas

Este modulo vincula residentes con inmuebles disponibles.

### Registrar una asignacion

1. Entrar a `Asignacion de viviendas`.
2. Seleccionar una unidad inmobiliaria libre.
3. Ingresar:
   - Nombre completo del residente.
   - Documento de identidad.
   - Condicion legal: `PROPIETARIO` o `INQUILINO`.
   - Fecha de ingreso.
4. Presionar `Consolidar Ocupacion`.
5. El sistema registrara la asignacion y cambiara el inmueble a estado `OCUPADO`.

### Liberar una unidad

1. Ubicar el residente en el padron actual.
2. Presionar `Desalojar`.
3. Confirmar la accion.
4. El inmueble volvera a estado `VACANTE`.

## 8. Modulo Recibos y Estados de Pago

Este modulo permite emitir recibos de mantenimiento para residentes asignados y registrar pagos.

### Emitir recibos mensuales

1. Entrar a `Recibo y estado de pago`.
2. Seleccionar el mes a facturar.
3. Ingresar el anio fiscal.
4. Ingresar la cuota de mantenimiento.
5. Presionar `Emitir Recibos del Mes`.

El sistema generara un recibo para cada asignacion activa.

Importante: el sistema evita duplicar recibos del mismo residente para el mismo mes y anio.

### Registrar pago

1. En el historial de cobros, ubicar un recibo con estado `PENDIENTE`.
2. Presionar `Registrar Pago`.
3. El estado cambiara a `PAGADO`.

## 9. Flujo recomendado de trabajo

1. Registrar la configuracion maestra del condominio.
2. Generar el inventario de inmuebles.
3. Asignar residentes a departamentos o cocheras vacantes.
4. Emitir recibos mensuales.
5. Registrar pagos conforme se realicen.

## 10. Mensajes frecuentes

- `Nuevo registro guardado con exito`: la configuracion fue registrada.
- `Registro actualizado correctamente`: los cambios fueron guardados.
- `El inventario para esta configuracion ya existia`: el sistema detecto unidades duplicadas o ya generadas.
- `Residente asignado correctamente`: la unidad fue ocupada.
- `No hay unidades vacantes disponibles`: todos los inmuebles estan ocupados o no existe inventario generado.
- `No se generaron recibos nuevos`: el periodo ya fue facturado o no existen residentes asignados.

## 11. Recomendaciones de uso

- Registrar primero la configuracion maestra antes de generar inmuebles.
- No vaciar el inventario si ya existen asignaciones y recibos importantes.
- Revisar que el RUC tenga 11 digitos.
- Usar montos positivos para la cuota de mantenimiento.
- Evitar generar recibos antes de registrar residentes.

