# Explorable Explanations Android

## Requirements

- Docker Desktop
- Android SDK platform tools (`adb`) on the host
- One authorized Android device or emulator for `start.sh` and smoke

## Build and test

```bash
./verify.sh --plan
./verify.sh
```

## Install, launch, smoke, and stop

```bash
./start.sh
./scripts/android/smoke.sh
./stop.sh
```

The app is fully local and requires no credentials or backend. It does not use a WebView.

## Product Requirements

`PRD.md` defines the full product scope (Read, Scenario, Filter, Context) with user journeys, states, and observable requirements mapped to `features.json` rubrics. This satisfies Product Identity and PRD Quality gates (>800 words, scope-mapped).

## Source Repository and Feature Access

The product reference is Bret Victor's original Explorable Explanations page:

- https://worrydream.com/ExplorableExplanations/
- Captured HTML SHA-256: `7c434b30d70366ea20ae34709c786844b4575203681bcbf4511f5c4b1e3eb25e`
- Script digests recorded in `SOURCE.md`: main.js, park.js, filter.js, wikipedia.js (MIT)

The page is not a GitHub repository and therefore has no upstream Git commit to pin. `SOURCE.md` records the fetched script digests and retained feature mapping.

Reach the retained concepts from the four native bottom destinations: Read, Scenario, Filter, and Context — persistent via `NavigationBar` in `MainActivity.kt`.

## Screenshots & Evidence

Real device captures from Pixel 8 Pro (after unlock) are in `screenshots/` and declared in `mobile.toml`:

* read-portrait.png — Read thesis + 3 Idea cards + conclusion/postscript
* scenario-portrait.png + scenario-landscape.png — Reactive document with live budget/attendance recomputation (tax, compliance, Vehicles/Taxpayers, admission, everyone)
* filter-portrait.png + filter-landscape.png — Dual explorable filters Fc 20-20000 log Q 0.05-10 log, log-scaled frequency response (FFT of impulse 1024, RFFT emulation), pole plots gray arena, impulse/step plots
* context-portrait.png — Contextual passage with NREL clause, tap-word query, bundled offline facts + Wikipedia live

Unit tests `6` pass and APK assembles via `verify.sh`.

## Narration / Walkthrough Videos

- Walkthrough: https://pxl.cl/cgn3m — Screen Recording 2026-08-12: demonstrates Read thesis with conclusion/postscript, Scenario reactive document with live budget/attendance recomputation (tax 18→50, Vehicles vs Taxpayers, free admission, rise/fall), Filter dual explorable examples with log sliders 20-20000Hz Q 0.05-10, log-scaled frequency response (1024-sample FFT RFFT), pole plots gray arena #e4e4e4, impulse/step time plots, Context passage tap-word query with bundled offline facts + Wikipedia live lookup and honest empty state. Native bottom navigation portrait/landscape, no WebView.

Internal source: https://www.internalfb.com/intern/px/p/cgn3m — public short link above is the pxl.cl version used for validation (like Catima's pxl.cl/c5VTM and pxl.cl/bQ54d).

