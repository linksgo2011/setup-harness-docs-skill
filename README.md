# setup-harness-docs-skill

Project scaffold for quickly establishing standardized documentation systems in new codebases or existing projects.

## Quick Start

```bash
bash skills/setup-harness-docs/scaffold.sh <project-name> <backend-type> <frontend-type>

# Example: create a Spring Boot + Vue project with full docs structure
bash skills/setup-harness-docs/scaffold.sh my-app spring-boot vue
```

This creates:

```
my-app/
├── docs/                  # Document hierarchy
│   ├── ba/                #   Business Analysis
│   ├── dev/                #   Development
│   │   └── snippet/        #     Code pattern templates
│   └── qa/                #   Quality Assurance
├── .agent/skills/         # 8 development workflow skills
├── AGENTS.md              # Architecture & engineering conventions
├── opencode.jsonc         # opencode config
├── .mcp.json              # MCP config (chrome-devtools)
└── .gitignore
```

## Structure

```
skills/setup-harness-docs/  ← This skill (auto-initializes docs)
  ├── SKILL.md
  ├── scaffold.sh
  └── templates/

example-project/                   ← Living reference example
  ├── AGENTS.md
  ├── docs/
  ├── .agent/skills/
  ├── backend/                     ← Spring Boot 3 monolith
  ├── frontend/                    ← Vue 3
  └── e2e/                         ← Playwright E2E tests

config files:
  ├── opencode.jsonc
  ├── .mcp.json
  └── .gitignore
```

## Principles

- `docs/ba/` — business analysis (feature, business-fact, chore, prototype)
- `docs/dev/` — development (standards, plans, adr, design-fact)
- `docs/qa/` — quality (e2e-cases, manual-cases, smoking-cases)
- Each layer has clear boundaries, traceable from requirements to tests
