package com.bestmates.worldclock.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Orbit Charm foundation family. Golden Set 3 adopts only the explicitly approved
 * Undo/Redo, global Settings, local Adjust and refined Font artwork in production.
 */
object OrbitIcons {
    val Back: ImageVector by lazy {
        orbitIcon("OrbitBack") {
            moveTo(19f, 12f); lineTo(5f, 12f)
            moveTo(10f, 7f); lineTo(5f, 12f); lineTo(10f, 17f)
        }
    }

    /** Global preferences only. Local parameter controls use Adjust. */
    val Settings: ImageVector by lazy {
        orbitIcon("OrbitGlobalSettings") {
            // Equal tooth rhythm, softened shoulders and a centered counter.
            // Midpoint quadratic joins avoid the previous gear's sharp, uneven corners.
            val points = buildList {
                repeat(8) { tooth ->
                    listOf(-22.5 to 7.0, -11.0 to 9.3, 11.0 to 9.3).forEach { (offset,radius) ->
                        val angle=Math.toRadians(tooth*45.0-90.0+offset)
                        add((12f+(kotlin.math.cos(angle)*radius).toFloat()) to (12f+(kotlin.math.sin(angle)*radius).toFloat()))
                    }
                }
            }
            moveTo((points.last().first+points.first().first)/2f,(points.last().second+points.first().second)/2f)
            points.forEachIndexed { index,point ->
                val next=points[(index+1)%points.size]
                quadTo(point.first,point.second,(point.first+next.first)/2f,(point.second+next.second)/2f)
            }
            close()
            orbitDot(12f, 12f, 3.15f)
        }
    }

    /** Exact Golden Set 2 sliders, retained for local adjustments. */
    val Adjust: ImageVector by lazy {
        orbitIcon("OrbitAdjust") {
            moveTo(4.2f, 6.2f); lineTo(6.2f, 6.2f); orbitDot(8.1f, 6.2f, 1.75f)
            moveTo(9.85f, 6.2f); lineTo(19.8f, 6.2f)
            moveTo(4.2f, 12.1f); lineTo(13.45f, 12.1f); orbitDot(15.35f, 12.1f, 1.75f)
            moveTo(17.1f, 12.1f); lineTo(19.8f, 12.1f)
            moveTo(4.2f, 17.9f); lineTo(8.65f, 17.9f); orbitDot(10.55f, 17.9f, 1.75f)
            moveTo(12.3f, 17.9f); lineTo(19.8f, 17.9f)
        }
    }

    val Undo: ImageVector by lazy {
        orbitIcon("OrbitUndo") {
            moveTo(8.55f, 8.15f)
            curveTo(11.15f, 6.15f, 15.15f, 6.35f, 17.65f, 8.85f)
            curveTo(20.2f, 11.4f, 20f, 15.55f, 17.05f, 18.15f)
            curveTo(15.35f, 19.65f, 13f, 20.05f, 10.95f, 19.35f)
            moveTo(8.6f, 4.85f); lineTo(4.85f, 8.65f); lineTo(8.8f, 11.85f)
        }
    }

    val Redo: ImageVector by lazy {
        orbitIcon("OrbitRedo") {
            moveTo(15.45f, 8.15f)
            curveTo(12.85f, 6.15f, 8.85f, 6.35f, 6.35f, 8.85f)
            curveTo(3.8f, 11.4f, 4f, 15.55f, 6.95f, 18.15f)
            curveTo(8.65f, 19.65f, 11f, 20.05f, 13.05f, 19.35f)
            moveTo(15.4f, 4.85f); lineTo(19.15f, 8.65f); lineTo(15.2f, 11.85f)
        }
    }

    val CreateWidget: ImageVector by lazy {
        orbitIcon("OrbitCreateWidget") {
            roundedBox(4f, 5f, 20f, 19f, 2.5f)
            moveTo(12f, 9f); lineTo(12f, 15f)
            moveTo(9f, 12f); lineTo(15f, 12f)
        }
    }

    val AddComponent: ImageVector by lazy {
        orbitIcon("OrbitAddComponent") {
            roundedBox(4f, 4f, 14.5f, 14.5f, 2.2f)
            moveTo(17.5f, 13f); lineTo(17.5f, 20f)
            moveTo(14f, 16.5f); lineTo(21f, 16.5f)
        }
    }

    val AddCity: ImageVector by lazy {
        orbitIcon("OrbitAddCity") {
            moveTo(11f, 20f)
            curveTo(8.2f, 16.4f, 6.5f, 13.9f, 6.5f, 10.6f)
            curveTo(6.5f, 6.9f, 9.2f, 4.5f, 12.3f, 4.5f)
            curveTo(15.4f, 4.5f, 18f, 6.9f, 18f, 10.4f)
            curveTo(18f, 11.4f, 17.8f, 12.3f, 17.4f, 13.2f)
            orbitDot(12.3f, 10.3f, 1.9f)
            moveTo(18f, 15f); lineTo(18f, 21f)
            moveTo(15f, 18f); lineTo(21f, 18f)
        }
    }

    val AddText: ImageVector by lazy {
        orbitIcon("OrbitAddText") {
            moveTo(4f, 6f); lineTo(15f, 6f)
            moveTo(9.5f, 6f); lineTo(9.5f, 19f)
            moveTo(6.5f, 19f); lineTo(12.5f, 19f)
            moveTo(18f, 11f); lineTo(18f, 19f)
            moveTo(14f, 15f); lineTo(22f, 15f)
        }
    }

    val Rename: ImageVector by lazy {
        orbitIcon("OrbitRename") {
            moveTo(5f, 18.5f); lineTo(6.2f, 14.2f); lineTo(15.3f, 5.1f)
            curveTo(16.1f, 4.3f, 17.3f, 4.3f, 18.1f, 5.1f)
            lineTo(18.9f, 5.9f)
            curveTo(19.7f, 6.7f, 19.7f, 7.9f, 18.9f, 8.7f)
            lineTo(9.8f, 17.8f); close()
            moveTo(5f, 20f); lineTo(11.5f, 20f)
        }
    }

    val Copy: ImageVector by lazy {
        orbitIcon("OrbitCopy") {
            roundedBox(8f, 7f, 20f, 20f, 2.2f)
            moveTo(16f, 7f); lineTo(16f, 6f)
            curveTo(16f, 4.9f, 15.1f, 4f, 14f, 4f)
            lineTo(6f, 4f)
            curveTo(4.9f, 4f, 4f, 4.9f, 4f, 6f)
            lineTo(4f, 15f)
            curveTo(4f, 16.1f, 4.9f, 17f, 6f, 17f)
            lineTo(8f, 17f)
        }
    }

    val Paste: ImageVector by lazy {
        orbitIcon("OrbitPaste") {
            roundedBox(5f, 5.5f, 19f, 21f, 2.2f)
            roundedBox(8f, 3f, 16f, 7.5f, 1.5f)
            moveTo(8.5f, 12f); lineTo(15.5f, 12f)
            moveTo(8.5f, 16f); lineTo(14f, 16f)
        }
    }

    val Cut: ImageVector by lazy {
        orbitIcon("OrbitCut") {
            orbitDot(6.5f, 7f, 2.5f)
            orbitDot(6.5f, 17f, 2.5f)
            moveTo(8.5f, 8.5f); lineTo(20f, 18.5f)
            moveTo(8.5f, 15.5f); lineTo(20f, 5.5f)
        }
    }

    val Pin: ImageVector by lazy {
        orbitIcon("OrbitPin") {
            roundedBox(8.3f, 4.5f, 15.7f, 8.4f, 1.7f)
            moveTo(9.6f, 8.4f)
            curveTo(9.6f, 10.6f, 8.7f, 12.3f, 7.4f, 14f)
            curveTo(9.9f, 14.5f, 14.1f, 14.5f, 16.6f, 14f)
            curveTo(15.3f, 12.3f, 14.4f, 10.6f, 14.4f, 8.4f)
            moveTo(12f, 14.4f); lineTo(12f, 19.2f)
        }
    }

    val Lock: ImageVector by lazy {
        orbitIcon("OrbitLock") {
            roundedBox(5f, 10f, 19f, 20f, 2.5f)
            moveTo(8f, 10f); lineTo(8f, 7.5f)
            curveTo(8f, 4.8f, 9.8f, 3.5f, 12f, 3.5f)
            curveTo(14.2f, 3.5f, 16f, 4.8f, 16f, 7.5f)
            lineTo(16f, 10f)
            moveTo(12f, 14f); lineTo(12f, 16.5f)
        }
    }

    val Delete: ImageVector by lazy {
        orbitIcon("OrbitDelete") {
            moveTo(5f, 7f); lineTo(19f, 7f)
            moveTo(9f, 7f); lineTo(9.7f, 4.5f); lineTo(14.3f, 4.5f); lineTo(15f, 7f)
            moveTo(7f, 7f); lineTo(8f, 20f); lineTo(16f, 20f); lineTo(17f, 7f)
            moveTo(10.5f, 11f); lineTo(10.8f, 16f)
            moveTo(13.5f, 11f); lineTo(13.2f, 16f)
        }
    }

        val Rotate: ImageVector by lazy {
        orbitIcon("OrbitRotate") {
            moveTo(21f, 12f)
            curveTo(21f, 16.97f, 16.97f, 21f, 12f, 21f)
            curveTo(7.03f, 21f, 3f, 16.97f, 3f, 12f)
            curveTo(3f, 7.03f, 7.03f, 3f, 12f, 3f)
            curveTo(15f, 3f, 17.65f, 4.47f, 19.3f, 6.7f)
            moveTo(20.5f, 2.5f); lineTo(20.5f, 7.5f); lineTo(15.5f, 7.5f)
        }
    }

        val Resize: ImageVector by lazy {
        orbitIcon("OrbitResize") {
            moveTo(14f, 4f); lineTo(20f, 4f); lineTo(20f, 10f)
            moveTo(10f, 20f); lineTo(4f, 20f); lineTo(4f, 14f)
            moveTo(4f, 20f); lineTo(20f, 4f)
        }
    }

    val Snap: ImageVector by lazy {
        orbitIcon("OrbitSnap") {
            moveTo(5f, 5f); lineTo(5f, 13f)
            curveTo(5f, 17f, 7.8f, 20f, 12f, 20f)
            curveTo(16.2f, 20f, 19f, 17f, 19f, 13f)
            lineTo(19f, 5f)
            moveTo(5f, 9f); lineTo(9f, 9f)
            moveTo(15f, 9f); lineTo(19f, 9f)
        }
    }

    val Guides: ImageVector by lazy {
        orbitIcon("OrbitGuides") {
            moveTo(12f, 3.5f); lineTo(12f, 20.5f)
            moveTo(3.5f, 12f); lineTo(20.5f, 12f)
            roundedBox(7f, 7f, 17f, 17f, 1.5f)
        }
    }

    val Theme: ImageVector by lazy {
        orbitIcon("OrbitTheme") {
            orbitDot(10f, 12f, 6f)
            moveTo(10f, 6f)
            curveTo(13.4f, 7f, 15.5f, 10.2f, 15f, 13.6f)
            curveTo(14.6f, 16.5f, 12.3f, 18.1f, 9.6f, 18f)
            moveTo(17.5f, 4f); lineTo(17.5f, 7f)
            moveTo(16f, 5.5f); lineTo(19f, 5.5f)
        }
    }

    val Font: ImageVector by lazy {
        orbitIcon("OrbitFont") {
            moveTo(2.9f, 19f); lineTo(7.1f, 5.2f)
            curveTo(7.2f, 4.9f, 7.5f, 4.9f, 7.6f, 5.2f); lineTo(11.8f, 19f)
            moveTo(4.6f, 13.8f); lineTo(10.1f, 13.8f)
            moveTo(20.7f, 11.8f); lineTo(20.7f, 19f)
            moveTo(20.7f, 14.2f)
            curveTo(20.1f, 11.8f, 16.8f, 11.6f, 15.6f, 13.4f)
            curveTo(14.1f, 15.6f, 15.2f, 19f, 17.4f, 19f)
            curveTo(19.1f, 19f, 20.7f, 17.7f, 20.7f, 16f)
        }
    }

    val Color: ImageVector by lazy {
        orbitIcon("OrbitColor") {
            moveTo(12f, 3.5f)
            curveTo(9f, 7.3f, 6f, 10.7f, 6f, 14f)
            curveTo(6f, 17.7f, 8.7f, 20.5f, 12f, 20.5f)
            curveTo(15.3f, 20.5f, 18f, 17.7f, 18f, 14f)
            curveTo(18f, 10.7f, 15f, 7.3f, 12f, 3.5f)
            moveTo(9.3f, 16.2f); curveTo(10.7f, 17.3f, 12.8f, 17.5f, 14.5f, 16f)
        }
    }

    val Sun: ImageVector by lazy {
        orbitIcon("OrbitSun") {
            orbitDot(12f, 12f, 3.6f)
            moveTo(12f, 3f); lineTo(12f, 5f)
            moveTo(12f, 19f); lineTo(12f, 21f)
            moveTo(3f, 12f); lineTo(5f, 12f)
            moveTo(19f, 12f); lineTo(21f, 12f)
            moveTo(5.6f, 5.6f); lineTo(7f, 7f)
            moveTo(17f, 17f); lineTo(18.4f, 18.4f)
            moveTo(18.4f, 5.6f); lineTo(17f, 7f)
            moveTo(7f, 17f); lineTo(5.6f, 18.4f)
        }
    }

    val Moon: ImageVector by lazy {
        orbitIcon("OrbitMoon") {
            moveTo(17.8f, 16.8f)
            curveTo(14.1f, 17.2f, 10.8f, 14.4f, 10.8f, 10.7f)
            curveTo(10.8f, 7.9f, 12.4f, 5.5f, 14.8f, 4.3f)
            curveTo(9.6f, 3.4f, 5f, 7.2f, 5f, 12.4f)
            curveTo(5f, 17.2f, 8.8f, 21f, 13.6f, 21f)
            curveTo(16.8f, 21f, 19.6f, 19.2f, 21f, 16.6f)
            curveTo(20f, 16.8f, 18.9f, 16.9f, 17.8f, 16.8f)
            moveTo(19f, 3.8f)
            curveTo(19.3f, 5.1f, 19.8f, 5.7f, 21.2f, 6f)
            curveTo(19.8f, 6.3f, 19.3f, 6.9f, 19f, 8.2f)
            curveTo(18.7f, 6.9f, 18.2f, 6.3f, 16.8f, 6f)
            curveTo(18.2f, 5.7f, 18.7f, 5.1f, 19f, 3.8f); close()
        }
    }

    val Search: ImageVector by lazy {
        orbitIcon("OrbitSearch") {
            orbitDot(10.5f, 10.5f, 6f)
            moveTo(15f, 15f); lineTo(20f, 20f)
        }
    }

    val Close: ImageVector by lazy {
        orbitIcon("OrbitClose") {
            moveTo(6f, 6f); lineTo(18f, 18f)
            moveTo(18f, 6f); lineTo(6f, 18f)
        }
    }

    val Confirm: ImageVector by lazy {
        orbitIcon("OrbitConfirm") {
            moveTo(4.7f, 12.7f)
            curveTo(6.2f, 14.2f, 7.8f, 15.8f, 9.4f, 17.1f)
            curveTo(12.4f, 14.3f, 16.1f, 10.1f, 19.4f, 6.8f)
        }
    }
}

private fun orbitIcon(
    name: String,
    pathBuilder: PathBuilder.() -> Unit,
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(
        fill = null,
        stroke = SolidColor(Color.Black),
        strokeLineWidth = DefaultOrbitIconMetrics.defaultStrokeViewportUnits,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        pathBuilder = pathBuilder,
    )
}.build()

private fun PathBuilder.orbitDot(cx: Float, cy: Float, radius: Float) {
    val control = radius * 0.5522848f
    moveTo(cx + radius, cy)
    curveTo(cx + radius, cy + control, cx + control, cy + radius, cx, cy + radius)
    curveTo(cx - control, cy + radius, cx - radius, cy + control, cx - radius, cy)
    curveTo(cx - radius, cy - control, cx - control, cy - radius, cx, cy - radius)
    curveTo(cx + control, cy - radius, cx + radius, cy - control, cx + radius, cy)
    close()
}

private fun PathBuilder.roundedBox(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    radius: Float,
) {
    moveTo(left + radius, top)
    lineTo(right - radius, top)
    curveTo(right, top, right, top, right, top + radius)
    lineTo(right, bottom - radius)
    curveTo(right, bottom, right, bottom, right - radius, bottom)
    lineTo(left + radius, bottom)
    curveTo(left, bottom, left, bottom, left, bottom - radius)
    lineTo(left, top + radius)
    curveTo(left, top, left, top, left + radius, top)
    close()
}
