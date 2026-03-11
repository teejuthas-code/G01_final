package com.example.heartnote


import android.R.attr.fontWeight
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val plannerList = viewModel.plannerList
    val context =LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()


    LaunchedEffect(Unit) {
        viewModel.getPlanner(userId)
    }
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM", Locale.ENGLISH)
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val calendarState = rememberSelectableCalendarState(
        initialMonth = YearMonth.now()
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showMonthYearPicker by remember { mutableStateOf(false) }
    val bgRes = when (viewModel.background) {
        "bg1" -> R.drawable.bg1
        "bg2" -> R.drawable.bg2
        else -> R.drawable.bg1
    }

    Box {

        Image(
            painter = painterResource(bgRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )



    Scaffold( containerColor =  Color.Transparent)

    { padding ->
        Box(
            modifier = Modifier
                .size(45.dp)
                .shadow(
                    elevation = 4.dp, shape = CircleShape,
                    ambientColor = Color(0x99FF95B1),
                    spotColor = Color(0xFFFF6991)
                )
                .background(Color.White, CircleShape)
                .clickable { navController.navigate(Screen.SelectBG.route) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.brush),
                contentDescription = "changeBG",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Spacer(modifier = Modifier.height(8.dp))

                SelectableCalendar(
                    calendarState = calendarState,

                    monthHeader = { monthState ->

                        val currentMonth = monthState.currentMonth
                        val year = currentMonth.year
                        val monthName = currentMonth.month.getDisplayName(
                            java.time.format.TextStyle.FULL,
                            java.util.Locale.ENGLISH
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // YEAR (กดเปิด picker)
                            Text(
                                text = year.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    showMonthYearPicker = true
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                IconButton(
                                    onClick = {
                                        monthState.currentMonth =
                                            monthState.currentMonth.minusMonths(1)
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.left_arrow),
                                        contentDescription = "Previous",
                                        tint = Color.Unspecified
                                    )
                                }

                                Text(
                                    text = monthName,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .clickable {
                                            showMonthYearPicker = true
                                        }
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                IconButton(
                                    onClick = {
                                        monthState.currentMonth =
                                            monthState.currentMonth.plusMonths(1)
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.right_arrow),
                                        contentDescription = "Previous",
                                        tint = Color.Unspecified
                                    )
                                }

                            }

                        }
                    },


                    dayContent = { dayState ->

                        val date = dayState.date
                        val isToday = date == today
                        val isSelected = date == selectedDate

                        val plannersForDate = plannerList.filter {

                            val start = it.event_start?.take(10)?.let { LocalDate.parse(it) }
                            val end = it.event_end?.take(10)?.let { LocalDate.parse(it) }

                            if (start != null && end != null) {
                                !date.isBefore(start) && !date.isAfter(end)
                            } else {
                                false
                            }
                        }

                        val plannersToShow = plannersForDate.take(2)
                        val extraCount = plannersForDate.size - plannersToShow.size

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .height(80.dp)
                                .fillMaxWidth()
                                .clickable {

                                    selectedDate = date
                                    showDialog = true

                                    scope.launch {
                                        sheetState.show()
                                    }

                                }
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                // วงกลมวัน
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(
                                            when {
                                                isSelected -> Color(0xFFFF6B8A)
                                                isToday -> Color(0xFFFFB74D)
                                                else -> Color.Transparent
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        fontSize = 12.sp,
                                        color =
                                            if (isSelected || isToday)
                                                Color.White
                                            else
                                                Color.Black
                                    )

                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // planner list
                                plannersToShow.forEach { planner ->

                                    Text(
                                        text = planner.planner_content,
                                        fontSize = 8.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .padding(vertical = 1.dp)
                                            .background(
                                                if (planner.is_done == 1)
                                                    Color.LightGray
                                                else
                                                    Color(0xFF83D4FF),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 3.dp, vertical = 0.dp)
                                    )
                                }

                                if (extraCount > 0) {

                                    Text(
                                        text = "+$extraCount",
                                        fontSize = 8.sp,
                                        color = Color.Gray
                                    )

                                }

                            }

                        }

                    }
                )


                if (showMonthYearPicker) {

                    val monthState = calendarState.monthState
                    var pickerYear by remember {
                        mutableStateOf(monthState.currentMonth.year)
                    }

                    var showYearList by remember { mutableStateOf(false) }

                    AlertDialog(
                        onDismissRequest = { showMonthYearPicker = false },
                        confirmButton = {},
                        text = {

                            Column {

                                // YEAR DROPDOWN
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        showYearList = !showYearList
                                    }
                                ) {

                                    Text(
                                        text = pickerYear.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.width(6.dp))
                                    Text("▼")

                                }

                                // YEAR LIST
                                if (showYearList) {

                                    LazyColumn(
                                        modifier = Modifier.height(200.dp)
                                    ) {

                                        items(201) { index ->

                                            val year = 1900 + index

                                            Text(
                                                text = year.toString(),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        pickerYear = year
                                                        showYearList = false
                                                    }
                                                    .padding(12.dp)
                                            )

                                        }

                                    }

                                }

                                Spacer(Modifier.height(16.dp))

                                // MONTH GRID
                                val months = java.time.Month.values()

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4)
                                ) {

                                    items(months.size) { i ->

                                        val month = months[i]

                                        Text(
                                            text = month.name.take(3),
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .clickable {

                                                    monthState.currentMonth =
                                                        java.time.YearMonth.of(
                                                            pickerYear,
                                                            month
                                                        )

                                                    showMonthYearPicker = false
                                                }
                                        )

                                    }

                                }

                            }

                        }
                    )
                }


            }
        }

    }

    var selectedPlannerId by remember { mutableStateOf<Int?>(null) }
    if (showDialog) {

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            containerColor = Color.White
        ) {
            var showDeleteDialog by remember { mutableStateOf(false) }
            var plannerToDelete by remember { mutableStateOf<PlannerClass?>(null) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(20.dp)
            ) {

                Text(
                    text = selectedDate?.format(formatter) ?: "",
                    fontSize = 22.sp,
                    color = Color(0xFFFF6B8A),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                val plannersForDay = plannerList.filter {

                    val start = it.event_start?.take(10)?.let { LocalDate.parse(it) }
                    val end = it.event_end?.take(10)?.let { LocalDate.parse(it) }

                    if (start != null && end != null && selectedDate != null) {
                        !selectedDate!!.isBefore(start) && !selectedDate!!.isAfter(end)
                    } else {
                        false
                    }
                }

                    if (plannersForDay.isEmpty()) {
                        Box( modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .padding(20.dp)){
                        Text(
                            text = "No planner for this day",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        }
                    } else {
                        plannersForDay.forEach { planner ->

                            var expanded by remember { mutableStateOf(false) }


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    RadioButton(
                                        selected = planner.is_done == 1,
                                        onClick = {
                                            viewModel.plannerDone(planner.planner_id)
                                        }
                                    )

                                    Text(
                                        text = planner.planner_content,
                                        fontSize = 16.sp,
                                        textDecoration =
                                            if (planner.is_done == 1)
                                                TextDecoration.LineThrough
                                            else
                                                TextDecoration.None
                                    )
                                }

                                Box {

                                    IconButton(
                                        onClick = { expanded = true }
                                    ) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "menu")
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {

                                        DropdownMenuItem(
                                            text = { Text("Edit") },
                                            onClick = {
                                                expanded = false
                                                viewModel.setPlannerForEdit(planner)
                                                navController.navigate(route = "EditPlanner_screen")

                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {

                                                expanded = false
                                                plannerToDelete = planner
                                                showDeleteDialog = true

                                            }
                                        )

                                    }

                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        if (showDeleteDialog) {

                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },

                                title = {
                                    Text("Delete Planner")
                                },

                                text = {
                                    Text("want to delete this planner?")
                                },

                                confirmButton = {

                                    TextButton(
                                        onClick = {

                                            plannerToDelete?.let {

                                                viewModel.deletePlanner(
                                                    it.planner_id,
                                                    it.user_id
                                                )

                                            }

                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Delete", color = Color.Red)
                                    }

                                },

                                dismissButton = {

                                    TextButton(
                                        onClick = { showDeleteDialog = false }
                                    ) {
                                        Text("Cancel")
                                    }

                                }

                            )

                        }

                    }




                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        selectedDate?.let {
                            navController.navigate(
                                Screen.AddPlanner.route + "/$it"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B8A),
                        contentColor = Color.White
                    )

                ) {

                    Text("Add Your Plan",
                        fontSize = 20.sp)

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
                    icon = R.drawable.calendar2,
                    route = Screen.Planner.route,
                    navController = navController,
                    selected = true
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














