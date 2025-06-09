#!/bin/bash
echo "Esperando MongoDB iniciar..."

# Espera até o MongoDB aceitar conexões
until mongosh --host mongodb:27017 --eval "print('Mongo está pronto')" > /dev/null 2>&1; do
  sleep 2
done

echo "⏰ Hora atual: $(date +"%T")"
echo "1) Iniciando replica set…"

mongosh --host mongodb:27017 <<EOF
  var cfg = {
    "_id": "rs0",
    "version": 1,
    "members": [
      {
        "_id": 0,
        "host": "mongodb:27017",
        "priority": 2
      }
    ]
  };
  rs.initiate(cfg);
EOF

echo "2) Carregando schema…"
mongosh --host mongodb:27017 /scripts/init-nexusFC-schema.js

echo "✅ Replica set & schema carregados!"
