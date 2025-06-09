#!/bin/bash
set -e

DATA_DIR="./nexusFc/scripts/data"

echo "⏳ Aguardando MongoDB iniciar..."
sleep 10

echo "⏫ Importando arquivos JSON para o MongoDB..."

shopt -s nullglob
json_files=("$DATA_DIR"/*.json)

if [ ${#json_files[@]} -eq 0 ]; then
  echo "⚠️ Nenhum arquivo JSON encontrado em $DATA_DIR"
  exit 1
fi

for file in "${json_files[@]}"; do
  filename=$(basename "$file")
  collection=$(echo "$filename" | sed -E 's/^nexusfc\.//' | sed -E 's/\.json$//')

  echo "📁 Importando '$filename' na coleção '$collection'..."

  mongoimport \
    --host localhost \
    --port 27017 \
    --db nexusfc \
    --collection "$collection" \
    --file "$file" \
    --jsonArray \
    --drop
done

echo "✅ Importação concluída!"
