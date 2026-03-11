package com.example.heartnote

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.res.painterResource

@Composable
fun AddNoteCollectionScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel,
    collectionId: Int
) {

    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    LaunchedEffect(Unit) {
        viewModel.getNotesByUser(userId)
        viewModel.getNotesByCollection(collectionId)

    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {

        Spacer(modifier = Modifier.height(55.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ){

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = "Add note to collection",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Select notes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(1.dp)
                .background(Color(0xFFFFFFFF))
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 120.dp
            )
        ) {

            items(
                items = viewModel.noteList,
                key = { it.note_id }
            ) { note ->

                val isSelected =
                    viewModel.notesInCollection.any { it.note_id == note.note_id }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Image(
                        painter = painterResource(R.drawable.document),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = note.title,
                        modifier = Modifier.weight(1f),
                        fontSize = 17.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (isSelected) {

                        Image(
                            painter = painterResource(R.drawable.check1),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .clickable {

                                    viewModel.deleteNoteFromCollection(
                                        note.note_id,
                                        collectionId
                                    )
                                }
                        )

                    } else {

                        Image(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(13.dp))

                                .clickable {

                                    viewModel.addNoteToCollection(
                                        note.note_id,
                                        collectionId
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}