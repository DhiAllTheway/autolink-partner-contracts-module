#!/bin/bash
# Script to generate a self-signed SSL certificate for local/dev HTTPS

CERT_DIR="./docker/nginx/ssl"
DOMAIN="192.168.1.18"

mkdir -p "$CERT_DIR"

if [ -f "$CERT_DIR/selfsigned.crt" ] && [ -f "$CERT_DIR/selfsigned.key" ]; then
    echo "Certificate already exists at $CERT_DIR. Overwriting with updated domain..."
fi

openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout "$CERT_DIR/selfsigned.key" \
  -out "$CERT_DIR/selfsigned.crt" \
  -subj "/C=TN/ST=Tunis/L=Tunis/O=Autolink/CN=$DOMAIN"

echo "SSL certificate generated successfully at $CERT_DIR for domain: $DOMAIN"
