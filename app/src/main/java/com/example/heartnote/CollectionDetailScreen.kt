package com.example.heartnote

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun CollectionDetailScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel,
    collectionId: Int
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    val profile = viewModel.userProfile
    val errorMsg = viewModel.errorMessage

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getProfile(userId)
        viewModel.getCollections(userId)
        viewModel.getNotesByCollection(collectionId)

    }


    val collectionName = viewModel.collectionList
        .firstOrNull { it.collection_id == collectionId }
        ?.collection_name ?: ""





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
                    text = if (errorMsg.isNotEmpty()) errorMsg else "Loading...",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Column {

            Spacer(modifier = Modifier.height(120.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                        ,modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
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
                            text = collectionName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    showDeleteDialog = true

                                },
                            tint = Color.DarkGray
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFFF6B8A))
                                .clickable {
                                    navController.navigate(
                                        Screen.AddNoteCollection.createRoute(collectionId)
                                    )
                                }
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                        ) {

                            Text(
                                text = "+ Add note",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    val user_notes = viewModel.notesInCollection


                    if (user_notes.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Let’s write something ✨",
                                fontSize = 18.sp
                            )
                        }

                    }else{
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 120.dp)
                        ) {

                            items(
                                items = user_notes,
                                key = { it.note_id }
                            ) { item ->

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

                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            modifier = Modifier.size(20.dp)
                                                .clickable{
                                                viewModel.deleteNoteFromCollection(
                                                    item.note_id,
                                                    collectionId
                                                )
                                            },
                                            tint = Color.DarkGray
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
    if (showDeleteDialog) {

        androidx.compose.material3.AlertDialog(

            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text("Delete note")
            },

            text = {
                Text("Are you sure you want to delete this note?")
            },

            confirmButton = {

                Text(
                    text = "Delete",
                    color = Color.Red,
                    modifier = Modifier
                        .clickable {

                            viewModel.deleteCollection(
                                collectionId,
                                userId
                            )
                            navController.popBackStack()


                            showDeleteDialog = false
                        }
                        .padding(8.dp)
                )

            },

            dismissButton = {

                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .clickable {
                            showDeleteDialog = false
                        }
                        .padding(8.dp)
                )

            }

        )
    }
}