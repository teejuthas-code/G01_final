package com.example.heartnote

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography
import com.example.heartnote.R

val PoetsenOneFont = FontFamily(
    Font(R.font.poetsenone_regula)
)

val DefaultFont = FontFamily.Default
val ComicReliefFont = FontFamily(
    Font(R.font.comicrelief_regular)
)

@Composable
fun HeartNoteTheme(
    theme: String,
    font: String,
    content: @Composable () -> Unit
) {

    val color = when (theme) {
        "Pink" -> Color(0xFFFF95B1)
        "Purple" -> Color(0xFFD6B3E6)
        "Blue" -> Color(0xFFA9C8E8)
        "Green" -> Color(0xFF7ED6B2)
        else -> Color(0xFFFF95B1)
    }

    val fontFamily = when (font) {
        "PoetsenOne" -> PoetsenOneFont
        else -> DefaultFont
    }

    val typography = Typography(
        bodyLarge = TextStyle(fontFamily = fontFamily),
        titleLarge = TextStyle(fontFamily = fontFamily),
        bodyMedium = TextStyle(fontFamily=fontFamily),
        titleMedium = TextStyle(fontFamily=fontFamily),
        labelLarge = TextStyle(fontFamily=fontFamily)
    )

    MaterialTheme(
        colorScheme = lightColorScheme(primary = color),
        typography = typography
    ) {
        content()
    }
}