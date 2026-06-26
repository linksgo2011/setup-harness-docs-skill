#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
BACKEND_DIR="$ROOT/backend"
FRONTEND_DIR="$ROOT/frontend"
E2E_DIR="$ROOT/e2e"
BACKEND_PORT=8080
FRONTEND_PORT=5173
BACKEND_PID=""
FRONTEND_PID=""

cleanup() {
  echo ""
  echo "🛑 Cleaning up..."
  [ -n "$FRONTEND_PID" ] && kill "$FRONTEND_PID" 2>/dev/null && echo "  Frontend (PID $FRONTEND_PID) stopped"
  [ -n "$BACKEND_PID" ] && kill "$BACKEND_PID" 2>/dev/null && echo "  Backend (PID $BACKEND_PID) stopped"
  wait 2>/dev/null
  echo "✅ Cleanup done"
}

trap cleanup EXIT INT TERM

# ── Start backend ──
echo "🚀 Starting backend (port $BACKEND_PORT)..."
cd "$BACKEND_DIR"
mvn spring-boot:run -q > /tmp/e2e-backend.log 2>&1 &
BACKEND_PID=$!
echo "  Backend PID: $BACKEND_PID"

echo "  Waiting for backend..."
for i in $(seq 1 60); do
  if curl -s -o /dev/null -w "%{http_code}" "http://localhost:$BACKEND_PORT/api/v1/home/info" 2>/dev/null | grep -q 200; then
    echo "  Backend ready after ${i}s"
    break
  fi
  if [ "$i" -eq 60 ]; then
    echo "❌ Backend failed to start within 60s"
    tail -20 /tmp/e2e-backend.log
    exit 1
  fi
  sleep 1
done

# ── Start frontend ──
echo "🚀 Starting frontend (port $FRONTEND_PORT)..."
cd "$FRONTEND_DIR"
npm run dev > /tmp/e2e-frontend.log 2>&1 &
FRONTEND_PID=$!
echo "  Frontend PID: $FRONTEND_PID"

echo "  Waiting for frontend..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null "http://localhost:$FRONTEND_PORT" 2>/dev/null; then
    echo "  Frontend ready after ${i}s"
    break
  fi
  if [ "$i" -eq 30 ]; then
    echo "❌ Frontend failed to start within 30s"
    tail -10 /tmp/e2e-frontend.log
    exit 1
  fi
  sleep 1
done

# ── Run E2E tests ──
echo ""
echo "🧪 Running E2E tests..."
cd "$E2E_DIR"
npx playwright test "$@"
EXIT_CODE=$?

echo ""
if [ $EXIT_CODE -eq 0 ]; then
  echo "✅ All E2E tests passed!"
else
  echo "❌ Some E2E tests failed (exit code: $EXIT_CODE)"
fi

exit $EXIT_CODE
