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

## Source Repository and Feature Access

The product reference is Bret Victor's original Explorable Explanations page:

- https://worrydream.com/ExplorableExplanations/
- Captured HTML SHA-256: `7c434b30d70366ea20ae34709c786844b4575203681bcbf4511f5c4b1e3eb25e`

The page is not a GitHub repository and therefore has no upstream Git commit to pin. `SOURCE.md` records the fetched script digests and retained feature mapping. This is a MobileCraft eligibility warning, not a claim that a GitHub source exists.

Reach the retained concepts from the four native bottom destinations: Read, Scenario, Filter, and Context.

## Narration / Walkthrough Videos

Human recording required after final runtime parity verification. Add the `pxl.cl` URL here before submission.
