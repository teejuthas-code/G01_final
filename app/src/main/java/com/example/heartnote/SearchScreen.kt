package com.example.heartnote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Locale

import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.draw.shadow
import java.util.Date

fun parseDate(time: String?): Date? {

    if (time.isNullOrEmpty()) return null

    val formats = listOf(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )

    for (pattern in formats) {
        try {

            val inputFormat = SimpleDateFormat(pattern, Locale.US)
            return inputFormat.parse(time)

        } catch (e: Exception) { }
    }

    return null
}
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)

    val userId = sharedPref.getSavedUserId()
    val username = sharedPref.getSavedUsername() ?: "User"


    val items = viewModel.todoList
    val notes = viewModel.noteList

    val planners = viewModel.plannerList.toList()
    var searchQuery by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())

    LaunchedEffect(userId) {
        viewModel.currentUserId = userId

        viewModel.fetchTasks(userId)
        viewModel.getNotesByUser(userId)
        viewModel.getPlanner(userId)
    }


    val filteredItems = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        items.filter {

            it.taskText.contains(searchQuery, ignoreCase = true) ||

                    (it.description?.contains(searchQuery, ignoreCase = true) ?: false)

        }
    }
    val filteredNotes = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        notes.filter {

            it.title.contains(searchQuery, ignoreCase = true) ||

                    it.content.contains(searchQuery, ignoreCase = true)

        }
    }
    val filteredPlanners = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        planners.filter {

            it.planner_content.contains(searchQuery, ignoreCase = true) ||

                    it.event_start.substringBefore("T")
                        .contains(searchQuery, ignoreCase = true) ||

                    it.event_end.substringBefore("T")
                        .contains(searchQuery, ignoreCase = true)

        }
    }

    val plannerWithTime = planners
        .filter {
            it.event_start.isNotBlank() &&
                    it.is_done == 0
        }
        .sortedBy { parseDate(it.event_start) }

    val todoWithTime = items
        .filter {
            it.reminderAt != null &&
                    it.reminderAt!!.isNotBlank() &&
                    it.isDone == 0
        }
        .sortedBy { parseDate(it.reminderAt) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEEF3))
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(Color(0xFFFF6B8D))
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.resource_default),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            "Hello, $username",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )


                    }

                }

            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-30).dp),
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                color = Color.White
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.search), // รูป icon
                            contentDescription = "Search",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Search",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.clickable {
                                showNotification = false
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { showNotification = !showNotification }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notification"
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(15.dp))


                    if (!showNotification){OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, null)
                        }
                    )}

                    if (showNotification) {

                        LazyColumn {

                            item {
                                Text(
                                    "Reminder",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            if (plannerWithTime.isNotEmpty()) {

                                item {
                                    Text(
                                        "Planner",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }

                                items(plannerWithTime) { planner ->

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFFFF0F5)
                                        )
                                    ) {

                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {

                                            Text(
                                                planner.planner_content,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text(
                                                formatDisplayTime(planner.event_start),
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )

                                        }

                                    }

                                }

                            }

                            if (todoWithTime.isNotEmpty()) {

                                item {
                                    Text(
                                        "Todo",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }

                                items(todoWithTime) { todo ->

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFFFF0F5)
                                        )
                                    ) {

                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {

                                            Text(
                                                todo.taskText,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text(
                                                formatDisplayTime(todo.reminderAt),
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )

                                        }

                                    }

                                }

                            }

                        }

                    }

                    when {
                        showNotification -> {}

                        searchQuery.isBlank() -> {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp),
                                        tint = Color.LightGray
                                    )

                                    Text(
                                        "Start typing to search",
                                        color = Color.Gray
                                    )

                                }

                            }

                        }

                        filteredItems.isEmpty()&&
                                filteredNotes.isEmpty() &&
                                filteredPlanners.isEmpty() -> {

                            EmptySearchState()

                        }

                        else -> {

                            LazyColumn(
                                contentPadding = PaddingValues(bottom = 100.dp)
                            ) {

                                if (filteredNotes.isNotEmpty()) {

                                    item {
                                        Text(
                                            "Notes",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    items(filteredNotes) { note ->

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp)
                                                .clickable {

                                                    viewModel.setNoteForEdit(note)

                                                    navController.navigate("edit_note_screen")

                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFFFF0F5)
                                            )
                                        ) {

                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {

                                                Text(
                                                    note.title,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    note.content,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )

                                            }

                                        }

                                    }

                                }

                                if (filteredItems.isNotEmpty()) {

                                    item {
                                        Text(
                                            "Todo",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    items(filteredItems) { item ->

                                        TodoCard(
                                            item = item,
                                            viewModel = viewModel,
                                            navController = navController
                                        )

                                    }

                                }
                                if (filteredPlanners.isNotEmpty()) {

                                    item {
                                        Text(
                                            "Planner",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    items(filteredPlanners) { planner ->

                                        val date_start = inputFormat.parse(planner.event_start)
                                        val date_end = inputFormat.parse(planner.event_end)
                                        val formattedDate_start = outputFormat.format(date_start!!)
                                        val formattedDate_end = outputFormat.format(date_end!!)

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFFFF0F5)
                                            )
                                        ) {

                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {

                                                Text(
                                                    planner.planner_content,
                                                    fontWeight = FontWeight.Bold
                                                )



                                                Text(
                                                    "Start:" + formattedDate_start,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )

                                                Text(
                                                    "End: "+ formattedDate_end,
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
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
                    icon = R.drawable.search2,
                    route = Screen.SearchScreen.route,
                    navController = navController,
                    selected = true
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
fun EmptySearchState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )

        Text(
            "No task found",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

    }

}