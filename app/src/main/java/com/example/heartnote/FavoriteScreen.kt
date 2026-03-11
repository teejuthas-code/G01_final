package com.example.heartnote

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import coil.compose.AsyncImage


@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    val profile = viewModel.userProfile
    val errorMsg = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.getProfile(userId)
        viewModel.getNotesByUser(userId)
    }

    val favNotes = viewModel.noteList.filter { it.is_fav == 1 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF6991))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            profile?.let {

                AsyncImage(
                    model = "http://192.168.0.54/uploads/${it.profile_image}?v=${System.currentTimeMillis()}",
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )

                ProfileTextItem2(text = "Hello, ${it.nickname}")

            } ?: run {

                Text(
                    text = if (errorMsg.isNotEmpty()) errorMsg else "Loading..."
                )
            }
        }

        Column {

            Spacer(modifier = Modifier.height(120.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navController.popBackStack() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Favorite",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (favNotes.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "No favorite notes ⭐",
                                fontSize = 18.sp
                            )

                        }

                    } else {

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 120.dp)
                        ) {

                            items(favNotes) { item ->

                                val iconColor = item.background_color.toColor()
                                val bgColor = iconColor.copy(alpha = 0.2f)

                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .fillMaxWidth()
                                        .height(70.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = bgColor
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            painter = painterResource(id = R.drawable.document),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp),
                                            tint = iconColor
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = item.title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }
}