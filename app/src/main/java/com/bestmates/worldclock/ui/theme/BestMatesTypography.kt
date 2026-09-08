package com.bestmates.worldclock.ui.theme

import android.graphics.Typeface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class BestMatesTypographyTokens(
    val material: Typography,
    val valueTime: TextStyle,
)

private val BestMatesRoundedUiFamily = FontFamily(
    Typeface.create("sans-serif-rounded", Typeface.NORMAL),
)
private val BestMatesSansUiFamily = FontFamily.SansSerif

private fun uiTextStyle(
    fontFamily: FontFamily,
    fontWeight: FontWeight,
    fontSize: Int,
    lineHeight: Int,
    fontFeatureSettings: String? = null,
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = 0.sp,
    fontFeatureSettings = fontFeatureSettings,
)

internal val DefaultBestMatesTypography = BestMatesTypographyTokens(
    material = Typography(
        displayLarge = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 36, 44),
        displayMedium = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 32, 40),
        displaySmall = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 28, 36),
        headlineLarge = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 26, 34),
        headlineMedium = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 24, 32),
        headlineSmall = uiTextStyle(BestMatesRoundedUiFamily, FontWeight.SemiBold, 21, 28),
        titleLarge = uiTextStyle(BestMatesSansUiFamily, FontWeight.SemiBold, 20, 26),
        titleMedium = uiTextStyle(BestMatesSansUiFamily, FontWeight.SemiBold, 16, 22),
        titleSmall = uiTextStyle(BestMatesSansUiFamily, FontWeight.SemiBold, 14, 20),
        bodyLarge = uiTextStyle(BestMatesSansUiFamily, FontWeight.Normal, 16, 24),
        bodyMedium = uiTextStyle(BestMatesSansUiFamily, FontWeight.Normal, 14, 21),
        bodySmall = uiTextStyle(BestMatesSansUiFamily, FontWeight.Normal, 12, 18),
        labelLarge = uiTextStyle(BestMatesSansUiFamily, FontWeight.Medium, 14, 18),
        labelMedium = uiTextStyle(BestMatesSansUiFamily, FontWeight.Medium, 12, 16),
        labelSmall = uiTextStyle(BestMatesSansUiFamily, FontWeight.Medium, 12, 16),
    ),
    valueTime = uiTextStyle(
        fontFamily = BestMatesSansUiFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24,
        lineHeight = 30,
        fontFeatureSettings = "tnum",
    ),
)
