package com.example.heartnote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
fun Addplanner(navController: NavHostController, viewModel: HeartnoteViewModel,date: String) {
    var content_field by rememberSaveable { mutableStateOf("") }

    val cleanDate = date.substringBefore("T")
    val parts = cleanDate.split("-")
    var hour by rememberSaveable { mutableStateOf("")}
    var minute by rememberSaveable { mutableStateOf("")}
    var startyear by rememberSaveable { mutableStateOf(parts[0]) }
    var startmonth by rememberSaveable { mutableStateOf(parts[1]) }
    var startday by rememberSaveable { mutableStateOf(parts[2]) }

    val context =LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()


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
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var endYear by rememberSaveable { mutableStateOf(parts[0]) }
    var endMonth by rememberSaveable { mutableStateOf(parts[1]) }
    var endDay by rememberSaveable { mutableStateOf(parts[2]) }
    val endDatePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val dayStr = if (d < 10) "0$d" else d.toString()
            val monthStr = if (m + 1 < 10) "0${m + 1}" else (m + 1).toString()
            endDay = dayStr
            endMonth = monthStr
            endYear = y.toString()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
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
                        .shadow(elevation = 4.dp, shape = CircleShape,
                            ambientColor = Color(0x99FF95B1),
                            spotColor = Color(0xFFFF6991))
                        .background(Color.White, CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = "bachstack",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = "New plan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape,
                            ambientColor = Color(0x99FF95B1),
                            spotColor = Color(0xFFFF6991))
                        .background(Color.White, CircleShape)
                        .clickable {

                            if (
                                content_field.isNotEmpty() &&
                                startday.isNotEmpty() &&
                                endDay.isNotEmpty()&&
                                hour.isNotEmpty() &&
                                minute.isNotEmpty()
                            ) {

                                val planner = PlannerClass(
                                    planner_id = 0,
                                    user_id = userId,
                                    planner_content = content_field,
                                    event_start = "$startyear-$startmonth-$startday",
                                    event_end = "$endYear-$endMonth-$endDay",
                                    remind = "$hour:$minute:00",
                                    is_done =0
                                )

                                viewModel.addPlanner(planner)
                                val reminderTime = "$startyear-$startmonth-$startday $hour:$minute:00"

                                ReminderScheduler.scheduleNotification(
                                    context,
                                    content_field,
                                    reminderTime
                                )
                                navController.popBackStack()
                            }
                        },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "save",
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
        ){
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
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(30.dp),
                            ambientColor = Color(0x99FF95B1),
                            spotColor = Color(0xFFFF6991)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        BasicTextField(
                            value = content_field,
                            onValueChange = { content_field = it },
                            decorationBox = { innerTextField ->
                                if (content_field.isEmpty()) {
                                    Text("Type the title...", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Start",
                                color = Color.Gray,
                                modifier = Modifier.width(60.dp)
                            )
                            FilledIconButton(
                                onClick = { StartdatePickerDialog.show() }
                            ) {
                                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                            }
                            Text(
                                text = "$startyear-$startmonth-$startday",
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "End",
                                color = Color.Gray,
                                modifier = Modifier.width(60.dp)
                            )
                            FilledIconButton(
                                onClick = { endDatePickerDialog.show() }
                            ) {
                                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                            }
                            Text(
                                text = "$endYear-$endMonth-$endDay",
                                modifier = Modifier.padding(start = 12.dp)
                            )
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
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(30.dp),
                            ambientColor = Color(0x99FF6991),
                            spotColor = Color(0xFFFF6991)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ){
                    TimeContent(
                        selectedHour = hour,
                        selectedMinute = minute,
                        onTimeSelected = {h,m ->
                            hour=h
                            minute=m
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TimeContent(
    selectedHour: String,
    selectedMinute: String,
    onTimeSelected:(String, String) -> Unit
){
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mTimePickerDialog = TimePickerDialog(
        mContext,
        android.R.style.Theme_Holo_Light_Dialog_NoActionBar, // ทำให้เป็นล้อหมุน Spinner
        { _, hour:Int, minute:Int ->
            val hStr = if (hour<10) "0$hour" else "$hour"
            val mStr = if (minute<10) "0$minute" else "$minute"
            onTimeSelected(hStr,mStr)
        },
        mHour,
        mMinute,
        true
    ).apply {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    // ปรับ Arrangement เป็น SpaceEvenly เพื่อให้ปุ่มและข้อความตรงกับ Row ด้านบน
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Select Time",
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        FilledIconButton(
            modifier = Modifier.padding(start = 8.dp),
            onClick = { mTimePickerDialog.show() }
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Time Icon"
            )
        }

        // แสดงผลแค่ตัวเลขเวลา
        Text(
            text = if (selectedHour.isEmpty()) "00:00" else "$selectedHour:$selectedMinute",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

