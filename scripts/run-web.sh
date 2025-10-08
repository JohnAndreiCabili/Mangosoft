#!/usr/bin/env bash
set -euo pipefail

# Starts the Mangosoft web experience via Python's built-in HTTP server.
# Usage: ./scripts/run-web.sh [port]

PORT="${1:-4173}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WEB_DIR="$ROOT/web"

if ! command -v python3 >/dev/null 2>&1; then
  echo "python3 is required to run the Mangosoft web server." >&2
  echo "Install Python 3 from https://www.python.org/downloads/ or via Homebrew (brew install python)." >&2
  exit 1
fi

cd "$WEB_DIR"

echo "Starting Mangosoft web server on http://localhost:$PORT"
echo "Press Ctrl+C to stop the server."

python3 -m http.server "$PORT"
