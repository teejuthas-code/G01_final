package com.example.todolist.ui

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
import androidx.navigation.NavController
import com.example.heartnote.HeartnoteViewModel
import com.example.heartnote.TodoItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: HeartnoteViewModel,
    userId: Int
) {
    val context = LocalContext.current
    var taskInput by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    var reminderDateTime by remember { mutableStateOf<String?>(null) }
    var displayDateTime by remember { mutableStateOf("Select date & time") }

    val pink = Color(0xFFFF6991)
    val displayFormat = SimpleDateFormat("dd/MMM/yyyy • HH:mm", Locale.getDefault())
    val dbFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    showDatePicker = false

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selectedDateMillis

                    TimePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar, // สไตล์ล้อหมุน (Spinner)
                        { _, hour, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)

                            reminderDateTime = dbFormat.format(calendar.time)
                            displayDateTime = displayFormat.format(calendar.time)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).apply {

                        window?.setBackgroundDrawableResource(android.R.color.transparent)
                    }.show()

                }) {
                    Text("Next", color = pink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add List",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    label = { Text("List") },
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


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
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
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (taskInput.isEmpty()) {
                            Toast.makeText(context, "Add List", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addTodo(
                                TodoItem(
                                    todoListId = 0,
                                    userId = userId,
                                    taskText = taskInput,
                                    description = description,
                                    reminderAt = reminderDateTime,
                                    isDone = 0
                                )
                            )
                            navController.popBackStack()
                            Toast.makeText(context, "List added", Toast.LENGTH_SHORT).show()

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = pink),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done")
                }
            }
        }
    }
}