package com.bharatisethiya.explorableexplanations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bharatisethiya.explorableexplanations.model.ParkInputs
import com.bharatisethiya.explorableexplanations.model.ParkScenarioCalculator
import com.bharatisethiya.explorableexplanations.ui.components.ExplanationCard
import com.bharatisethiya.explorableexplanations.ui.components.MetricRow
import java.util.Locale

@Composable
fun ScenarioScreen(innerPadding: PaddingValues) {
    var tax by rememberSaveable { mutableFloatStateOf(18f) }
    var compliance by rememberSaveable { mutableFloatStateOf(100f) }
    var taxPerVehicle by rememberSaveable { mutableStateOf(true) }
    var admission by rememberSaveable { mutableFloatStateOf(0f) }
    var admissionForEveryone by rememberSaveable { mutableStateOf(false) }
    val outcome = ParkScenarioCalculator.calculate(ParkInputs(tax, compliance, taxPerVehicle, admission, admissionForEveryone))

    LazyColumn(
        Modifier.fillMaxSize().padding(innerPadding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("State park scenario", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text("Adjust the proposition’s assumptions. The explanation responds as a model, not a static page.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            ExplanationCard("Annual charge", "Explore values from $0 to $50.") {
                Text("$${tax.toInt()}", style = MaterialTheme.typography.headlineMedium)
                AccessibleSlider("Annual charge", "${tax.toInt()} dollars") {
                    Slider(tax, { tax = it }, valueRange = 0f..50f)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(taxPerVehicle, { taxPerVehicle = true }, { Text("Vehicles") })
                    FilterChip(!taxPerVehicle, { taxPerVehicle = false }, { Text("Taxpayers") })
                }
            }
        }
        item {
            ExplanationCard("Participation", "Percentage expected to pay the annual charge.") {
                Text("${compliance.toInt()}%", style = MaterialTheme.typography.headlineMedium)
                AccessibleSlider("Participation", "${compliance.toInt()} percent") {
                    Slider(compliance, { compliance = it }, valueRange = 0f..100f, steps = 19)
                }
            }
        }
        item {
            ExplanationCard("Park admission", "Set the new per-vehicle price.") {
                Text(if (admission == 0f) "Free" else "$${admission.toInt()}", style = MaterialTheme.typography.headlineMedium)
                AccessibleSlider("Park admission", if (admission == 0f) "Free" else "${admission.toInt()} dollars") {
                    Slider(admission, { admission = it }, valueRange = 0f..25f, steps = 24)
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Applies to everyone")
                    Switch(
                        checked = admissionForEveryone,
                        onCheckedChange = { admissionForEveryone = it },
                        modifier = Modifier.semantics {
                            contentDescription = "Admission applies to everyone"
                            stateDescription = if (admissionForEveryone) "Everyone" else "Only visitors who paid the annual charge"
                        },
                    )
                }
            }
        }
        item {
            // Mirrors original HTML's detailed analysis paragraph with plus/minus and rise/fall
            ExplanationCard(
                "Analysis breakdown",
                "How the budget composes from tax and admission revenue, like the original document."
            ) {
                MetricRow(
                    "Budget" to "$${format(outcome.budgetMillions)}M",
                    "Change" to "${if (outcome.deltaBudgetPositive) "+" else "-"}$${format(kotlin.math.abs(outcome.budgetDeltaMillions))}M",
                    "Visits" to "${format(outcome.visitsMillions)}M",
                )
                MetricRow(
                    "Tax collected" to "$${format(outcome.taxCollectedMillions)}M",
                    "Admission Δ" to "${if (outcome.deltaRevenuePositive) "+" else "-"}$${format(kotlin.math.abs(outcome.deltaRevenueMillions))}M",
                    "" to "",
                )
                Text(outcome.detailedSummary, style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            ExplanationCard("Live outcome", outcome.summary) {
                MetricRow(
                    "Budget" to "$${format(outcome.budgetMillions)}M",
                    "Change" to "$${format(outcome.budgetDeltaMillions)}M",
                    "Visits" to "${format(outcome.visitsMillions)}M",
                )
                Text(outcome.attendanceSentence, style = MaterialTheme.typography.bodyMedium)
                if (outcome.scenarioIndex == 0) {
                    Text(
                        "${outcome.closedParkCount} parks would be shut down at least part-time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        item {
            ExplanationCard(
                "How to read it",
                "Model (from park.js): budget = oldBudget + taxCollected + (newRevenue-oldRevenue). Visits follow fake demand curve max(0.2, 1+0.5*atan(1-avgAdmission/old)). Toggle vehicles vs taxpayers changes tax base 28M vs 13.6M."
            )
        }
    }
}

private fun format(value: Float): String = String.format(Locale.US, "%.1f", value)

@Composable
private fun AccessibleSlider(label: String, value: String, content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().semantics(mergeDescendants = true) {
        contentDescription = label
        stateDescription = value
    }) { content() }
}
