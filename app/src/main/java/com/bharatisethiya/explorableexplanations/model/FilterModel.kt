package com.bharatisethiya.explorableexplanations.model

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class FilterState(val cutoffHz: Float = 2_000f, val resonance: Float = 0.8f)

data class FilterResult(
    val kf: Float,
    val kq: Float,
    val b0: Float,
    val a1: Float,
    val a2: Float,
    val pole1: Pair<Float, Float>,
    val pole2: Pair<Float, Float>,
    val stable: Boolean,
    val response: List<Float>, // impulse
    val stepResponse: List<Float>,
    val frequencyResponse: List<Float>,
)

object ChamberlinFilter {
    private const val SAMPLE_RATE = 44_100f
    private const val TIME_SAMPLES = 160 // kept for unit-test compatibility (original v_timePlot uses 256)
    private const val FREQ_IMPULSE_SAMPLES = 1024 // original v_freqPlot uses 2048 - 1024 keeps performance while faithful
    private const val FREQ_BINS = 512 // half spectrum (0..Nyquist)

    fun calculate(state: FilterState, samples: Int = TIME_SAMPLES): FilterResult {
        val kf = (2.0 * sin(PI * state.cutoffHz / SAMPLE_RATE)).toFloat()
        val kq = 1f / state.resonance
        val b0 = kf * kf
        val a1 = -2f + kf * (kf + kq)
        val a2 = 1f - kf * kq
        val roots = poles(a1, a2)
        val stable = roots.all { (real, imaginary) -> real * real + imaginary * imaginary < 1f }

        // Time-domain views - matches chamberlinImpulseResponse / chamberlinStepResponse
        val impulse = response(kf, kq, samples, inputAfterFirst = 0f)
        val step = response(kf, kq, samples, inputAfterFirst = 1f)

        // Frequency-domain view - original v_freqPlot: N=2048 impulse then RFFT + log scaling
        // We use 1024 impulse + DFT magnitudes to emulate RFFT. Raw magnitudes returned; log scaling happens in UI
        val freqImpulse = response(kf, kq, FREQ_IMPULSE_SAMPLES, inputAfterFirst = 0f)
        val magnitudeSpectrum = spectrumRFFT(freqImpulse, FREQ_BINS)

        return FilterResult(kf, kq, b0, a1, a2, roots[0], roots[1], stable, impulse, step, magnitudeSpectrum)
    }

    // Mirrors chamberlinResponse in filter.js - input=1 first sample, then x (0 impulse, 1 step)
    private fun response(kf: Float, kq: Float, samples: Int, inputAfterFirst: Float): List<Float> {
        var lowPass = 0f
        var bandPass = 0f
        var input = 1f
        return List(samples) {
            bandPass += kf * (input - lowPass - kq * bandPass)
            lowPass += kf * bandPass
            input = inputAfterFirst
            lowPass
        }
    }

    // Emulates RFFT forward + spectrum as in views.js v_freqPlot, using DFT for correctness
    // Returns raw magnitudes for bins 0..bins-1 covering 0..Nyquist, like fft.spectrum
    private fun spectrumRFFT(impulse: List<Float>, bins: Int): List<Float> {
        val n = impulse.size
        return List(bins) { k ->
            var real = 0.0
            var imag = 0.0
            // DFT: X[k] = sum_{n} x[n] * exp(-j*2pi*k*n/N)
            for (idx in impulse.indices) {
                val angle = 2.0 * PI * k * idx / n
                val sample = impulse[idx].toDouble()
                real += sample * cos(angle)
                imag -= sample * sin(angle)
            }
            sqrt(real * real + imag * imag).toFloat()
        }
    }

    private fun poles(a1: Float, a2: Float): List<Pair<Float, Float>> {
        if (a2 == 0f) return listOf(-1f / a1 to 0f, -1f / a1 to 0f)
        val real = -a1 / (2f * a2)
        val discriminant = a1 * a1 - 4f * a2
        val reciprocalRoots = if (discriminant < 0f) {
            val imaginary = sqrt(-discriminant) / (2f * a2)
            listOf(real to imaginary, real to -imaginary)
        } else {
            val offset = sqrt(discriminant) / (2f * a2)
            listOf(real + offset to 0f, real - offset to 0f)
        }
        return reciprocalRoots.map { (r, i) ->
            val magnitude = r * r + i * i
            r / magnitude to -i / magnitude
        }
    }
}
