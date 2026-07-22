# Plan de Mantenimiento - HabiTech

## Requisitos previos

- Docker Desktop ejecutandose con los contenedores de HabiTech activos
- PowerShell abierto en la carpeta `metricas/`

---

## 1. VACUUM - Optimizacion de base de datos

```powershell
docker exec habitech-postgres psql -U postgres -d habitech_db -c "VACUUM ANALYZE;"
```

Limpia las tuplas muertas y actualiza las estadisticas del planificador de consultas.
Se recomienda ejecutar semanalmente (domingos).

---

## 2. Health Check - Verificacion de servicios

### Aplicacion web

```powershell
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/habitech/
```

Resultado esperado: **302** (la app funciona).

### Base de datos PostgreSQL

```powershell
docker exec habitech-postgres pg_isready -U postgres
```

Resultado esperado: **"accepting connections"**.

### Prometheus

```powershell
curl.exe -s -o NUL -w "%{http_code}" http://localhost:9090/-/healthy
```

Resultado esperado: **200**.

---

## 3. Monitoreo en Grafana

Acceder a `http://localhost:3000`

- Usuario: `admin`
- Contrasena: `habitech2026`

### Dashboards disponibles

| Dashboard | Que muestra |
|---|---|
| Vista General de la App | Memoria heap, CPU, hilos, tasa de solicitudes y errores |
| PostgreSQL | Conexiones activas, transacciones, tamano de la BD |
| Logs | Registros de la aplicacion en tiempo real |

---

## 4. Calendario de mantenimiento

| Frecuencia | Tarea | Comando |
|---|---|---|
| Diario (3 AM) | Backup automatico de BD | Programador de Tareas de Windows |
| Semanal (domingos) | VACUUM ANALYZE | `docker exec habitech-postgres psql -U postgres -d habitech_db -c "VACUUM ANALYZE;"` |
| Cada 5 minutos | Health check | Verificar app, BD y Prometheus |
| Mensual | Revision de logs | Revisar dashboard de Logs en Grafana |
| Mensual | Limpieza de backups | Eliminar backups con mas de 30 dias |

---

## 5. Riesgos identificados

| Riesgo | Indicador | Donde verlo en Grafana |
|---|---|---|
| Memoria alta (OutOfMemoryError) | Heap usado se acerca al maximo | Vista General > Memoria Heap |
| Conexiones DB agotadas | Conexiones activas cercanas a 100 | PostgreSQL > Conexiones |
| Errores frecuentes | Picos en la tasa de errores | Vista General > Tasa de Errores |
| Disco lleno por backups | Backups acumulados sin limpieza | Revisar carpeta `metricas/backups/` |

---

## 6. Plan de recuperacion

### 6.1 Caida de la aplicacion

**Detener (simular caida):**

```powershell
docker compose stop habitech-app
```

**Recuperar:**

```powershell
docker compose start habitech-app
```

**Verificar:**

```powershell
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/habitech/
```

### 6.2 Perdida de datos

Ver el documento [BACKUP_PASOS.md](BACKUP_PASOS.md) seccion 5 para el proceso de restauracion completo.

### 6.3 Caida de todo el stack

```powershell
docker compose down
docker compose up -d
```

Tiempo estimado de recuperacion: menos de 2 minutos.
