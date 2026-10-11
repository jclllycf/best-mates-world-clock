package com.bestmates.worldclock.scene

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import com.bestmates.worldclock.model.WidgetShape
import java.util.EnumMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

/**
 * Background shape geometry shared by the editor, previews and the launcher widget.
 *
 * Each silhouette is designed once in its own design space and then normalised so its
 * visible geometry fills the unit square exactly. A frame of any proportion therefore
 * receives the whole shape edge to edge, with no artificial margins (a long-standing
 * defect for cloud, ticket, flag and petal).
 *
 * Shapes that read as a specific object (cloud, four-petal, heart) are drawn for their
 * natural proportion ([WidgetShape.designAspect]); stretching away from it is allowed.
 */
object ShapePaths {
    private val unit = EnumMap<WidgetShape, Path>(WidgetShape::class.java)

    /** Path for [shape] filling [rect] exactly. Corner radii adapt to the real proportions. */
    fun path(shape: WidgetShape, rect: RectF): Path = when (shape) {
        // Proportion-aware shapes are built directly in target space so corners stay round.
        WidgetShape.ROUNDED -> continuousRect(rect, minOf(rect.width(), rect.height()) * .22f)
        WidgetShape.CAPSULE -> Path().apply { val r = minOf(rect.width(), rect.height()) / 2f; addRoundRect(rect, r, r, Path.Direction.CW) }
        WidgetShape.CIRCLE -> Path().apply { addOval(rect, Path.Direction.CW) }
        WidgetShape.PILL -> arch(rect)
        WidgetShape.TICKET -> ticket(rect)
        else -> Path(unit.getOrPut(shape) { normalised(design(shape)) }).apply {
            transform(Matrix().apply { setRectToRect(RectF(0f, 0f, 1f, 1f), rect, Matrix.ScaleToFit.FILL) })
        }
    }

    private fun normalised(p: Path): Path {
        val b = RectF(); p.computeBounds(b, true)
        return Path(p).apply { transform(Matrix().apply { setRectToRect(b, RectF(0f, 0f, 1f, 1f), Matrix.ScaleToFit.FILL) }) }
    }

    private fun design(shape: WidgetShape): Path = when (shape) {
        WidgetShape.SQUIRCLE -> superellipse(4.6)
        WidgetShape.CLOUD -> cloud()
        WidgetShape.DIAMOND -> quatrefoil()
        WidgetShape.PETAL -> Path().apply {
            // A lemon-shaped petal: two mirrored arcs meeting in soft points.
            moveTo(.5f, 0f)
            cubicTo(1.166f, 0f, 1.166f, 1f, .5f, 1f)
            cubicTo(-.166f, 1f, -.166f, 0f, .5f, 0f); close()
        }
        WidgetShape.HEART -> Path().apply {
            // The approved canonical heart, unchanged.
            moveTo(.5f, 1f)
            cubicTo(.39f, .90f, 0f, .58f, 0f, .30f)
            cubicTo(0f, .05f, .34f, 0f, .5f, .20f)
            cubicTo(.66f, 0f, 1f, .05f, 1f, .30f)
            cubicTo(1f, .58f, .61f, .90f, .5f, 1f); close()
        }
        WidgetShape.DROP -> Path().apply {
            moveTo(.5f, 0f)
            cubicTo(.68f, .20f, 1f, .46f, 1f, .70f)
            cubicTo(1f, .88f, .78f, 1f, .5f, 1f)
            cubicTo(.22f, 1f, 0f, .88f, 0f, .70f)
            cubicTo(0f, .46f, .32f, .20f, .5f, 0f); close()
        }
        WidgetShape.WAVE -> Path().apply {
            // A soft waving banner: parallel undulating top and bottom edges.
            moveTo(0f, .12f)
            cubicTo(.30f, -.04f, .62f, .26f, 1f, .10f)
            lineTo(1f, .88f)
            cubicTo(.62f, 1.04f, .30f, .74f, 0f, .90f)
            close()
        }
        else -> Path().apply { addRect(0f, 0f, 1f, 1f, Path.Direction.CW) }
    }

    /** iOS-like continuous corners: the curve starts earlier and eases into the edge. */
    private fun continuousRect(r: RectF, radius: Float): Path = Path().apply {
        val rad = radius.coerceAtMost(minOf(r.width(), r.height()) / 2f)
        val e = (rad * 1.28f).coerceAtMost(minOf(r.width(), r.height()) / 2f)
        val c = rad * .30f
        moveTo(r.left + e, r.top)
        lineTo(r.right - e, r.top)
        cubicTo(r.right - c, r.top, r.right, r.top + c, r.right, r.top + e)
        lineTo(r.right, r.bottom - e)
        cubicTo(r.right, r.bottom - c, r.right - c, r.bottom, r.right - e, r.bottom)
        lineTo(r.left + e, r.bottom)
        cubicTo(r.left + c, r.bottom, r.left, r.bottom - c, r.left, r.bottom - e)
        lineTo(r.left, r.top + e)
        cubicTo(r.left, r.top + c, r.left + c, r.top, r.left + e, r.top)
        close()
    }

    private fun superellipse(n: Double): Path = Path().apply {
        val steps = 96
        val pts = (0 until steps).map { i ->
            val t = 2 * Math.PI * i / steps
            val c = cos(t); val s = sin(t)
            val x = sign(c) * abs(c).pow(2.0 / n)
            val y = sign(s) * abs(s).pow(2.0 / n)
            (.5f + .5f * x.toFloat()) to (.5f + .5f * y.toFloat())
        }
        // Quadratic smoothing through segment midpoints gives a continuous outline.
        val mid = { a: Pair<Float, Float>, b: Pair<Float, Float> -> ((a.first + b.first) / 2f) to ((a.second + b.second) / 2f) }
        val start = mid(pts.last(), pts.first())
        moveTo(start.first, start.second)
        for (i in pts.indices) {
            val p = pts[i]; val m = mid(p, pts[(i + 1) % pts.size])
            quadTo(p.first, p.second, m.first, m.second)
        }
        close()
    }

    /** Three soft bumps over a flat rounded base, designed at 160 × 100. */
    private fun cloud(): Path {
        // Design units divided by 160 on both axes keep circles round; normalisation later
        // stretches the 160 × 100 result to the unit square.
        fun circle(cx: Float, cy: Float, r: Float) = Path().apply { addCircle(cx / 160f, cy / 160f, r / 160f, Path.Direction.CW) }
        val shape = Path().apply {
            addRoundRect(RectF(0f, 45f / 160f, 1f, 100f / 160f), 27.5f / 160f, 27.5f / 160f, Path.Direction.CW)
        }
        shape.op(circle(46f, 52f, 26f), Path.Op.UNION)
        shape.op(circle(88f, 36f, 36f), Path.Op.UNION)
        shape.op(circle(126f, 56f, 22f), Path.Op.UNION)
        return shape
    }

    private fun quatrefoil(): Path {
        val r = .27f
        val p = Path().apply { addCircle(.5f, .27f, r, Path.Direction.CW) }
        p.op(Path().apply { addCircle(.73f, .5f, r, Path.Direction.CW) }, Path.Op.UNION)
        p.op(Path().apply { addCircle(.5f, .73f, r, Path.Direction.CW) }, Path.Op.UNION)
        p.op(Path().apply { addCircle(.27f, .5f, r, Path.Direction.CW) }, Path.Op.UNION)
        return p
    }

    /** Half-round top, straight sides, gently rounded foot. */
    private fun arch(r: RectF): Path = Path().apply {
        val ry = minOf(r.height() * .5f, r.width() * .5f)
        val foot = minOf(r.width(), r.height()) * .10f
        moveTo(r.left, r.top + ry)
        arcTo(RectF(r.left, r.top, r.right, r.top + ry * 2f), 180f, 180f, false)
        lineTo(r.right, r.bottom - foot)
        quadTo(r.right, r.bottom, r.right - foot, r.bottom)
        lineTo(r.left + foot, r.bottom)
        quadTo(r.left, r.bottom, r.left, r.bottom - foot)
        close()
    }

    /** Rounded card with true semicircular side notches. */
    private fun ticket(r: RectF): Path {
        val m = minOf(r.width(), r.height())
        val corner = m * .13f
        val notch = m * .11f
        val body = Path().apply { addRoundRect(r, corner, corner, Path.Direction.CW) }
        body.op(Path().apply { addCircle(r.left, r.centerY(), notch, Path.Direction.CW) }, Path.Op.DIFFERENCE)
        body.op(Path().apply { addCircle(r.right, r.centerY(), notch, Path.Direction.CW) }, Path.Op.DIFFERENCE)
        return body
    }
}
