#!/usr/bin/env bash
set -euo pipefail

# Prepares a standalone Git repository containing only the web build.
# Usage: ./scripts/prepare-web-repo.sh [output-directory-name]

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WEB_DIR="$ROOT/web"
OUTPUT_NAME="${1:-mangosoft-web}"
OUTPUT_PARENT="$ROOT/build"
OUTPUT_DIR="$OUTPUT_PARENT/$OUTPUT_NAME"

if [ ! -d "$WEB_DIR" ]; then
  echo "Unable to find the web directory at $WEB_DIR" >&2
  exit 1
fi

mkdir -p "$OUTPUT_PARENT"
rm -rf "$OUTPUT_DIR"

rsync -a \
  --exclude '*.DS_Store' \
  --exclude 'Thumbs.db' \
  "$WEB_DIR/" "$OUTPUT_DIR/"

pushd "$OUTPUT_DIR" >/dev/null
rm -rf .git

git init --initial-branch=main >/dev/null

git add . >/dev/null
git commit -m "Initial commit for Mangosoft web" >/dev/null
popd >/dev/null

cat <<MSG
Standalone repository created at $OUTPUT_DIR

Next steps:
  cd "$OUTPUT_DIR"
  git remote add origin <your_github_repo_url>
  git push -u origin main

To launch locally on macOS once you've pushed or if you stay local:
  cd "$OUTPUT_DIR"
  python3 -m http.server 4173
  open http://localhost:4173
MSG
