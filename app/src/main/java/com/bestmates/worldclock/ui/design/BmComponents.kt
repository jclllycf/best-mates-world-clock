package com.bestmates.worldclock.ui.design

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlin.math.roundToInt

/* ------------------------------------------------------------------ Press feel */

/** Gentle 3% press compression shared by every tappable surface. */
@Composable
fun Modifier.pressable(interaction: MutableInteractionSource, enabled: Boolean = true): Modifier {
    val pressed by interaction.collectIsPressedAsState()
    val s by animateFloatAsState(if (pressed && enabled) .965f else 1f, spring(dampingRatio = .6f, stiffness = 900f), label = "press")
    return this.scale(s)
}

/* ------------------------------------------------------------------ Buttons */

enum class BmButtonStyle { Primary, Accent, Secondary, Ghost, Danger }
enum class BmButtonSize(val height: Dp, val pad: Dp, val icon: Dp) { Large(54.dp, 24.dp, 20.dp), Medium(46.dp, 18.dp, 18.dp), Small(36.dp, 14.dp, 16.dp) }

@Composable
fun BmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BmButtonStyle = BmButtonStyle.Primary,
    size: BmButtonSize = BmButtonSize.Medium,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val c = Bm.colors
    val interaction = remember { MutableInteractionSource() }
    val (bg, fg, border) = when (style) {
        BmButtonStyle.Primary -> Triple(c.primary, c.onPrimary, null)
        BmButtonStyle.Accent -> Triple(c.accent, c.onAccent, null)
        BmButtonStyle.Secondary -> Triple(c.surfaceRaised, c.ink, c.hairlineStrong)
        BmButtonStyle.Ghost -> Triple(Color.Transparent, c.ink, null)
        BmButtonStyle.Danger -> Triple(c.dangerSoft, c.danger, null)
    }
    val alpha = if (enabled) 1f else .38f
    Row(
        modifier
            .pressable(interaction, enabled)
            .heightIn(min = size.height)
            .clip(Bm.shapes.pill)
            .background(bg.copy(alpha = bg.alpha * alpha))
            .then(if (border != null) Modifier.border(1.dp, border.copy(alpha = alpha), Bm.shapes.pill) else Modifier)
            .clickable(interaction, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = size.pad),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(icon, null, tint = fg.copy(alpha = alpha), modifier = Modifier.size(size.icon))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = Bm.type.label, color = fg.copy(alpha = alpha), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun BmIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    tonal: Boolean = false,
    tint: Color? = null,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
) {
    val c = Bm.colors
    val interaction = remember { MutableInteractionSource() }
    val bg by animateColorAsState(
        when {
            selected -> c.ink
            tonal -> c.surfaceRaised
            else -> Color.Transparent
        },
        tween(160), label = "iconbg",
    )
    val fg = when {
        selected -> c.paper
        tint != null -> tint
        else -> c.ink
    }.copy(alpha = if (enabled) 1f else .3f)
    Box(
        modifier
            .size(size)
            .pressable(interaction, enabled)
            .clip(CircleShape)
            .background(bg)
            .then(if (tonal && !selected) Modifier.border(1.dp, c.hairline, CircleShape) else Modifier)
            .clickable(interaction, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics {
                this.contentDescription = contentDescription
                if (selected) this.selected = true
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = fg, modifier = Modifier.size(iconSize))
    }
}

/* ------------------------------------------------------------------ Switch */

@Composable
fun BmSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    val c = Bm.colors
    val view = LocalView.current
    val track by animateColorAsState(if (checked) c.ink else c.hairlineStrong, tween(180), label = "track")
    val knobX by animateDpAsState(if (checked) 20.dp else 2.dp, spring(dampingRatio = .62f, stiffness = 700f), label = "knob")
    Box(
        modifier
            .size(width = 46.dp, height = 28.dp)
            .clip(Bm.shapes.pill)
            .background(track.copy(alpha = if (enabled) 1f else .35f))
            .clickable(enabled = enabled, role = Role.Switch) {
                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                onCheckedChange(!checked)
            }
            .semantics { stateDescription = if (checked) "On" else "Off" },
    ) {
        Box(
            Modifier
                .offset(x = knobX, y = 2.dp)
                .size(24.dp)
                .shadow(2.dp, CircleShape, ambientColor = c.shadow, spotColor = c.shadow)
                .clip(CircleShape)
                .background(if (c.isDark && checked) c.paper else c.surfaceRaised),
        )
    }
}

/* ------------------------------------------------------------------ Slider */

/**
 * A quiet, precise slider: 4dp track, 22dp knob, the value is written next to the
 * label rather than floating over the finger.
 */
@Composable
fun BmSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    onValueChangeFinished: () -> Unit = {},
) {
    val c = Bm.colors
    val latestChange by rememberUpdatedState(onValueChange)
    val latestFinish by rememberUpdatedState(onValueChangeFinished)
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it > 0f } ?: 1f
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    BoxWithConstraints(modifier.fillMaxWidth().height(36.dp)) {
        val density = LocalDensity.current
        val knob = 22.dp
        val widthPx = with(density) { maxWidth.toPx() }
        val knobPx = with(density) { knob.toPx() }
        val usable = (widthPx - knobPx).coerceAtLeast(1f)
        fun fromX(x: Float) = valueRange.start + ((x - knobPx / 2f) / usable).coerceIn(0f, 1f) * span
        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(valueRange) {
                    detectTapGestures(onTap = { latestChange(fromX(it.x)); latestFinish() })
                }
                .pointerInput(valueRange) {
                    detectDragGestures(
                        onDragStart = { latestChange(fromX(it.x)) },
                        onDragEnd = { latestFinish() },
                        onDragCancel = { latestFinish() },
                    ) { change, _ -> change.consume(); latestChange(fromX(change.position.x)) }
                },
        ) {
            Box(
                Modifier.align(Alignment.CenterStart).padding(horizontal = knob / 2).fillMaxWidth().height(4.dp)
                    .clip(Bm.shapes.pill).background(c.hairline),
            )
            Box(
                Modifier.align(Alignment.CenterStart).padding(start = knob / 2)
                    .width(with(density) { (usable * fraction).toDp() }).height(4.dp)
                    .clip(Bm.shapes.pill).background(c.ink),
            )
            Box(
                Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset((usable * fraction).roundToInt(), 0) }
                    .size(knob)
                    .shadow(3.dp, CircleShape, ambientColor = c.shadow, spotColor = c.shadow)
                    .clip(CircleShape)
                    .background(c.surfaceRaised)
                    .border(1.5.dp, c.ink, CircleShape),
            )
        }
    }
}

@Composable
fun BmSliderRow(
    label: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
    onFinished: () -> Unit = {},
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = Bm.type.bodySmall, color = Bm.colors.inkSoft, modifier = Modifier.weight(1f))
            Text(valueText, style = Bm.type.numeric, color = Bm.colors.ink)
        }
        BmSlider(value, onChange, range, onValueChangeFinished = onFinished)
    }
}

/* ------------------------------------------------------------------ Segmented */

@Composable
fun BmSegmented(
    options: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    icons: List<ImageVector?> = emptyList(),
) {
    val c = Bm.colors
    BoxWithConstraints(
        modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(Bm.shapes.pill)
            .background(c.surfaceSunken)
            .padding(3.dp),
    ) {
        val segment = maxWidth / options.size.coerceAtLeast(1)
        val x by animateDpAsState(segment * selected, spring(dampingRatio = .82f, stiffness = 600f), label = "seg")
        Box(
            Modifier.offset(x = x).width(segment).fillMaxHeight()
                .shadow(1.dp, Bm.shapes.pill, ambientColor = c.shadow, spotColor = c.shadow)
                .clip(Bm.shapes.pill).background(c.surfaceRaised),
        )
        Row(Modifier.fillMaxSize()) {
            options.forEachIndexed { index, label ->
                val active = index == selected
                Row(
                    Modifier.weight(1f).fillMaxHeight().clip(Bm.shapes.pill)
                        .clickable(role = Role.Tab) { onSelect(index) }
                        .semantics { this.selected = active },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icons.getOrNull(index)?.let {
                        Icon(it, null, tint = if (active) c.ink else c.inkFaint, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(label, style = Bm.type.caption, color = if (active) c.ink else c.inkSoft, maxLines = 1)
                }
            }
        }
    }
}

/* ------------------------------------------------------------------ Chips */

@Composable
fun BmChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
) {
    val c = Bm.colors
    val interaction = remember { MutableInteractionSource() }
    val bg by animateColorAsState(if (selected) c.ink else c.surfaceRaised, tween(160), label = "chipbg")
    val fg = if (selected) c.paper else c.ink
    Row(
        modifier
            .pressable(interaction)
            .heightIn(min = 38.dp)
            .clip(Bm.shapes.pill)
            .background(bg)
            .border(1.dp, if (selected) c.ink else c.hairlineStrong, Bm.shapes.pill)
            .clickable(interaction, indication = null, role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leading != null) { leading(); Spacer(Modifier.width(8.dp)) }
        Text(label, style = Bm.type.caption, color = fg, maxLines = 1)
    }
}

/* ------------------------------------------------------------------ Fields */

@Composable
fun BmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null,
    textStyle: TextStyle? = null,
    onDone: (() -> Unit)? = null,
) {
    val c = Bm.colors
    val style = (textStyle ?: Bm.type.body).copy(color = c.ink)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        textStyle = style,
        cursorBrush = SolidColor(c.accentInk),
        keyboardOptions = KeyboardOptions(imeAction = if (singleLine) ImeAction.Done else ImeAction.Default),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
        modifier = modifier,
        decorationBox = { inner ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
                    .clip(Bm.shapes.control)
                    .background(c.surfaceSunken)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, null, tint = c.inkFaint, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                }
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(placeholder, style = style.copy(color = c.inkFaint), maxLines = 1)
                    inner()
                }
            }
        },
    )
}

/* ------------------------------------------------------------------ Surfaces and rows */

@Composable
fun BmCard(modifier: Modifier = Modifier, padding: Dp = 18.dp, content: @Composable ColumnScope.() -> Unit) {
    val c = Bm.colors
    Column(
        modifier
            .fillMaxWidth()
            .clip(Bm.shapes.card)
            .background(c.surface)
            .border(1.dp, c.hairline, Bm.shapes.card)
            .padding(padding),
        content = content,
    )
}

@Composable
fun BmOverline(text: String, modifier: Modifier = Modifier, color: Color = Bm.colors.inkFaint) {
    Text(text.uppercase(), style = Bm.type.overline, color = color, modifier = modifier)
}

@Composable
fun BmRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val c = Bm.colors
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leading != null) { leading(); Spacer(Modifier.width(14.dp)) }
        Column(Modifier.weight(1f)) {
            Text(title, style = Bm.type.body, color = c.ink)
            if (subtitle != null) Text(subtitle, style = Bm.type.bodySmall, color = c.inkSoft)
        }
        if (trailing != null) { Spacer(Modifier.width(12.dp)); trailing() }
    }
}

@Composable
fun BmDivider(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(1.dp).background(Bm.colors.hairline))
}

/* ------------------------------------------------------------------ Sheet */

/**
 * A paper bottom sheet. Implemented on a Dialog window so it layers above the editor's
 * canvas while keeping full control over motion, scrim and insets.
 */
@Composable
fun BmSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    fullHeight: Boolean = false,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    var closing by remember { mutableStateOf(false) }
    val latestDismiss by rememberUpdatedState(onDismiss)
    fun close() { closing = true; state.targetState = false }
    LaunchedEffect(closing, state.currentState, state.isIdle) {
        if (closing && state.isIdle && !state.currentState) latestDismiss()
    }
    Dialog(
        onDismissRequest = { close() },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.setDimAmount(0f)
        val c = Bm.colors
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(state, enter = fadeIn(tween(220)), exit = fadeOut(tween(180))) {
                Box(
                    Modifier.fillMaxSize().background(c.scrim)
                        .clickable(remember { MutableInteractionSource() }, indication = null) { close() },
                )
            }
            AnimatedVisibility(
                state,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically(spring(dampingRatio = .86f, stiffness = Spring.StiffnessMediumLow)) { it },
                exit = slideOutVertically(tween(200)) { it },
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .then(if (fullHeight) Modifier.statusBarsPadding().padding(top = 24.dp).fillMaxSize() else Modifier.statusBarsPadding().padding(top = 48.dp))
                        .shadow(24.dp, Bm.shapes.sheet, ambientColor = c.shadow, spotColor = c.shadow)
                        .clip(Bm.shapes.sheet)
                        .background(c.surfaceRaised)
                        .navigationBarsPadding()
                        .imePadding(),
                ) {
                    Box(Modifier.fillMaxWidth().padding(top = 10.dp), contentAlignment = Alignment.Center) {
                        Box(Modifier.size(width = 36.dp, height = 4.dp).clip(Bm.shapes.pill).background(c.hairlineStrong))
                    }
                    if (title != null) {
                        Row(
                            Modifier.fillMaxWidth().padding(start = 24.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(title, style = Bm.type.headline, color = c.ink, modifier = Modifier.weight(1f))
                            trailing?.invoke(this)
                            BmIconButton(BmIcons.Close, "Close", onClick = { close() })
                        }
                    }
                    content()
                }
            }
        }
    }
}

/** Centered confirmation dialog for destructive or naming moments. */
@Composable
fun BmDialog(
    title: String,
    onDismiss: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    destructive: Boolean = false,
    message: String? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val c = Bm.colors
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .shadow(28.dp, RoundedCornerShape(28.dp), ambientColor = c.shadow, spotColor = c.shadow)
                .clip(RoundedCornerShape(28.dp))
                .background(c.surfaceRaised)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(title, style = Bm.type.headline, color = c.ink)
            if (message != null) Text(message, style = Bm.type.body, color = c.inkSoft)
            content?.invoke(this)
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BmButton(dismissText, onDismiss, Modifier.weight(1f), style = BmButtonStyle.Secondary)
                BmButton(confirmText, onConfirm, Modifier.weight(1f), style = if (destructive) BmButtonStyle.Danger else BmButtonStyle.Primary)
            }
        }
    }
}

/** Small round swatch used by colour controls. */
@Composable
fun BmSwatch(color: Color, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, size: Dp = 34.dp) {
    val c = Bm.colors
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .border(if (selected) 2.dp else 1.dp, if (selected) c.ink else c.hairlineStrong, CircleShape)
            .padding(if (selected) 4.dp else 0.dp)
            .clip(CircleShape)
            .background(CheckerBrush.brush(c.isDark))
            .background(color)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected },
    )
}

/** Checkerboard used behind translucent colours so opacity is readable. */
object CheckerBrush {
    private val light = androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFFF1EDE6), Color(0xFFDCD4C8)))
    private val dark = androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFF2A2830), Color(0xFF3A3742)))
    fun brush(isDark: Boolean) = if (isDark) dark else light
}

