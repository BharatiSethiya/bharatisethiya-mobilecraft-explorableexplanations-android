# Mobile replica audit handoff

## State

- Report: `audit/parity-report.md`
- Verdict: conditional go; all reproduced gaps fixed and verified, with strict exclusions listed in the report.
- Last replica checkpoint: Read after returning from direct card navigation (`J05-read-cards/read-after-card-return.*`).
- Original runtime mutations were restored by reload/Escape.

## Final artifact identity

- Installed/final build SHA-256: `4eadcd0ab207c6ea45e027221a019fa82a20be3400ad656f30375fdf7eb72904`.
- The user authorized replacing the old differently signed APK; it was uninstalled and this exact final artifact was installed fresh.

## Completed

- Live original desktop-site runtime, identity, anchors, representative park/filter/context actions, and second discovery pass.
- Replica portrait navigation, Read cards, Scenario controls/outcomes, both Filter examples/plots, Context live/empty/unknown paths, Back, and relaunch.
- `./verify.sh --plan`, `./verify.sh`, Android lint, and device smoke passed.
- Scenario, Filter, and Context fixes were replayed on the final installed artifact; no crash/ANR remained.

## Open verification

- TalkBack names, actions, values, and focus order.
- Landscape rotation/route retention.
- Repair and rerun `scripts/android/smoke.sh` selector.
- If required for strict exhaustive status, repeat all 122 original word targets with reliable desktop pointer automation.

## Working tree

Existing uncommitted app-source/test changes were preserved and not authored or reverted by this audit pass. Audit artifacts and evidence remain uncommitted under `audit/`.
