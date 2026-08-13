package com.bharatisethiya.explorableexplanations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bharatisethiya.explorableexplanations.ui.components.ContextLookupPanel
import com.bharatisethiya.explorableexplanations.ui.components.ExplanationCard

@Composable
fun ContextScreen(innerPadding: PaddingValues) {
    var query by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Contextual Information", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        }
        item {
            Text(
                "How do we make existing documents explorable? How can active readers ask questions and question assumptions while reading normal text?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            Text(
                "Consider this passage from a typical advocacy site. Tap any word to check it without leaving your place.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        item {
            AdvocacyPassage(
                query = query,
                onQueryChange = { query = it },
                onTermSelected = { query = it },
            )
        }
        item {
            Text(
                if (query.isBlank()) "Tap any word in the passage to open contextual information."
                else "Edit the lookup to follow another question, just as on the original site.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        item {
            Text(
                "Does California really lead the nation in wind capacity? In original, hover word + press W (for Wikipedia) + type 'wind' shows Texas and Iowa now ahead. Article is old or outdated. Try other words.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ExplanationCard(
                "Encouragement",
                "There's nothing new about looking up related information. You probably do it frequently — by selecting a word, copying to clipboard, opening new tab, pasting into Google, scanning results, clicking Wikipedia, scanning article, closing tab, finding your place. The example above does essentially just that, except almost effortless and you don't lose your place. This makes a huge difference. Readers constantly trade off curiosity vs laziness. Lowering effort barrier encourages asking every question that comes to mind.",
            )
        }
        item {
            ExplanationCard(
                "Original interaction",
                "On desktop: Move your mouse over word California in passage above, press W key, type wind. In this app: tap any word to populate search box positioned near passage, edit query, stay in place — per SOURCE.md touch-first adaptation."
            )
        }
    }
}

@Composable
private fun AdvocacyPassage(
    query: String,
    onQueryChange: (String) -> Unit,
    onTermSelected: (String) -> Unit,
) {
    val passage = "California leads the nation in installed wind generation capacity. Over a third of the wind power in the United States is generated in California. In 2004, wind energy in California produced 4,258 million kilowatt-hours of electricity, about 1.5 percent of the state’s total electricity. That's more than enough to light a city the size of San Francisco.\n\n" +
        "More than 13,000 of California’s wind turbines, or 95 percent of all of California’s wind generating capacity and output, are located in three primary regions: Altamont Pass (east of San Francisco - a portion of which is shown on the right in this photo from NREL), Tehachapi (south east of Bakersfield) and San Gorgonio (near Palm Springs, east of Los Angeles)."

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Renewable Energy in California", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            AccessiblePassage(passage, onTermSelected)
            if (query.isNotBlank()) {
                ContextLookupPanel(query, onQueryChange)
            }
        }
    }
}

internal fun passageWords(text: String): List<String> =
    Regex("[\\p{L}\\p{N}]+(?:[’'][\\p{L}\\p{N}]+)?")
        .findAll(text)
        .map { it.value }
        .distinctBy { it.lowercase() }
        .toList()

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AccessiblePassage(text: String, onTermSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        text.split("\n\n").forEach { paragraph ->
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Regex("\\S+").findAll(paragraph).forEach { match ->
                    val displayed = match.value
                    val lookup = displayed.trim { !it.isLetterOrDigit() && it != '’' && it != '\'' }
                    Text(
                        text = displayed,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .semantics {
                                contentDescription = lookup
                                role = Role.Button
                            }
                            .clickable(enabled = lookup.isNotBlank()) { onTermSelected(lookup) },
                    )
                }
            }
        }
    }
}
