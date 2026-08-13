#!/usr/bin/env bash
set -euo pipefail

package=com.bharatisethiya.explorableexplanations
command -v adb >/dev/null || { echo "adb is required" >&2; exit 2; }
device_count=$(adb devices | awk 'NR>1 && $2=="device" {count++} END {print count+0}')
[[ "$device_count" -eq 1 ]] || { echo "expected exactly one authorized Android device; found $device_count" >&2; exit 2; }

focused=$(adb shell dumpsys window | grep -E 'mCurrentFocus|mFocusedApp' | head -2 || true)
[[ "$focused" == *"$package"* ]] || { echo "declared package is not foreground: $focused" >&2; exit 3; }

remote=/sdcard/explorable-smoke-$$.xml
cleanup() { adb shell rm -f "$remote" >/dev/null 2>&1 || true; }
trap cleanup EXIT INT TERM
adb shell uiautomator dump "$remote" >/dev/null
tree=$(adb shell cat "$remote")
scenario_node=$(printf '%s' "$tree" | tr '>' '\n' | grep -E '(content-desc|text)="Scenario"' | head -1 || true)
[[ -n "$scenario_node" ]] || { echo "Scenario destination not found" >&2; exit 4; }

bounds=$(printf '%s' "$scenario_node" | sed -n 's/.*bounds="\[\([0-9]*\),\([0-9]*\)\]\[\([0-9]*\),\([0-9]*\)\]".*/\1 \2 \3 \4/p')
read -r x1 y1 x2 y2 <<< "$bounds"
[[ -n ${x1:-} ]] || { echo "Scenario bounds unavailable" >&2; exit 4; }
adb shell input tap $(((x1+x2)/2)) $(((y1+y2)/2))
sleep 1
adb shell uiautomator dump "$remote" >/dev/null
tree=$(adb shell cat "$remote")
[[ "$tree" == *'text="State park scenario"'* ]] || { echo "Scenario screen did not open" >&2; exit 5; }
[[ "$tree" == *'content-desc="Annual charge"'* ]] || { echo "Scenario controls are not visible" >&2; exit 5; }

for _ in 1 2 3; do
  [[ "$tree" == *'text="Live outcome"'* ]] && break
  adb shell input swipe 500 1800 500 600 350
  sleep 1
  adb shell uiautomator dump "$remote" >/dev/null
  tree=$(adb shell cat "$remote")
done
[[ "$tree" == *'text="Live outcome"'* ]] || { echo "Scenario computed outcome was not found after scrolling" >&2; exit 5; }
echo "smoke passed: Scenario opened and computed outcome is visible"
