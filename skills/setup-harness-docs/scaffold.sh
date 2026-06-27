#!/usr/bin/env bash
set -euo pipefail

# ── setup-harness-docs 脚手架 ──
# 为项目初始化规范的文档体系结构。
#
# 用法：
#   bash scaffold.sh <project-name> <backend-type> <frontend-type>
#
# 示例：
#   bash scaffold.sh my-app spring-boot vue
#
# 后端类型：spring-boot, go, python, node, none
# 前端类型：vue, react, none
# ─────────────────────────────────────

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TEMPLATES="$SCRIPT_DIR/templates"

if [ $# -lt 1 ]; then
  echo "用法：bash scaffold.sh <project-name> [backend-type] [frontend-type]"
  exit 1
fi

PROJECT_NAME="$1"
BACKEND="${2:-spring-boot}"
FRONTEND="${3:-vue}"
TARGET_DIR="${PROJECT_NAME}"

# ── 创建目标目录 ──
if [ -d "$TARGET_DIR" ]; then
  echo "⚠️  目标目录 '$TARGET_DIR' 已存在。文件可能会被覆盖。"
else
  mkdir -p "$TARGET_DIR"
fi

cd "$TARGET_DIR"

echo "📁 正在初始化 $PROJECT_NAME ..."

# ── 复制文档结构 ──
echo "  docs/  — 创建文档层级目录"
mkdir -p docs/ba/{chore,feature,prototype,business-fact}
mkdir -p docs/dev/{standards,plans,adr,design-fact/others,snippet}
mkdir -p docs/qa/{e2e-cases,manual-cases,smoking-cases}

# ── 复制占位文档 ──
for dir in $(find "$TEMPLATES/docs" -type d | tail -n +2); do
  rel="${dir#$TEMPLATES/}"
  for f in "$dir"/*.md 2>/dev/null; do
    [ -f "$f" ] && cp "$f" "$rel/"
  done
done

# ── 复制 agent skills ──
echo "  .agent/skills/  — 复制开发流程 skills"
if [ -d "$TEMPLATES/agent/skills" ]; then
  mkdir -p .agent/skills
  for skill_dir in "$TEMPLATES"/agent/skills/*/; do
    [ -d "$skill_dir" ] && cp -r "$skill_dir" .agent/skills/
  done
fi

# ── 复制配置文件（含替换） ──
echo "  配置文件  — 正在生成"
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

# ── 完成 ──
echo ""
echo "✅ $PROJECT_NAME 初始化完成！"
echo ""
echo "后续步骤："
echo "  1. 编辑 AGENTS.md，填写项目专属规范"
echo "  2. 在 docs/ba/feature/ 中编写需求"
echo "  3. 在 docs/dev/standards/ 中编写开发规范"
echo "  4. 使用 dev-plan skill 开始开发"
