#!/usr/bin/env bash
set -euo pipefail

# ── Visual (VLM) E2E Tests ──
# Requires a Vision Language Model API that supports image input.
#
# Usage:
#   OpenAI:              VLM_API_KEY=sk-... ./run-visual.sh
#   Ollama (local/free): VLM_BASE_URL=http://localhost:11434/v1 VLM_MODEL=llava VLM_API_KEY=ollama ./run-visual.sh
#   Anthropic/Claude:    VLM_BASE_URL=https://api.anthropic.com VLM_MODEL=claude-3-5-sonnet-latest VLM_API_KEY=sk-... ./run-visual.sh
#   Zhipu/GLM-5V-Turbo:  VLM_BASE_URL=https://open.bigmodel.cn/api/anthropic VLM_MODEL=glm-5v-turbo VLM_PROVIDER=anthropic VLM_API_KEY=your-key ./run-visual.sh
#
# ⚠️ DeepSeek API does NOT support vision/image input on any model.

cd "$(dirname "$0")"

if [ -z "${VLM_API_KEY:-}" ]; then
  echo "❌ VLM_API_KEY not set."
  echo ""
  echo "Usage:"
  echo "  OpenAI:              VLM_API_KEY=sk-... ./run-visual.sh"
  echo "  Ollama (local):      VLM_BASE_URL=http://localhost:11434/v1 VLM_MODEL=llava VLM_API_KEY=ollama ./run-visual.sh"
  echo "  Anthropic/Claude:    VLM_BASE_URL=https://api.anthropic.com VLM_MODEL=claude-3-5-sonnet-latest VLM_API_KEY=sk-... ./run-visual.sh"
  echo "  Zhipu/GLM-5V-Turbo:  VLM_BASE_URL=https://open.bigmodel.cn/api/anthropic VLM_MODEL=glm-5v-turbo VLM_PROVIDER=anthropic VLM_API_KEY=your-key ./run-visual.sh"
  exit 1
fi

echo "🔍 Running visual E2E tests..."
echo "   Model:    ${VLM_MODEL:-gpt-4o-mini}"
echo "   Base URL: ${VLM_BASE_URL:-https://api.openai.com/v1}"

npx playwright test tests/visual.spec.ts "$@"
