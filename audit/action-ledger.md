# Recursive action ledger

Audit date: 2026-08-13. Original: live website in Chrome desktop-site mode. Replica: installed `com.bharatisethiya.explorableexplanations` 1.0.0 on Pixel 8 Pro / Android 16.

| ID | Surface/control | Original outcome | Installed replica outcome | Classification | Status/evidence |
|---|---|---|---|---|---|
| NAV.READ | Bottom Read | Essay section | Opens Read | Match | Verified, J00/J05 |
| NAV.SCENARIO | Bottom Scenario | Reactive park model | Opens Scenario | Match | Verified, J02 |
| NAV.FILTER | Bottom Filter | Two filter examples | Opens Filter | Match | Verified, J03 |
| NAV.CONTEXT | Bottom Context | Contextual lookup | Opens Context | Match | Verified, J04 |
| NAV.BACK | Hardware Back | Return without destructive side effect | Returned to Read | Match | Verified live |
| NAV.RELAUNCH | Force-stop/relaunch | Stable entry | Relaunched on Read | Match | Verified, J01 |
| READ.SCENARIO | State-park card | Open park model | Opened Scenario | Match | Verified, J05 |
| READ.FILTER | Digital-filter card | Open filter | Opened Filter | Match | Verified, J05 |
| READ.CONTEXT | Concept card | Open contextual lookup | Opened Context | Match | Verified, J05 |
| PARK.CHARGE | Annual charge | Drag `$18→$50`; outcome updates | `$18→$49`; outcome updates | Match | O01/J02 |
| PARK.BASIS.VEHICLES | Registrations/vehicles | Select vehicle base | Default selected | Match | O01/J02 |
| PARK.BASIS.TAXPAYERS | Taxpayers | Recalculate with taxpayer base | Recalculated | Match | O01/J02 |
| PARK.PARTICIPATION | Compliance | `100%→0%`; collected tax falls | `100%→0%`; collected tax falls | Match | O01/J02 |
| PARK.ADMISSION | Admission | `free→$25`; attendance/revenue react | `free→$25`; attendance/revenue react | Match | O01/J02 |
| PARK.ELIGIBILITY | Paid-only/everyone | Toggle eligible visitors | Toggle recalculates | Match | O01/J02 |
| PARK.DEFAULT | Initial state | $18, 100%, vehicles, free, paid-only | Same | Documentation mismatch | Website/app verified; PRD differs |
| FILTER.1.FC | Example 1 cutoff | Actual log drag updates response | Slider updates response | Adaptation match | O02/J03 |
| FILTER.1.Q | Example 1 Q | Actual log drag updates stability | Slider updates stability | Adaptation match | O02/J03 |
| FILTER.2.FC | Example 2 cutoff | Independent update | Independent update | Adaptation match | O02/J03 |
| FILTER.2.Q | Example 2 Q | Independent update | Independent update | Adaptation match | O02/J03 |
| FILTER.2.POLES | Numeric poles | Linked pole information | Plot only; numeric values absent | Replica defect | Open in installed; source fix pending runtime |
| FILTER.PLOTS | Response/poles/impulse/step | Linked visual representations | All render/react | Partial match | Presence verified; numeric geometry not compared |
| CONTEXT.SELECT | California word | Hover + W selects | Tap selects | Declared mobile adaptation | O03/J04 |
| CONTEXT.EDIT | Query field | Context search interaction | Editable query works | Match in native scope | J04 |
| CONTEXT.UNKNOWN | Unsupported query | N/A retained native journey | Honest empty state | Match declared scope | J04 |
| CONTEXT.RESULT | Result placement | Absolute overlay anchored to word; passage stays put | Unbounded result inserted above passage; passage shifts | Replica defect | Open in installed; source fix pending runtime |
| CONTEXT.CLOSE | Escape | Closes overlay | Navigate/reselect clears path | Adaptation | Original verified; native reset verified |
| A11Y.PARK | Scenario controls | N/A | Participation/admission sliders and eligibility lack sufficient names | Replica defect | Installed hierarchy; TalkBack pending |
| A11Y.FILTER | Four sliders | N/A | SeekBar nodes have empty names | Replica defect | Installed hierarchy; source fix pending runtime |
| A11Y.WORDS | Per-word lookup | Keyboard-assisted word action | Passage is one non-actionable text node | Replica defect | Installed hierarchy; partial source fix pending runtime |
| NAV.LANDSCAPE | Rotate/retain route | N/A | All routes retained; compact centered navigation added after visual review | Match | J07/J09 live landscape evidence |
| LINKS.* | Essay outbound links | Open external destinations | Intentionally omitted | Declared omission | Inventory only; destinations not opened |
| ORIGINAL.WORDS122 | All word spans | Each is a candidate hover/W target | Native supports touch-any-word by implementation | Not verified exhaustively | O03 batch became unreliable after 11 |
| SMOKE.SCENARIO | Script destination lookup | N/A | Script fails although manual navigation works | Tooling defect | `Scenario destination not found` |
