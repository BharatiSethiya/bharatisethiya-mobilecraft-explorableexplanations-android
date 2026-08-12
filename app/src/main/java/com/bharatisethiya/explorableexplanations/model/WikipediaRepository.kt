package com.bharatisethiya.explorableexplanations.model

import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import org.json.JSONObject

data class WikipediaResult(val title: String, val summary: String)

object WikipediaRepository {
    fun lookup(query: String): WikipediaResult? {
        val encoded = URLEncoder.encode(query.trim(), Charsets.UTF_8.name())
        if (encoded.isBlank()) return null
        val endpoint = "https://en.wikipedia.org/w/api.php?action=query&generator=search" +
            "&gsrsearch=$encoded&gsrlimit=1&prop=extracts&exintro=1&explaintext=1&format=json&formatversion=2"
        val connection = URL(endpoint).openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 5_000
            connection.readTimeout = 5_000
            connection.setRequestProperty("User-Agent", "ExplorableExplanationsAndroid/1.0")
            if (connection.responseCode !in 200..299) return null
            val page = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
                .optJSONObject("query")
                ?.optJSONArray("pages")
                ?.optJSONObject(0)
                ?: return null
            WikipediaResult(page.optString("title"), page.optString("extract"))
                .takeIf { it.title.isNotBlank() && it.summary.isNotBlank() }
        } finally {
            connection.disconnect()
        }
    }
}
