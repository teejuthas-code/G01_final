package com.example.heartnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.heartnote.ui.theme.HeartNoteTheme

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createChannel(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        enableEdgeToEdge()
        setContent {
            HeartNoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MyScreen(modifier: Modifier = Modifier){

    val navController = rememberNavController()
    val heartnoteViewModel: HeartnoteViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    val theme by themeViewModel.theme.collectAsState()
    val font by themeViewModel.font.collectAsState()

    HeartNoteTheme(theme, font) {

        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            NavGraph(
                navController = navController,
                viewModel = heartnoteViewModel
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HeartNoteTheme {
        MyScreen()
    }
}