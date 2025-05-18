#!/usr/bin/env bash
set -eu

export PGPASSWORD="$POSTGRES_PASSWORD"      # ← ключевая строка
until pg_isready -h "$POSTGRES_HOST" -U "$POSTGRES_USER" >/dev/null 2>&1; do
  echo "Postgres is not ready; waiting…"
  sleep 2
done

IFS=',' read -ra DBS <<< "$POSTGRES_MULTIPLE_DATABASES"
for db in "${DBS[@]}"; do
  db="$(echo "$db" | xargs)"           # убираем случайные пробелы
  if psql -h "$POSTGRES_HOST" -U "$POSTGRES_USER" -tAc \
        "SELECT 1 FROM pg_database WHERE datname = '$db';" | grep -q 1; then
    echo "✔ $db already exists"
  else
    echo "➕ creating $db"
    psql -h "$POSTGRES_HOST" -U "$POSTGRES_USER" -c "CREATE DATABASE \"$db\";"
  fi
done
