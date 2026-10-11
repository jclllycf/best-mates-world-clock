package com.bestmates.worldclock.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.icons.*
import com.bestmates.worldclock.ui.theme.*

// Historical Golden Set 3 review studies. Production now uses ui.design (BmIcons, BmTheme);
// these remain only so the archived review gallery keeps rendering.

@Composable fun ProductionPinStudy(artworkSize: Dp) {
    val tint = BestMatesDesign.colors.textPrimary
    Canvas(Modifier.size(artworkSize)) {
        val stroke = size.width * .08f
        fun p(x: Float, y: Float) = Offset(size.width * x, size.height * y)
        drawRoundRect(tint, p(.33f, .12f), Size(size.width * .34f, size.height * .20f), CornerRadius(size.width * .08f), style = Stroke(stroke))
        drawLine(tint, p(.37f, .33f), p(.28f, .60f), stroke, StrokeCap.Round)
        drawLine(tint, p(.63f, .33f), p(.72f, .60f), stroke, StrokeCap.Round)
        drawLine(tint, p(.28f, .60f), p(.72f, .60f), stroke, StrokeCap.Round)
        drawLine(tint, p(.50f, .60f), p(.50f, .88f), stroke, StrokeCap.Round)
    }
}

@Composable fun ProductionRotateStudy(artworkSize: Dp) {
    Icon(OrbitIcons.Rotate, "Rotate", tint = BestMatesDesign.colors.textPrimary, modifier = Modifier.size(artworkSize))
}

@Composable fun BestMatesProductionToolbarReview() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Icon(OrbitIcons.Undo, "Undo", modifier = Modifier.size(20.dp))
        Icon(OrbitIcons.Redo, "Redo", modifier = Modifier.size(20.dp))
        ProductionPinStudy(20.dp)
        Icon(OrbitIcons.Font, "Font", modifier = Modifier.size(20.dp))
        Icon(OrbitIcons.Adjust, "Local adjustments", modifier = Modifier.size(20.dp))
        ProductionRotateStudy(20.dp)
    }
}

@Composable fun BestMatesCanvasReview() {
    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BestMatesProductionToolbarReview()
        Text("The production canvas moved to the Best Mates 0.4 editor; this archived study no longer embeds it.", style = MaterialTheme.typography.bodySmall)
    }
}
