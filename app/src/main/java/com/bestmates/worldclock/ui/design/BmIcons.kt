package com.bestmates.worldclock.ui.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.icons.OrbitIcons

/**
 * Best Mates action icons. One 24-unit grid, 1.7 stroke, round caps and joins,
 * open counters, no mixed fill/outline within a glyph except sun/moon state marks.
 * Undo, Redo and Settings keep the user-approved Orbit geometry.
 */
object BmIcons {
    val Undo: ImageVector get() = OrbitIcons.Undo
    val Redo: ImageVector get() = OrbitIcons.Redo
    val Settings: ImageVector get() = OrbitIcons.Settings

    val Back by lazy { icon("Back", mirror = true) { moveTo(14.5f, 5.5f); lineTo(8f, 12f); lineTo(14.5f, 18.5f) } }
    val ChevronRight by lazy { icon("ChevronRight", mirror = true) { moveTo(9.5f, 5.5f); lineTo(16f, 12f); lineTo(9.5f, 18.5f) } }
    val ChevronDown by lazy { icon("ChevronDown") { moveTo(6f, 9.5f); lineTo(12f, 15.5f); lineTo(18f, 9.5f) } }
    val Close by lazy { icon("Close") { moveTo(6.5f, 6.5f); lineTo(17.5f, 17.5f); moveTo(17.5f, 6.5f); lineTo(6.5f, 17.5f) } }
    val Plus by lazy { icon("Plus") { moveTo(12f, 5f); lineTo(12f, 19f); moveTo(5f, 12f); lineTo(19f, 12f) } }
    val Check by lazy { icon("Check") { moveTo(5f, 12.6f); lineTo(9.6f, 17f); lineTo(19f, 7.2f) } }
    val More by lazy { icon("More", stroke = 2.4f) { dot(5.5f, 12f, .2f); dot(12f, 12f, .2f); dot(18.5f, 12f, .2f) } }

    /** A rounded square overlapped by a circle: "add a background shape". */
    val Shape by lazy {
        icon("Shape") {
            roundRect(3.5f, 8.5f, 15.5f, 20.5f, 3.2f)
            // Circle centred at (15.2, 8.8) r 5.3, drawn only outside the square: from its
            // crossing of the square's top edge, clockwise, to its crossing of the right edge.
            moveTo(9.91f, 8.5f)
            arcTo(5.3f, 5.3f, 0f, isMoreThanHalf = true, isPositiveArc = true, x1 = 15.5f, y1 = 14.09f)
        }
    }

    /** A location drop with a small clock hand: "add a city". */
    val City by lazy {
        icon("City") {
            moveTo(12f, 21f)
            curveTo(8.2f, 17.2f, 5.5f, 13.9f, 5.5f, 10.2f)
            curveTo(5.5f, 6.4f, 8.4f, 3.5f, 12f, 3.5f)
            curveTo(15.6f, 3.5f, 18.5f, 6.4f, 18.5f, 10.2f)
            curveTo(18.5f, 13.9f, 15.8f, 17.2f, 12f, 21f)
            close()
            moveTo(12f, 7.4f); lineTo(12f, 10.4f); lineTo(14.2f, 11.6f)
        }
    }

    /** A bracketed serif T: "add text". */
    val Text by lazy {
        icon("Text") {
            moveTo(5.5f, 7.5f); lineTo(5.5f, 5f); lineTo(18.5f, 5f); lineTo(18.5f, 7.5f)
            moveTo(12f, 5f); lineTo(12f, 19f)
            moveTo(9f, 19f); lineTo(15f, 19f)
        }
    }

    val Layers by lazy {
        icon("Layers") {
            moveTo(12f, 3.8f); lineTo(20.5f, 8.4f); lineTo(12f, 13f); lineTo(3.5f, 8.4f); close()
            moveTo(3.5f, 12.2f); lineTo(12f, 16.8f); lineTo(20.5f, 12.2f)
            moveTo(3.5f, 15.9f); lineTo(12f, 20.5f); lineTo(20.5f, 15.9f)
        }
    }

    /** Widget canvas size: a frame with corner ticks. */
    val Frame by lazy {
        icon("Frame") {
            moveTo(4f, 8.5f); lineTo(4f, 4f); lineTo(8.5f, 4f)
            moveTo(15.5f, 4f); lineTo(20f, 4f); lineTo(20f, 8.5f)
            moveTo(20f, 15.5f); lineTo(20f, 20f); lineTo(15.5f, 20f)
            moveTo(8.5f, 20f); lineTo(4f, 20f); lineTo(4f, 15.5f)
            roundRect(8f, 8f, 16f, 16f, 1.6f)
        }
    }

    val Sun by lazy {
        icon("Sun") {
            circle(12f, 12f, 3.9f)
            for (i in 0 until 8) {
                val a = Math.toRadians(i * 45.0)
                val c = kotlin.math.cos(a).toFloat(); val s = kotlin.math.sin(a).toFloat()
                moveTo(12f + c * 6.6f, 12f + s * 6.6f); lineTo(12f + c * 8.6f, 12f + s * 8.6f)
            }
        }
    }

    val Moon by lazy {
        icon("Moon") {
            moveTo(19.5f, 14.6f)
            curveTo(18.4f, 15.1f, 17.2f, 15.4f, 15.9f, 15.4f)
            curveTo(11.4f, 15.4f, 7.8f, 11.8f, 7.8f, 7.3f)
            curveTo(7.8f, 6f, 8.1f, 4.8f, 8.6f, 3.7f)
            curveTo(5.6f, 5.1f, 3.5f, 8.1f, 3.5f, 11.6f)
            curveTo(3.5f, 16.5f, 7.5f, 20.5f, 12.4f, 20.5f)
            curveTo(15.9f, 20.5f, 18.9f, 18.4f, 20.3f, 15.4f)
            close()
        }
    }

    val Lock by lazy {
        icon("Lock") {
            roundRect(5f, 10.5f, 19f, 20.5f, 2.6f)
            moveTo(8f, 10.5f); lineTo(8f, 7.8f)
            curveTo(8f, 5.3f, 9.8f, 3.5f, 12f, 3.5f)
            curveTo(14.2f, 3.5f, 16f, 5.3f, 16f, 7.8f); lineTo(16f, 10.5f)
        }
    }

    val Unlock by lazy {
        icon("Unlock") {
            roundRect(5f, 10.5f, 19f, 20.5f, 2.6f)
            moveTo(8f, 10.5f); lineTo(8f, 7.8f)
            curveTo(8f, 5.3f, 9.8f, 3.5f, 12f, 3.5f)
            curveTo(13.7f, 3.5f, 15.1f, 4.5f, 15.7f, 6f)
        }
    }

    val Duplicate by lazy {
        icon("Duplicate") {
            roundRect(8.5f, 8.5f, 20f, 20f, 2.6f)
            moveTo(15.5f, 8.5f); lineTo(15.5f, 6.4f)
            curveTo(15.5f, 5f, 14.4f, 4f, 13.1f, 4f); lineTo(6.4f, 4f)
            curveTo(5f, 4f, 4f, 5f, 4f, 6.4f); lineTo(4f, 13.1f)
            curveTo(4f, 14.4f, 5f, 15.5f, 6.4f, 15.5f); lineTo(8.5f, 15.5f)
            moveTo(14.25f, 11.8f); lineTo(14.25f, 16.7f); moveTo(11.8f, 14.25f); lineTo(16.7f, 14.25f)
        }
    }

    val Trash by lazy {
        icon("Trash") {
            moveTo(4.5f, 7f); lineTo(19.5f, 7f)
            moveTo(9.5f, 7f); lineTo(9.5f, 5.2f); curveTo(9.5f, 4.5f, 10f, 4f, 10.7f, 4f); lineTo(13.3f, 4f)
            curveTo(14f, 4f, 14.5f, 4.5f, 14.5f, 5.2f); lineTo(14.5f, 7f)
            moveTo(6.5f, 7f); lineTo(7.4f, 18.6f); curveTo(7.5f, 19.7f, 8.4f, 20.5f, 9.5f, 20.5f)
            lineTo(14.5f, 20.5f); curveTo(15.6f, 20.5f, 16.5f, 19.7f, 16.6f, 18.6f); lineTo(17.5f, 7f)
            moveTo(10.3f, 11f); lineTo(10.5f, 16.5f); moveTo(13.7f, 11f); lineTo(13.5f, 16.5f)
        }
    }

    val BringForward by lazy {
        icon("BringForward") {
            roundRect(9f, 9f, 20f, 20f, 2.4f)
            moveTo(15f, 6f); lineTo(15f, 5.6f); curveTo(15f, 4.7f, 14.3f, 4f, 13.4f, 4f); lineTo(5.6f, 4f)
            curveTo(4.7f, 4f, 4f, 4.7f, 4f, 5.6f); lineTo(4f, 13.4f); curveTo(4f, 14.3f, 4.7f, 15f, 5.6f, 15f); lineTo(6f, 15f)
            moveTo(14.5f, 17f); lineTo(14.5f, 12f); moveTo(12.2f, 14.2f); lineTo(14.5f, 12f); lineTo(16.8f, 14.2f)
        }
    }

    val SendBackward by lazy {
        icon("SendBackward") {
            roundRect(4f, 4f, 15f, 15f, 2.4f)
            moveTo(18f, 9f); lineTo(18.4f, 9f); curveTo(19.3f, 9f, 20f, 9.7f, 20f, 10.6f); lineTo(20f, 18.4f)
            curveTo(20f, 19.3f, 19.3f, 20f, 18.4f, 20f); lineTo(10.6f, 20f); curveTo(9.7f, 20f, 9f, 19.3f, 9f, 18.4f); lineTo(9f, 18f)
            moveTo(9.5f, 7f); lineTo(9.5f, 12f); moveTo(7.2f, 9.8f); lineTo(9.5f, 12f); lineTo(11.8f, 9.8f)
        }
    }

    val Rotate by lazy {
        icon("Rotate") {
            moveTo(19.5f, 12f)
            curveTo(19.5f, 16.1f, 16.1f, 19.5f, 12f, 19.5f)
            curveTo(7.9f, 19.5f, 4.5f, 16.1f, 4.5f, 12f)
            curveTo(4.5f, 7.9f, 7.9f, 4.5f, 12f, 4.5f)
            curveTo(14.6f, 4.5f, 16.8f, 5.8f, 18.2f, 7.8f)
            moveTo(18.6f, 3.6f); lineTo(18.6f, 8f); lineTo(14.2f, 8f)
        }
    }

    /** Horseshoe magnet: snapping. */
    val Magnet by lazy {
        icon("Magnet") {
            moveTo(5.5f, 4.5f); lineTo(5.5f, 11.5f)
            curveTo(5.5f, 15.4f, 8.4f, 18.5f, 12f, 18.5f)
            curveTo(15.6f, 18.5f, 18.5f, 15.4f, 18.5f, 11.5f); lineTo(18.5f, 4.5f)
            moveTo(5.5f, 8.5f); lineTo(9.5f, 8.5f); moveTo(14.5f, 8.5f); lineTo(18.5f, 8.5f)
            moveTo(9.5f, 4.5f); lineTo(9.5f, 11.5f); curveTo(9.5f, 13.1f, 10.6f, 14.5f, 12f, 14.5f)
            curveTo(13.4f, 14.5f, 14.5f, 13.1f, 14.5f, 11.5f); lineTo(14.5f, 4.5f)
        }
    }

    val Grid by lazy {
        icon("Grid") {
            roundRect(4f, 4f, 20f, 20f, 3f)
            moveTo(9.33f, 4f); lineTo(9.33f, 20f); moveTo(14.67f, 4f); lineTo(14.67f, 20f)
            moveTo(4f, 9.33f); lineTo(20f, 9.33f); moveTo(4f, 14.67f); lineTo(20f, 14.67f)
        }
    }

    /** Home screen with an arrow arriving: "add to home screen". */
    val AddToHome by lazy {
        icon("AddToHome") {
            roundRect(5f, 3.5f, 19f, 20.5f, 3f)
            moveTo(12f, 7.5f); lineTo(12f, 14f); moveTo(9.2f, 11.4f); lineTo(12f, 14.2f); lineTo(14.8f, 11.4f)
            moveTo(9.5f, 17.2f); lineTo(14.5f, 17.2f)
        }
    }

    val Search by lazy { icon("Search") { circle(10.8f, 10.8f, 6.3f); moveTo(15.4f, 15.4f); lineTo(20f, 20f) } }

    val Palette by lazy {
        icon("Palette") {
            moveTo(12f, 20.5f)
            curveTo(7.3f, 20.5f, 3.5f, 16.7f, 3.5f, 12f)
            curveTo(3.5f, 7.3f, 7.3f, 3.5f, 12f, 3.5f)
            curveTo(16.7f, 3.5f, 20.5f, 6.9f, 20.5f, 11.1f)
            curveTo(20.5f, 13.6f, 18.5f, 15.2f, 16.2f, 15.2f); lineTo(14.6f, 15.2f)
            curveTo(13.4f, 15.2f, 12.6f, 16.3f, 13.1f, 17.4f)
            curveTo(13.6f, 18.6f, 13.4f, 20.5f, 12f, 20.5f); close()
            dot(8f, 11.5f, .9f); dot(10.6f, 7.6f, .9f); dot(15.2f, 8.2f, .9f)
        }
    }

    val Type by lazy {
        icon("Type") {
            moveTo(3.5f, 18.5f); lineTo(8.2f, 5.5f); lineTo(12.9f, 18.5f)
            moveTo(5.3f, 13.8f); lineTo(11.1f, 13.8f)
            moveTo(20.5f, 11f); lineTo(20.5f, 18.5f)
            moveTo(20.5f, 14.8f)
            curveTo(20.5f, 12.7f, 19.2f, 11f, 17.4f, 11f)
            curveTo(15.6f, 11f, 14.4f, 12.7f, 14.4f, 14.8f)
            curveTo(14.4f, 16.9f, 15.6f, 18.5f, 17.4f, 18.5f)
            curveTo(19.2f, 18.5f, 20.5f, 16.9f, 20.5f, 14.8f)
        }
    }

    val Edit by lazy {
        icon("Edit") {
            moveTo(4.5f, 19.5f); lineTo(5.4f, 15.6f); lineTo(15.6f, 5.4f)
            curveTo(16.4f, 4.6f, 17.6f, 4.6f, 18.4f, 5.4f); lineTo(18.6f, 5.6f)
            curveTo(19.4f, 6.4f, 19.4f, 7.6f, 18.6f, 8.4f); lineTo(8.4f, 18.6f); close()
            moveTo(13.6f, 7.4f); lineTo(16.6f, 10.4f)
        }
    }

    val Globe by lazy {
        icon("Globe") {
            circle(12f, 12f, 8.5f)
            moveTo(3.5f, 12f); lineTo(20.5f, 12f)
            moveTo(12f, 3.5f)
            curveTo(9.6f, 6f, 8.4f, 8.9f, 8.4f, 12f); curveTo(8.4f, 15.1f, 9.6f, 18f, 12f, 20.5f)
            moveTo(12f, 3.5f)
            curveTo(14.4f, 6f, 15.6f, 8.9f, 15.6f, 12f); curveTo(15.6f, 15.1f, 14.4f, 18f, 12f, 20.5f)
        }
    }

    val Swap by lazy {
        icon("Swap") {
            moveTo(4.5f, 8.5f); lineTo(18.5f, 8.5f); moveTo(15f, 5f); lineTo(18.5f, 8.5f); lineTo(15f, 12f)
            moveTo(19.5f, 15.5f); lineTo(5.5f, 15.5f); moveTo(9f, 12f); lineTo(5.5f, 15.5f); lineTo(9f, 19f)
        }
    }

    val Eye by lazy {
        icon("Eye") {
            moveTo(2.8f, 12f)
            curveTo(4.8f, 8f, 8.1f, 5.8f, 12f, 5.8f); curveTo(15.9f, 5.8f, 19.2f, 8f, 21.2f, 12f)
            curveTo(19.2f, 16f, 15.9f, 18.2f, 12f, 18.2f); curveTo(8.1f, 18.2f, 4.8f, 16f, 2.8f, 12f); close()
            circle(12f, 12f, 2.8f)
        }
    }

    val Sparkle by lazy {
        icon("Sparkle") {
            moveTo(12f, 3.5f)
            curveTo(12.6f, 8.6f, 15.4f, 11.4f, 20.5f, 12f)
            curveTo(15.4f, 12.6f, 12.6f, 15.4f, 12f, 20.5f)
            curveTo(11.4f, 15.4f, 8.6f, 12.6f, 3.5f, 12f)
            curveTo(8.6f, 11.4f, 11.4f, 8.6f, 12f, 3.5f); close()
        }
    }

    val Clock by lazy { icon("Clock") { circle(12f, 12f, 8.5f); moveTo(12f, 7.5f); lineTo(12f, 12.2f); lineTo(15.2f, 14f) } }

    val Center by lazy {
        icon("Center") {
            moveTo(12f, 3.5f); lineTo(12f, 6.5f); moveTo(12f, 17.5f); lineTo(12f, 20.5f)
            moveTo(3.5f, 12f); lineTo(6.5f, 12f); moveTo(17.5f, 12f); lineTo(20.5f, 12f)
            roundRect(8f, 8f, 16f, 16f, 2f)
        }
    }

    val Download by lazy {
        icon("Import") {
            moveTo(12f, 4f); lineTo(12f, 15f); moveTo(7.8f, 10.8f); lineTo(12f, 15f); lineTo(16.2f, 10.8f)
            moveTo(4.5f, 15.5f); lineTo(4.5f, 17.5f); curveTo(4.5f, 19.2f, 5.8f, 20.5f, 7.5f, 20.5f)
            lineTo(16.5f, 20.5f); curveTo(18.2f, 20.5f, 19.5f, 19.2f, 19.5f, 17.5f); lineTo(19.5f, 15.5f)
        }
    }

    val Info by lazy { icon("Info") { circle(12f, 12f, 8.5f); moveTo(12f, 11f); lineTo(12f, 16.5f); dot(12f, 7.8f, .25f) } }
}

private fun icon(name: String, stroke: Float = 1.7f, mirror: Boolean = false, block: PathBuilder.() -> Unit): ImageVector =
    ImageVector.Builder(
        name = "Bm$name",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        // Direction-bearing glyphs flip in right-to-left languages.
        autoMirror = mirror,
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = stroke,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathBuilder = block,
        )
    }.build()

private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
    val k = r * 0.5522848f
    moveTo(cx + r, cy)
    curveTo(cx + r, cy + k, cx + k, cy + r, cx, cy + r)
    curveTo(cx - k, cy + r, cx - r, cy + k, cx - r, cy)
    curveTo(cx - r, cy - k, cx - k, cy - r, cx, cy - r)
    curveTo(cx + k, cy - r, cx + r, cy - k, cx + r, cy)
    close()
}

private fun PathBuilder.dot(cx: Float, cy: Float, r: Float) = circle(cx, cy, r)

private fun PathBuilder.roundRect(l: Float, t: Float, r: Float, b: Float, rad: Float) {
    val k = rad * 0.5522848f
    moveTo(l + rad, t)
    lineTo(r - rad, t); curveTo(r - rad + k, t, r, t + rad - k, r, t + rad)
    lineTo(r, b - rad); curveTo(r, b - rad + k, r - rad + k, b, r - rad, b)
    lineTo(l + rad, b); curveTo(l + rad - k, b, l, b - rad + k, l, b - rad)
    lineTo(l, t + rad); curveTo(l, t + rad - k, l + rad - k, t, l + rad, t)
    close()
}
