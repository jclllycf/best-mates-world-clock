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

internal val CloudLightNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFFFFF8FB),
    surfaceBase = Color(0xFFFFFDFE),
    surfaceRaised = Color(0xFFFFFFFF),
    surfaceInset = Color(0xFFF9F0F6),
    surfaceOverlay = Color(0xFFFFFBFD),
    surfaceElevated = Color(0xFFF8EDF5),
    textPrimary = Color(0xFF322A35),
    textSecondary = Color(0xFF6C5E6C),
    textMuted = Color(0xFF776879),
    borderSubtle = Color(0xFFEBDCE6),
    borderControl = Color(0xFF957F91),
    shadowColor = Color(0xFF553D57),
    canvasMat = Color(0xFFF2EEF4),
    onCanvasMat = Color(0xFF554B59),
    checkerLight = Color(0xFFF5F0F4),
    checkerDark = Color(0xFFE2D9E1),
)

internal val CloudDarkNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFF19131D),
    surfaceBase = Color(0xFF211925),
    surfaceRaised = Color(0xFF2C2131),
    surfaceInset = Color(0xFF151018),
    surfaceOverlay = Color(0xFF35283B),
    surfaceElevated = Color(0xFF403048),
    textPrimary = Color(0xFFF8EDF5),
    textSecondary = Color(0xFFD4C1CF),
    textMuted = Color(0xFFB09DAD),
    borderSubtle = Color(0xFF4C394F),
    borderControl = Color(0xFF937997),
    shadowColor = Color(0xFF08050A),
    canvasMat = Color(0xFF29222D),
    onCanvasMat = Color(0xFFE5D9E4),
    checkerLight = Color(0xFF352A39),
    checkerDark = Color(0xFF211A25),
)

internal val DreamLightNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFFFFF5FA),
    surfaceBase = Color(0xFFFFFBFD),
    surfaceRaised = Color(0xFFFFFFFF),
    surfaceInset = Color(0xFFF9EAF2),
    surfaceOverlay = Color(0xFFFFF9FC),
    surfaceElevated = Color(0xFFFCECF4),
    textPrimary = Color(0xFF382A34),
    textSecondary = Color(0xFF765F6E),
    textMuted = Color(0xFF7A6673),
    borderSubtle = Color(0xFFEFD7E3),
    borderControl = Color(0xFF96798C),
    shadowColor = Color(0xFF63384E),
    canvasMat = Color(0xFFF3EDF2),
    onCanvasMat = Color(0xFF594B55),
    checkerLight = Color(0xFFF7EFF4),
    checkerDark = Color(0xFFE6D5DF),
)

internal val DreamDarkNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFF1D121A),
    surfaceBase = Color(0xFF271821),
    surfaceRaised = Color(0xFF33212C),
    surfaceInset = Color(0xFF170E15),
    surfaceOverlay = Color(0xFF3D2934),
    surfaceElevated = Color(0xFF49303E),
    textPrimary = Color(0xFFFFEEF5),
    textSecondary = Color(0xFFDCC1CE),
    textMuted = Color(0xFFB49AA8),
    borderSubtle = Color(0xFF583846),
    borderControl = Color(0xFFA4768D),
    shadowColor = Color(0xFF090407),
    canvasMat = Color(0xFF2C222B),
    onCanvasMat = Color(0xFFEAD9E2),
    checkerLight = Color(0xFF3A2932),
    checkerDark = Color(0xFF24181F),
)

internal val MidnightLightNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFFFAF7FC),
    surfaceBase = Color(0xFFFEFCFF),
    surfaceRaised = Color(0xFFFFFFFF),
    surfaceInset = Color(0xFFF1ECF7),
    surfaceOverlay = Color(0xFFFCF9FF),
    surfaceElevated = Color(0xFFF5EFFA),
    textPrimary = Color(0xFF302A38),
    textSecondary = Color(0xFF665D70),
    textMuted = Color(0xFF716979),
    borderSubtle = Color(0xFFE3DCEB),
    borderControl = Color(0xFF897C92),
    shadowColor = Color(0xFF3D314C),
    canvasMat = Color(0xFFF0EDF4),
    onCanvasMat = Color(0xFF51495D),
    checkerLight = Color(0xFFF2EFF6),
    checkerDark = Color(0xFFDDD8E5),
)

internal val MidnightDarkNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFF120E17),
    surfaceBase = Color(0xFF1B1521),
    surfaceRaised = Color(0xFF261D2D),
    surfaceInset = Color(0xFF0E0B12),
    surfaceOverlay = Color(0xFF30243A),
    surfaceElevated = Color(0xFF3A2B46),
    textPrimary = Color(0xFFF4EDF8),
    textSecondary = Color(0xFFCDBFD4),
    textMuted = Color(0xFFA697AD),
    borderSubtle = Color(0xFF44354E),
    borderControl = Color(0xFF887695),
    shadowColor = Color(0xFF050307),
    canvasMat = Color(0xFF241E2A),
    onCanvasMat = Color(0xFFDED5E4),
    checkerLight = Color(0xFF2E2635),
    checkerDark = Color(0xFF1A1520),
)

internal val MonoLightNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFFFBF8F8),
    surfaceBase = Color(0xFFFEFCFC),
    surfaceRaised = Color(0xFFFFFFFF),
    surfaceInset = Color(0xFFF2EDEC),
    surfaceOverlay = Color(0xFFFDFBFB),
    surfaceElevated = Color(0xFFF7F1F3),
    textPrimary = Color(0xFF312D30),
    textSecondary = Color(0xFF686166),
    textMuted = Color(0xFF70696E),
    borderSubtle = Color(0xFFE4DDDF),
    borderControl = Color(0xFF8C8287),
    shadowColor = Color(0xFF443B40),
    canvasMat = Color(0xFFF0ECEE),
    onCanvasMat = Color(0xFF514B4F),
    checkerLight = Color(0xFFF3EFF0),
    checkerDark = Color(0xFFDED8DA),
)

internal val MonoDarkNeutralReferences = BestMatesNeutralReferencePalette(
    background = Color(0xFF191719),
    surfaceBase = Color(0xFF211E21),
    surfaceRaised = Color(0xFF2B272B),
    surfaceInset = Color(0xFF141214),
    surfaceOverlay = Color(0xFF342F34),
    surfaceElevated = Color(0xFF3D373D),
    textPrimary = Color(0xFFF3EFF2),
    textSecondary = Color(0xFFCBC3C8),
    textMuted = Color(0xFFAAA2A7),
    borderSubtle = Color(0xFF474047),
    borderControl = Color(0xFF898087),
    shadowColor = Color(0xFF050405),
    canvasMat = Color(0xFF272327),
    onCanvasMat = Color(0xFFDCD6DA),
    checkerLight = Color(0xFF302B30),
    checkerDark = Color(0xFF1E1A1E),
)

internal val CuteAtelierLightSupportingReferences = BestMatesSupportingReferencePalette(
    secondary = Color(0xFFB97891),
    onSecondary = Color(0xFF302027),
    secondaryContainer = Color(0xFFF8E3EC),
    onSecondaryContainer = Color(0xFF593342),
    tertiary = Color(0xFFC98A72),
    onTertiary = Color(0xFF3E2922),
    tertiaryContainer = Color(0xFFF9E5DC),
    onTertiaryContainer = Color(0xFF623C2E),
    destructive = Color(0xFFA84762),
    onDestructive = Color(0xFFFFFFFF),
    destructiveContainer = Color(0xFFFBE5EB),
    onDestructiveContainer = Color(0xFF6C263A),
    success = Color(0xFF5F7D6C),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFE5F0E8),
    onSuccessContainer = Color(0xFF304C3B),
    warning = Color(0xFF9A6D31),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFF8EBD2),
    onWarningContainer = Color(0xFF5D411D),
)

internal val CuteAtelierDarkSupportingReferences = BestMatesSupportingReferencePalette(
    secondary = Color(0xFFE1ACC0),
    onSecondary = Color(0xFF4C2736),
    secondaryContainer = Color(0xFF553140),
    onSecondaryContainer = Color(0xFFFFDDEA),
    tertiary = Color(0xFFE7B29D),
    onTertiary = Color(0xFF4C2A1F),
    tertiaryContainer = Color(0xFF5B372B),
    onTertiaryContainer = Color(0xFFFFDED0),
    destructive = Color(0xFFF1A2B5),
    onDestructive = Color(0xFF541729),
    destructiveContainer = Color(0xFF552532),
    onDestructiveContainer = Color(0xFFFFDCE5),
    success = Color(0xFFA8CEB5),
    onSuccess = Color(0xFF1C3928),
    successContainer = Color(0xFF294335),
    onSuccessContainer = Color(0xFFD8F0DF),
    warning = Color(0xFFE8C58F),
    onWarning = Color(0xFF412E12),
    warningContainer = Color(0xFF4C371D),
    onWarningContainer = Color(0xFFFFE6BC),
)

internal val CloudLightIdentityReferences = BestMatesIdentityReferencePalette(
    // Deepened from the #9367B2 visual anchor so white control labels meet WCAG AA.
    accent = Color(0xFF8F60AD),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFF1DDF6),
    onAccentContainer = Color(0xFF533461),
    atmosphereA = Color(0xFFF7CFDF),
    atmosphereB = Color(0xFFDDD0F3),
)

internal val CloudDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFDABCEA),
    onAccent = Color(0xFF3D2348),
    accentContainer = Color(0xFF4D3157),
    onAccentContainer = Color(0xFFF7DFFF),
    atmosphereA = Color(0xFFD88EAA),
    atmosphereB = Color(0xFF9A7AC5),
)

internal val DreamLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFA15D83),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFF6DDE9),
    onAccentContainer = Color(0xFF62344C),
    atmosphereA = Color(0xFFF3BFD3),
    atmosphereB = Color(0xFFDFC6F2),
)

internal val DreamDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFECB2CF),
    onAccent = Color(0xFF4C2643),
    accentContainer = Color(0xFF5C3048),
    onAccentContainer = Color(0xFFFFDDEA),
    atmosphereA = Color(0xFFC66F91),
    atmosphereB = Color(0xFF9C70BC),
)

internal val MidnightLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF745B91),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFE8DDF2),
    onAccentContainer = Color(0xFF49385E),
    atmosphereA = Color(0xFFD8CBE8),
    atmosphereB = Color(0xFFE7D3DC),
)

internal val MidnightDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFD3B8E8),
    onAccent = Color(0xFF352242),
    accentContainer = Color(0xFF493357),
    onAccentContainer = Color(0xFFF2DBFF),
    atmosphereA = Color(0xFF76568E),
    atmosphereB = Color(0xFF8C526C),
)

internal val MonoLightIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFF786871),
    onAccent = Color(0xFFFFFFFF),
    accentContainer = Color(0xFFEEE4E8),
    onAccentContainer = Color(0xFF494047),
    atmosphereA = Color(0xFFE8DADD),
    atmosphereB = Color(0xFFDFD8E2),
)

internal val MonoDarkIdentityReferences = BestMatesIdentityReferencePalette(
    accent = Color(0xFFCDBFC7),
    onAccent = Color(0xFF342C31),
    accentContainer = Color(0xFF443B41),
    onAccentContainer = Color(0xFFF1E8ED),
    atmosphereA = Color(0xFF8D737F),
    atmosphereB = Color(0xFF796F82),
)
