package com.bestmates.worldclock.widget

import android.graphics.Path
import android.graphics.RectF
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.scene.ShapePaths

/** Compatibility facade; geometry now lives in [ShapePaths] and drawing in SceneRenderer. */
object BackgroundRenderer {
    internal fun shapePath(shape: WidgetShape, r: RectF): Path = ShapePaths.path(shape, r)
}
