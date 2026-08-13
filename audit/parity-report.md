# Explorable Explanations website vs Android replica audit

## Verdict

**Conditional go.** All eight actionable audit gaps are fixed in the final APK and the PRD discrepancy is resolved. Build, seven JVM tests, Android lint, smoke navigation, fresh install, relaunch, Scenario defaults/semantics, Filter content/semantics, Context word selection/result placement, and crash/ANR checks pass.

Strict completion exclusions remain: complete TalkBack focus order/Context-word activation, quantitative plot geometry, and exhaustive execution of all 122 original website word targets were not verified. These are verification exclusions, not reproduced open defects. Live landscape navigation is now verified.

## Audit identity

- Original: `https://worrydream.com/ExplorableExplanations/`
- Original HTML SHA-256: `7c434b30d70366ea20ae34709c786844b4575203681bcbf4511f5c4b1e3eb25e` (matches the pinned digest)
- Original runtime: Chrome 151 on the Pixel in verified desktop-site mode; desktop UA, 1000 CSS-pixel viewport
- Replica: `com.bharatisethiya.explorableexplanations` 1.0.0
- Repository revision: `505db86714f572ef16f8c107db7f3c54ff1e75c1`
- Device: Pixel 8 Pro `3A060DLJG001CE`, Android 16/API 36, 1008×2244, 360 dpi, font scale 1.0, en-US
- Audit date: 2026-08-13 (America/Toronto)

## What matches

- Read exposes the retained essay sections and all three idea cards; each card was tapped and opened the correct destination.
- Scenario implements all five controls and recalculates the budget, attendance, effective tax, and explanatory outcome. The installed defaults ($18, 100%, vehicles, free admission, paid-only admission) match the live original.
- Both filter examples respond independently to cutoff and Q changes and update stability, frequency response, coefficients, poles, impulse response, and step response.
- Context supports touch-first word selection, an editable query, live Wikipedia results, bundled fallback content, and an honest no-result state.
- All four bottom destinations, hardware Back, and force-stop/relaunch behaved consistently.
- Native sliders, cards, bottom navigation, and tap-first contextual lookup are reasonable mobile adaptations of the website's inline drag knobs, long-form layout, and hover-plus-key gesture.

## Prioritized findings

| ID | Severity | Area | Classification | Observed finding | Current-source status |
|---|---|---|---|---|---|
| GAP-001 | High | Context result placement | Replica defect | Result previously displaced the passage. | Fixed and verified: result follows the passage and summary is bounded to eight lines. |
| GAP-002 | High | Scenario/Filter accessibility | Replica defect | Interactive controls lacked usable names/states. | Fixed and hierarchy-verified for three Scenario sliders, eligibility switch, and four Filter sliders; TalkBack speech remains excluded. |
| GAP-003 | High | Context accessibility | Replica defect | Passage was one non-actionable node. | Fixed and runtime-verified: 121 individually clickable word nodes are exposed; California lookup succeeds without crash. |
| GAP-004 | Medium | Filter example 2 | Replica defect | Numeric complex poles were absent. | Fixed and runtime-verified for both pole values and inside/outside labels. |
| GAP-005 | Medium | Filter explanation | PRD/replica mismatch | Visible 44.1 kHz sample-rate label was absent. | Fixed and runtime-verified. |
| GAP-006 | Medium | Smoke verification | Tooling defect | Smoke assumed one navigation attribute and an above-fold outcome. | Fixed; smoke resolves text/content description, scrolls to the computed outcome, and passes. |
| GAP-007 | Medium | Scenario requirements | Documentation mismatch | PRD initial state differed from the live original. | Fixed in PRD §6.2; final APK defaults to $18, 100%, Vehicles, free, paid-only. |
| GAP-008 | Medium | Artifact identity | Verification limitation | Old installed APK was differently signed. | Resolved with authorized uninstall/fresh install. Final installed artifact SHA-256: `4eadcd0ab207c6ea45e027221a019fa82a20be3400ad656f30375fdf7eb72904`. |

## Website comparison details

- Actual pointer drags changed original park tax `$18 → $50`, compliance `100% → 0%`, and admission `free → $25`; actual clicks changed registrations → taxpayers and paid-only → everyone. Reloads restored each baseline.
- Actual pointer drags changed original filter example 1 from `Fc 2KHz, Q .8` to `15.73KHz, Q .01`, and example 2 from `Fc 1.2KHz, Q 3.5` to `119Hz, Q 10`.
- Hovering California and pressing W opened the live anchored Wikipedia overlay; Escape closed it.
- A second original discovery pass found 156 candidate controls: anchors, three park adjustables, two park toggles, two filter knobs, and 122 word spans. No new interaction class was found.
- Exhaustive execution of all 122 word spans is **not verified**. Phone-Chrome pointer hit-testing became unreliable after scrolling; the retained run reached 11 candidates before selecting incorrect targets. Only the representative California path is claimed.
- External essay links were inventoried but not opened because outbound browsing is explicitly outside the retained native product scope.

## Build and checks

- `./verify.sh --plan`: passed.
- `./verify.sh`: passed; Docker verification, six JVM tests, and `assembleDebug` completed successfully (41 tasks).
- `./scripts/android/smoke.sh`: passed on the final installed APK.
- `:app:lintDebug`: passed.
- Fresh-install runtime replay and crash/ANR log scan: passed.
- Landscape navigation now uses a compact 64 dp row with centered icon/label pairs, full-width semantic tab targets, and no pill backgrounds; paired screenshot/hierarchy evidence is under J09.

## Not verified

- Complete TalkBack focus order and Context-word activation. TalkBack itself was enabled and its visible caption verified `Filter. Tab. 3 of 4. In list. 4 items`; the Context caption disappeared before a valid capture.
- Quantitative pixel/curve equivalence between original and native plots.
- All 122 original contextual word targets; only one representative live lookup was verified.
- External destination contents.

## Cleanup

The disposable unknown query was removed by navigating away and force-stop/relaunching. Original website mutations were restored by reload or Escape. The authorized old-package uninstall/fresh install is recorded in the cleanup ledger. Temporary TalkBack and forced-rotation settings were restored; accessibility is disabled and auto-rotate is enabled. Accessibility volume remains at Android's minimum `1/15`, and media volume is `0/25`, per user request. Chrome remains configured to request the desktop site for the website comparison.

Evidence is indexed in `audit/capture-manifest.md`; action-level results are in `audit/action-ledger.md`.
