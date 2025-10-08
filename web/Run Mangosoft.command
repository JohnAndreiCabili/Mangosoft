#!/bin/bash
set -euo pipefail

cd "$(dirname "$0")"
PORT="${1:-4173}"

if ! command -v python3 >/dev/null 2>&1; then
  echo "Python 3 is required to run Mangosoft Web. Install it with 'brew install python' or from python.org." >&2
  read -rp "Press Return to close..." _
  exit 1
fi

trap 'kill "$SERVER_PID" 2>/dev/null || true' EXIT

python3 -m http.server "$PORT" &
SERVER_PID=$!

sleep 1
open "http://localhost:${PORT}" >/dev/null 2>&1 || true

echo "Mangosoft Web is now running on http://localhost:${PORT}".
echo "Leave this window open; press Ctrl+C when you are done."
wait "$SERVER_PID"
