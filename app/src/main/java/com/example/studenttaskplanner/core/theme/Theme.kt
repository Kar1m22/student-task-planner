package com.example.studenttaskplanner.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightPrimary,
    onSecondary = LightOnPrimary,
    secondaryContainer = LightPrimaryContainer,
    onSecondaryContainer = LightOnPrimaryContainer,
    tertiary = LightSuccess,
    onTertiary = Color.White,
    tertiaryContainer = LightSuccessContainer,
    onTertiaryContainer = LightOnSuccessContainer,
    background = LightBackground,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightSecondaryText,
    surfaceTint = LightPrimary,
    outline = LightOutline,
    outlineVariant = LightDivider,
    error = LightError,
    onError = Color.White,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    inverseSurface = DarkSurface,
    inverseOnSurface = DarkText,
    inversePrimary = DarkPrimary
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkPrimary,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkPrimaryContainer,
    onSecondaryContainer = DarkOnPrimaryContainer,
    tertiary = DarkSuccess,
    onTertiary = DarkOnSuccessContainer,
    tertiaryContainer = DarkSuccessContainer,
    onTertiaryContainer = DarkOnSuccessContainer,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkSecondaryText,
    surfaceTint = DarkPrimary,
    outline = DarkOutline,
    outlineVariant = DarkDivider,
    error = DarkError,
    onError = DarkOnErrorContainer,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    inverseSurface = LightSurface,
    inverseOnSurface = LightText,
    inversePrimary = LightPrimary
)

@Immutable
data class PlannerStatusColors(
    val completed: Color,
    val overdue: Color
)

private val LocalPlannerStatusColors = staticCompositionLocalOf {
    PlannerStatusColors(
        completed = LightSuccess,
        overdue = LightError
    )
}

object PlannerTheme {
    val statusColors: PlannerStatusColors
        @Composable
        get() = LocalPlannerStatusColors.current
}

private val PlannerShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun StudentTaskPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val statusColors = if (darkTheme) {
        PlannerStatusColors(completed = DarkSuccess, overdue = DarkError)
    } else {
        PlannerStatusColors(completed = LightSuccess, overdue = LightError)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = PlannerTypography,
        shapes = PlannerShapes,
        content = content
    )
}