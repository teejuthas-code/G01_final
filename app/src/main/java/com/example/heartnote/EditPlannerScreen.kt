package com.example.heartnote

import android.app.DatePickerDialog
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Calendar

@Composable
fun Editplanner(navController: NavHostController, viewModel: HeartnoteViewModel) {


    var content_field by rememberSaveable { mutableStateOf(viewModel.contentText) }

    val startParts = viewModel.startDateText.split("-")
    var startyear by rememberSaveable { mutableStateOf(startParts.getOrElse(0){"2024"}) }
    var startmonth by rememberSaveable { mutableStateOf(startParts.getOrElse(1){"01"}) }
    var startday by rememberSaveable { mutableStateOf(startParts.getOrElse(2){"01"}) }

    val endParts = viewModel.endDateText.split("-")
    var endYear by rememberSaveable { mutableStateOf(endParts.getOrElse(0){"2024"}) }
    var endMonth by rememberSaveable { mutableStateOf(endParts.getOrElse(1){"01"}) }
    var endDay by rememberSaveable { mutableStateOf(endParts.getOrElse(2){"01"}) }

    val timeParts = viewModel.remindTimeText.split(":")
    var hour by rememberSaveable { mutableStateOf(timeParts.getOrElse(0){"00"}) }
    var minute by rememberSaveable { mutableStateOf(timeParts.getOrElse(1){"00"}) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val StartdatePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val dayStr = if (d < 10) "0$d" else d.toString()
            val monthStr = if (m + 1 < 10) "0${m + 1}" else (m + 1).toString()
            startday = dayStr
            startmonth = monthStr
            startyear = y.toString()
        },
        startyear.toInt(),
        startmonth.toInt() - 1,
        startday.toInt()
    )

    val endDatePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val dayStr = if (d < 10) "0$d" else d.toString()
            val monthStr = if (m + 1 < 10) "0${m + 1}" else (m + 1).toString()
            endDay = dayStr
            endMonth = monthStr
            endYear = y.toString()
        },
        endYear.toInt(),
        endMonth.toInt() - 1,
        endDay.toInt()
    )

    Scaffold(
        containerColor = Color(0xFFFFF2F5),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = "Edit plan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable {

                            val planner = PlannerClass(
                                planner_id = viewModel.plannerId,
                                user_id = viewModel.currentUserId,
                                planner_content = content_field,
                                event_start = "$startyear-$startmonth-$startday",
                                event_end = "$endYear-$endMonth-$endDay",
                                remind = "$hour:$minute",
                                is_done = 0
                            )

                            viewModel.updatePlanner(planner)
                            navController.popBackStack()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.check1),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF2F5))
        ) {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .shadow(6.dp, RoundedCornerShape(30.dp))
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {

                    Column {

                        BasicTextField(
                            value = content_field,
                            onValueChange = { content_field = it },
                            decorationBox = { inner ->
                                if (content_field.isEmpty()) {
                                    Text("Type the title...", color = Color.Gray)
                                }
                                inner()
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Text("Start", color = Color.Gray)

                            FilledIconButton(
                                onClick = { StartdatePickerDialog.show() }
                            ) {
                                Icon(Icons.Outlined.DateRange, null)
                            }

                            Text("$startyear-$startmonth-$startday")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Text("End", color = Color.Gray)

                            FilledIconButton(
                                onClick = { endDatePickerDialog.show() }
                            ) {
                                Icon(Icons.Outlined.DateRange, null)
                            }

                            Text("$endYear-$endMonth-$endDay")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Reminder",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .shadow(6.dp, RoundedCornerShape(30.dp))
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {

                    TimeContent(
                        selectedHour = hour,
                        selectedMinute = minute,
                        onTimeSelected = { h, m ->
                            hour = h
                            minute = m
                        }
                    )

                }
            }
        }
    }

}
