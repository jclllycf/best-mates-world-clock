package com.bestmates.worldclock.debug

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Bundle
import android.widget.ImageView
import android.widget.ScrollView
import com.bestmates.worldclock.model.MATERIAL_MIST
import com.bestmates.worldclock.model.MATERIAL_SOLID
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.scene.SceneRenderer
import java.io.File
import kotlin.math.roundToInt

/**
 * BM-REAL-06/07 contact sheet: every shape at landscape, square and portrait proportions,
 * in Mist (the new default) and Solid, over a bright and a dark wallpaper tone, drawn by
 * the production [SceneRenderer]. Saved to `files/review/shapes.png` for `adb pull`.
 */
class ShapeContactSheetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = draw()
        runCatching {
            val dir = File(getExternalFilesDir(null), "review").apply { mkdirs() }
            File(dir, "shapes.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
        setContentView(ScrollView(this).apply { addView(ImageView(this@ShapeContactSheetActivity).apply { setImageBitmap(bitmap); adjustViewBounds = true }) })
    }

    private fun draw(): Bitmap {
        val d = resources.displayMetrics.density
        fun dp(v: Float) = v * d
        val renderer = SceneRenderer(this)
        val shapes = WidgetShape.entries
        val rowH = dp(76f)
        val width = (dp(600f)).roundToInt()
        val height = (dp(40f) + shapes.size * rowH).roundToInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Left half: a bright pastel wallpaper; right half: a dark one.
        canvas.drawRect(0f, 0f, width / 2f, height.toFloat(), Paint().apply {
            shader = LinearGradient(0f, 0f, width / 2f, height.toFloat(), Color.rgb(236, 214, 230), Color.rgb(196, 204, 238), Shader.TileMode.CLAMP)
        })
        canvas.drawRect(width / 2f, 0f, width.toFloat(), height.toFloat(), Paint().apply {
            shader = LinearGradient(width / 2f, 0f, width.toFloat(), height.toFloat(), Color.rgb(40, 36, 62), Color.rgb(18, 22, 40), Shader.TileMode.CLAMP)
        })
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(40, 34, 52); textSize = dp(11f) }
        canvas.drawText("Mist / Solid · 2:1, 1:1, 2:3 · bright and dark wallpaper", dp(8f), dp(24f), text)
        val mist = WidgetLayer(dayColor = 0x57302B3D, nightColor = 0x6115131C, borderWidth = 0f, glowOpacity = 0f, shadow = 12f, shadowOpacity = .14f, shadowYOffset = 4f, material = MATERIAL_MIST)
        val solid = mist.copy(dayColor = 0xE655507F, material = MATERIAL_SOLID)
        shapes.forEachIndexed { i, shape ->
            val top = dp(40f) + i * rowH
            text.textSize = dp(8f)
            text.color = Color.rgb(60, 52, 74)
            canvas.drawText(shape.name.lowercase(), dp(4f), top + dp(9f), text)
            val sizes = listOf(dp(56f) to dp(28f), dp(40f) to dp(40f), dp(30f) to dp(45f))
            var x = dp(6f)
            listOf(mist to false, solid to false, mist to true, solid to true).forEach { (layer, dark) ->
                if (dark && x < width / 2f) x = width / 2f + dp(6f)
                sizes.forEach { (w, h) ->
                    val l = layer.copy(shape = shape)
                    val bleed = dp(16f).roundToInt()
                    val b = renderer.renderLayerBitmap(l, w.roundToInt(), h.roundToInt(), bleed, d, night = dark)
                    val y = top + dp(14f) + (dp(56f) - h) / 2f
                    canvas.drawBitmap(b, x - bleed, y - bleed, null)
                    x += w + dp(5f)
                }
                x += dp(4f)
            }
        }
        return bitmap
    }
}
