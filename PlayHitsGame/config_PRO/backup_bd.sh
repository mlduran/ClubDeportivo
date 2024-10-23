#!/bin/bash

# Configuración
DB_NAME="playhitsgame"
DB_USER="playhitsgame"
DB_PASSWORD="Platero_1970"
BACKUP_DIR="/home/miguel/backup_bd"
DAYS_TO_KEEP=10  # Cantidad de días a mantener

# Fecha actual
CURRENT_DATE=$(date +"%Y-%m-%d")

# Nombre del archivo de backup
BACKUP_FILE="$BACKUP_DIR/$DB_NAME-$CURRENT_DATE.sql"

# Crear backup
mysqldump --no-tablespaces -u $DB_USER -p$DB_PASSWORD $DB_NAME > $BACKUP_FILE

# Comprimir el archivo (opcional, si quieres ahorrar espacio)
gzip $BACKUP_FILE

# Eliminar backups más antiguos que $DAYS_TO_KEEP días
find $BACKUP_DIR -name "$DB_NAME-*.sql.gz" -type f -mtime +$DAYS_TO_KEEP -exec rm -f {} \;

echo "Backup completado: $BACKUP_FILE"

