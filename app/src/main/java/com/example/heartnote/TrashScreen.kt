package com.example.heartnote

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Trashscreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    val plannerTrash = viewModel.trashPlannerList
    val noteTrash = viewModel.trashNoteList

    var selectedPlannerId by remember { mutableStateOf<Int?>(null) }
    var selectedNoteId by remember { mutableStateOf<Int?>(null) }

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showRestoreConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        Log.d("CHECK_USER_ID", viewModel.currentUserId.toString())

        viewModel.getTrashPlanner(viewModel.currentUserId)
        viewModel.getTrashNote(viewModel.currentUserId)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B8A)
                ),

                title = {

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            "Trash",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }

                },

                navigationIcon = {

                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )

                    }

                },

                actions = {

                    if (selectedPlannerId != null || selectedNoteId != null) {

                        IconButton(
                            onClick = { showMenu = true }
                        ) {

                            Icon(
                                painter = painterResource(R.drawable.moree),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )

                        }

                    }

                }

            )

        }

    ) { padding ->

        Column(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize()

        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${noteTrash.size + plannerTrash.size} items deleted recently.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF2F5))

            ) {

                //bote

                if (noteTrash.isNotEmpty()) {

                    item {

                        SectionHeader(
                            title = "Notes (${noteTrash.size})"
                        )

                    }

                    items(
                        noteTrash,
                        key = { "note_${it.note_id}" }
                    ) { note ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clickable {

                                    selectedNoteId =
                                        if (selectedNoteId == note.note_id)
                                            null
                                        else
                                            note.note_id

                                    selectedPlannerId = null
                                },

                            shape = RoundedCornerShape(16.dp),

                            border =
                                if (selectedNoteId == note.note_id)
                                    BorderStroke(2.dp, Color(0xFFFF6B8A))
                                else
                                    null
                        ) {

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),

                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                Icon(
                                    painter = painterResource(R.drawable.doc),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = "deleted:"+ note.delete_at ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                }

                            }

                        }

                    }

                }



                if (plannerTrash.isNotEmpty()) {

                    item {

                        SectionHeader(
                            title = "Planner (${plannerTrash.size})"
                        )

                    }

                    items(
                        plannerTrash,
                        key = { "planner_${it.planner_id}" }
                    ) { planner ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clickable {

                                    selectedPlannerId =
                                        if (selectedPlannerId == planner.planner_id)
                                            null
                                        else
                                            planner.planner_id

                                    selectedNoteId = null
                                },

                            shape = RoundedCornerShape(16.dp),

                            border =
                                if (selectedPlannerId == planner.planner_id)
                                    BorderStroke(2.dp, Color(0xFFFF6B8A))
                                else
                                    null
                        ){

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),

                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                Icon(
                                    painter = painterResource(R.drawable.doc),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp),
                                    tint = Color.Unspecified
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = planner.planner_content ?: "",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = "deleted:"+planner.delete_at ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )

                                }

                            }

                        }

                    }

                }

            }

        }


        if (showMenu) {

            Dialog(
                onDismissRequest = { showMenu = false }
            ) {

                Card(
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            "Delete",
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    showMenu = false
                                    showDeleteConfirm = true

                                }
                                .padding(12.dp)
                        )

                        Text(
                            "Restore",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    showMenu = false
                                    showRestoreConfirm = true

                                }
                                .padding(12.dp)
                        )

                    }

                }

            }

        }



        if (showDeleteConfirm) {

            AlertDialog(

                onDismissRequest = {
                    showDeleteConfirm = false
                },

                title = {
                    Text("Delete permanently?")
                },

                confirmButton = {

                    Button(

                        onClick = {

                            selectedPlannerId?.let {

                                viewModel.deletePlannerPermanent(
                                    it,
                                    viewModel.currentUserId
                                )

                            }
                            selectedNoteId?.let {
                                viewModel.deleteNotePermanent(it, viewModel.currentUserId)
                            }

                            showDeleteConfirm = false
                            selectedPlannerId = null
                            selectedNoteId = null

                        }

                    ) {
                        Text("Delete")
                    }

                },

                dismissButton = {

                    Button(
                        onClick = {
                            showDeleteConfirm = false
                        }
                    ) {
                        Text("Cancel")
                    }

                }

            )

        }



        if (showRestoreConfirm) {

            AlertDialog(

                onDismissRequest = {
                    showRestoreConfirm = false
                },

                title = {
                    Text("Restore item?")
                },

                confirmButton = {

                    Button(

                        onClick = {

                            selectedPlannerId?.let {
                                viewModel.restorePlanner(it, viewModel.currentUserId)
                            }

                            selectedNoteId?.let {
                                viewModel.restoreNote(it, viewModel.currentUserId)
                            }

                            viewModel.getTrashPlanner(viewModel.currentUserId)
                            viewModel.getTrashNote(viewModel.currentUserId)
                            Log.d("CHECK_USER", viewModel.currentUserId.toString())

                            showRestoreConfirm = false
                            selectedPlannerId = null
                            selectedNoteId = null

                        }

                    ) {
                        Text("Restore")
                    }

                },

                dismissButton = {

                    Button(
                        onClick = {
                            showRestoreConfirm = false
                        }
                    ) {
                        Text("Cancel")
                    }

                }

            )

        }

    }

}

@Composable
fun SectionHeader(title: String) {

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                Color(0xFFFFD6DF),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)

    ) {

        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )

    }

}