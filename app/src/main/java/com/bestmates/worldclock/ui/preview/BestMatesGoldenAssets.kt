package com.bestmates.worldclock.ui.preview

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Phase 2D-0.5 candidates. Nothing in this catalog is connected to production
 * models, saved IDs, pickers, renderer enums, or widget output.
 */
internal object BestMatesGoldenAssets {
    val orbit: List<GoldenVectorAsset> = listOf(
        GoldenVectorAsset("golden.orbit.settings", "Settings", GoldenOrbitIcons.Settings),
        GoldenVectorAsset("golden.orbit.undo", "Undo", GoldenOrbitIcons.Undo),
        GoldenVectorAsset("golden.orbit.redo", "Redo", GoldenOrbitIcons.Redo),
        GoldenVectorAsset("golden.orbit.font", "Font", GoldenOrbitIcons.Font),
        GoldenVectorAsset("golden.orbit.pin", "Pin", GoldenOrbitIcons.Pin),
        GoldenVectorAsset("golden.orbit.rotate", "Rotate", GoldenOrbitIcons.Rotate),
    )

    val celestial: List<GoldenCelestialFamily> = listOf(
        GoldenCelestialFamily(
            id = "golden.celestial.minimal",
            name = "Minimal Celestial",
            sun = GoldenCelestialIcons.MinimalSun,
            moon = GoldenCelestialIcons.MinimalMoon,
            note = "Quiet outline · smallest-size anchor",
        ),
        GoldenCelestialFamily(
            id = "golden.celestial.halo",
            name = "Soft Halo",
            sun = GoldenCelestialIcons.HaloSun,
            moon = GoldenCelestialIcons.HaloMoon,
            note = "Airy ring · calm orbital relationship",
        ),
        GoldenCelestialFamily(
            id = "golden.celestial.dreamy",
            name = "Dreamy Charm",
            sun = GoldenCelestialIcons.DreamySun,
            moon = GoldenCelestialIcons.DreamyMoon,
            note = "One shared sparkle · restrained personality",
        ),
    )

    val shapes: List<GoldenShapeAsset> = listOf(
        GoldenShapeAsset("golden.shape.heart", "Heart", 1f, ::heartPath),
        GoldenShapeAsset("golden.shape.cloud", "Cloud", 1.52f, ::cloudPath),
        GoldenShapeAsset("golden.shape.soft_arch", "Soft Arch", 1.32f, ::softArchPath),
        GoldenShapeAsset("golden.shape.ticket", "Atelier Ticket", 1.46f, ::ticketPath),
    )

    val typeWardrobe: List<GoldenTypeAsset> = listOf(
        GoldenTypeAsset(
            id = "golden.type.clean",
            name = "Clean Sans",
            family = FontFamily.SansSerif,
            timeWeight = FontWeight.Medium,
        ),
        GoldenTypeAsset(
            id = "golden.type.editorial",
            name = "Editorial Serif",
            family = FontFamily.Serif,
            timeWeight = FontWeight.Normal,
        ),
        GoldenTypeAsset(
            id = "golden.type.friendly",
            name = "Friendly Display",
            family = FontFamily(android.graphics.Typeface.create("sans-serif-rounded", android.graphics.Typeface.BOLD)),
            timeWeight = FontWeight.Bold,
        ),
    )

    fun validate(): GoldenAssetIntegrityReport {
        val failures = mutableListOf<String>()
        if (orbit.size != 6) failures += "Orbit count is ${orbit.size}; expected 6"
        if (celestial.size != 3) failures += "Celestial pair count is ${celestial.size}; expected 3"
        if (shapes.size != 4) failures += "Shape count is ${shapes.size}; expected 4"
        if (typeWardrobe.size != 3) failures += "Type card count is ${typeWardrobe.size}; expected 3"

        val vectorEntries = buildList {
            addAll(orbit.map { it.id to it.vector })
            celestial.forEach { family ->
                add("${family.id}.sun" to family.sun)
                add("${family.id}.moon" to family.moon)
            }
        }
        vectorEntries.forEach { (id, vector) ->
            if (vector.viewportWidth <= 0f || vector.viewportHeight <= 0f) failures += "$id has an invalid viewport"
            if (vector.defaultWidth.value <= 0f || vector.defaultHeight.value <= 0f) failures += "$id has an invalid default size"
        }

        shapes.forEach { shape ->
            runCatching {
                val bounds = shape.path(Size(320f, 240f)).getBounds()
                check(bounds.width > 1f && bounds.height > 1f)
                check(bounds.left >= -0.1f && bounds.top >= -0.1f)
                check(bounds.right <= 320.1f && bounds.bottom <= 240.1f)
            }.onFailure { failures += "${shape.id} failed path creation" }
        }

        val ids = buildList {
            addAll(orbit.map { it.id })
            celestial.forEach { add("${it.id}.sun"); add("${it.id}.moon") }
            addAll(shapes.map { it.id })
            addAll(typeWardrobe.map { it.id })
        }
        val duplicateIds = ids.groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        if (duplicateIds.isNotEmpty()) failures += "Duplicate IDs: ${duplicateIds.joinToString()}"

        return GoldenAssetIntegrityReport(
            assetCount = ids.size,
            isValid = failures.isEmpty(),
            failures = failures,
        )
    }
}

@Immutable
internal data class GoldenVectorAsset(
    val id: String,
    val name: String,
    val vector: ImageVector,
)

@Immutable
internal data class GoldenCelestialFamily(
    val id: String,
    val name: String,
    val sun: ImageVector,
    val moon: ImageVector,
    val note: String,
)

@Immutable
internal data class GoldenShapeAsset(
    val id: String,
    val name: String,
    val aspectRatio: Float,
    val path: (Size) -> Path,
)

@Immutable
internal data class GoldenTypeAsset(
    val id: String,
    val name: String,
    val family: FontFamily,
    val timeWeight: FontWeight,
)

@Immutable
internal data class GoldenAssetIntegrityReport(
    val assetCount: Int,
    val isValid: Boolean,
    val failures: List<String>,
)

private object GoldenOrbitIcons {
    val Settings = orbitVector("GoldenSettings") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(4.2f, 6.2f); lineTo(6.2f, 6.2f)
            circle(8.1f, 6.2f, 1.75f)
            moveTo(9.85f, 6.2f); lineTo(19.8f, 6.2f)
            moveTo(4.2f, 12.1f); lineTo(13.45f, 12.1f)
            circle(15.35f, 12.1f, 1.75f)
            moveTo(17.1f, 12.1f); lineTo(19.8f, 12.1f)
            moveTo(4.2f, 17.9f); lineTo(8.65f, 17.9f)
            circle(10.55f, 17.9f, 1.75f)
            moveTo(12.3f, 17.9f); lineTo(19.8f, 17.9f)
        }
    }

    val Undo = orbitVector("GoldenUndo") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(8.55f, 8.15f)
            cubicTo(11.15f, 6.15f, 15.15f, 6.35f, 17.65f, 8.85f)
            cubicTo(20.2f, 11.4f, 20.0f, 15.55f, 17.05f, 18.15f)
            cubicTo(15.35f, 19.65f, 13.0f, 20.05f, 10.95f, 19.35f)
            moveTo(8.6f, 4.85f); lineTo(4.85f, 8.65f); lineTo(8.8f, 11.85f)
        }
    }

    val Redo = orbitVector("GoldenRedo") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(15.45f, 8.15f)
            cubicTo(12.85f, 6.15f, 8.85f, 6.35f, 6.35f, 8.85f)
            cubicTo(3.8f, 11.4f, 4.0f, 15.55f, 6.95f, 18.15f)
            cubicTo(8.65f, 19.65f, 11.0f, 20.05f, 13.05f, 19.35f)
            moveTo(15.4f, 4.85f); lineTo(19.15f, 8.65f); lineTo(15.2f, 11.85f)
        }
    }

    val Font = orbitVector("GoldenFont") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(7.0f, 3.8f); lineTo(17.0f, 3.8f)
            cubicTo(18.8f, 3.8f, 20.2f, 5.2f, 20.2f, 7.0f)
            lineTo(20.2f, 17.0f)
            cubicTo(20.2f, 18.8f, 18.8f, 20.2f, 17.0f, 20.2f)
            lineTo(7.0f, 20.2f)
            cubicTo(5.2f, 20.2f, 3.8f, 18.8f, 3.8f, 17.0f)
            lineTo(3.8f, 7.0f)
            cubicTo(3.8f, 5.2f, 5.2f, 3.8f, 7.0f, 3.8f)
            moveTo(7.0f, 16.4f); lineTo(10.65f, 7.1f)
            cubicTo(10.95f, 6.25f, 12.05f, 6.25f, 12.4f, 7.1f)
            lineTo(16.25f, 16.4f)
            moveTo(8.65f, 12.4f); cubicTo(10.4f, 11.9f, 12.9f, 11.9f, 14.55f, 12.4f)
            moveTo(7.0f, 17.15f); cubicTo(9.7f, 17.65f, 14.25f, 17.65f, 17.0f, 17.15f)
        }
    }

    val Pin = orbitVector("GoldenPin") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(7.6f, 4.0f)
            cubicTo(7.6f, 3.45f, 8.05f, 3.0f, 8.6f, 3.0f)
            lineTo(15.4f, 3.0f)
            cubicTo(15.95f, 3.0f, 16.4f, 3.45f, 16.4f, 4.0f)
            moveTo(8.55f, 4.1f); lineTo(9.3f, 9.6f)
            cubicTo(9.45f, 10.65f, 8.3f, 11.85f, 6.65f, 12.9f)
            lineTo(17.35f, 12.9f)
            cubicTo(15.7f, 11.85f, 14.55f, 10.65f, 14.7f, 9.6f)
            lineTo(15.45f, 4.1f)
            moveTo(6.3f, 12.9f); lineTo(17.7f, 12.9f)
            moveTo(12.0f, 13.3f); lineTo(12.0f, 21.0f)
        }
    }

    val Rotate = orbitVector("GoldenRotate") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(7.55f, 19.1f)
            cubicTo(4.25f, 17.25f, 3.4f, 12.7f, 5.35f, 9.1f)
            cubicTo(7.55f, 4.95f, 13.15f, 3.25f, 17.0f, 5.35f)
            moveTo(18.75f, 3.1f); lineTo(19.25f, 7.3f); lineTo(15.15f, 7.55f)
        }
    }
}

private object GoldenCelestialIcons {
    val MinimalSun = celestialVector("GoldenMinimalSun") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.7f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            circle(12f, 12f, 3.85f)
            rays(12f, 12f, 7.0f, 9.25f)
        }
    }

    val MinimalMoon = celestialVector("GoldenMinimalMoon") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.7f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(15.9f, 4.2f)
            cubicTo(10.8f, 4.75f, 7.0f, 8.25f, 7.0f, 12.7f)
            cubicTo(7.0f, 17.15f, 10.45f, 20.2f, 14.75f, 20.2f)
            cubicTo(16.65f, 20.2f, 18.4f, 19.55f, 19.65f, 18.45f)
            cubicTo(15.05f, 18.6f, 11.65f, 15.45f, 11.65f, 11.35f)
            cubicTo(11.65f, 8.45f, 13.25f, 5.85f, 15.9f, 4.2f)
            close()
        }
    }

    val HaloSun = celestialVector("GoldenHaloSun") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.65f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            circle(12f, 12f, 2.75f)
            circle(12f, 12f, 6.65f)
            rays(12f, 12f, 8.15f, 9.35f)
        }
    }

    val HaloMoon = celestialVector("GoldenHaloMoon") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.65f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(13.9f, 5.1f)
            cubicTo(9.55f, 5.65f, 6.55f, 8.75f, 6.55f, 12.65f)
            cubicTo(6.55f, 16.55f, 9.55f, 19.2f, 13.25f, 19.2f)
            cubicTo(14.8f, 19.2f, 16.2f, 18.7f, 17.3f, 17.8f)
            cubicTo(13.5f, 17.75f, 10.75f, 15.05f, 10.75f, 11.65f)
            cubicTo(10.75f, 8.95f, 11.95f, 6.55f, 13.9f, 5.1f)
            close()
            moveTo(17.2f, 5.15f)
            cubicTo(20.05f, 7.8f, 20.85f, 12.05f, 19.1f, 15.55f)
        }
    }

    val DreamySun = celestialVector("GoldenDreamySun") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.7f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            circle(10.4f, 13.1f, 3.45f)
            rays(10.4f, 13.1f, 6.15f, 8.0f)
            sparkle(18.35f, 5.55f, 2.0f)
        }
    }

    val DreamyMoon = celestialVector("GoldenDreamyMoon") {
        path(stroke = SolidColor(Color.Black), fill = null, strokeLineWidth = 1.7f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
            moveTo(13.55f, 6.2f)
            cubicTo(9.45f, 6.7f, 6.65f, 9.6f, 6.65f, 13.2f)
            cubicTo(6.65f, 16.85f, 9.5f, 19.3f, 12.95f, 19.3f)
            cubicTo(14.45f, 19.3f, 15.75f, 18.85f, 16.8f, 18.0f)
            cubicTo(13.25f, 17.9f, 10.7f, 15.4f, 10.7f, 12.3f)
            cubicTo(10.7f, 9.8f, 11.75f, 7.6f, 13.55f, 6.2f)
            close()
            sparkle(18.35f, 5.55f, 2.0f)
        }
    }
}

private fun orbitVector(name: String, block: ImageVector.Builder.() -> Unit): ImageVector =
    ImageVector.Builder(name = name, defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .apply(block)
        .build()

private fun celestialVector(name: String, block: ImageVector.Builder.() -> Unit): ImageVector =
    ImageVector.Builder(name = name, defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .apply(block)
        .build()

private fun PathBuilder.circle(cx: Float, cy: Float, radius: Float) {
    val control = radius * 0.5522848f
    moveTo(cx + radius, cy)
    cubicTo(cx + radius, cy + control, cx + control, cy + radius, cx, cy + radius)
    cubicTo(cx - control, cy + radius, cx - radius, cy + control, cx - radius, cy)
    cubicTo(cx - radius, cy - control, cx - control, cy - radius, cx, cy - radius)
    cubicTo(cx + control, cy - radius, cx + radius, cy - control, cx + radius, cy)
    close()
}

private fun PathBuilder.cubicTo(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    x3: Float,
    y3: Float,
) = curveTo(x1, y1, x2, y2, x3, y3)

private fun PathBuilder.rays(cx: Float, cy: Float, inner: Float, outer: Float) {
    repeat(8) { index ->
        val angle = Math.toRadians(index * 45.0 - 90.0)
        moveTo(cx + kotlin.math.cos(angle).toFloat() * inner, cy + kotlin.math.sin(angle).toFloat() * inner)
        lineTo(cx + kotlin.math.cos(angle).toFloat() * outer, cy + kotlin.math.sin(angle).toFloat() * outer)
    }
}

private fun PathBuilder.sparkle(cx: Float, cy: Float, radius: Float) {
    moveTo(cx, cy - radius); cubicTo(cx, cy - radius * 0.38f, cx - radius * 0.38f, cy, cx - radius, cy)
    cubicTo(cx - radius * 0.38f, cy, cx, cy + radius * 0.38f, cx, cy + radius)
    cubicTo(cx, cy + radius * 0.38f, cx + radius * 0.38f, cy, cx + radius, cy)
    cubicTo(cx + radius * 0.38f, cy, cx, cy - radius * 0.38f, cx, cy - radius)
    close()
}

private fun heartPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.49f, h * 0.96f)
    cubicTo(w * 0.40f, h * 0.85f, w * 0.08f, h * 0.62f, w * 0.08f, h * 0.33f)
    cubicTo(w * 0.08f, h * 0.13f, w * 0.23f, h * 0.05f, w * 0.37f, h * 0.09f)
    cubicTo(w * 0.44f, h * 0.11f, w * 0.48f, h * 0.16f, w * 0.50f, h * 0.23f)
    cubicTo(w * 0.53f, h * 0.16f, w * 0.57f, h * 0.10f, w * 0.64f, h * 0.08f)
    cubicTo(w * 0.79f, h * 0.03f, w * 0.93f, h * 0.14f, w * 0.92f, h * 0.34f)
    cubicTo(w * 0.91f, h * 0.62f, w * 0.58f, h * 0.86f, w * 0.49f, h * 0.96f)
    close()
}

private fun cloudPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.08f, h * 0.62f)
    cubicTo(w * 0.05f, h * 0.47f, w * 0.16f, h * 0.35f, w * 0.31f, h * 0.37f)
    cubicTo(w * 0.35f, h * 0.19f, w * 0.50f, h * 0.11f, w * 0.64f, h * 0.19f)
    cubicTo(w * 0.72f, h * 0.24f, w * 0.75f, h * 0.32f, w * 0.76f, h * 0.41f)
    cubicTo(w * 0.88f, h * 0.39f, w * 0.95f, h * 0.48f, w * 0.94f, h * 0.61f)
    cubicTo(w * 0.93f, h * 0.74f, w * 0.81f, h * 0.81f, w * 0.67f, h * 0.80f)
    cubicTo(w * 0.52f, h * 0.88f, w * 0.29f, h * 0.86f, w * 0.16f, h * 0.78f)
    cubicTo(w * 0.11f, h * 0.75f, w * 0.08f, h * 0.69f, w * 0.08f, h * 0.62f)
    close()
}

private fun softArchPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.07f, h * 0.93f)
    lineTo(w * 0.07f, h * 0.39f)
    cubicTo(w * 0.07f, h * 0.17f, w * 0.25f, h * 0.05f, w * 0.50f, h * 0.05f)
    cubicTo(w * 0.75f, h * 0.05f, w * 0.93f, h * 0.17f, w * 0.93f, h * 0.39f)
    lineTo(w * 0.93f, h * 0.84f)
    cubicTo(w * 0.93f, h * 0.90f, w * 0.90f, h * 0.93f, w * 0.84f, h * 0.93f)
    lineTo(w * 0.16f, h * 0.93f)
    cubicTo(w * 0.10f, h * 0.93f, w * 0.07f, h * 0.90f, w * 0.07f, h * 0.84f)
    close()
}

private fun ticketPath(size: Size): Path = Path().apply {
    val w = size.width
    val h = size.height
    moveTo(w * 0.10f, h * 0.06f)
    lineTo(w * 0.90f, h * 0.06f)
    cubicTo(w * 0.95f, h * 0.06f, w * 0.97f, h * 0.10f, w * 0.97f, h * 0.16f)
    lineTo(w * 0.97f, h * 0.38f)
    cubicTo(w * 0.88f, h * 0.40f, w * 0.88f, h * 0.60f, w * 0.97f, h * 0.62f)
    lineTo(w * 0.97f, h * 0.84f)
    cubicTo(w * 0.97f, h * 0.90f, w * 0.95f, h * 0.94f, w * 0.90f, h * 0.94f)
    lineTo(w * 0.10f, h * 0.94f)
    cubicTo(w * 0.05f, h * 0.94f, w * 0.03f, h * 0.90f, w * 0.03f, h * 0.84f)
    lineTo(w * 0.03f, h * 0.62f)
    cubicTo(w * 0.12f, h * 0.60f, w * 0.12f, h * 0.40f, w * 0.03f, h * 0.38f)
    lineTo(w * 0.03f, h * 0.16f)
    cubicTo(w * 0.03f, h * 0.10f, w * 0.05f, h * 0.06f, w * 0.10f, h * 0.06f)
    close()
}
