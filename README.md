# HabiTech - Stack de Monitoreo y Mantenimiento

Sistema de monitoreo, observabilidad y mantenimiento automatizado para el proyecto HabiTech (Sistema de Gestion de Condominios).

## Arquitectura

```
habitech-app (Tomcat 10 + WAR)  -->  JMX Exporter :9404
        |
        v
habitech-postgres (PostgreSQL 16)  -->  postgres-exporter :9187
        |
        v
   Prometheus :9090  <--  scraping cada 15 segundos
        |
        v
   Grafana :3000  (dashboards de metricas y logs)
        |
   Loki :3100  <--  Promtail (recoleccion de logs)
```

## Servicios (Docker Compose)

| Servicio | Puerto | Descripcion |
|---|---|---|
| habitech-postgres | 5432 | Base de datos PostgreSQL 16 |
| habitech-app | 8080 | Aplicacion web (Tomcat 10 + Jakarta Servlets) |
| postgres-exporter | 9187 | Exportador de metricas de PostgreSQL |
| prometheus | 9090 | Recolector de metricas |
| loki | 3100 | Agregador de logs |
| promtail | - | Agente que envia logs a Loki |
| grafana | 3000 | Visualizacion de dashboards |

## Inicio rapido

Abrir PowerShell en la carpeta `metricas/` y ejecutar:

### Levantar todos los servicios

```powershell
docker compose up -d --build
```

### Detener todos los servicios

```powershell
docker compose down
```

### Ver estado de los contenedores

```powershell
docker compose ps
```

## Accesos

| Servicio | URL | Credenciales |
|---|---|---|
| Aplicacion HabiTech | http://localhost:8080/habitech/ | javier_admin / Javier@191 |
| Grafana | http://localhost:3000 | admin / habitech2026 |
| Prometheus | http://localhost:9090 | Sin autenticacion |

## Dashboards de Grafana

1. **Vista General de la App** - Metricas JVM: memoria heap, CPU, hilos, solicitudes HTTP, errores
2. **PostgreSQL** - Metricas de la base de datos: conexiones, transacciones, tamano
3. **Logs** - Registros de la aplicacion en tiempo real via Loki

## Estructura de archivos

```
metricas/
├── docker-compose.yml          # Orquestacion de servicios
├── Dockerfile                  # Build de la app con JMX Exporter
├── README.md                   # Este archivo
├── BACKUP_PASOS.md             # Guia de backups paso a paso
├── MANTENIMIENTO_PASOS.md      # Guia de mantenimiento paso a paso
├── backups/                    # Backups automaticos de la BD
├── scripts/
│   └── backup-db.bat           # Script de backup automatizado
├── grafana/
│   └── provisioning/           # Dashboards y datasources de Grafana
├── prometheus/
│   └── prometheus.yml          # Configuracion de scraping
├── loki/
│   └── loki-config.yml         # Configuracion de Loki
└── promtail/
    └── promtail-config.yml     # Configuracion de Promtail
```

## Documentacion

- [BACKUP_PASOS.md](BACKUP_PASOS.md) - Estrategia de backups y restauracion
- [MANTENIMIENTO_PASOS.md](MANTENIMIENTO_PASOS.md) - Plan de mantenimiento, riesgos y recuperacion
- [Plan_Automatizacion_Mantenimiento_HabiTech.docx](Plan_Automatizacion_Mantenimiento_HabiTech.docx) - Informe formal en Word
- [Informe_Plan_Monitoreo_HabiTech.docx](Informe_Plan_Monitoreo_HabiTech.docx) - Plan de monitoreo en Word

## Usuarios de la aplicacion

| Usuario | Rol | Contrasena |
|---|---|---|
| javier_admin | ADMIN_SISTEMA | Javier@191 |
| carlos_admin | ADMIN_SISTEMA | Carlos@2026 |
| luis_conserje | CONSERJE | Javier@191 |
| ana_residente | RESIDENTE | Javier@191 |
