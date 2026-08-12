#!/usr/bin/env bash
set -euo pipefail

command -v adb >/dev/null || { echo "adb is required" >&2; exit 2; }
device_count=$(adb devices | awk 'NR>1 && $2=="device" {count++} END {print count+0}')
[[ "$device_count" -eq 1 ]] || { echo "expected exactly one authorized Android device; found $device_count" >&2; exit 2; }
docker compose build app-runner
docker compose run --rm app-runner gradle --no-daemon :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk >/dev/null
adb shell am start -W -n com.bharatisethiya.explorableexplanations/.MainActivity >/dev/null
echo "Explorable Explanations started"
