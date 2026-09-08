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
 * Orbit Charm foundation family. The vectors are production-ready, but Phase 2C
 * intentionally leaves every existing production icon call site unchanged.
 */
object OrbitIcons {
    val Back: ImageVector by lazy {
        orbitIcon("OrbitBack") {
            moveTo(19f, 12f); lineTo(5f, 12f)
            moveTo(10f, 7f); lineTo(5f, 12f); lineTo(10f, 17f)
        }
    }

    val Settings: ImageVector by lazy {
        orbitIcon("OrbitSettings") {
            moveTo(4f, 7f); lineTo(20f, 7f)
            orbitDot(9f, 7f, 1.7f)
            moveTo(4f, 12f); lineTo(20f, 12f)
            orbitDot(15f, 12f, 1.7f)
            moveTo(4f, 17f); lineTo(20f, 17f)
            orbitDot(11f, 17f, 1.7f)
        }
    }

    val Undo: ImageVector by lazy {
        orbitIcon("OrbitUndo") {
            moveTo(9f, 7f); lineTo(5f, 10.5f); lineTo(9f, 14f)
            moveTo(5.5f, 10.5f)
            curveTo(8f, 7.8f, 12f, 6.8f, 15.5f, 8.4f)
            curveTo(18.4f, 9.8f, 19.5f, 13.3f, 18.2f, 16.3f)
            curveTo(17.5f, 18f, 15.9f, 19.1f, 14f, 19.4f)
            orbitDot(18.7f, 6f, 0.8f)
        }
    }

    val Redo: ImageVector by lazy {
        orbitIcon("OrbitRedo") {
            moveTo(15f, 7f); lineTo(19f, 10.5f); lineTo(15f, 14f)
            moveTo(18.5f, 10.5f)
            curveTo(16f, 7.8f, 12f, 6.8f, 8.5f, 8.4f)
            curveTo(5.6f, 9.8f, 4.5f, 13.3f, 5.8f, 16.3f)
            curveTo(6.5f, 18f, 8.1f, 19.1f, 10f, 19.4f)
            orbitDot(5.3f, 6f, 0.8f)
        }
    }

    val CreateWidget: ImageVector by lazy {
        orbitIcon("OrbitCreateWidget") {
            roundedBox(4f, 5f, 20f, 19f, 2.5f)
            moveTo(12f, 9f); lineTo(12f, 15f)
            moveTo(9f, 12f); lineTo(15f, 12f)
            orbitDot(18.7f, 4.2f, 0.8f)
        }
    }

    val AddComponent: ImageVector by lazy {
        orbitIcon("OrbitAddComponent") {
            roundedBox(4f, 4f, 14.5f, 14.5f, 2.2f)
            moveTo(17.5f, 13f); lineTo(17.5f, 20f)
            moveTo(14f, 16.5f); lineTo(21f, 16.5f)
            orbitDot(6.1f, 17.8f, 0.75f)
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
            orbitDot(19.7f, 6f, 0.75f)
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
            orbitDot(19.5f, 17.8f, 0.8f)
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
            orbitDot(18.8f, 5f, 0.75f)
        }
    }

    val Paste: ImageVector by lazy {
        orbitIcon("OrbitPaste") {
            roundedBox(5f, 5.5f, 19f, 21f, 2.2f)
            roundedBox(8f, 3f, 16f, 7.5f, 1.5f)
            moveTo(8.5f, 12f); lineTo(15.5f, 12f)
            moveTo(8.5f, 16f); lineTo(14f, 16f)
            orbitDot(18.8f, 10f, 0.75f)
        }
    }

    val Cut: ImageVector by lazy {
        orbitIcon("OrbitCut") {
            orbitDot(6.5f, 7f, 2.5f)
            orbitDot(6.5f, 17f, 2.5f)
            moveTo(8.5f, 8.5f); lineTo(20f, 18.5f)
            moveTo(8.5f, 15.5f); lineTo(20f, 5.5f)
            orbitDot(20f, 12f, 0.7f)
        }
    }

    val Pin: ImageVector by lazy {
        orbitIcon("OrbitPin") {
            moveTo(9f, 4f); lineTo(16.5f, 4f)
            lineTo(15f, 9f); lineTo(18f, 12f); lineTo(6f, 12f)
            lineTo(9f, 9f); close()
            moveTo(12f, 12f); lineTo(12f, 21f)
            orbitDot(18.8f, 5.2f, 0.75f)
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
            orbitDot(18.8f, 7f, 0.7f)
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
            moveTo(18.6f, 8.2f)
            curveTo(16.8f, 4.9f, 12.7f, 3.6f, 9.2f, 5f)
            curveTo(5.5f, 6.4f, 3.6f, 10.5f, 4.8f, 14.3f)
            curveTo(6f, 18.2f, 10.2f, 20.5f, 14.2f, 19.4f)
            curveTo(16.5f, 18.8f, 18.4f, 17.1f, 19.3f, 15f)
            moveTo(14.8f, 7.8f); lineTo(19f, 8.5f); lineTo(18.5f, 4.3f)
            orbitDot(19.3f, 15f, 1f)
        }
    }

    val Resize: ImageVector by lazy {
        orbitIcon("OrbitResize") {
            moveTo(6f, 10f); lineTo(6f, 6f); lineTo(10f, 6f)
            moveTo(14f, 18f); lineTo(18f, 18f); lineTo(18f, 14f)
            moveTo(7f, 7f); lineTo(17f, 17f)
            moveTo(13.5f, 5f); lineTo(19f, 5f); lineTo(19f, 10.5f)
            orbitDot(5f, 18.8f, 0.8f)
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
            orbitDot(12f, 7f, 1f)
        }
    }

    val Guides: ImageVector by lazy {
        orbitIcon("OrbitGuides") {
            moveTo(12f, 3.5f); lineTo(12f, 20.5f)
            moveTo(3.5f, 12f); lineTo(20.5f, 12f)
            roundedBox(7f, 7f, 17f, 17f, 1.5f)
            orbitDot(12f, 12f, 1f)
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
            moveTo(4.5f, 19f); lineTo(10f, 5f); lineTo(15.5f, 19f)
            moveTo(6.5f, 14f); lineTo(13.5f, 14f)
            moveTo(15f, 11f)
            curveTo(16.5f, 9.8f, 19.5f, 10.2f, 19.5f, 12.5f)
            lineTo(19.5f, 19f)
            moveTo(19.5f, 15f)
            curveTo(17f, 14f, 14.8f, 15.1f, 15f, 17f)
            curveTo(15.2f, 19f, 18f, 19.4f, 19.5f, 17.5f)
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
            orbitDot(18.8f, 7f, 0.75f)
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
            orbitDot(19f, 6f, 0.75f)
        }
    }

    val Search: ImageVector by lazy {
        orbitIcon("OrbitSearch") {
            orbitDot(10.5f, 10.5f, 6f)
            moveTo(15f, 15f); lineTo(20f, 20f)
            orbitDot(19.5f, 5.2f, 0.75f)
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
            moveTo(4.5f, 12.5f); lineTo(9.5f, 17.5f); lineTo(19.5f, 6.5f)
            orbitDot(20f, 17.8f, 0.8f)
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
