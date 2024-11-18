#!/bin/bash
# Nombre
# KEY_PLAYHITSGAME
# Prefijo público
# f0e79193372c447899bfd43e0428763a
# Secreto
# uIwSFJuCqOaifCVzV6IKC9db9cS3c4yWE9LvZlmUJ10imu2HzPnd2LzICmiuea7uGAOlgcCjlV0xzVWs69dJog
# Zone ID
# a077f221-8229-11ef-8363-0a5864441c97
# Record ID
# 424c4132-a03f-7e18-0176-e51859fe3b83

# Para obtener el id de la zona IONOS_ZONE_ID
# curl -X GET "https://api.hosting.ionos.com/dns/v1/zones" -H "X-API-Key: f0e79193372c447899bfd43e0428763a.uIwSFJuCqOaifCVzV6IKC9db9cS3c4yWE9LvZlmUJ10imu2HzPnd2LzICmiuea7uGAOlgcCjlV0xzVWs69dJog"

# Para obtener el id del registro IONOS_RECORD_ID
# curl -X GET "https://api.hosting.ionos.com/dns/v1/zones/a077f221-8229-11ef-8363-0a5864441c97?recordName=playhitsgame.es&recordType=A" -H "X-API-Key: f0e79193372c447899bfd43e0428763a.uIwSFJuCqOaifCVzV6IKC9db9cS3c4yWE9LvZlmUJ10imu2HzPnd2LzICmiuea7uGAOlgcCjlV0xzVWs69dJog"

# Configuración de usuario
IONOS_USER="ml_duran@live.com"
IONOS_API_KEY="f0e79193372c447899bfd43e0428763a.uIwSFJuCqOaifCVzV6IKC9db9cS3c4yWE9LvZlmUJ10imu2HzPnd2LzICmiuea7uGAOlgcCjlV0xzVWs69dJog"
IONOS_DOMAIN="playhitsgame.es"
IONOS_ZONE_ID="a077f221-8229-11ef-8363-0a5864441c97"
IONOS_RECORD_ID="424c4132-a03f-7e18-0176-e51859fe3b83"
DNS_RECORD_NAME=""  # El subdominio a actualizar, deja vacío si es el dominio raíz
NEW_IP=$(curl -s ifconfig.me)  # La nueva IP que quieres asignar
LOG_FILE="/home/miguel/scripts/ionos_ip_update.log"
IP_FILE="/home/miguel/scripts/last_ip.txt"

# Verificar si el archivo de IP existe; si no, crearlo
if [ ! -f "$IP_FILE" ]; then
    echo "0.0.0.0" > "$IP_FILE"
fi

# Leer la IP almacenada previamente
LAST_IP=$(cat "$IP_FILE")

# URL base de la API de IONOS
API_URL="https://api.hosting.ionos.com/dns/v1"
JSON_PUT=$(jq -n --arg ip "$NEW_IP" '{"disabled": false,"content": $ip,"ttl": 3600, "prio": 0}')

# Actualizar el registro A con la nueva IP
if [ "$NEW_IP" != "$LAST_IP" ]; then
    # Ejecutar la actualización solo si la IP ha cambiado
    UPDATE_RESPONSE=$(curl -X PUT "$API_URL/zones/$IONOS_ZONE_ID/records/$IONOS_RECORD_ID" -H "X-API-Key:$IONOS_API_KEY" -H 'accept: application/json' -H 'Content-Type: application/json' -d "$JSON_PUT")

    echo "[$(date)] $UPDATE_RESPONSE" | tee -a "$LOG_FILE"

    # Verificar si la respuesta contiene la nueva IP antes de continuar
    if echo "$UPDATE_RESPONSE" | grep -q "$NEW_IP"; then
        # Actualizar el archivo con la nueva IP
        echo "$NEW_IP" > "$IP_FILE"
        echo "[$(date)] El registro DNS se actualizó a la IP $NEW_IP" | tee -a "$LOG_FILE"
    fi
fi



