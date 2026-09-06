package com.bestmates.worldclock.data

import com.bestmates.worldclock.model.City
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL

object CitySearchRepository {
    fun search(query: String, language: String): List<City> {
        if (query.trim().length < 2) return emptyList()
        val encoded = URLEncoder.encode(query.trim(), "UTF-8")
        val url = URL("https://geocoding-api.open-meteo.com/v1/search?name=$encoded&count=12&language=$language")
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 7000; connection.readTimeout = 7000
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val results = JSONObject(body).optJSONArray("results") ?: return emptyList()
            buildList {
                for (i in 0 until results.length()) results.getJSONObject(i).let {
                    val zone = it.optString("timezone")
                    if (zone.isNotBlank()) add(City(it.getString("name"), it.optString("country"), it.getDouble("latitude"), it.getDouble("longitude"), zone))
                }
            }
        } finally { connection.disconnect() }
    }
}
