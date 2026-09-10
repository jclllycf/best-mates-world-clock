package com.bestmates.worldclock.debug

import android.graphics.Matrix
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.bestmates.worldclock.ui.editor.rotateCanvasVector
import kotlin.math.abs

/** Opt-in numerical checks; no project writes, gestures, screenshot capture, or test dependency. */
internal object EditorDragRegressionChecks {
    fun run() {
        val angles = listOf(0f, 45f, 90f, 180f, 270f, 360f, 450f, 720f, -45f, -90f, -450f)
        val movements = listOf(Offset(30f, 0f), Offset(-30f, 0f), Offset(0f, 30f),
            Offset(0f, -30f), Offset(17f, -9f), Offset.Zero)
        var cases = 0
        for (angle in angles) {
            // Independent platform transform oracle, including a nonzero pivot and translation.
            val transform = Matrix().apply {
                setRotate(angle, 71f, 43f)
                postTranslate(137f, 211f)
            }
            val inverse = Matrix()
            check(transform.invert(inverse))
            for (movement in movements) {
                val local = floatArrayOf(movement.x, movement.y)
                inverse.mapVectors(local)
                val actual = rotateCanvasVector(Offset(local[0], local[1]), angle)
                near(actual, movement, "canvas direction at $angle")
                for (density in listOf(1f, 2.75f, 3.5f)) {
                    // The two existing normalized conventions must receive the same canvas delta.
                    near(actual / density / 300f, movement / density / 300f, "layer fraction")
                    near(actual / density / 150f, movement / density / 150f, "city/text position")
                }
                cases++
            }
            // Framework maps current and previous points in the SAME current local frame.
            // Even after placement moves, subtracting those points cancels pivot/translation.
            var localTotal = Offset.Zero
            var expectedTotal = Offset.Zero
            for (step in 1..4) {
                val moving = Matrix(transform).apply { postTranslate(step * 11f, step * -7f) }
                val movingInverse = Matrix()
                check(moving.invert(movingInverse))
                val points = floatArrayOf(400f, 500f, 419f, 489f)
                movingInverse.mapPoints(points)
                localTotal += Offset(points[2] - points[0], points[3] - points[1])
                expectedTotal += Offset(19f, -11f)
                near(rotateCanvasVector(localTotal, angle), expectedTotal, "moving origin at $angle")
                cases++
            }
        }
        // This fixture fails with the former 'local delta equals canvas delta' assumption.
        val localRightAt90 = floatArrayOf(30f, 0f)
        Matrix().apply { setRotate(-90f) }.mapVectors(localRightAt90)
        check(abs(localRightAt90[0] - 30f) > 1f)
        near(rotateCanvasVector(Offset(localRightAt90[0], localRightAt90[1]), 90f),
            Offset(30f, 0f), "former 90 degree defect")
        Log.i("BestMatesEngineChecks", "PHASE_3A PASS: $cases drag cases; matrix oracle, densities, moving origins, former defect")
    }

    private fun near(actual: Offset, expected: Offset, label: String) {
        check(abs(actual.x - expected.x) < .002f && abs(actual.y - expected.y) < .002f) {
            "$label: expected $expected, got $actual"
        }
    }
}
