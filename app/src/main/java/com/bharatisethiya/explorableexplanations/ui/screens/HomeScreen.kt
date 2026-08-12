package com.bharatisethiya.explorableexplanations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(innerPadding: PaddingValues, onOpen: (String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Explorable Explanations", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text("Bret Victor  /  March 10, 2011", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }
        }

        item {
            Text("What does it mean to be an active reader?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        item {
            EssayParagraph(
                "An active reader asks questions, considers alternatives, questions assumptions, and even questions the trustworthiness of the author. An active reader doesn’t passively sponge up information, but uses the author’s argument as a springboard for critical thought and deep understanding."
            )
        }

        item {
            EssayParagraph(
                "A typical book or website displays the author’s argument, and nothing else. We form questions, but can’t answer them. We consider alternatives, but can’t explore them. We question assumptions, but can’t verify them."
            )
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(
                    "The goal is to change people’s relationship with text: from information to be consumed into an environment to think in.",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                )
            }
        }

        item {
            Text("This essay presents three initial ideas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        item {
            EssayIdea(
                title = "1. Reactive documents",
                body = "Play with the author’s premise and assumptions, and see the consequences update immediately—like a spreadsheet that can be read as an explanation.",
                action = "Try the state-park proposition",
                icon = { Icon(Icons.Outlined.Tune, contentDescription = null) },
                onClick = { onOpen("scenario") },
            )
        }

        item {
            EssayIdea(
                title = "2. Explorable examples",
                body = "Make an abstract system concrete. Vary its parameters and watch multiple representations change together to build intuition and make discoveries.",
                action = "Explore the digital filter",
                icon = { Icon(Icons.Outlined.AutoGraph, contentDescription = null) },
                onClick = { onOpen("filter") },
            )
        }

        item {
            EssayIdea(
                title = "3. Contextual information",
                body = "Learn related material just in time and cross-check an author’s claims without abandoning the explanation you are reading.",
                action = "Look up a concept",
                icon = { Icon(Icons.AutoMirrored.Outlined.FactCheck, contentDescription = null) },
                onClick = { onOpen("context") },
            )
        }

        item {
            Text("Reactive Documents — deeper", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            EssayParagraph("Ten Brighter Ideas was early prototype of reactive document: reader can play with premise and assumptions, see consequences immediately. Like spreadsheet without spreadsheet.")
            EssayParagraph("Modeling: There's nothing new about scenario modeling. Authors surely had Excel which answered same questions. But spreadsheet is not an explanation. It is merely dataset and model; it cannot be read. Explanation requires author to interpret results and present via language and graphics.")
            EssayParagraph("Transparency: Reactive document requires author to disclose models behind argument, open them for scrutiny. In Ten Brighter Ideas, reader can directly edit source code of model and visit primary sources for all data. Dishonest authors will always exist, but transparency means faulty model is available to be examined and refuted.")
            EssayParagraph("Debate: Multiple authors could model same situation, readers compare. For Prop 21, groups for and against hurl unsourced soundbites. What if both sides offered reactive documents and reader could critically explore predicted scenarios? What if readers wanted to explore because it was actually fun?")
        }

        item {
            Text("Explorable Examples — deeper", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            EssayParagraph("Intuition: Frequency response not simply plotted from transfer function. Instead, impulse response of actual filter simulated, and its FFT shown — more honest, works even if reader changes topology. z-plane pole positions from transfer function.")
            EssayParagraph("We are shown six different ways of characterizing filter: schematic topology (Media/FilterSchematic.png), coefficients kf/kq, transfer function b0/a1/a2, pole plot, frequency response (FilterFrequencyApprox), impulse/step time-domain (FilterRepresentations.png). Each gives unique insight. By watching how they dance together, develop deep understanding.")
            EssayParagraph("Trust: Playing with response, author hasn't been entirely honest. Formula for Fc is actually approximation. Blue line (nominal Fc) doesn't line up with peak (actual resonant frequency). Good at high Q where we care. Premise of separable parameters is close for 0.3<kf<0.5.")
            EssayParagraph("Explanation: Primary point is subtlety with which explorable is integrated with explanation. Filter description works as static explanation — can be read like normal text. Reader not forced to interact to learn. Interacts to go deeper if curiosity piqued. No UI screaming for attention. Reader simply nudges examples author already presented. Most widgets dump user in sandbox and say figure it out — those are not explanations. Author must guide and provide structure.")
        }

        item {
            Text("What to do?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            EssayParagraph("Bret lists three forward steps: Examples — almost everywhere static explanations begging to be brought to life. Tools — explorable won't catch on if difficult to author; must invent tools enabling authors to make work explorable, almost as easy as writing static text. Released Tangle.js behind this essay. What might authoring tool look like? Fusion of word processor and spreadsheet? Inform-like environment?")
            EssayParagraph("Culture — How do we make readers demand explorable explanations and reject static text?")
        }

        item {
            Text("Postscript from 2024", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            EssayParagraph("Since essay, term 'explorable explanation' gained currency, now means 'any article with interactive pictures'. See explorabl.es, Distill journal, Nicky Case, Amit Patel, Jack Schaedler, Observable, Nextjournal.")
            EssayParagraph("However almost all are pedagogical, not what Bret meant. He meant written argument whose assertions backed by explorable computational models, whose facts, assumptions, calculations all visible and editable. Author's role not just to teach but to convince. Reader's role not to believe but to critically evaluate, rebut, come to broad understanding by modifying models.")
            EssayParagraph("For clearer example, see Model-driven debate section of his climate essay, note on transactional interpretations. At time essay written, he had given up on computer screen as medium for model-grounded discussion. Better approach integrating explorable models into everyday spatial environment — motivation for Dynamicland.")
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Original: https://worrydream.com/ExplorableExplanations/", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("Adaptation: Long-form intro + conclusion condensed but Modeling/Transparency/Debate/Intuition/Trust/What-to-do/Postscript retained per audit. Original images Media/Filter* referenced in Filter lab. Tap words + W becomes tap word in Context.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            Text(
                "Adapted from Bret Victor’s Explorable Explanations, March 10 2011, postscript Feb 2024. Assets: Script/park.js, filter.js, wikipedia.js (MIT).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EssayParagraph(text: String) {
    Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun EssayIdea(
    title: String,
    body: String,
    action: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                icon()
            }
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                Text(action)
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
