#!/usr/bin/env bash
set -euo pipefail

# ── setup-harness-docs scaffold ──
# Initializes a project with harness documentation structure.
#
# Usage:
#   bash scaffold.sh <project-name> <backend-type> <frontend-type>
#
# Example:
#   bash scaffold.sh my-app spring-boot vue
#
# Backend types: spring-boot, go, python, node, none
# Frontend types: vue, react, none
# ─────────────────────────────────────

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TEMPLATES="$SCRIPT_DIR/templates"

if [ $# -lt 1 ]; then
  echo "Usage: bash scaffold.sh <project-name> [backend-type] [frontend-type]"
  exit 1
fi

PROJECT_NAME="$1"
BACKEND="${2:-spring-boot}"
FRONTEND="${3:-vue}"
TARGET_DIR="${PROJECT_NAME}"

# ── Create target directory ──
if [ -d "$TARGET_DIR" ]; then
  echo "⚠️  Target directory '$TARGET_DIR' already exists. Files may be overwritten."
else
  mkdir -p "$TARGET_DIR"
fi

cd "$TARGET_DIR"

echo "📁 Setting up $PROJECT_NAME ..."

# ── Copy docs structure ──
echo "  docs/  — creating document hierarchy"
mkdir -p docs/ba/{chore,feature,prototype,business-fact}
mkdir -p docs/dev/{standards,plans,adr,design-fact/others}
mkdir -p docs/qa/{e2e-cases,manual-cases,smoking-cases}

# ── Copy placeholder docs ──
for dir in $(find "$TEMPLATES/docs" -type d | tail -n +2); do
  rel="${dir#$TEMPLATES/}"
  for f in "$dir"/*.md 2>/dev/null; do
    [ -f "$f" ] && cp "$f" "$rel/"
  done
done

# ── Copy agent skills ──
echo "  .agent/skills/  — copying development skills"
if [ -d "$TEMPLATES/agent/skills" ]; then
  mkdir -p .agent/skills
  for skill_dir in "$TEMPLATES"/agent/skills/*/; do
    [ -d "$skill_dir" ] && cp -r "$skill_dir" .agent/skills/
  done
fi

# ── Copy config files (with replacements) ──
echo "  Config files  — generating"
for f in AGENTS.md opencode.jsonc .mcp.json .gitignore; do
  src="$TEMPLATES/$f"
  if [ -f "$src" ]; then
    sed \
      -e "s/{{PROJECT_NAME}}/$PROJECT_NAME/g" \
      -e "s/{{BACKEND_TYPE}}/$BACKEND/g" \
      -e "s/{{FRONTEND_TYPE}}/$FRONTEND/g" \
      "$src" > "$f"
  fi
done

# ── Done ──
echo ""
echo "✅ $PROJECT_NAME initialized!"
echo ""
echo "Next steps:"
echo "  1. Edit AGENTS.md with project-specific conventions"
echo "  2. Fill docs/ba/feature/ with requirements"
echo "  3. Fill docs/dev/standards/ with development conventions"
echo "  4. Start implementing with dev-plan skill"
