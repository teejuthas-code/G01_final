package com.example.heartnote

import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import android.graphics.Color.parseColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
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
fun CollectionScreen(navController: NavHostController, viewModel: HeartnoteViewModel) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    val profile = viewModel.userProfile
    val errorMsg = viewModel.errorMessage

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.getProfile(userId)
        viewModel.getAllNote()
        viewModel.getCollections(userId)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )

                Icon(
                    painter = painterResource(id = R.drawable.collection),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "My Collections",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 ช่องต่อแถว
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 120.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {


                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.collectionadd),
                            contentDescription = null,
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { navController.navigate(Screen.AddCollection.route) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                item {

                    val favNotes = viewModel.noteList.filter { it.is_fav == 1 }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    navController.navigate(Screen.Favorite.route)
                                },
                            shape = RoundedCornerShape(20.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFFFD54F)),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(50.dp)
                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Favorite",
                            modifier = Modifier.width(140.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }



                items(viewModel.collectionList) { item ->

                    CollectionCard(item,navController)

                }
            }


        }



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
                    icon = R.drawable.wirte__1_,
                    route = Screen.Note.route,
                    navController = navController

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
                    icon = R.drawable.user2,
                    route = Screen.Profile.route,
                    navController = navController,
                    selected = true
                )
            }
        }

    }
}

@Composable
fun CollectionCard(item: CollectionClass, navController: NavHostController) {

    val color = parseColor(item.background_color)

    val imageRes = when (item.background_color) {
        "#FF6991" -> R.drawable.pinkbook
        "#4698E5" -> R.drawable.bluebook
        "#FFB74D" -> R.drawable.orangebook
        else -> R.drawable.pinkbook
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    navController.navigate(
                        Screen.CollectionDetail.createRoute(item.collection_id)
                    )
                },
            shape = RoundedCornerShape(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(color)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null
                )

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.collection_name,
            modifier = Modifier
                .width(140.dp)
                .padding(start = 6.dp),
            textAlign = TextAlign.Start
        )
    }
}