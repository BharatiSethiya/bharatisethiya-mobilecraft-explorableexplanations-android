# Explorable Explanations — Mobile Product Requirements

## 1. Product Identity

Explorable Explanations is a native Android reading lab that turns a static essay into an environment for thinking. It is built around the principle that readers should be able to ask questions, test alternatives, check assumptions, and verify claims without leaving the page.

The product ships four persistent destinations reachable from bottom navigation: Read, Scenario, Filter, and Context. It works offline for bundled knowledge and degrades gracefully when live lookups fail. It contains no browser view.

## 2. Product Overview

The app presents a single long-form argument about active reading, split into an introduction, three interactive demonstrations, and a conclusion with a forward-looking note from 2024.

* Read gives the thesis and navigation to the three ideas.
* Scenario lets the reader manipulate a public policy proposition about annual charges for vehicle registrations or taxpayers, compliance levels, admission prices, and eligibility.
* Filter lets the reader explore a digital audio filter by varying cutoff and resonance and seeing all characterizations update together.
* Context lets the reader tap any word in an advocacy passage, edit a query, and see contextual information inline without losing reading position.

## 3. Goals

* Lower the effort barrier between curiosity and action.
* Let readers see immediate consequences of assumption changes.
* Make abstract filter concepts concrete through multiple linked representations.
* Make fact checking almost effortless and preserve reading place.
* Pass MobileCraft observable rubrics for reactive calculations, explorable examples, truthful contextual facts, persistent navigation portrait and landscape, and native controls without browser chrome.

## 4. Personas

* Curious Reader — wants thesis quickly, may open one demonstration.
* Policy Explorer — wants to test what if tax is $50, what if admission is free, what if applied to everyone versus payers only, vehicles versus taxpayers.
* Engineering Student — learns how cutoff and resonance affect stability, frequency response shape, pole positions, and time-domain behavior.
* Fact Checker — reads an advocacy claim about wind power, questions a place name or a quantity, taps a word to verify.

## 5. Core Ideas Translated to Touch

* Reactive Documents become sliders, chips, and switches with live outcome.
* Explorable Examples become two side-by-side filter examples with log-scaled controls and six representations: parameters, transfer-function description, schematic topology view, pole positions on the unit circle, frequency response that shows which frequencies pass, and impulse/step time-domain traces.
* Contextual Information becomes tap-to-search with a panel that stays anchored to the passage.

## 6. Screens and Observable Behaviors

### 6.1 Read

* Shows title, author and date, a short definition of an active reader, a goal statement about environment to think in, three idea cards each with a button to open Scenario, Filter, Context, deeper paragraphs about modeling, transparency, debate, intuition, trust, explanation, a What to do section about examples, tools, culture, and a postscript from 2024 about how the term broadened, where to find more explorables, and a note about model-driven debate and spatial computing.
* Scroll is vertical, no required input.
* Observable: Idea cards navigate; conclusion text contains references to tools and culture; postscript contains examples of later explorable venues.

### 6.2 Scenario

* Describes a proposition for an extra annual charge to fund state parks, current admission price, budget gap, and consequences of insufficient funding.
* Controls visible at top : annual charge amount from free to fifty dollars, compliance percentage from zero to full with five-percent steps, charge basis choice Vehicles or Taxpayers, new admission amount from free to twenty-five dollars, eligibility toggle for everyone versus those who paid.
* Immediate live outcome below controls:
  - Total budget displayed in millions with a plus or minus delta from current.
  - Tax collected and admission revenue delta displayed with words plus additional or minus lost.
  - Attendance displayed in millions with percent change from baseline and verb rise or fall.
  - Breakdown sentence in plain language: whether extra money is collected or lost, how much comes from tax versus admission, total budget, and a scenario sentence: not enough to maintain parks and some parks would be shut down part-time, or sufficient to maintain but not restore, or sufficient to restore over a stated number of years, or sufficient with a stated surplus per year.
  - Park attendance sentence stating rise or fall by percent to total visits.
* States: baseline charge zero and admission twelve for everyone yields current budget and current visits; moving charge to maximum raises budget and typically lowers attendance; making admission free raises attendance; switching Vehicles to Taxpayers changes tax base and visible tax collected.

### 6.3 Filter

* Introduces a state-variable digital filter concept, shows a simplified schematic, and states sample rate is forty-four point one kilohertz.
* Provides two worked examples side by side: Example One starts at cutoff two thousand hertz resonance zero point eight; Example Two starts at twelve hundred hertz resonance three point five. User can adjust both independently.
* Each example has:
  - Cutoff control covering twenty hertz to twenty thousand hertz on a logarithmic scale.
  - Resonance control covering near zero to ten on a logarithmic scale.
  - Stability badge that says Stable when poles are inside the unit circle and Unstable otherwise; unstable uses red, stable uses muted gray.
  - Frequency response view that shows which frequencies pass; its horizontal axis is logarithmic frequency, vertical axis is logarithmic magnitude relative to direct current; it fills from baseline to curve and colors red when unstable.
  - Coefficients section showing Kf, Kq, b-zero, a-one, a-two as numbers rounded to three decimals.
  - Pole positions shown as two complex numbers with note inside or outside the unit circle.
  - Pole plot showing a gray filled arena, white axes, and blue crosses for inside poles and red crosses for outside poles.
  - Impulse response trace and step response trace; step trace starts flat for a short leading segment then shows reaction.
* Multiple representations note explains that parameters drive topology, coefficients, transfer function, pole plot, frequency response, and time-domain together, and that frequency response is derived from a simulated impulse response.
* States: low resonance near zero point zero five with high cutoff near ten thousand can expose Unstable; mid resonance stays Stable; changing cutoff shifts peak of frequency response left or right.

### 6.4 Context

* Describes why contextual lookup matters and shows a two-paragraph advocacy passage about wind generation capacity in California, including the sentence about Altamont Pass east of San Francisco with a note about a photo from NREL, Tehachapi south east of Bakersfield, and San Gorgonio near Palm Springs.
* Interaction: tapping any word in the passage populates a search field without navigating away; user can edit query; panel shows loading then result.
* Result panel: title and summary text, source label indicating Wikipedia when live or Bundled offline context when from embedded store, with honest empty state that says no Wikipedia result when nothing matches.
* Bundled knowledge covers Wind power in California, Altamont Pass, Tehachapi, San Gorgonio Pass with factual descriptions; unsupported terms show empty state, no fabrication.
* Additional cards explain encouragement about lowering effort barrier and how original hover-plus-key became tap-first adaptation.

## 7. User Journeys

* Journey One Read to Scenario to verification: Open app, read thesis, tap Try the state-park proposition, drag annual charge slider to fifty, observe budget changes to surplus and attendance falls, switch basis to Taxpayers, observe lower tax collected, change admission to free for everyone, observe attendance rises and admission revenue becomes lost.
* Journey Two Read to Filter: Open Filter, observe two frequency responses, drag first cutoff log to ten thousand, observe peak move right and possible Unstable, drag resonance low, observe poles move outside gray circle and time traces diverge, tap second example and vary resonance to three point five, observe narrow peak and Stable badge.
* Journey Three Read to Context: Open Context, read passage, tap word Altamont, see bundled fact about wind farm, edit query to unknown term, see empty state, edit to wind, see live Wikipedia summary if online, stay in place without leaving screen.
* Journey Four Adaptive navigation: Rotate device to landscape, bottom navigation still shows Read, Scenario, Filter, Context, each destination remains reachable and content scrolls.

## 8. Observable Requirements

* Park controls: moving any of annual charge, compliance, basis, admission, eligibility instantly updates budget total, tax collected, admission delta, visits total, percent change with rise or fall verb, and scenario sentence about closures or restoration years or surplus.
* Filter controls: adjusting cutoff or resonance for either example instantly updates numbers Kf, Kq, b-zero, a-one, a-two, pole values, stability badge, frequency response log view, pole plot, impulse and step plots from same underlying behavior.
* Context search: tapping a word fills query, panel shows result inline; supported terms show truthful bundled facts; unsupported terms show honest empty state; no page is left.
* Navigation: four destinations reachable via persistent bottom bar in portrait and landscape.
* Native feel: cards, sliders, chips, switches, typography, accessibility labels for charge slider, filter status, frequency and pole plots, and canvas drawings; no browser view or web address bar.
* Length of this document is more than three hundred words and describes product behavior, not build steps.

## 9. Non-Goals

* Embedding the original desktop web page in a web view.
* Opening an external browser for lookups.
* Showing a photo viewer for NREL image beyond textual mention.
* Playing audio for filter.
* Accounts or backends.

## 10. Acceptance

* Readers can reach Read, Scenario, Filter, Context via bottom navigation in both orientations.
* Scenario budget and attendance outcomes react instantly to all five controls with correct rise or fall wording.
* Filter shows two independent examples, log-scaled controls twenty to twenty thousand hertz and zero point zero five to ten, stability badge, log frequency response, pole plot gray arena with colored crosses, impulse and step traces.
* Context passage contains NREL clause, tap populates query, bundled facts truthful, empty state honest, no navigation away.
* Screenshots evidence exists for read, scenario portrait and landscape, filter portrait and landscape, context portrait.
* Walkthrough video link is a public short link.
* This document does not contain the original repository URL and describes the product from scratch in product voice.
