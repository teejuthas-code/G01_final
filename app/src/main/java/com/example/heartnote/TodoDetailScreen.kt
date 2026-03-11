package com.example.heartnote

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    todoId: Int,
    viewModel: HeartnoteViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val item = viewModel.todoList.find { it.todoListId == todoId }

    var taskInput by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }

    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedHour by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MINUTE)) }

    var reminderDateTime by remember { mutableStateOf<String?>(null) }
    var displayDateTime by remember { mutableStateOf("Select date & time") }

    val pink = Color(0xFFFF6991)
    val displayFormat = SimpleDateFormat("dd/MMM/yyyy • HH:mm", Locale.getDefault())
    val dbFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    dbFormat.timeZone = TimeZone.getDefault()
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(item) {
        item?.let {
            taskInput = it.taskText ?: ""
            description = it.description ?: ""

            if (!it.reminderAt.isNullOrEmpty()) {
                try {
                    val date = dbFormat.parse(it.reminderAt)
                    if (date != null) {
                        val calendar = Calendar.getInstance()
                        calendar.time = date

                        selectedDateMillis = calendar.timeInMillis
                        selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
                        selectedMinute = calendar.get(Calendar.MINUTE)

                        reminderDateTime = it.reminderAt
                        displayDateTime = displayFormat.format(date)

                        datePickerState.selectedDateMillis = selectedDateMillis
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val dateFromPicker = datePickerState.selectedDateMillis ?: selectedDateMillis

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = dateFromPicker

                    TimePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        { _, hour, minute ->
                            selectedHour = hour
                            selectedMinute = minute

                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)

                            reminderDateTime = dbFormat.format(calendar.time)
                            displayDateTime = displayFormat.format(calendar.time)
                        },
                        selectedHour,
                        selectedMinute,
                        true
                    ).apply {
                        window?.setBackgroundDrawableResource(android.R.color.transparent)
                    }.show()

                }) {
                    Text("Confirm", color = pink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pink.copy(alpha = 0.25f))
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Task", style = MaterialTheme.typography.headlineMedium)
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    label = { Text("Task") },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                    OutlinedTextField(
                        value = displayDateTime,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text("Reminder") },
                        leadingIcon = { Text("📅") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        item?.let {
                            viewModel.updateTodo(
                                it.todoListId,
                                TodoItem(
                                    todoListId = it.todoListId,
                                    userId = it.userId,
                                    taskText = taskInput,
                                    description = description,
                                    reminderAt = reminderDateTime,
                                    isDone = it.isDone
                                )
                            )
                            Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
                            viewModel.fetchTasks(it.userId)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = pink),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}