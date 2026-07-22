@echo off
set BACKUP_DIR=%~dp0..\backups

for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set DT=%%I
set FECHA=%DT:~0,4%-%DT:~4,2%-%DT:~6,2%_%DT:~8,2%-%DT:~10,2%

if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

docker exec habitech-postgres pg_dump -U postgres habitech_db > "%BACKUP_DIR%\habitech_db_%FECHA%.sql"

echo [OK] Backup generado: habitech_db_%FECHA%.sql >> "%BACKUP_DIR%\backup.log"
