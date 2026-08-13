# Fixture cleanup ledger

| Unique fixture | App/package | Creation evidence | Cleanup method | Absence evidence | Status |
|---|---|---|---|---|---|
| `CMP_AUDIT_UNKNOWN_XYZ` query | replica | `J04-context/replica-unknown` | Navigate away and force-stop/relaunch | `J01-navigation/replica-relaunch-read` | Cleaned |

Original park/filter mutations were restored by page reload; the California overlay was closed with Escape. The user explicitly authorized uninstalling the old differently signed replica and clearing its package-local data; the final APK was then installed fresh. Temporary stay-awake, forced-rotation, and TalkBack settings were restored; accessibility is disabled and auto-rotate is enabled. Accessibility volume remains at the requested minimum `1/15`, and media volume at `0/25`. No records, files, notifications, or scheduled work remain. Chrome remains in desktop-site mode for the original comparison.
