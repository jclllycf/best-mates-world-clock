package com.bestmates.worldclock.ui.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.data.CityCatalog
import com.bestmates.worldclock.ui.components.*
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.ui.i18n.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable internal fun CityPicker(onDismiss: () -> Unit, onPick: (City) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val language = LocalBestMatesLanguage.current
    val catalog = remember { CityCatalog(context) }
    var continent by remember { mutableStateOf<String?>(null) }; var country by remember { mutableStateOf<Pair<String,String>?>(null) }; var query by remember { mutableStateOf("") }
    var chooser by remember { mutableStateOf("") }
    var countries by remember { mutableStateOf(emptyList<Pair<String,String>>()) }; var results by remember { mutableStateOf(emptyList<City>()) }
    LaunchedEffect(continent) { countries = continent?.let { withContext(Dispatchers.IO) { catalog.countries(it) } }.orEmpty(); country = null }
    LaunchedEffect(query) { delay(180); results = withContext(Dispatchers.IO) { catalog.searchEnglish(query) } }
    BackHandler { if (chooser.isNotBlank()) chooser = "" else onDismiss() }
    CrystalDialog(onDismissRequest = onDismiss, confirmButton = {}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("close")) } }, title = { Text(uiText("choose_city")) }, text = { Column(Modifier.heightIn(max = 620.dp)) {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(uiText("search_city")) }, singleLine = true)
        if (query.length >= 2) { LazyColumn(Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { items(results, key = { it.geoId }) { city -> PickerResultRow("${city.name} · ${city.country} · ${city.zoneId}") { onPick(city) } } }; Text(uiText("city_search_note"), style = MaterialTheme.typography.bodySmall) }
        else { ChoiceButton(uiText("continent"), continent?.let { continentName(it, language) } ?: uiText("please_choose")) { chooser = "continent" }; ChoiceButton(uiText("country"), country?.second ?: uiText("choose_continent_first"), continent != null) { chooser = "country" }; ChoiceButton(uiText("city"), if (country == null) uiText("choose_country_first") else uiText("choose_or_search_city"), country != null) { chooser = "city" } }
    } })
    if (chooser == "continent") SimpleChoiceDialog(uiText("choose_continent"), uiText("search_continent"), catalog.continents().map { it to continentName(it, language) }, onDismiss = { chooser = "" }) { continent = it; chooser = "" }
    if (chooser == "country") SimpleChoiceDialog(uiText("choose_country"), uiText("search_country"), countries, onDismiss = { chooser = "" }) { code -> country = code to countries.first { it.first == code }.second; chooser = "" }
    if (chooser == "city" && country != null) CityListDialog(catalog, country!!.first, onDismiss = { chooser = "" }) { onPick(it); chooser = "" }
}

@Composable internal fun ChoiceButton(label: String, value: String, enabled: Boolean = true, action: () -> Unit) = OrbitButton(label="$label：$value",onClick=action,enabled=enabled,modifier=Modifier.fillMaxWidth(),style=OrbitButtonStyle.Secondary)
@Composable internal fun PickerResultRow(label: String, action: () -> Unit) = CrystalPickerRow(onClick=action){Text(label,Modifier.weight(1f),style=MaterialTheme.typography.bodyMedium)}
@Composable internal fun SimpleChoiceDialog(title: String, hint: String, values: List<Pair<String,String>>, onDismiss: () -> Unit, pick: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(values, query) { values.filter { it.second.contains(query, ignoreCase = true) || it.first.contains(query, ignoreCase = true) } }
    CrystalDialog(onDismissRequest = onDismiss, confirmButton = {}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("back")) } }, title = { Text(title) }, text = { Column {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(hint) }, singleLine = true)
        LazyColumn(Modifier.heightIn(max=420.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { items(filtered, key={it.first}) { item -> PickerResultRow(item.second){pick(item.first)} } }
    } })
}

@Composable internal fun CityListDialog(catalog: CityCatalog, countryCode: String, onDismiss: () -> Unit, pick: (City)->Unit) {
    var query by remember { mutableStateOf("") }
    var cities by remember { mutableStateOf(emptyList<City>()) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(countryCode, query) {
        loading = true; delay(160)
        cities = withContext(Dispatchers.IO) { catalog.cities(countryCode, query, 120) }
        loading = false
    }
    CrystalDialog(onDismissRequest=onDismiss, confirmButton={}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("back")) } }, title={Text(uiText("choose_city"))}, text={Column {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(uiText("search_city_english")) }, singleLine = true)
        if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(if (query.isBlank()) uiText("popular_city_note") else uiFormat("search_results", cities.size), style = MaterialTheme.typography.bodySmall)
        LazyColumn(Modifier.heightIn(max=420.dp), verticalArrangement = Arrangement.spacedBy(6.dp)){items(cities,key={it.geoId}){city->PickerResultRow("${city.name} · ${city.zoneId}"){pick(city)}}}
    }})
}

internal fun continentName(code: String, language: AppLanguage): String {
    val english = mapOf("AS" to "Asia", "EU" to "Europe", "AF" to "Africa", "NA" to "North America", "SA" to "South America", "OC" to "Oceania", "AN" to "Antarctica")
    val chinese = mapOf("AS" to "亚洲", "EU" to "欧洲", "AF" to "非洲", "NA" to "北美洲", "SA" to "南美洲", "OC" to "大洋洲", "AN" to "南极洲")
    return (if (language == AppLanguage.CHINESE) chinese else english)[code] ?: code
}
