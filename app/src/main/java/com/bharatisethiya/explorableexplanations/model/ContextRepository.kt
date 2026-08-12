package com.bharatisethiya.explorableexplanations.model

data class ContextFact(val title: String, val summary: String, val source: String)

object ContextRepository {
    private val facts = listOf(
        ContextFact(
            "Wind power in California",
            "California was an early wind-energy leader, but later national rankings changed. The original passage demonstrates why dates and comparative claims should be checked.",
            "Bundled context derived from the original Explorable Explanations example",
        ),
        ContextFact(
            "Altamont Pass",
            "A major wind-resource area east of San Francisco and one of the three California regions named in the passage.",
            "Bundled context",
        ),
        ContextFact(
            "Tehachapi",
            "A Southern California wind-resource region near the Tehachapi Mountains.",
            "Bundled context",
        ),
        ContextFact(
            "San Gorgonio Pass",
            "A wind-resource region near Palm Springs in Southern California.",
            "Bundled context",
        ),
    )

    fun search(query: String): List<ContextFact> {
        val normalized = query.trim()
        if (normalized.isEmpty()) return facts
        return facts.filter { fact ->
            fact.title.contains(normalized, ignoreCase = true) ||
                fact.summary.contains(normalized, ignoreCase = true)
        }
    }
}
