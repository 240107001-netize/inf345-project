# Currency Converter

A small HTTP service written in Java that converts an amount between currencies
(USD, EUR, KZT, RUB, GBP) using fixed exchange rates.

## What it does

- `GET /` returns a short description of the service
- `GET /healthz` returns `200` with `{"status":"ok"}`
- `GET /convert?from=USD&to=KZT&amount=10` returns the converted amount

Example:

    curl "http://localhost:8080/convert?from=USD&to=KZT&amount=10"
    {"from":"USD","to":"KZT","amount":10.0,"result":4800.0}

## Requirements

- JDK 17 or newer
- Maven 3.9 or newer

## How to run

    ./scripts/run.sh

## Port

The service listens on the port from the `PORT` environment variable.
If `PORT` is not set, it uses `8080`.

    PORT=9000 ./scripts/run.sh

## How to test

    ./scripts/test.sh

The script runs the unit and HTTP tests with Maven and prints a summary
line such as `TESTS: 8/8`.