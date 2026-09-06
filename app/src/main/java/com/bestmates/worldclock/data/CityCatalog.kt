package com.bestmates.worldclock.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bestmates.worldclock.model.City
import java.io.File

class CityCatalog(private val context: Context) {
    private val database: SQLiteDatabase by lazy {
        val target = File(context.noBackupFilesDir, "cities-v1.db")
        if (!target.exists()) context.assets.open("cities.db").use { input -> target.outputStream().use(input::copyTo) }
        SQLiteDatabase.openDatabase(target.path, null, SQLiteDatabase.OPEN_READONLY)
    }

    fun continents(): List<String> = listOf("AS", "EU", "AF", "NA", "SA", "OC", "AN")

    fun countries(continent: String): List<Pair<String, String>> = database.rawQuery(
        "SELECT code,name FROM countries WHERE continent=? ORDER BY name COLLATE NOCASE", arrayOf(continent)
    ).use { cursor -> buildList { while (cursor.moveToNext()) add(cursor.getString(0) to cursor.getString(1)) } }

    /**
     * Returns a small, useful page instead of loading every settlement in a
     * country into Compose. Some countries contain tens of thousands of rows.
     */
    fun cities(countryCode: String, query: String = "", limit: Int = 120): List<City> {
        val clean = query.trim()
        val sql: String
        val args: Array<String>
        if (clean.isBlank()) {
            sql = "SELECT c.id,c.name,c.ascii_name,co.name,c.latitude,c.longitude,c.timezone " +
                "FROM cities c JOIN countries co ON co.code=c.country_code " +
                "WHERE c.country_code=? ORDER BY c.population DESC,c.ascii_name COLLATE NOCASE LIMIT ?"
            args = arrayOf(countryCode, limit.toString())
        } else {
            sql = "SELECT c.id,c.name,c.ascii_name,co.name,c.latitude,c.longitude,c.timezone " +
                "FROM cities c JOIN countries co ON co.code=c.country_code " +
                "WHERE c.country_code=? AND (c.ascii_name LIKE ? COLLATE NOCASE OR c.name LIKE ? COLLATE NOCASE OR c.alternates LIKE ? COLLATE NOCASE) " +
                "ORDER BY CASE WHEN c.ascii_name=? COLLATE NOCASE OR c.name=? COLLATE NOCASE THEN 0 ELSE 1 END,c.population DESC LIMIT ?"
            args = arrayOf(countryCode, "$clean%", "$clean%", "%$clean%", clean, clean, limit.toString())
        }
        return database.rawQuery(sql, args).use(::readCities).distinctBy { it.geoId }
    }

    fun searchEnglish(query: String): List<City> {
        val clean = query.trim()
        if (clean.length < 2 || clean.any { it.code > 127 }) return emptyList()
        return database.rawQuery(
            "SELECT c.id,c.name,c.ascii_name,co.name,c.latitude,c.longitude,c.timezone " +
                "FROM cities c JOIN countries co ON co.code=c.country_code " +
                "WHERE c.ascii_name LIKE ? COLLATE NOCASE OR c.alternates LIKE ? COLLATE NOCASE " +
                "ORDER BY CASE WHEN c.ascii_name=? COLLATE NOCASE THEN 0 ELSE 1 END,c.population DESC LIMIT 30",
            arrayOf("$clean%", "%$clean%", clean)
        ).use(::readCities).distinctBy { Triple(it.name.lowercase(), it.country, it.zoneId) }.take(20)
    }

    private fun readCities(cursor: android.database.Cursor): List<City> = buildList {
        while (cursor.moveToNext()) add(City(cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), geoId = cursor.getLong(0)))
    }
}
