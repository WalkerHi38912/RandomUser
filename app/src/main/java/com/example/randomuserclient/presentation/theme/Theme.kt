package com.example.randomuserclient.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    secondary = BlueSecondaryDark,
    background = BlueBackgroundDark,
    surface = BlueSurfaceDark,
    error = BlueErrorDark,
    onPrimary = BlueOnPrimaryDark,
    onSecondary = BlueOnSecondaryDark,
    onBackground = BlueOnBackgroundDark,
    onSurface = BlueOnSurfaceDark,
    onError = BlueOnErrorDark
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimaryLight,
    secondary = BlueSecondaryLight,
    background = BlueBackgroundLight,
    surface = BlueSurfaceLight,
    error = BlueErrorLight,
    onPrimary = BlueOnPrimaryLight,
    onSecondary = BlueOnSecondaryLight,
    onBackground = BlueOnBackgroundLight,
    onSurface = BlueOnSurfaceLight,
    onError = BlueOnErrorLight
)

@Composable
fun RandomUserClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

         */

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}