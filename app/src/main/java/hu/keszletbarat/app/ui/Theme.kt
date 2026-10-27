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

private val GoldLightColors = lightColorScheme(
    primary = Color(0xFF785900),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDF9E),
    onPrimaryContainer = Color(0xFF261A00),
    secondary = Color(0xFF6B5D3F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF5E0BB),
    onSecondaryContainer = Color(0xFF241A04),
    tertiary = Color(0xFF4A6547),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCCEBC4),
    onTertiaryContainer = Color(0xFF072109)
)

private val GoldDarkColors = darkColorScheme(
    primary = Color(0xFFFABD00),
    onPrimary = Color(0xFF3F2E00),
    primaryContainer = Color(0xFF5B4300),
    onPrimaryContainer = Color(0xFFFFDF9E),
    secondary = Color(0xFFD8C4A0),
    onSecondary = Color(0xFF3B2F15),
    secondaryContainer = Color(0xFF52452A),
    onSecondaryContainer = Color(0xFFF5E0BB),
    tertiary = Color(0xFFB0CFAA),
    onTertiary = Color(0xFF1D361C),
    tertiaryContainer = Color(0xFF334D31),
    onTertiaryContainer = Color(0xFFCCEBC4)
)

private val TealLightColors = lightColorScheme(
    primary = Color(0xFF006874),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF97F0FF),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF4A6267),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDE7EC),
    onSecondaryContainer = Color(0xFF051F23),
    tertiary = Color(0xFF525E7D),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDAE2FF),
    onTertiaryContainer = Color(0xFF0E1B37)
)

private val TealDarkColors = darkColorScheme(
    primary = Color(0xFF4FD8EB),
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = Color(0xFFB1CBD0),
    onSecondary = Color(0xFF1C3438),
    secondaryContainer = Color(0xFF334B4F),
    onSecondaryContainer = Color(0xFFCDE7EC),
    tertiary = Color(0xFFBAC6EA),
    onTertiary = Color(0xFF24304D),
    tertiaryContainer = Color(0xFF3B4664),
    onTertiaryContainer = Color(0xFFDAE2FF)
)

private val IndigoLightColors = lightColorScheme(
    primary = Color(0xFF4355B9),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDEE0FF),
    onPrimaryContainer = Color(0xFF00105C),
    secondary = Color(0xFF5B5D72),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE0E1F9),
    onSecondaryContainer = Color(0xFF181A2C),
    tertiary = Color(0xFF77536D),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD7F1),
    onTertiaryContainer = Color(0xFF2D1228)
)

private val IndigoDarkColors = darkColorScheme(
    primary = Color(0xFFBAC3FF),
    onPrimary = Color(0xFF08218A),
    primaryContainer = Color(0xFF293CA0),
    onPrimaryContainer = Color(0xFFDEE0FF),
    secondary = Color(0xFFC3C5DD),
    onSecondary = Color(0xFF2D2F42),
    secondaryContainer = Color(0xFF434659),
    onSecondaryContainer = Color(0xFFE0E1F9),
    tertiary = Color(0xFFE6BAD7),
    onTertiary = Color(0xFF44263D),
    tertiaryContainer = Color(0xFF5D3C55),
    onTertiaryContainer = Color(0xFFFFD7F1)
)

private val ForestLightColors = lightColorScheme(
    primary = Color(0xFF2E6C00),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA6F779),
    onPrimaryContainer = Color(0xFF092100),
    secondary = Color(0xFF55624C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD9E7CB),
    onSecondaryContainer = Color(0xFF131F0D),
    tertiary = Color(0xFF386666),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBBEBEB),
    onTertiaryContainer = Color(0xFF002020)
)

private val ForestDarkColors = darkColorScheme(
    primary = Color(0xFF8BDA60),
    onPrimary = Color(0xFF153800),
    primaryContainer = Color(0xFF215100),
    onPrimaryContainer = Color(0xFFA6F779),
    secondary = Color(0xFFBDCBAF),
    onSecondary = Color(0xFF283420),
    secondaryContainer = Color(0xFF3E4A35),
    onSecondaryContainer = Color(0xFFD9E7CB),
    tertiary = Color(0xFFA0CFCF),
    onTertiary = Color(0xFF003737),
    tertiaryContainer = Color(0xFF1E4E4E),
    onTertiaryContainer = Color(0xFFBBEBEB)
)

private val CoffeeLightColors = lightColorScheme(
    primary = Color(0xFF705845),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFBDAC2),
    onPrimaryContainer = Color(0xFF271708),
    secondary = Color(0xFF665C53),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEEE0D5),
    onSecondaryContainer = Color(0xFF211A13),
    tertiary = Color(0xFF596239),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDDE8B3),
    onTertiaryContainer = Color(0xFF171E00)
)

private val CoffeeDarkColors = darkColorScheme(
    primary = Color(0xFFDEC0A7),
    onPrimary = Color(0xFF3E2B1B),
    primaryContainer = Color(0xFF574130),
    onPrimaryContainer = Color(0xFFFBDAC2),
    secondary = Color(0xFFD1C4B9),
    onSecondary = Color(0xFF372F27),
    secondaryContainer = Color(0xFF4E453D),
    onSecondaryContainer = Color(0xFFEEE0D5),
    tertiary = Color(0xFFC1CC99),
    onTertiary = Color(0xFF2C340F),
    tertiaryContainer = Color(0xFF424A23),
    onTertiaryContainer = Color(0xFFDDE8B3)
)

private val GraphiteLightColors = lightColorScheme(
    primary = Color(0xFF45474A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8DADD),
    onPrimaryContainer = Color(0xFF16181A),
    secondary = Color(0xFF5C5E61),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE1E2E5),
    onSecondaryContainer = Color(0xFF191B1E),
    tertiary = Color(0xFF505E6B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD4E4F2),
    onTertiaryContainer = Color(0xFF0D1B27)
)

private val GraphiteDarkColors = darkColorScheme(
    primary = Color(0xFFC4C6CA),
    onPrimary = Color(0xFF2C2F32),
    primaryContainer = Color(0xFF434548),
    onPrimaryContainer = Color(0xFFE0E2E6),
    secondary = Color(0xFFC5C6C9),
    onSecondary = Color(0xFF2E3033),
    secondaryContainer = Color(0xFF44474A),
    onSecondaryContainer = Color(0xFFE1E2E5),
    tertiary = Color(0xFFB8C8D6),
    onTertiary = Color(0xFF22323E),
    tertiaryContainer = Color(0xFF394855),
    onTertiaryContainer = Color(0xFFD4E4F2)
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
        AppColorPalette.GOLD -> if (useDarkTheme) GoldDarkColors else GoldLightColors
        AppColorPalette.TEAL -> if (useDarkTheme) TealDarkColors else TealLightColors
        AppColorPalette.INDIGO -> if (useDarkTheme) IndigoDarkColors else IndigoLightColors
        AppColorPalette.FOREST -> if (useDarkTheme) ForestDarkColors else ForestLightColors
        AppColorPalette.COFFEE -> if (useDarkTheme) CoffeeDarkColors else CoffeeLightColors
        AppColorPalette.GRAPHITE -> if (useDarkTheme) GraphiteDarkColors else GraphiteLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
