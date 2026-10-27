package hu.keszletbarat.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import hu.keszletbarat.app.data.AppColorPalette
import hu.keszletbarat.app.data.ThemeMode

private val EmeraldLightColors = lightColorScheme(
    primary = Color(0xFF006C4C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF89F8C7),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4D6357),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFE9D9),
    onSecondaryContainer = Color(0xFF092016),
    tertiary = Color(0xFF3D6373),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC1E8FB),
    onTertiaryContainer = Color(0xFF001F29)
)

private val EmeraldDarkColors = darkColorScheme(
    primary = Color(0xFF6CDBAC),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF005138),
    onPrimaryContainer = Color(0xFF89F8C7),
    secondary = Color(0xFFB3CCBE),
    onSecondary = Color(0xFF1F352A),
    secondaryContainer = Color(0xFF354B40),
    onSecondaryContainer = Color(0xFFCFE9D9),
    tertiary = Color(0xFFA5CCDF),
    onTertiary = Color(0xFF073543),
    tertiaryContainer = Color(0xFF244C5B),
    onTertiaryContainer = Color(0xFFC1E8FB)
)

private val OceanLightColors = lightColorScheme(
    primary = Color(0xFF0061A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF535F70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7E3F7),
    onSecondaryContainer = Color(0xFF101C2B),
    tertiary = Color(0xFF6B5778),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431)
)

private val OceanDarkColors = darkColorScheme(
    primary = Color(0xFF9ECAFF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFFBBC7DB),
    onSecondary = Color(0xFF253140),
    secondaryContainer = Color(0xFF3B4858),
    onSecondaryContainer = Color(0xFFD7E3F7),
    tertiary = Color(0xFFD6BEE4),
    onTertiary = Color(0xFF3B2948),
    tertiaryContainer = Color(0xFF523F5F),
    onTertiaryContainer = Color(0xFFF2DAFF)
)

private val SunsetLightColors = lightColorScheme(
    primary = Color(0xFF9C4146),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDAD9),
    onPrimaryContainer = Color(0xFF400008),
    secondary = Color(0xFF775656),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDAD9),
    onSecondaryContainer = Color(0xFF2C1515),
    tertiary = Color(0xFF755A2F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDDAF),
    onTertiaryContainer = Color(0xFF281800)
)

private val SunsetDarkColors = darkColorScheme(
    primary = Color(0xFFFFB3B4),
    onPrimary = Color(0xFF5F131B),
    primaryContainer = Color(0xFF7E292F),
    onPrimaryContainer = Color(0xFFFFDAD9),
    secondary = Color(0xFFE6BDBD),
    onSecondary = Color(0xFF442929),
    secondaryContainer = Color(0xFF5D3F3F),
    onSecondaryContainer = Color(0xFFFFDAD9),
    tertiary = Color(0xFFE5C18D),
    onTertiary = Color(0xFF422C05),
    tertiaryContainer = Color(0xFF5B431A),
    onTertiaryContainer = Color(0xFFFFDDAF)
)

private val LavenderLightColors = lightColorScheme(
    primary = Color(0xFF6B538C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEDDCFF),
    onPrimaryContainer = Color(0xFF250E44),
    secondary = Color(0xFF645A70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEBDDF7),
    onSecondaryContainer = Color(0xFF20182A),
    tertiary = Color(0xFF7F525B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD9DF),
    onTertiaryContainer = Color(0xFF321019)
)

private val LavenderDarkColors = darkColorScheme(
    primary = Color(0xFFD6BAFF),
    onPrimary = Color(0xFF3B255A),
    primaryContainer = Color(0xFF523C72),
    onPrimaryContainer = Color(0xFFEDDCFF),
    secondary = Color(0xFFCEC2DB),
    onSecondary = Color(0xFF352D40),
    secondaryContainer = Color(0xFF4C4357),
    onSecondaryContainer = Color(0xFFEBDDF7),
    tertiary = Color(0xFFF1B7C2),
    onTertiary = Color(0xFF4B252E),
    tertiaryContainer = Color(0xFF643B44),
    onTertiaryContainer = Color(0xFFFFD9DF)
)

private val RoseLightColors = lightColorScheme(
    primary = Color(0xFF984061),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFD9E2),
    onPrimaryContainer = Color(0xFF3E001D),
    secondary = Color(0xFF74565F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9E2),
    onSecondaryContainer = Color(0xFF2B151C),
    tertiary = Color(0xFF7C5635),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDCC1),
    onTertiaryContainer = Color(0xFF2E1500)
)

private val RoseDarkColors = darkColorScheme(
    primary = Color(0xFFFFB1C8),
    onPrimary = Color(0xFF5E1133),
    primaryContainer = Color(0xFF7B2949),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = Color(0xFFE3BDC6),
    onSecondary = Color(0xFF422931),
    secondaryContainer = Color(0xFF5A3F47),
    onSecondaryContainer = Color(0xFFFFD9E2),
    tertiary = Color(0xFFEFBD94),
    onTertiary = Color(0xFF48290B),
    tertiaryContainer = Color(0xFF623F20),
    onTertiaryContainer = Color(0xFFFFDCC1)
)

@Composable
fun KeszletBaratTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    colorPalette: AppColorPalette = AppColorPalette.DYNAMIC,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val useDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme: ColorScheme = when (colorPalette) {
        AppColorPalette.DYNAMIC -> {
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        AppColorPalette.EMERALD -> if (useDarkTheme) EmeraldDarkColors else EmeraldLightColors
        AppColorPalette.OCEAN -> if (useDarkTheme) OceanDarkColors else OceanLightColors
        AppColorPalette.SUNSET -> if (useDarkTheme) SunsetDarkColors else SunsetLightColors
        AppColorPalette.LAVENDER -> if (useDarkTheme) LavenderDarkColors else LavenderLightColors
        AppColorPalette.ROSE -> if (useDarkTheme) RoseDarkColors else RoseLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
