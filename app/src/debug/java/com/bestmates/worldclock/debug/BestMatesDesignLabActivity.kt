package com.bestmates.worldclock.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bestmates.worldclock.ui.preview.BestMatesFoundationDesignLab

/** Debug APK-only host for physical-device review of the Phase 2B Design Lab. */
class BestMatesDesignLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BestMatesFoundationDesignLab() }
    }
}
