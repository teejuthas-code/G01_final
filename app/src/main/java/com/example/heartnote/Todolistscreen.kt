package com.example.heartnote

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController

import java.text.SimpleDateFormat
import java.util.*

fun formatDisplayTime(time: String?): String {
    if (time.isNullOrEmpty()) return ""

    val formats = listOf(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )

    // ฟอร์แมตที่จะแสดงผลบนหน้าจอ (เวลาไทย)
    val outputFormat = SimpleDateFormat("dd/MMM/yyyy • HH:mm", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getDefault() // มั่นใจว่าใช้เวลาเครื่อง

    for (pattern in formats) {
        try {
            val inputFormat = SimpleDateFormat(pattern, Locale.US)


            if (pattern.contains("'Z'")) {
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            } else {
                inputFormat.timeZone = TimeZone.getDefault()
            }

            val date = inputFormat.parse(time)
            if (date != null) {
                return outputFormat.format(date)
            }
        } catch (e: Exception) { }
    }
    return time
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)

    val userId = sharedPref.getSavedUserId()
    val username = sharedPref.getSavedUsername() ?: "User"

    val items = viewModel.todoList
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.fetchTasks(userId)
    }

    val filteredItems = items.filter {
        it.taskText?.contains(searchQuery, ignoreCase = true) == true
    }

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
                        painter = painterResource(id = R.drawable.todo),
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp)
                    )
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            "My List",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.weight(1f))



                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, null)
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            navController.navigate("Addtodolist_screen")
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B8D)
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Text("+ Add List")

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (filteredItems.isEmpty()) {

                        EmptyState()

                    } else {

                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {

                            items(filteredItems) { item ->

                                TodoCard(
                                    item = item,
                                    viewModel = viewModel,
                                    navController = navController
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
                    icon = R.drawable.to_do_list_2,
                    route = Screen.Todolist.route,
                    navController = navController,
                    selected = true
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    item: TodoItem,
    viewModel: HeartnoteViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current

    var showMenu by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }





    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                navController.navigate("detaillist/${item.todoListId}")
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F5)
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = item.isDone == 1,
                onCheckedChange = {

                    viewModel.updateTodo(
                        item.todoListId,
                        item.copy(isDone = if (item.isDone == 1) 0 else 1)
                    )

                    viewModel.fetchTasks(item.userId)

                }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    item.taskText,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    item.description ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (item.reminderAt != null) {

                    Text(
                        formatDisplayTime(item.reminderAt),
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B8D)
                    )

                }



            }

            Box {

                IconButton(
                    onClick = { showMenu = true }
                ) {
                    Icon(Icons.Default.MoreVert, null)
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            showEdit = true
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Edit, null)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            showDelete = true
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, null)
                        }
                    )

                }

            }

        }

    }



    if (showEdit) {
        EditTodoDialog(item, viewModel) {
            showEdit = false
        }
    }

    if (showDelete) {
        DeleteDialog(item, viewModel) {
            showDelete = false
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoDialog(
    item: TodoItem,
    viewModel: HeartnoteViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(item.taskText) }
    var description by remember { mutableStateOf(item.description ?: "") }
    var reminderAt by remember { mutableStateOf(item.reminderAt) }

    var showDatePicker by remember { mutableStateOf(false) }
    val dbFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    showDatePicker = false

                    // เมื่อเลือกวันที่เสร็จ ให้เปิด TimePicker แบบ Spinner ทันที
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selectedDate

                    android.app.TimePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar, // ใช้ Style นี้เพื่อให้เป็น Spinner
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                            reminderAt = dbFormat.format(calendar.time)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).apply {
                        window?.setBackgroundDrawableResource(android.R.color.transparent)
                    }.show()
                }) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Todo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                    OutlinedTextField(
                        value = if (reminderAt != null) formatDisplayTime(reminderAt) else "Select date & time",
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text("Reminder") },
                        leadingIcon = { Icon(Icons.Default.DateRange, null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.updateTodo(
                    item.todoListId,
                    item.copy(taskText = title, description = description, reminderAt = reminderAt)
                )
                viewModel.fetchTasks(item.userId)
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@Composable
fun DeleteDialog(
    item: TodoItem,
    viewModel: HeartnoteViewModel,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Todo") },

        text = {
            Text("Delete '${item.taskText}' ?")
        },

        confirmButton = {

            Button(onClick = {

                viewModel.deleteTodo(item)
                viewModel.fetchTasks(item.userId)
                onDismiss()

            }) {

                Text("Delete")

            }

        },

        dismissButton = {

            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }

        }

    )

}

@Composable
fun EmptyState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )

        Text(
            "Let's add your first List ( ^ ◡ ^ )",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

    }

}

