package com.bestmates.worldclock.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.bestmates.worldclock.ui.preview.BestMatesFoundationDesignLab

/** Debug APK-only host for physical-device review of the Best Mates 2.0 Design Lab. */
class BestMatesDesignLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BestMatesFoundationDesignLab { isDark, background ->
                updateSystemBars(isDark = isDark, background = background)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun updateSystemBars(isDark: Boolean, background: Color) {
        window.statusBarColor = background.toArgb()
        window.navigationBarColor = background.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).run {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }
}
