#!/usr/bin/env bash
set -euo pipefail

# Prepares a standalone Git repository containing the entire Mangosoft project.
# Usage: ./scripts/prepare-full-repo.sh [output-directory-name]

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_NAME="${1:-mangosoft-full}"
OUTPUT_PARENT="$ROOT/build"
OUTPUT_DIR="$OUTPUT_PARENT/$OUTPUT_NAME"

mkdir -p "$OUTPUT_PARENT"
rm -rf "$OUTPUT_DIR"

rsync -a \
  --exclude '.git/' \
  --exclude 'build/' \
  --exclude '*.DS_Store' \
  --exclude 'Thumbs.db' \
  "$ROOT/" "$OUTPUT_DIR/"

pushd "$OUTPUT_DIR" >/dev/null
rm -rf .git

git init --initial-branch=main >/dev/null

git add . >/dev/null

git commit -m "Initial commit for Mangosoft" >/dev/null
popd >/dev/null

cat <<MSG
Standalone repository created at $OUTPUT_DIR

Next steps:
  cd "$OUTPUT_DIR"
  git remote add origin <your_github_repo_url>
  git push -u origin main
MSG
