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

# URL base de la API de IONOS
API_URL="https://api.hosting.ionos.com/dns/v1"
JSON_PUT=$(jq -n --arg ip "$NEW_IP" '{"disabled": false,"content": $ip,"ttl": 3600, "prio": 0}')

# Actualizar el registro A con la nueva IP
UPDATE_RESPONSE=$(curl -X PUT "$API_URL/zones/$IONOS_ZONE_ID/records/$IONOS_RECORD_ID" -H 'accept: application/json' -H 'Content-Type: application/json' -d "$JSON_PUT")

# Verificar si se actualizó correctamente
if echo "$UPDATE_RESPONSE" | jq -e '.errors' > /dev/null; then
    echo "[$(date)] Error al actualizar el registro DNS: $(echo "$UPDATE_RESPONSE" | jq -r '.errors[].detail')" | tee -a "$LOG_FILE"
else
    echo "[$(date)] El registro DNS se actualizó correctamente a la IP $NEW_IP" | tee -a "$LOG_FILE"
fi



