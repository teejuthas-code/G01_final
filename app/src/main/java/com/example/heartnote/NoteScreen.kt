package com.example.heartnote

import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun NoteScreen(navController: NavHostController, viewModel: HeartnoteViewModel) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    val profile = viewModel.userProfile
    val errorMsg = viewModel.errorMessage

    val lifecycleOwner = LocalLifecycleOwner.current

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())



    LaunchedEffect(Unit) {
        viewModel.getProfile(userId)
        viewModel.getAllNote()
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getAllNote()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF6991))
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            profile?.let {

                AsyncImage(
                    model = "http://10.0.2.2:3000/uploads/${it.profile_image}",
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

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),

            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp
            ),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            // TITLE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.wirte__1_),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "All Note",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            // ADD NOTE BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .clickable {
                        navController.navigate(Screen.Insert.route)
                    }
            ) {

                Image(
                    painter = painterResource(id = R.drawable.addnote),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),

                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.wirte__2_),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "New Note",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            //ข้อมูลลลล
            val user_notes = viewModel.noteList.filter {
                it.user_id == userId
            }

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

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {

                    items(
                        items = user_notes,
                        key = { it.note_id }
                    ) { item ->

                        val date = inputFormat.parse(item.create_at)
                        val formattedDate = outputFormat.format(date!!)

                        val iconColor = item.background_color.toColor()
                        val bgColor = iconColor.copy(alpha = 0.2f)

                        Card(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(180.dp)
                                .clickable {
                                    viewModel.setNoteForEdit(item)
                                    navController.navigate("edit_note_screen")
                                },

                            colors = CardDefaults.cardColors(
                                containerColor = bgColor
                            ),

                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .padding(30.dp)
                            ) {

                                Icon(
                                    painter = painterResource(id = R.drawable.document),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = iconColor
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = item.title,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        IconButton(
                                            onClick = {
                                                viewModel.starFavorite(item)
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {

                                            Icon(
                                                imageVector = if (item.is_fav == 1)
                                                    Icons.Default.Star
                                                else
                                                    Icons.Default.StarBorder,
                                                contentDescription = null,
                                                tint = if (item.is_fav == 1)
                                                    Color(0xFFFFC107)
                                                else
                                                    Color.Gray
                                            )
                                        }

                                    }

                                    Text(
                                        text = formattedDate,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Text(
                                    text = item.content,
                                    fontSize = 14.sp,
                                    maxLines = 2,
                                    color = Color.Gray
                                )
                            }
                        }

                    }
                }
            }


        }

//menuuuuuuuuuuuuuuuuuuuuu
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                )
                .fillMaxWidth()
                .height(70.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(40.dp),
                    ambientColor = Color(0x40FF6B9A),
                    spotColor = Color(0x40FF6B9A)
                )
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFFFFF3B0))
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                PrettyBottomIcon(
                    icon = R.drawable.wirte__2_,
                    route = Screen.Note.route,
                    navController = navController,
                    selected = true
                )

                PrettyBottomIcon(
                    icon = R.drawable.calender,
                    route = Screen.Planner.route,
                    navController = navController
                )

                PrettyBottomIcon(
                    icon = R.drawable.search,
                    route = Screen.SearchScreen.route,
                    navController = navController
                )

                PrettyBottomIcon(
                    icon = R.drawable.todo,
                    route = Screen.Todolist.route,
                    navController = navController
                )

                PrettyBottomIcon(
                    icon = R.drawable.user,
                    route = Screen.Profile.route,
                    navController = navController
                )
            }
        }

    }



}

@Composable
fun PrettyBottomIcon(
    icon: Int,
    route: String,
    navController: NavHostController,
    selected: Boolean = false
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            navController.navigate(route)
        }
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(
                    if (selected) Color(0xFFFFF3B0) // เหลืองด้านใน
                    else Color.Transparent
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color(0xFFFF6B9A),
                modifier = Modifier.size(24.dp)
            )
        }

        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(22.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF6B9A))
            )
        }
    }
}

fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color(0xFFFFE4EC)
    }
}

@Composable
fun ProfileTextItem2(text: String) {

    Text(
        text = text,
        fontSize = 26.sp,
        modifier = Modifier.padding(start = 12.dp),
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}