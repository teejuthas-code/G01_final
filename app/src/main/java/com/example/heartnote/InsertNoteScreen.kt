package com.example.heartnote

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun InsertNoteScreen(navController: NavHostController,viewModel: HeartnoteViewModel) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF2F5))
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // Top Bar
            Card(
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)
                ),
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {

                            if (title.isBlank() && content.isBlank()) {

                                navController.popBackStack()

                            } else {

                                val finalTitle = if (title.isBlank()) "Untitled" else title
                                val finalContent = if (content.isBlank()) "" else content

                                val note = Note(
                                    user_id = userId,
                                    title = finalTitle,
                                    content = finalContent,
                                    background_color = viewModel.backgroundColorText
                                )

                                viewModel.insertNote(note) {}

                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "My Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Card
            Card(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxSize(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {

                    // Title Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.document),
                            contentDescription = null,
                            tint = Color(0xFFFF6B8A),
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = title,
                            onValueChange = {title = it},
                            placeholder = { Text("Title") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.Gray
                            )
                        )

                        Row {

                            ColorCircle(
                                color = Color(0xFFFF6B8A),
                                hex = "#FF6B8A",
                                selectedColor = viewModel.backgroundColorText
                            ) {
                                viewModel.backgroundColorText = it
                            }

                            ColorCircle(
                                color = Color(0xFF4A90E2),
                                hex = "#4A90E2",
                                selectedColor = viewModel.backgroundColorText
                            ) {
                                viewModel.backgroundColorText = it
                            }

                            ColorCircle(
                                color = Color(0xFFFFB74D),
                                hex = "#FFB74D",
                                selectedColor = viewModel.backgroundColorText
                            ) {
                                viewModel.backgroundColorText = it
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Content
                    TextField(
                        value = content,
                        onValueChange = {content = it},
                        placeholder = {
                            Text("Add your words or something....")
                        },
                        modifier = Modifier
                            .fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ColorCircle(
    color: Color,
    hex: String,
    selectedColor: String,
    onClick: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(18.dp)
            .clip(CircleShape)
            .background(color)
            .clickable {
                onClick(hex)
            }
            .then(
                if (selectedColor == hex)
                    Modifier.border(3.dp, Color.White, CircleShape)
                else
                    Modifier
            )
    )
}