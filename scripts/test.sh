#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

set +e
out=$(mvn -B clean test 2>&1)
code=$?
set -e
echo "$out"

line=$(echo "$out" | grep -E '^\[(INFO|WARNING|ERROR)\] Tests run: [0-9]+, Failures: [0-9]+, Errors: [0-9]+, Skipped: [0-9]+$' | tail -n 1 || true)

if [ -n "$line" ]; then
  total=$(echo "$line" | sed -E 's/.*Tests run: ([0-9]+),.*/\1/')
  failures=$(echo "$line" | sed -E 's/.*Failures: ([0-9]+),.*/\1/')
  errors=$(echo "$line" | sed -E 's/.*Errors: ([0-9]+),.*/\1/')
  passed=$((total - failures - errors))
  echo "TESTS: $passed/$total"
fi

exit "$code"
