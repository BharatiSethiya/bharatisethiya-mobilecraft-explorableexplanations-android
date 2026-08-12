package com.bharatisethiya.explorableexplanations.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExplanationCard(title: String, subtitle: String, modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            content()
        }
    }
}

@Composable
fun MetricRow(vararg metrics: Pair<String, String>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        metrics.forEach { (label, value) ->
            Column {
                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ResponsePlot(values: List<Float>, stable: Boolean, modifier: Modifier = Modifier, isStep: Boolean = false) {
    val description = if (stable) "Stable filter ${if (isStep) "step" else "impulse"} response" else "Unstable filter ${if (isStep) "step" else "impulse"} response"
    Canvas(modifier.fillMaxWidth().height(180.dp).semantics { contentDescription = description }) {
        val axisColor = Color(0xFFAAA79E)
        // Match original v_timePlot: background white already via card, but draw axes
        drawLine(axisColor, Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), 1.dp.toPx())
        drawLine(axisColor, Offset(0f, 0f), Offset(0f, size.height), 1.dp.toPx())
        if (values.size < 2) return@Canvas

        val beforeStepPx = 16.dp.toPx()
        val scale = values.maxOf { kotlin.math.abs(it) }.coerceAtLeast(0.01f)
        val path = Path()
        // start flat like original: move 0,height-1 to beforeStepPx,height-1
        path.moveTo(0f, size.height - 1f)
        path.lineTo(beforeStepPx, size.height - 1f)

        for (x in beforeStepPx.toInt() until size.width.toInt()) {
            val idxFloat = (x - beforeStepPx) / (size.width - beforeStepPx) * (values.size - 1)
            val lowIdx = idxFloat.toInt().coerceIn(0, values.size - 1)
            val highIdx = (lowIdx + 1).coerceIn(0, values.size - 1)
            val frac = idxFloat - lowIdx
            val lowV = values[lowIdx]
            val highV = values[highIdx]
            val v = lowV + frac * (highV - lowV)
            val y = size.height - (v / scale * size.height * 0.5f + size.height * 0.0f) - if (isStep) 0f else size.height * 0.0f
            // For step, original scales by height/2 from bottom: y = height - value*height/2
            // For impulse centered, we want centered around middle
            val yMapped = if (isStep) {
                size.height - v / scale * size.height * 0.5f
            } else {
                size.height / 2f - v / scale * size.height * 0.42f
            }
            if (x == beforeStepPx.toInt()) path.moveTo(x.toFloat(), yMapped) else path.lineTo(x.toFloat(), yMapped)
        }
        val strokeColor = if (stable) Color(0xFF0000FF) else Color(0xFFFF0000) // original #00f stable, #f00 unstable
        drawPath(path, strokeColor, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun FrequencyResponsePlot(values: List<Float>, stable: Boolean, modifier: Modifier = Modifier) {
    // Faithful to views.js v_freqPlot: log X base 100, log Y 32*log(value/max) where max=DC values[0]
    val lineColor = if (stable) Color(0xFF555555) else Color(0xFFFF0000) // original #555 stable, #f00 unstable
    Canvas(
        modifier.fillMaxWidth().height(180.dp).semantics {
            contentDescription = if (stable) "Stable filter frequency response log scaled" else "Unstable filter frequency response log scaled"
        },
    ) {
        val axisColor = Color(0xFFAAA79E)
        drawLine(axisColor, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
        drawLine(axisColor, Offset(0f, 0f), Offset(0f, size.height), 1.dp.toPx())
        if (values.isEmpty()) return@Canvas

        val base = 100.0
        val maxValue = if (values[0] > 0.0001f) values[0] else (values.maxOrNull() ?: 1f)
        val maxValueD = maxValue.toDouble().coerceAtLeast(0.0001)

        // For each x pixel, interpolate magnitude similar to original JS
        val widthPx = size.width.toInt().coerceAtLeast(1)
        val bins = values.size

        // Original getNormalizedFrequencyForX
        fun normFreqForX(x: Int, canvasWidth: Int): Double {
            return 0.5 * (Math.pow(base, x.toDouble() / canvasWidth - 1) - 1.0 / base)
        }

        // Draw column by column like original fillRect per x
        for (x in 0 until widthPx) {
            val norm = normFreqForX(x, widthPx) // 0..~0.5
            // Map norm to spectrum index: norm 0..0.5 -> 0..bins-1
            val idxFloat = norm * 2.0 * (bins - 1) // because bins covers 0..0.5
            val idx = idxFloat.toInt().coerceIn(0, bins - 1)
            val nextIdx = (idx + 1).coerceIn(0, bins - 1)
            val frac = (idxFloat - idx).coerceIn(0.0, 1.0)
            val lowV = values[idx].toDouble()
            val highV = values[nextIdx].toDouble()
            val interp = lowV + frac * (highV - lowV)

            // Log Y like original: y = height/2 + 32*log(value/max)
            val y = if (interp > 0) {
                val logRatio = kotlin.math.ln(interp / maxValueD)
                (size.height / 2.0 + 32.0 * logRatio).coerceAtLeast(0.0)
            } else 0.0

            // Draw 1px vertical bar from bottom up
            val top = (size.height - y).toFloat()
            val bottom = size.height
            drawLine(
                color = lineColor,
                start = Offset(x.toFloat(), bottom),
                end = Offset(x.toFloat(), top),
                strokeWidth = 1.dp.toPx(),
            )
        }
    }
}

@Composable
fun FilterTopology(modifier: Modifier = Modifier, kfLabel: String = "kf", kqLabel: String = "kq") {
    Canvas(modifier.fillMaxWidth().height(120.dp).semantics { contentDescription = "State-variable filter topology kf=$kfLabel kq=$kqLabel" }) {
        val color = Color(0xFF4F4D49)
        val stroke = 2.dp.toPx()
        val y = size.height * 0.38f
        val points = listOf(0.08f, 0.28f, 0.5f, 0.72f, 0.92f).map { size.width * it }
        // main line like Media/FilterSchematic.png approximated
        points.zipWithNext().forEach { (start, end) -> drawLine(color, Offset(start, y), Offset(end, y), stroke) }
        listOf(points[1], points[2], points[3]).forEach { x ->
            drawCircle(Color.White, 13.dp.toPx(), Offset(x, y))
            drawCircle(color, 13.dp.toPx(), Offset(x, y), style = Stroke(stroke))
            drawLine(color, Offset(x - 6.dp.toPx(), y), Offset(x + 6.dp.toPx(), y), stroke)
            drawLine(color, Offset(x, y - 6.dp.toPx()), Offset(x, y + 6.dp.toPx()), stroke)
        }
        // feedback paths mimicking schematic: bp and lp loops
        drawLine(color, Offset(points[3], y), Offset(points[3], size.height * 0.82f), stroke)
        drawLine(color, Offset(points[3], size.height * 0.82f), Offset(points[1], size.height * 0.82f), stroke)
        drawLine(color, Offset(points[1], size.height * 0.82f), Offset(points[1], y + 13.dp.toPx()), stroke)
        // kq feedback annotation
        drawLine(color, Offset(points[1], size.height * 0.55f), Offset(points[1] - 20.dp.toPx(), size.height * 0.55f), stroke)
    }
}

@Composable
fun PolePlot(
    pole1: Pair<Float, Float>,
    pole2: Pair<Float, Float>,
    stable: Boolean,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier.fillMaxWidth().height(200.dp).semantics {
            contentDescription = if (stable) "Stable poles inside unit circle" else "Unstable poles outside unit circle"
        }
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = minOf(size.width, size.height) * 0.42f * 0.6f // original uses canvasWidth*1/4 ~ quarter
        val axisColor = Color.White
        val grayFill = Color(0xFFE4E4E4)
        val unitStroke = Color(0xFF4F4D49)

        // background white (card already white) - draw gray arena like original #e4e4e4
        drawCircle(grayFill, radius, center)
        // white axes inside circle as original
        drawLine(axisColor, Offset(center.x - radius, center.y), Offset(center.x + radius, center.y), 2.dp.toPx())
        drawLine(axisColor, Offset(center.x, center.y - radius), Offset(center.x, center.y + radius), 2.dp.toPx())

        // unit circle outline
        drawCircle(unitStroke, radius, center, style = Stroke(1.dp.toPx()))

        fun mapPole(p: Pair<Float, Float>): Offset {
            val x = center.x + p.first * radius
            val y = center.y - p.second * radius
            return Offset(x, y)
        }

        fun isInside(p: Pair<Float, Float>) = p.first * p.first + p.second * p.second < 1f

        listOf(pole1, pole2).forEach { pole ->
            val pos = mapPole(pole)
            val inside = isInside(pole)
            val poleColor = if (inside) Color(0xFF0000FF) else Color(0xFFFF0000) // original #00f inside, #f00 outside
            // cross as in views.js drawCrossAtPoint
            val crossR = 4.dp.toPx()
            drawLine(poleColor, Offset(pos.x - crossR, pos.y - crossR), Offset(pos.x + crossR, pos.y + crossR), 1.dp.toPx())
            drawLine(poleColor, Offset(pos.x - crossR, pos.y + crossR), Offset(pos.x + crossR, pos.y - crossR), 1.dp.toPx())
        }
    }
}
