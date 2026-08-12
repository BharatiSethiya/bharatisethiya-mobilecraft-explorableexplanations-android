# Source provenance and adaptation

Original: https://worrydream.com/ExplorableExplanations/

Fetched source digests:

- HTML: `7c434b30d70366ea20ae34709c786844b4575203681bcbf4511f5c4b1e3eb25e`
- `Script/main.js`: `a726fc9f1e5f530e4e78260a7fd5ca9ec6005ed835756173bbdcc19f3a534191`
- `Script/park.js`: `79e9f446e1bfbb59db485c31d3cfcc3b74ecb67045fd88a1821b2e0b03ac9cea`
- `Script/filter.js`: `e5b2b3666c4a96d87d584816f2a3b0d5b54bf2df879bf5416c0043b1d25093d1`
- `Script/wikipedia.js`: `8af4ce10433cc9ee9b77344e1ac72d97748aba34e7b1ed96e4925e650ffc8fbd`

The referenced JavaScript headers state: “Created by Bret Victor” and “MIT open-source license.” The Kotlin app translates the public park and filter models but does not embed the website, its HTML, or its presentation assets.

Native adaptation:

- Long-form introduction and conclusion become a concise Read destination.
- Inline draggable numbers become labeled Material sliders and chips.
- The filter canvases become a touch-controlled native Compose Canvas and numeric representations.
- Hover + W contextual lookup becomes a touch-first search field with bundled, offline context.
- Persistent bottom navigation replaces document anchors and browser scrolling.
