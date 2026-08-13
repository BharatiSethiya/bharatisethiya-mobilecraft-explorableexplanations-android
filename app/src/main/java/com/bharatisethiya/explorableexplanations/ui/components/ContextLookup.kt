package com.bharatisethiya.explorableexplanations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bharatisethiya.explorableexplanations.model.ContextRepository
import com.bharatisethiya.explorableexplanations.model.WikipediaRepository
import com.bharatisethiya.explorableexplanations.model.WikipediaResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ContextLookupPanel(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val bundledFact = ContextRepository.search(query).firstOrNull()
    var wikiResult by remember { mutableStateOf<WikipediaResult?>(null) }
    var loading by remember { mutableStateOf(false) }
    var lookupFailed by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        wikiResult = null
        lookupFailed = false
        if (query.isBlank()) return@LaunchedEffect
        loading = true
        delay(350)
        wikiResult = runCatching {
            withContext(Dispatchers.IO) { WikipediaRepository.lookup(query) }
        }.getOrNull()
        loading = false
        lookupFailed = wikiResult == null && bundledFact == null
    }

    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Wikipedia lookup") },
                singleLine = true,
            )
            val title = wikiResult?.title ?: bundledFact?.title
            val summary = wikiResult?.summary ?: bundledFact?.summary
            when {
                loading -> Text("Looking up Wikipedia…")
                title != null && summary != null -> {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        summary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 8,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        if (wikiResult != null) "Wikipedia" else "Bundled offline context",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                lookupFailed -> Text("No Wikipedia result. Check the connection or edit the lookup.")
            }
        }
    }
}

fun wordAt(text: String, offset: Int): String? {
    if (text.isEmpty()) return null
    var position = offset.coerceIn(0, text.lastIndex)
    if (!text[position].isLetterOrDigit() && position > 0) position--
    if (!text[position].isLetterOrDigit()) return null
    var start = position
    var end = position + 1
    while (start > 0 && text[start - 1].isWordCharacter()) start--
    while (end < text.length && text[end].isWordCharacter()) end++
    return text.substring(start, end).trim('’', '\'').takeIf { it.isNotBlank() }
}

private fun Char.isWordCharacter() = isLetterOrDigit() || this == '’' || this == '\''
