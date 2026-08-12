package com.bharatisethiya.explorableexplanations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bharatisethiya.explorableexplanations.model.ChamberlinFilter
import com.bharatisethiya.explorableexplanations.model.FilterState
import com.bharatisethiya.explorableexplanations.ui.components.ArticleSection
import com.bharatisethiya.explorableexplanations.ui.components.ExplanationCard
import com.bharatisethiya.explorableexplanations.ui.components.FilterTopology
import com.bharatisethiya.explorableexplanations.ui.components.FrequencyResponsePlot
import com.bharatisethiya.explorableexplanations.ui.components.MetricRow
import com.bharatisethiya.explorableexplanations.ui.components.RepresentationStrip
import com.bharatisethiya.explorableexplanations.ui.components.ResponsePlot
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

@Composable
fun FilterScreen(innerPadding: PaddingValues) {
    // Two filters as in original: fc1=2000 q1=0.8 and fc2=1200 q2=3.5
    var cutoff1 by rememberSaveable { mutableFloatStateOf(2_000f) }
    var resonance1 by rememberSaveable { mutableFloatStateOf(0.8f) }
    var cutoff2 by rememberSaveable { mutableFloatStateOf(1_200f) }
    var resonance2 by rememberSaveable { mutableFloatStateOf(3.5f) }

    val result1 = ChamberlinFilter.calculate(FilterState(cutoff1, resonance1))
    val result2 = ChamberlinFilter.calculate(FilterState(cutoff2, resonance2))

    LazyColumn(
        Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Explore a digital filter", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text(
                "Move the controls and watch the topology, coefficients, frequency response, poles, and stability change together — just like the original two example responses.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ExplanationCard("State-variable filter", "A simplified digital adaptation of the analog state-variable filter. Kf = 2*sin(pi*Fc/Fs), Kq = 1/Q. Topology: in -> (+) -> (kf) -> (+) -> . -> (kf) -> (+) -> [z^-1] -> out, with feedback.") {
                FilterTopology(kfLabel = "%.3f".format(result1.kf), kqLabel = "%.3f".format(result1.kq))
                Text(
                    "H(z) = Kf²·z⁻¹ / (1 - (2-Kf·(Kf+Kq))·z⁻¹ + (1-Kf·Kq)·z⁻²)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Filter 1 controls - log-scaled like original c_filterKnob (Fc 20-20000 log, Q 0.01-10 log base 24 approximated as log10)
        item {
            ExplanationCard("Example 1 — Cutoff & Resonance", "First frequency response from original (Fc=2000, Q=0.8). Log-drag like original knob.") {
                Text("${formatFreq(cutoff1)}, Q ${format(resonance1)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Fc: ${formatFreq(cutoff1)}", style = MaterialTheme.typography.headlineMedium)
                Slider(
                    value = log10(cutoff1.coerceAtLeast(20f)),
                    onValueChange = { cutoff1 = 10f.pow(it) },
                    valueRange = log10(20f)..log10(20000f),
                    modifier = Modifier.semantics { contentDescription = "Fc1 ${cutoff1.toInt()} Hz" },
                )
                Text("Q: ${format(resonance1)} (Kq=${format(result1.kq)})", style = MaterialTheme.typography.headlineMedium)
                Slider(
                    value = log10(resonance1.coerceAtLeast(0.01f)),
                    onValueChange = { resonance1 = 10f.pow(it) },
                    valueRange = log10(0.05f)..log10(10f),
                    modifier = Modifier.semantics { contentDescription = "Q1 ${format(resonance1)}" },
                )

                val status = if (result1.stable) "Stable" else "Unstable"
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (result1.stable) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.semantics { contentDescription = "Filter 1 status: $status" },
                ) {
                    Text(status, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontWeight = FontWeight.SemiBold)
                }
                FrequencyResponsePlot(result1.frequencyResponse, result1.stable)
                MetricRow("Kf" to format(result1.kf), "Kq" to format(result1.kq), "b₀" to format(result1.b0))
                MetricRow("a₁" to format(result1.a1), "a₂" to format(result1.a2))
                Text("Pole 1: (${format(result1.pole1.first)}, ${format(result1.pole1.second)}i) ${if (result1.pole1.first * result1.pole1.first + result1.pole1.second * result1.pole1.second < 1f) "inside" else "outside"}")
                Text("Pole 2: (${format(result1.pole2.first)}, ${format(result1.pole2.second)}i) ${if (result1.pole2.first * result1.pole2.first + result1.pole2.second * result1.pole2.second < 1f) "inside" else "outside"}")
            }
        }

        // Filter 2 controls
        item {
            ExplanationCard("Example 2 — Cutoff & Resonance", "Second frequency response from original (Fc=1200, Q=3.5). Original y is log Q base 24, x log frequency base 100.") {
                Text("${formatFreq(cutoff2)}, Q ${format(resonance2)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Fc: ${formatFreq(cutoff2)}", style = MaterialTheme.typography.headlineMedium)
                Slider(
                    value = log10(cutoff2.coerceAtLeast(20f)),
                    onValueChange = { cutoff2 = 10f.pow(it) },
                    valueRange = log10(20f)..log10(20000f),
                    modifier = Modifier.semantics { contentDescription = "Fc2 ${cutoff2.toInt()} Hz" },
                )
                Text("Q: ${format(resonance2)} (Kq=${format(result2.kq)})", style = MaterialTheme.typography.headlineMedium)
                Slider(
                    value = log10(resonance2.coerceAtLeast(0.01f)),
                    onValueChange = { resonance2 = 10f.pow(it) },
                    valueRange = log10(0.05f)..log10(10f),
                    modifier = Modifier.semantics { contentDescription = "Q2 ${format(resonance2)}" },
                )

                val status = if (result2.stable) "Stable" else "Unstable"
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (result2.stable) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.semantics { contentDescription = "Filter 2 status: $status" },
                ) {
                    Text(status, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontWeight = FontWeight.SemiBold)
                }
                FrequencyResponsePlot(result2.frequencyResponse, result2.stable)
                MetricRow("Kf" to format(result2.kf), "Kq" to format(result2.kq), "b₀" to format(result2.b0))
                MetricRow("a₁" to format(result2.a1), "a₂" to format(result2.a2))
            }
        }

        item {
            ExplanationCard("Multiple representations", "The same parameters drive every view — 6 ways as in original: topology, coefficients, transfer function, poles (z-plane), frequency response (FFT of impulse), impulse/step time-domain.") {
                RepresentationStrip(listOf("Parameters", "Transfer function", "Schematic", "Z-plane", "Step response", "Frequency response"))
                Text("Example 1 poles inside unit circle = stable, outside = unstable — original shows v_polePlot.", style = MaterialTheme.typography.bodySmall)
                com.bharatisethiya.explorableexplanations.ui.components.PolePlot(result1.pole1, result1.pole2, result1.stable)
                Text("Example 2 poles:", style = MaterialTheme.typography.bodySmall)
                com.bharatisethiya.explorableexplanations.ui.components.PolePlot(result2.pole1, result2.pole2, result2.stable)
            }
        }

        item {
            ExplanationCard("Impulse response", "Time-domain view: how filter reacts to one brief input. Matches chamberlinImpulseResponse(kf,kq).") {
                Text("Example 1", style = MaterialTheme.typography.titleSmall)
                ResponsePlot(result1.response, result1.stable)
                Text("Example 2", style = MaterialTheme.typography.titleSmall)
                ResponsePlot(result2.response, result2.stable)
            }
        }

        item {
            ExplanationCard("Step response", "Second time-domain representation from filter.js chamberlinStepResponse — input stays at 1 after first sample.") {
                Text("Example 1 step", style = MaterialTheme.typography.titleSmall)
                ResponsePlot(result1.stepResponse, result1.stable, isStep = true)
                Text("Example 2 step", style = MaterialTheme.typography.titleSmall)
                ResponsePlot(result2.stepResponse, result2.stable, isStep = true)
                Text(
                    "Sidebar from original: frequency response is not simply plotted from transfer function. Instead, impulse response simulated and FFT shown — more honest, works if topology changes. Poles are from transfer function.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            ArticleSection(
                "Intuition",
                listOf(
                    "By watching the result change as we adjust parameters, we can develop an intuition for the system’s behavior. Each representation gives a unique insight.",
                    "Watching all six respond to experimentation—and dance with one another—builds understanding of not just this topology, but digital filtering in general. Exploring the filter space becomes a game.",
                    "The frequency response is derived from the simulated impulse response and its FFT, rather than simply plotted from the transfer function. The z-plane poles come from the transfer function.",
                ),
            )
        }

        item {
            ArticleSection(
                "Trust",
                listOf(
                    "Playing with the response reveals that the cutoff-frequency formula is an approximation: the nominal cutoff does not always line up with the actual resonant peak.",
                    "The approximation is good at high Q. The claim that cutoff and resonance are fully independent is not strictly true, although it is fairly close for 0.3 < Kf < 0.5. Try low Q and high cutoff to expose instability.",
                ),
            )
        }

        item {
            ArticleSection(
                "Explanation",
                listOf(
                    "The novelty of an interactive widget is not the point. This is an explorable explanation because the explorable model is integrated with the explanation.",
                    "It can be read like normal text. Interaction is optional and lets a curious reader go deeper without being transported to a separate sandbox.",
                    "The author must still guide the reader and structure the learning experience. The reader then responds by asking and answering the questions that the author provokes.",
                ),
            )
        }
    }
}

private fun format(value: Float): String = String.format(Locale.US, "%.3f", value)

private fun formatFreq(freq: Float): String {
    return if (freq < 100f) String.format(Locale.US, "%.1f Hz", freq)
    else if (freq < 1000f) String.format(Locale.US, "%.0f Hz", freq)
    else String.format(Locale.US, "%.2f KHz", freq / 1000f)
}
