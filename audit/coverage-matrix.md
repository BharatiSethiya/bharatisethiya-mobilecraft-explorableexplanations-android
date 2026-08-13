# Parity coverage matrix

| ID | Surface/state | Original evidence | Replica evidence | Result |
|---|---|---|---|---|
| READ.OVERVIEW | Essay, cards, postscript | O00 anchors/inventory | J00, J05 | Verified within condensed native scope |
| READ.CARDS | Three card actions | Original anchors | J05 card checkpoints | Verified |
| PARK.DEFAULT | Initial controls/outcome | O01 baseline/live controls | J06 final | Verified; website, PRD, and app aligned |
| PARK.VARIANTS | Five controls and outcome branches | O01 actual pointer actions | J02 mutations | Verified |
| FILTER.EXAMPLE1 | Default and mutation | O02 example 1 | J03 example 1 | Verified; installed a11y gap |
| FILTER.EXAMPLE2 | Default and mutation | O02 example 2 | J03/J06 | Verified, including numeric pole labels |
| FILTER.REPRESENTATIONS | Linked response/pole/time plots | O02 screenshot/behavior | J03 plots | Partially verified; geometry not quantitatively compared |
| CONTEXT.EMPTY | Passage before selection | O03 context section | J04 empty | Verified |
| CONTEXT.LIVE | Select word, result, close/return | O03 California/W/Escape | J06 final | Verified; bounded result follows passage |
| CONTEXT.UNKNOWN | Unsupported query | Declared native journey | J04 unknown | Verified |
| NAV.PORTRAIT | Four destinations, Back, relaunch | Declared native behavior | J00–J05 | Verified |
| NAV.LANDSCAPE | Rotation and retained route | Declared requirement | J07 live landscape hierarchies | Verified: all four routes retained all navigation labels |
| ACCESSIBILITY | Names, values, actions, focus | Declared requirement | J06 hierarchy; J08 TalkBack | Semantics and Filter caption verified; complete focus order/Context activation not verified |
| ORIGINAL.DISCOVERY2 | Fixed-point discovery pass | O00 second pass | Source/runtime recheck | No new control class found |
| ORIGINAL.WORDS122 | Every context word trigger | O03 attempted batch | N/A | Not verified; pointer targeting failed after 11 |
| BUILD.CURRENT | Current working-tree APK | N/A | verify/build/install digest | Build and runtime verified |
