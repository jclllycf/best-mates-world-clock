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
        WidgetShape.STAMP -> stamp(rect)
        WidgetShape.FRAME -> frame(rect)
        WidgetShape.NOTE -> note(rect)
        WidgetShape.BUBBLE -> bubble(rect)
        WidgetShape.RIBBON -> ribbon(rect)
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
        WidgetShape.PEBBLE -> smoothLoop(floatArrayOf(1f, .9f, .97f, .88f, 1f, .93f, .96f), .5f, .42f)
        WidgetShape.PUFF -> puff()
        WidgetShape.HEART_SOFT -> Path().apply {
            // A plumper heart with a rounded point; the approved HEART stays the classic one.
            moveTo(.5f, .93f)
            cubicTo(.47f, .93f, .44f, .91f, .41f, .885f)
            cubicTo(.22f, .74f, 0f, .58f, 0f, .32f)
            cubicTo(0f, .13f, .15f, 0f, .30f, 0f)
            cubicTo(.40f, 0f, .47f, .05f, .5f, .13f)
            cubicTo(.53f, .05f, .60f, 0f, .70f, 0f)
            cubicTo(.85f, 0f, 1f, .13f, 1f, .32f)
            cubicTo(1f, .58f, .78f, .74f, .59f, .885f)
            cubicTo(.56f, .91f, .53f, .93f, .5f, .93f); close()
        }
        WidgetShape.BLOOM -> bloom()
        WidgetShape.ROSETTE -> rosette()
        WidgetShape.SPARKLE -> Path().apply {
            // Four-point star with concave sides, the sparkle of the Daybook moon icons.
            moveTo(.5f, 0f)
            quadTo(.57f, .43f, 1f, .5f); quadTo(.57f, .57f, .5f, 1f)
            quadTo(.43f, .57f, 0f, .5f); quadTo(.43f, .43f, .5f, 0f); close()
        }
        WidgetShape.CRESCENT -> Path().apply { addCircle(.5f, .5f, .5f, Path.Direction.CW) }.also {
            it.op(Path().apply { addCircle(.71f, .35f, .43f, Path.Direction.CW) }, Path.Op.DIFFERENCE)
        }
        WidgetShape.SUNBURST -> sunburst(14)
        else -> Path().apply { addRect(0f, 0f, 1f, 1f, Path.Direction.CW) }
    }

    /** Closed smooth curve (Catmull-Rom) through points on an ellipse with varied radii. */
    private fun smoothLoop(radii: FloatArray, rx: Float, ry: Float): Path {
        val n = radii.size
        val pts = (0 until n).map { i ->
            val a = 2 * Math.PI * i / n - Math.PI / 2
            (.5f + rx * radii[i] * cos(a).toFloat()) to (.5f + ry * radii[i] * sin(a).toFloat())
        }
        fun p(i: Int) = pts[((i % n) + n) % n]
        return Path().apply {
            moveTo(p(0).first, p(0).second)
            for (i in 0 until n) {
                val p0 = p(i - 1); val p1 = p(i); val p2 = p(i + 1); val p3 = p(i + 2)
                cubicTo(
                    p1.first + (p2.first - p0.first) / 6f, p1.second + (p2.second - p0.second) / 6f,
                    p2.first - (p3.first - p1.first) / 6f, p2.second - (p3.second - p1.second) / 6f,
                    p2.first, p2.second,
                )
            }
            close()
        }
    }

    /** A round "thought" cloud: an ellipse ringed with soft lobes. */
    private fun puff(): Path {
        val shape = Path().apply { addOval(RectF(.14f, .12f, .86f, .64f), Path.Direction.CW) }
        val n = 9
        for (i in 0 until n) {
            val a = 2 * Math.PI * i / n - Math.PI / 2
            val r = if (i % 2 == 0) .165f else .14f
            shape.op(Path().apply { addCircle(.5f + .34f * cos(a).toFloat(), .38f + .24f * sin(a).toFloat(), r, Path.Direction.CW) }, Path.Op.UNION)
        }
        return shape
    }

    /** Five oval petals around the centre. */
    private fun bloom(): Path {
        val shape = Path().apply { addCircle(.5f, .5f, .16f, Path.Direction.CW) }
        for (k in 0 until 5) {
            val petal = Path().apply { addOval(RectF(.5f - .17f, 0f, .5f + .17f, .52f), Path.Direction.CW) }
            petal.transform(Matrix().apply { setRotate(k * 72f, .5f, .5f) })
            shape.op(petal, Path.Op.UNION)
        }
        return shape
    }

    /** A scalloped badge: a disc edged with small round bumps. */
    private fun rosette(): Path {
        val shape = Path().apply { addCircle(.5f, .5f, .40f, Path.Direction.CW) }
        val n = 16
        for (i in 0 until n) {
            val a = 2 * Math.PI * i / n
            shape.op(Path().apply { addCircle(.5f + .40f * cos(a).toFloat(), .5f + .40f * sin(a).toFloat(), .082f, Path.Direction.CW) }, Path.Op.UNION)
        }
        return shape
    }

    /** A soft seal: the outline passes through the inner points, curving out to each ray. */
    private fun sunburst(rays: Int): Path = Path().apply {
        val steps = rays * 2
        fun pt(i: Int, r: Float): Pair<Float, Float> {
            val a = 2 * Math.PI * i / steps - Math.PI / 2
            return (.5f + r * cos(a).toFloat()) to (.5f + r * sin(a).toFloat())
        }
        val start = pt(1, .40f)
        moveTo(start.first, start.second)
        for (k in 0 until rays) {
            val tip = pt(2 * k + 2, .58f)
            val next = pt(2 * k + 3, .40f)
            quadTo(tip.first, tip.second, next.first, next.second)
        }
        close()
    }

    /** A postage stamp: a card with semicircular perforations along every edge. */
    private fun stamp(r: RectF): Path {
        val m = minOf(r.width(), r.height())
        val bite = m * .045f
        val body = Path().apply { addRect(r, Path.Direction.CW) }
        fun edge(length: Float, place: (Float) -> Pair<Float, Float>) {
            val count = (length / (bite * 2.7f)).toInt().coerceAtLeast(3)
            val step = length / count
            for (i in 0 until count) {
                val (x, y) = place(step * (i + .5f))
                body.op(Path().apply { addCircle(x, y, bite, Path.Direction.CW) }, Path.Op.DIFFERENCE)
            }
        }
        edge(r.width()) { d -> (r.left + d) to r.top }
        edge(r.width()) { d -> (r.left + d) to r.bottom }
        edge(r.height()) { d -> r.left to (r.top + d) }
        edge(r.height()) { d -> r.right to (r.top + d) }
        return body
    }

    /** A picture frame: a soft card with a rounded window through it. */
    private fun frame(r: RectF): Path {
        val m = minOf(r.width(), r.height())
        val outer = continuousRect(r, m * .16f)
        val inner = RectF(r).apply { inset(m * .12f, m * .12f) }
        outer.op(continuousRect(inner, m * .07f), Path.Op.DIFFERENCE)
        return outer
    }

    /** A note card with its top-right corner folded down. */
    private fun note(r: RectF): Path {
        val m = minOf(r.width(), r.height())
        val c = m * .07f
        val s = m * .24f
        val gap = m * .022f
        val body = Path().apply {
            moveTo(r.left + c, r.top)
            lineTo(r.right - s, r.top)
            lineTo(r.right, r.top + s)
            lineTo(r.right, r.bottom - c)
            quadTo(r.right, r.bottom, r.right - c, r.bottom)
            lineTo(r.left + c, r.bottom)
            quadTo(r.left, r.bottom, r.left, r.bottom - c)
            lineTo(r.left, r.top + c)
            quadTo(r.left, r.top, r.left + c, r.top)
            close()
        }
        // The folded flap lies on the card, separated by a hairline gap.
        body.op(Path().apply {
            moveTo(r.right - s - gap, r.top); lineTo(r.right, r.top + s + gap); lineTo(r.right - s - gap, r.top + s + gap); close()
        }, Path.Op.DIFFERENCE)
        body.op(Path().apply {
            moveTo(r.right - s, r.top); lineTo(r.right, r.top + s); lineTo(r.right - s, r.top + s); close()
        }, Path.Op.UNION)
        return body
    }

    /** A speech bubble with a soft tail at the lower left. */
    private fun bubble(r: RectF): Path {
        val tail = r.height() * .17f
        val body = RectF(r.left, r.top, r.right, r.bottom - tail)
        val shape = continuousRect(body, minOf(body.width(), body.height()) * .24f)
        val w = r.width()
        shape.op(Path().apply {
            moveTo(r.left + w * .16f, body.bottom - tail)
            lineTo(r.left + w * .34f, body.bottom - tail)
            quadTo(r.left + w * .24f, body.bottom + tail * .4f, r.left + w * .09f, r.bottom)
            quadTo(r.left + w * .17f, body.bottom + tail * .2f, r.left + w * .16f, body.bottom - tail)
            close()
        }, Path.Op.UNION)
        return shape
    }

    /** A waving ribbon with swallowtail ends. */
    private fun ribbon(r: RectF): Path = Path().apply {
        val h = r.height()
        val notch = minOf(h * .30f, r.width() * .09f)
        // Both edges arch by the same amount, so the band keeps its width and still touches
        // every side of its frame.
        val arch = h * .10f
        val mid = (r.top + arch + r.bottom) / 2f
        moveTo(r.left, r.top + arch)
        quadTo(r.centerX(), r.top - arch, r.right, r.top + arch)
        lineTo(r.right - notch, mid)
        lineTo(r.right, r.bottom)
        quadTo(r.centerX(), r.bottom - arch * 2f, r.left, r.bottom)
        lineTo(r.left + notch, mid)
        close()
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
