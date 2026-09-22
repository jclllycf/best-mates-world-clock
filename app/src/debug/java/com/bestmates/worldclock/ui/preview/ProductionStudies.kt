package com.bestmates.worldclock.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.model.*
import com.bestmates.worldclock.ui.editor.*
import com.bestmates.worldclock.ui.icons.*
import com.bestmates.worldclock.ui.theme.*
import com.bestmates.worldclock.ui.i18n.uiText

// Moved from MainActivity.kt
@Composable fun ProductionPinStudy(artworkSize: Dp) {
    val tint=BestMatesDesign.colors.textPrimary
    Canvas(Modifier.size(artworkSize)) { drawProductionPin(tint) }
}

/** The exact Orbit Rotate the user identified in Future canvas language. */
@Composable fun ProductionRotateStudy(artworkSize: Dp) {
    Icon(OrbitIcons.Rotate,"Rotate",tint=BestMatesDesign.colors.textPrimary,modifier=Modifier.size(artworkSize))
}

/** Debug review bridge to actual production buttons; never saves a project. */
@Composable fun BestMatesProductionToolbarReview() {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically) {
        EditorActionButton(EditorAction.UNDO,"Undo",{})
        EditorActionButton(EditorAction.REDO,"Redo",{})
        EditorActionButton(EditorAction.PIN,"Pin position",{})
        Icon(OrbitIcons.Font,"Font",modifier=Modifier.size(20.dp))
        Icon(OrbitIcons.Adjust,"Local adjustments",modifier=Modifier.size(20.dp))
        ProductionRotateStudy(20.dp)
    }
}

/** Disposable local state around the unchanged geometry/gesture engine. */
@Composable fun BestMatesCanvasReview() {
    var study by remember { mutableStateOf(WidgetConfig(
        layers=listOf(WidgetLayer(id=1L,shape=WidgetShape.HEART,width=.82f,height=.82f,dayColor=0xDDEEDCEC,nightColor=0xDDEEDCEC,textColor=0xFF796584,softness=4f,glowOpacity=.08f)),
        cities=listOf(City("Tokyo","Japan",35.68,139.69,"Asia/Tokyo",componentId=1L,scale=.85f,nameColor=0xFF4A3A53,timeColor=0xFF4A3A53,dateColor=0xFF63536C))
    )) }
    var selection by remember { mutableStateOf<CanvasSelection?>(CanvasSelection.ComponentItem(0)) }
    Column(Modifier.fillMaxWidth().height(570.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        BestMatesProductionToolbarReview()
        LayoutCanvas(study,{index,value -> study=study.copy(cities=study.cities.toMutableList().also { it[index]=value })},
            {index,value -> study=study.copy(texts=study.texts.toMutableList().also { it[index]=value })},
            {index,value -> study=study.copy(layers=study.layers.toMutableList().also { it[index]=value })},
            {},{},selection,{selection=it},true, true, emptyCanvasText = uiText("empty_canvas"))
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(12.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("Soft inspector · local study",style=MaterialTheme.typography.titleMedium)
            FontField("Time typeface",study.cities.first().timeFont,"18:40") { next -> study=study.copy(cities=study.cities.map { it.copy(timeFont=next) }) }
            EditableDisplayRow("Date",study.cities.first().showDate,{next -> study=study.copy(cities=study.cities.map { it.copy(showDate=next) })},{})
            Text("Current production Heart · neutral work mat · no saved project",style=MaterialTheme.typography.bodySmall)
        }
    }
}


