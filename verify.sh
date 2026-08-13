#!/usr/bin/env bash
set -euo pipefail

if [[ ${1:-} == "--plan" ]]; then
  echo "Plan: validate the repository contract, compile the Android app, and run unit tests."
  exit 0
fi

python3 - <<'PY'
import json
import tomllib
from pathlib import Path

root = Path.cwd()
required = (
    "mobile.toml", "PRD.md", "SETUP.md", ".env.example", "features.json",
    "thumbnail.png", "verify.sh", "start.sh", "stop.sh",
)
missing = [name for name in required if not (root / name).is_file() or not (root / name).stat().st_size]
if missing:
    raise SystemExit(f"Missing or empty required files: {', '.join(missing)}")

with (root / "mobile.toml").open("rb") as handle:
    manifest = tomllib.load(handle)
mobile = manifest.get("mobile", {})
for section in ("tech", "substrate", "device", "commands", "smoke", "target", "android"):
    if section not in mobile:
        raise SystemExit(f"mobile.toml is missing mobile.{section}")
for command in ("verify", "start", "stop", "smoke"):
    if not mobile["commands"].get(command):
        raise SystemExit(f"mobile.toml is missing mobile.commands.{command}")

feature_data = json.loads((root / "features.json").read_text(encoding="utf-8"))
if not feature_data.get("app"):
    raise SystemExit("features.json must contain a non-empty app string")
if feature_data["app"] != mobile.get("name"):
    raise SystemExit("features.json app must match mobile.toml mobile.name")
rubrics = feature_data.get("rubrics", [])
if len(rubrics) != 5:
    raise SystemExit(f"features.json must contain exactly five rubrics; found {len(rubrics)}")
required_keys = {"id", "criterion", "type", "priority"}
if any(not required_keys.issubset(rubric) for rubric in rubrics):
    raise SystemExit("Every rubric must contain id, criterion, type, and priority")
if {rubric["id"] for rubric in rubrics} != {1, 2, 3, 4, 5}:
    raise SystemExit("features.json rubric IDs must be exactly 1 through 5")
if {rubric["type"] for rubric in rubrics} != {"CUJ", "design"}:
    raise SystemExit("features.json must include both CUJ and design rubric types")
if any(rubric["priority"] != "must-have" for rubric in rubrics):
    raise SystemExit("Every rubric must have must-have priority")

screenshots = manifest.get("screenshots", [])
if not screenshots:
    raise SystemExit("mobile.toml must declare screenshot evidence")
for screenshot in screenshots:
    path = root / screenshot.get("file", "")
    if not path.is_file() or not path.stat().st_size:
        raise SystemExit(f"Missing or empty screenshot: {screenshot.get('file', '')}")
PY

bash -n verify.sh start.sh stop.sh scripts/android/*.sh
command -v docker >/dev/null || { echo "docker is required" >&2; exit 2; }
docker compose build app-runner
docker compose run --rm app-runner gradle --no-daemon :app:testDebugUnitTest :app:assembleDebug
