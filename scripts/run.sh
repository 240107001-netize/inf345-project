#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

export PORT="${PORT:-8080}"

mkdir -p target/classes
javac -d target/classes src/main/java/app/*.java
exec java -cp target/classes app.Main
