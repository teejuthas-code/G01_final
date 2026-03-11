package com.example.heartnote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()


    var showLogoutDialog by remember { mutableStateOf(false) }
    var checkedState by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getProfile(userId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        val context = LocalContext.current
        val sharedPref = SharedPreferencesManager(context)
        val userId = sharedPref.getSavedUserId()

        val profile = viewModel.userProfile
        val errorMsg = viewModel.errorMessage

        var showLogoutDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.getProfile(userId)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7E9EC))
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(Color(0xFFFF6B8A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Me",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-40).dp),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .padding(bottom = 80.dp)
                    ) {

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(R.drawable.resource_default),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(Color.LightGray, CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text(
                                    text = profile?.nickname ?: "User",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Organize your life",
                                    fontSize = 12.sp
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Menu
                        MenuItem("Theme & Font", R.drawable.theme) { navController.navigate(Screen.ThemeFont.route)}

                        MenuItem("Favorite", R.drawable.star) {navController.navigate(Screen.Favorite.route) }

                        MenuItem("My collection", R.drawable.mycol) {navController.navigate(Screen.Collection.route) }

                        MenuItem("Trash", R.drawable.binn) {navController.navigate(Screen.Trash.route) }

                        MenuItem("About app", R.drawable.info) { navController.navigate(Screen.About.route)}

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B8A)
                            )
                        ) {
                            Text(
                                text = "Logout",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }

                    }



                }

            }

            if (showLogoutDialog) {

                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Confirm Logout") },
                    text = { Text("Do you want to sign out?") },
                    confirmButton = {

                        Button(
                            onClick = {

                                sharedPref.logout(false)
                                viewModel.resetLoginResult()

                                navController.navigate(Screen.Login.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }

                            }
                        ) {
                            Text("Logout")
                        }

                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )

            }

        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
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


    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = {
                Column {
                    Text("Do you want to sign out?")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = { checkedState = it }
                        )
                        Text("Remember my Username")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sharedPref.logout(rememberId = checkedState)
                        viewModel.resetLoginResult()
                        showLogoutDialog = false

                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileTextItem(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun MenuItem(
    title: String,
    icon: Int,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFFFF6B8A),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.go),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

    }
}