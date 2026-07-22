# Estrategia de Backups - HabiTech

## Requisitos previos

- Docker Desktop ejecutandose con los contenedores de HabiTech activos
- PowerShell abierto en la carpeta `metricas/`

## 1. Ejecutar backup manual

```powershell
.\scripts\backup-db.bat
```

## 2. Verificar que el backup se creo

```powershell
Get-ChildItem .\backups\*.sql
```

## 3. Ver el log de backups

```powershell
Get-Content .\backups\backup.log
```

## 4. Backup automatizado (Programador de Tareas de Windows)

La tarea **"HabiTech - Backup Diario BD"** esta configurada en el Programador de Tareas de Windows para ejecutarse todos los dias a las 3:00 AM.

Para verificarla:

1. Abrir menu inicio
2. Buscar **"Programador de tareas"**
3. Buscar la tarea **"HabiTech - Backup Diario BD"**

## 5. Restaurar un backup

### 5.1 Detener la aplicacion

```powershell
docker compose stop habitech-app
```

### 5.2 Terminar conexiones activas

```powershell
docker exec habitech-postgres psql -U postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='habitech_db' AND pid <> pg_backend_pid();"
```

### 5.3 Eliminar y recrear la base de datos

```powershell
docker exec habitech-postgres psql -U postgres -c "DROP DATABASE habitech_db;"
docker exec habitech-postgres psql -U postgres -c "CREATE DATABASE habitech_db;"
```

### 5.4 Restaurar desde el archivo de backup

```powershell
Get-Content .\backups\NOMBRE_DEL_BACKUP.sql | docker exec -i habitech-postgres psql -U postgres -d habitech_db
```

> Reemplazar `NOMBRE_DEL_BACKUP.sql` por el archivo mas reciente de la carpeta `backups/`.

### 5.5 Levantar la aplicacion

```powershell
docker compose start habitech-app
```

## Ubicacion de los backups

```
metricas/backups/
```

Cada archivo tiene formato: `habitech_db_YYYY-MM-DD_HH-MM.sql`
