package com.bestmates.worldclock.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Raw opaque reference swatches. Semantic meaning is assigned in BestMatesColors. */
@Immutable
internal data class BestMatesNeutralReferencePalette(
    val background: Color,
    val surfaceBase: Color,
    val surfaceRaised: Color,
    val surfaceInset: Color,
    val surfaceOverlay: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderControl: Color,
    val shadowColor: Color,
    val canvasMat: Color,
    val onCanvasMat: Color,
    val checkerLight: Color,
    val checkerDark: Color,
)

@Immutable
internal data class BestMatesSupportingReferencePalette(
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val destructive: Color,
    val onDestructive: Color,
    val destructiveContainer: Color,
    val onDestructiveContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
)

@Immutable
internal data class BestMatesIdentityReferencePalette(
    val accent: Color,
    val onAccent: Color,
    val accentContainer: Color,
    val onAccentContainer: Color,
    val atmosphereA: Color,
    val atmosphereB: Color,
)

internal val WarmPearlLightReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFFF8F5F2),
    surfaceBase = Color(0xFFFCF9F6),
    surfaceRaised = Color(0xFFFFFDFC),
    surfaceInset = Color(0xFFF0EBF2),
    surfaceOverlay = Color(0xFFFFFDFF),
    surfaceElevated = Color(0xFFF4EFF7),
    textPrimary = Color(0xFF302C38),
    textSecondary = Color(0xFF625B6D),
    textMuted = Color(0xFF6F6677),
    borderSubtle = Color(0xFFE2DAE8),
    borderControl = Color(0xFF877B93),
    shadowColor = Color(0xFF2E243B),
    canvasMat = Color(0xFFE9E5EC),
    onCanvasMat = Color(0xFF52485F),
    checkerLight = Color(0xFFEEEAF1),
    checkerDark = Color(0xFFDAD3E0),
)

internal val LavenderDarkReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFF19171F),
    surfaceBase = Color(0xFF201D28),
    surfaceRaised = Color(0xFF2A2534),
    surfaceInset = Color(0xFF17141D),
    surfaceOverlay = Color(0xFF302A3B),
    surfaceElevated = Color(0xFF362E42),
    textPrimary = Color(0xFFF4EDF7),
    textSecondary = Color(0xFFC8BED0),
    textMuted = Color(0xFFB2A6BD),
    borderSubtle = Color(0xFF463C52),
    borderControl = Color(0xFF8E809E),
    shadowColor = Color(0xFF000000),
    canvasMat = Color(0xFF25212D),
    onCanvasMat = Color(0xFFD9CFE4),
    checkerLight = Color(0xFF302938),
    checkerDark = Color(0xFF201C26),
)

internal val WarmPearlLightSupportingReferences = BestMatesSupportingReferencePalette(
    secondary = Color(0xFF755E6B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF3E2EA),
    onSecondaryContainer = Color(0xFF503943),
    tertiary = Color(0xFF536880),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDFE8F3),
    onTertiaryContainer = Color(0xFF2F435B),
    destructive = Color(0xFFA43F55),
    onDestructive = Color(0xFFFFFFFF),
    destructiveContainer = Color(0xFFFBE8EC),
    onDestructiveContainer = Color(0xFF6F2034),
    success = Color(0xFF406C57),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFE3F0E8),
    onSuccessContainer = Color(0xFF234D3B),
    warning = Color(0xFF805D22),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFF9EDCF),
    onWarningContainer = Color(0xFF594014),
)

internal val LavenderDarkSupportingReferences = BestMatesSupportingReferencePalette(
    secondary = Color(0xFFD9BDC9),
    onSecondary = Color(0xFF412D37),
    secondaryContainer = Color(0xFF4A3541),
    onSecondaryContainer = Color(0xFFF5DFEA),
    tertiary = Color(0xFFB7CAE3),
    onTertiary = Color(0xFF24364C),
    tertiaryContainer = Color(0xFF304157),
    onTertiaryContainer = Color(0xFFDAE7FA),
    destructive = Color(0xFFF2A8B7),
    onDestructive = Color(0xFF511627),
    destructiveContainer = Color(0xFF4E2633),
    onDestructiveContainer = Color(0xFFFFDCE4),
    success = Color(0xFFA1CEB3),
    onSuccess = Color(0xFF193626),
    successContainer = Color(0xFF233D30),
    onSuccessContainer = Color(0xFFD2F0DF),
    warning = Color(0xFFE2C58D),
    onWarning = Color(0xFF3F2F15),
    warningContainer = Color(0xFF44351E),
    onWarningContainer = Color(0xFFF6E6C6),
)

internal val CloudLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF72549D),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFECE1F7),
    onAccentContainer = Color(0xFF493361),
    atmosphereA = Color(0xFFDCCEED),
    atmosphereB = Color(0xFFF1DCE5),
)

internal val CloudDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFD3BAF3),
    onAccent = Color(0xFF3B2655),
    accentContainer = Color(0xFF49345F),
    onAccentContainer = Color(0xFFEFDCFF),
    atmosphereA = Color(0xFFDCCEED),
    atmosphereB = Color(0xFFF1DCE5),
)

internal val DreamLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF825274),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFF2E1ED),
    onAccentContainer = Color(0xFF5B3553),
    atmosphereA = Color(0xFFEBD3E7),
    atmosphereB = Color(0xFFF6DED8),
)

internal val DreamDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFE5B4D5),
    onAccent = Color(0xFF4C2643),
    accentContainer = Color(0xFF57334E),
    onAccentContainer = Color(0xFFF9DBEF),
    atmosphereA = Color(0xFFEBD3E7),
    atmosphereB = Color(0xFFF6DED8),
)

internal val MidnightLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF505F86),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFE2E8F7),
    onAccentContainer = Color(0xFF354365),
    atmosphereA = Color(0xFFD4DCF2),
    atmosphereB = Color(0xFFDCD3ED),
)

internal val MidnightDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFB8C7F1),
    onAccent = Color(0xFF283858),
    accentContainer = Color(0xFF303F5E),
    onAccentContainer = Color(0xFFDCE5FF),
    atmosphereA = Color(0xFFD4DCF2),
    atmosphereB = Color(0xFFDCD3ED),
)

internal val MonoLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF62616B),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFE8E5EA),
    onAccentContainer = Color(0xFF41404A),
    atmosphereA = Color(0xFFDFDCE3),
    atmosphereB = Color(0xFFE9E5E3),
)

internal val MonoDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFC9C5CF),
    onAccent = Color(0xFF323038),
    accentContainer = Color(0xFF3D3943),
    onAccentContainer = Color(0xFFEEEBF0),
    atmosphereA = Color(0xFFDFDCE3),
    atmosphereB = Color(0xFFE9E5E3),
)
