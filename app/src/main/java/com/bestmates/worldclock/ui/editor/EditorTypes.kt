package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.WidgetLayer

internal sealed interface CanvasSelection {
    data class ComponentItem(val index: Int) : CanvasSelection
    data class CityItem(val index: Int) : CanvasSelection
    data class TextItem(val index: Int) : CanvasSelection
}

internal sealed interface EditorClipboard {
    data class Layer(val value: WidgetLayer) : EditorClipboard
    data class CityValue(val value: City) : EditorClipboard
    data class TextValue(val value: CustomText) : EditorClipboard
}
