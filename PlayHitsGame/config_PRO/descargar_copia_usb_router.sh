#!/bin/bash

# Configuración
FTP_HOST="192.168.1.1"       # Dirección IP del router
FTP_USER="ml_duran"        # Usuario para el FTP
FTP_PASS="Midama_2014"     # Contraseña para el FTP
# Fecha actual
CURRENT_DATE=$(date +"%Y-%m-%d")
RUTA_REMOTA="/usb1_1"      # Ruta en el USB del router (puedes ajustar esto según sea necesario)
NOMBRE_ARCHIVO="/usb1_1/playhitsgame-$CURRENT_DATE.sql.gz"
ARCHIVO_LOCAL="/home/miguel/tmp/playhitsgame-$CURRENT_DATE.sql.gz"  # Archivo local a subir

# Conexión FTP y carga del archivo
ftp -inv $FTP_HOST <<EOF
user $FTP_USER $FTP_PASS
cd $RUTA_REMOTA
get $NOMBRE_ARCHIVO $ARCHIVO_LOCAL
bye
EOF

echo "El archivo $NOMBRE_ARCHIVO ha sido descargado a $ARCHIVO_LOCAL"
