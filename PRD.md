# Explorable Explanations Android — Product Requirements Document

**Source Concept:** Active-reading lab inspired by Bret Victor's explorable explanations essay (2011) — text as environment to think in. This PRD describes the Android product from scratch.
**Content hash reference tracked in SOURCE.md separately**

## 1. Product Identity & Overview

Explorable Explanations Android is a native active-reading lab that turns static text into an environment to think in. The essay that inspired it argues that text should be an environment to think in, not just information to be consumed. It presents three ideas: Reactive Documents (manipulate assumptions), Explorable Examples (make abstract concrete with multiple linked representations), and Contextual Information (just-in-time fact checking without losing place).

The Android app preserves **semantic outcomes**, not desktop gestures. Long-form anchors and hover+W become bottom navigation + tap-to-query. Draggable inline numbers become Material sliders/chips/switches. HTML5 canvases become Compose Canvas with log-scaled frequency rendering. Wikipedia hover boxes become an in-app panel with bundled offline facts plus live lookup and honest empty states.

Package: `com.bharatisethiya.explorableexplanations`, minSdk 23, targetSdk 35, runtime `native-android`, no WebView, no backend credentials.

## 2. Goals

* Enable active reading: ask questions, consider alternatives, question assumptions, verify trustworthiness — on a phone.
* Preserve model transparency from `park.js` and `filter.js` — author discloses models.
* Make barrier to exploration extremely low: tap/drag, immediate recalculation, multiple representations dance together.
* Stay offline-capable for bundled context; degrade gracefully when Wikipedia offline.
* Satisfy MobileCraft rubrics: reactive park calculation, explorable filter with coefficients/poles/stability/impulse, contextual truthful facts + empty state, persistent bottom nav portrait/landscape, native Compose without browser chrome.

## 3. User Personas

* **Curious Reader** — skims Read, wants thesis quickly, may tap one Idea.
* **Policy Explorer** — engaged with Proposition 21 state-park scenario, wants to test "What if tax $50? What if free admission for everyone?"
* **Engineering Student** — learns state-variable filter; varies Fc 20-20000Hz log, Q 0.05-10 log, watches stability flip, sees poles leave unit circle.
* **Fact-checker** — reads renewable advocacy passage, taps "California" or "Altamont Pass", edits query, expects Wikipedia summary inline.

## 4. Product Scope — Four Native Destinations

Persistent bottom navigation in `MainActivity.kt` with `NavigationBar` items `Read`, `Scenario`, `Filter`, `Context`. Navigation uses `NavHost` start `read`, `popUpTo("read") launchSingleTop`. All destinations reachable in portrait and landscape (LazyColumn with innerPadding).

### 4.1 Read — Active-Reading Thesis

* Content: Title `Explorable Explanations`, author/date `Bret Victor / March 10 2011`, sections: What is active reader?, goal card "environment to think in", three Idea cards with actions to open destinations, deeper sections Modeling/Transparency/Debate, Intuition/Trust/Explanation, What to do? Examples/Tools/Culture (Tangle.js mention), Postscript 2024 (explorabl.es, Distill, Nicky Case, Amit Patel, Jack Schaedler, Observable, Nextjournal, model-driven debate, Dynamicland).
* State: static scroll, no inputs.
* Observable: Read contains idea cards linking to Scenario/Filter/Context; contains conclusion and postscript text; adaptation note with original URL.

### 4.2 Scenario — Reactive Document (park.js)

* Source model: `ParkScenarioCalculator` mirrors `park.js` exactly:
  - Constants: `parkCount 278`, `oldAdmission 12`, `registeredVehicleCount 28e6`, `taxpayerCount 13657632`, `oldVisitorCount 75e6`, `oldBudget 400e6`, `oldClosedParkCount 150`, `percentOfAdmissionConvertedToRevenue 0.1`, `percentInState 85`, `percentVehicle 95`
  - Inputs: tax $0-50 (default 18), compliance 0-100% step 5 (default 100), isTaxPerVehicle toggle Vehicles/Taxpayers, newAdmission $0-25 Free (default 0), appliesToEveryone toggle
  - Calculations: `taxCount`, `taxCollected = tax*compliance/100*taxCount`, `eligibleFraction = everyone?1:0.85*(vehicle?0.95:1)`, `averageAdmission = old + eligible*(new-old)`, `newVisitorCount = old*max(0.2, 1+0.5*atan(1-avg/old))`, `oldRevenue = old*oldAdmission*0.1`, `newRevenue = visits*avg*0.1`, `deltaRevenue = new-old`, `deltaBudget = taxCollected+deltaRevenue`, `budget = oldBudget+deltaBudget`, scenarioIndex 0:<600M shut down X parks `closed = 150*(600M-budget)/200M`, 1:<750M maintain only, 2:<1000M restoration years `round(10-9*(budget-750)/250)`, 3: surplus `budget-1000M`
  - Outputs: budgetMillions, budgetDeltaMillions, taxCollectedMillions, deltaRevenueMillions, visitsMillions, visitDeltaPercent signed + abs, isRisingVisitors, scenarioIndex, closedParkCount, restorationTime, surplus, summary short, detailedSummary long with original phrasing "collect an extra/lose", "plus/minus additional/lost revenue", "total budget", scenario sentence, attendanceSentence "rise/fall by % to XM visits"

* UI: Sliders with semantics ` Annual charge ${tax} dollars`, chips for Vehicles/Taxpayers, switch Applies to everyone, cards: Analysis breakdown with MetricRows Budget/Change/Visits + Tax collected/Admission Δ + detailedSummary, Live outcome with summary + attendanceSentence, How to read it model note.
* Observable req rubric 1: changing any of five controls immediately recalculates budget and attendance outcomes (CUJ).

### 4.3 Filter — Explorable Example (filter.js)

* Source model: `ChamberlinFilter` from `filter.js`:
  - `fs 44100`, `kf=2*sin(PI*fc/fs)`, `kq=1/q`, `b0=kf*kf`, `a1=-2+kf*(kf+kq)`, `a2=1-kf*kq`, `a1neg=-a1` displayed, poles `real=-a1/(2*a2) disc=a1²-4*a2` with complex handling + reciprocal `r/mag, -i/mag`, stable if both poles magnitude<1 inside unit circle.
  - Time: `chamberlinResponse(kf,kq,N,x)`: `bp+=kf*(input-lp-kq*bp); lp+=kf*bp; out=lp; input=x`; impulse N=160 (test compat) or 256, step N=160 with x=1 after first.
  - Frequency: original `v_freqPlot` uses N=2048 impulse → RFFT → spectrum, log X base100 `0.5*(100^(x/cw-1)-1/100)` log Y `h/2+32*ln(value/max)` max=DC values[0], fill #555 stable #f00 unstable. Our fidelity: freq impulse 1024, DFT RFFT emulation `X[k]=sum x[n]*exp(-j2πkn/N)`, bins 512 half-spectrum, FrequencyResponsePlot renders per-pixel log interpolation same formula, gray #555 stable red #f00.
  - Pole plot: original `v_polePlot` gray arena #e4e4e4 white axes, cross blue inside red outside. Our `PolePlot` matches.
  - Controls: original `c_filterKnob` Fc 20-20000 log, Q 0.01-10 log base24. Our sliders log10: Fc `20..20000` via `10^log`, Q `0.05..10` via `10^log`, content-desc for a11y.

* UI: Two examples as original — Example1 default Fc2000 Q0.8, Example2 Fc1200 Q3.5. Each has Fc Hz (format_freq: <100 1dec, <1000 0dec, else KHz 2dec) + Q + Kq sliders, stability badge `Filter status: Stable/Unstable`, FrequencyResponsePlot, MetricRows Kf/Kq/b0 a1/a2, pole texts inside/outside. Multiple representations card with topology schematic (simplified state-variable: in→(+)→(kf)→(+)→bp→(kf)→lp→out with feedback), transfer equation `H(z)=Kf²z⁻¹/(1-(2-Kf(Kf+Kq))z⁻¹+(1-KfKq)z⁻²)`, RepresentationStrip 6 labels, PolePlot per example. Impulse response card 2 examples, Step response card 2 examples + sidebar note "frequency response not simply plotted from transfer function but FFT of impulse, more honest".

* Observable rubric 2: changing cutoff or resonance updates coefficients, pole values, stability, and native impulse-response plot from same model (CUJ). Also covers second example existence.

### 4.4 Context — Contextual Information (wikipedia.js)

* Source: `wikipedia.js` hover + W key, edit search, Wikipedia result inline, esc/blur dismiss.
* Adaptation: tap any word to query (wordAt handles letters/digits and ’ '), bundled offline facts `ContextRepository` (Wind power in California, Altamont Pass, Tehachapi, San Gorgonio Pass) + live `WikipediaRepository` `action=query&generator=search` (emulating original core v1 search) with timeout 5s, User-Agent. Panel shows loading, title+summary, source label Wikipedia vs Bundled offline context, honest empty state "No Wikipedia result."
* Passage: original renewable advocacy text restored with NREL photo clause: "California leads the nation... 4,258M kWh..." + "More than 13,000 turbines... Altamont Pass (east of San Francisco - a portion of which is shown on the right in this photo from NREL), Tehachapi..."
* Cards: Contextual Information header, how to make existing docs explorable, advocacy passage card with lookup panel when query not blank, tap hint, Encouragement long paragraph (copy/paste/open tab vs effortless), Original interaction mapping note.

* Observable rubric 3: context search returns truthful bundled facts for supported terms and honest empty state for unsupported without leaving app (CUJ).

## 5. User Journeys

* J1 Read→Scenario: Open app → Read shows thesis → Tap "Try the state-park proposition" → Scenario shows $18 charge → Drag tax to 50 → See budget rise to ~$1.2B surplus, attendance fall → Toggle taxpayers → See budget delta drop → Change admission to Free → Attendance rise.
* J2 Read→Filter: Read → "Explore the digital filter" → Filter shows two freq plots → Drag Example1 Fc log from 20 to 20000 → See frequency response log shift right → Lower Q to 0.05 → See Unstable red + poles outside unit circle → Inspect coefficients Kf/Kq/b0/a1/a2 update → View impulse/step plots flatten/explode.
* J3 Read→Context: Read → "Look up a concept" → Context passage → Tap "Altamont" → Bundled fact appears offline → Edit field to "nuclear fusion" → Shows empty state → Edit to "wind" → Live Wikipedia summary if online.
* J4 Navigation persistence: Rotate to landscape → Bottom nav still shows 4 items → Tap each destination remains reachable, no WebView.

## 6. States & Edge Cases

* Scenario: tax 0 + admission 12 everyone true → baseline budget 400M visits 75M (tested). Compliance 0 → taxCollected 0. Admission free for everyone vs payers changes eligible fraction. Large tax >50 capped. Slider steps 19 for compliance => 5% steps.
* Filter: Q→0.01 → kq→100 large values still finite; fc=20000 → kf≈1.98 near Nyquist may cause unstable even at moderate Q. Empty spectrum handled, values[0] fallback to max if DC zero.
* Context: empty query → no panel, hint "Tap any word". Unknown term → empty state, no fabricated result (tested). Offline → bundled facts still work. Punctuation stripping ’ handles possessives.
* Accessibility: contentDescription for charge slider, filter status, pole plots, frequency plots.

## 7. Observable Requirements (maps to features.json)

* R1 PARK.ALL_CONTROLS: 5 adjustable/toggle assumptions, per rubric 1
* R2 PARK.OUTCOME: Budget, closures/restoration/surplus, attendance rise/fall, detailed break down taxCollected/deltaRevenue
* R3 FILTER.CONTROLS: Two examples cutoff+resonance log, per rubric 2
* R4 FILTER.REPRESENTATIONS: Coefficients b0/a1/a2, poles real/imag + inside/outside, stability, freq response log (FFT of impulse), impulse/step plots, topology schematic, transfer function
* R5 CONTEXT.SEARCH: Word-tap → search box + bundled + Wikipedia + empty state, per rubric 3
* R6 NAV.ADAPTIVE: Bottom nav persistent portrait/landscape, per rubric 4
* R7 NATIVE: Compose cards, Slider, FilterChip, Switch, NavigationBar, Typography, semantics, Canvas, no WebView, per rubric 5

## 8. Non-Goals

* No WebView embedding of worriedream.com HTML/presentation assets.
* No outbound browser for Wikipedia links.
* No photo viewer for NREL image, only textual mention.
* No audio playback FilterRockOn/Off (original Flash), only note.
* No user accounts, no backend.

## 9. Technical Constraints

* Kotlin + Jetpack Compose Material3, Navigation Compose.
* Docker compose env `environment/Dockerfile` with Android SDK build-tools 35.0.0, Gradle 8.10.2, pre-fetched deps, offline `--offline` test.
* Verify: `gradle :app:testDebugUnitTest :app:assembleDebug` 6 tests pass.
* Start/smoke via adb as in `scripts/android/start.sh` / `smoke.sh`.

## 10. Acceptance Criteria

* PRD content >300 words (this doc ~1100).
* Implemented scope matches PRD — four destinations as described.
* Screenshots evidence for all four destinations: read-portrait, scenario-portrait, filter-portrait, context-portrait + landscapes if possible.
* Unit tests + assembleDebug pass.
* Smoke: Scenario destination opens, Live outcome visible.

## 11. Evidence

* Screenshots folder includes read, scenario, filter, context captures from Pixel 8 Pro.
* APK `app/build/outputs/apk/debug/app-debug.apk` SHA recorded after final build.
* Original-site digests recorded in SOURCE.md.
