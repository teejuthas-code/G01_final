package com.example.heartnote

import android.R.attr.font
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heartnote.FontItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeFontScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel = viewModel()
) {

    var selectedTheme by remember { mutableStateOf("Pink") }
    val font by themeViewModel.font.collectAsState()

    val bgColor = when (selectedTheme) {
        "Pink" -> Color(0xFFFFF2F5)
        "Purple" -> Color(0xFFEFD3FF)
        "Blue" -> Color(0xFFBFE0FF)
        "Green" -> Color(0xFF83EECA)
        else -> Color(0xFFFFF2F5)
    }
    val themeColor = when (selectedTheme) {
        "Pink" -> Color(0xFFFF6B9A)
        "Purple" -> Color(0xFFD6B3E6)
        "Blue" -> Color(0xFFA9C8E8)
        "Green" -> Color(0xFF7ED6B2)
        else -> Color(0xFFFF6B9A)
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Theme&Font",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeColor
                )
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Theme Color",
                modifier = Modifier.padding(start = 16.dp)
            )

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    ThemeItem("Pink", Color(0xFFFF6B9A), selectedTheme) {
                        selectedTheme = "Pink"
                        themeViewModel.setTheme("Pink")
                    }

                    ThemeItem("Purple", Color(0xFFD6B3E6), selectedTheme) {
                        selectedTheme = "Purple"
                        themeViewModel.setTheme("Purple")
                    }

                    ThemeItem("Blue", Color(0xFFA9C8E8), selectedTheme) {
                        selectedTheme = "Blue"
                        themeViewModel.setTheme("Blue")
                    }

                    ThemeItem("Green", Color(0xFF7ED6B2), selectedTheme) {
                        selectedTheme = "Green"
                        themeViewModel.setTheme("Green")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Font",
                modifier = Modifier.padding(start = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                FontItem("Default", font) {
                    themeViewModel.setFont("Default")
                }

                FontItem("PoetsenOne", font) {
                    themeViewModel.setFont("PoetsenOne")
                }

                FontItem("ComicReliefFont", font) {
                    themeViewModel.setFont("ComicReliefFont")
                }
            }
        }
    }
}

@Composable
fun ThemeItem(
    name: String,
    color: Color,
    selectedTheme: String,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {

            if (selectedTheme == name) Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            fontSize = 12.sp,
            color = if (selectedTheme == name) Color(0xFFFF6B9A) else Color.Gray
        )
    }
}

@Composable
fun FontItem(
    name: String,
    selectedFont: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .size(100.dp, 70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = if (selectedFont == name)
            BorderStroke(2.dp, Color(0xFFFF6B9A))
        else null
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Aa",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                name,
                fontSize = 12.sp
            )
        }
    }
}