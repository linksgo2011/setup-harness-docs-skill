---
name: dev-debug
description: Debug frontend and backend issues using chrome-devtools-mcp. Use when the browser shows errors, UI looks wrong, or network requests fail.
---

# Dev Debug

## Start the Stack

```bash
./e2e-run.sh
```

Only start services without running tests if needed.

## chrome-devtools-mcp Tools

The MCP server (`chrome-devtools` in `.mcp.json`) provides:

- `navigate_page` — Go to a URL
- `take_screenshot` — Capture page/element screenshot
- `take_snapshot` — Get accessibility tree (text DOM snapshot with uids)
- `click`, `fill`, `fill_form` — Interact with elements
- `list_console_messages`, `get_console_message` — Read browser console
- `list_network_requests`, `get_network_request` — Inspect network
- `evaluate_script` — Run JS in page context
- `performance_start_trace` — Record performance trace

## Debug Flow

1. Navigate to the problem page
2. Check console for errors (`list_console_messages`)
3. Check network for failed requests (`list_network_requests`)
4. Take a DOM snapshot to inspect state (`take_snapshot`)
5. If UI issue, take a screenshot (`take_screenshot`)
6. For performance issues, start a trace (`performance_start_trace`)
