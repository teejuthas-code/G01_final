package com.example.heartnote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.text.isBlank

@androidx.compose.runtime.Composable
fun AddCollectionScreen(
    navController: NavHostController,
    viewModel: HeartnoteViewModel
) {

    var name by _root_ide_package_.androidx.compose.runtime.remember {
        mutableStateOf(
            ""
        )
    }
    val context =LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(Color(0xFFFF6991))
    ) {
        Card(
            modifier =Modifier.Companion
                .fillMaxSize()
                .padding(top = 80.dp),

            shape = _root_ide_package_.androidx.compose.foundation.shape.RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp
            ),

            colors = _root_ide_package_.androidx.compose.material3.CardDefaults.cardColors(
                containerColor = _root_ide_package_.androidx.compose.ui.graphics.Color.Companion.White
            )
        ) {

            _root_ide_package_.androidx.compose.foundation.layout.Column(
                modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion
                    .fillMaxSize()
                    .padding(24.dp)
            ) {

                                _root_ide_package_.androidx.compose.material3.Icon(
                    imageVector = _root_ide_package_.androidx.compose.material.icons.Icons.Default.Close,
                    contentDescription = null,
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.size(20.dp)
                        .clickable { navController.popBackStack() }
                )

                _root_ide_package_.androidx.compose.foundation.layout.Spacer(
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.height(
                        12.dp
                    )
                )

                _root_ide_package_.androidx.compose.material3.Text(
                    text = "Give your collection a name",
                    fontSize = 22.sp,
                    fontWeight = _root_ide_package_.androidx.compose.ui.text.font.FontWeight.Companion.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                _root_ide_package_.androidx.compose.material3.Text(
                    text = "Pick a color",
                    fontWeight = _root_ide_package_.androidx.compose.ui.text.font.FontWeight.Companion.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )

                _root_ide_package_.androidx.compose.foundation.layout.Spacer(
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.height(
                        12.dp
                    )
                )

                _root_ide_package_.androidx.compose.foundation.layout.Row {

                    _root_ide_package_.com.example.heartnote.ColorCircle(
                        color = _root_ide_package_.androidx.compose.ui.graphics.Color(0xFFFF6B8A),
                        hex = "#FF6B8A",
                        selectedColor = viewModel.backgroundColorText
                    ) {
                        viewModel.backgroundColorText = it
                    }

                    _root_ide_package_.com.example.heartnote.ColorCircle(
                        color = _root_ide_package_.androidx.compose.ui.graphics.Color(0xFF4A90E2),
                        hex = "#4698E5",
                        selectedColor = viewModel.backgroundColorText
                    ) {
                        viewModel.backgroundColorText = it
                    }

                    _root_ide_package_.com.example.heartnote.ColorCircle(
                        color = _root_ide_package_.androidx.compose.ui.graphics.Color(0xFFFFB74D),
                        hex = "#FFB74D",
                        selectedColor = viewModel.backgroundColorText
                    ) {
                        viewModel.backgroundColorText = it
                    }

                }

                Spacer(
                    modifier =Modifier.Companion.height(
                        20.dp
                    )
                )


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {Text("Name your collection...") },
                    shape = _root_ide_package_.androidx.compose.foundation.shape.RoundedCornerShape(
                        12.dp
                    ),
                    modifier =Modifier.Companion.fillMaxWidth()
                )

                Spacer(
                    modifier =Modifier.Companion.height(
                        20.dp
                    )
                )


                Button(
                    onClick = {
                        if (name.isBlank()) return@Button

                        val collection = CollectionClass(
                            user_id = userId,
                            collection_name = name,
                            background_color = viewModel.backgroundColorText
                        )

                        viewModel.addCollection(collection) {
                            navController.popBackStack()
                        }
                    },
                    modifier =Modifier.Companion
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(
                        50
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =Color(
                            0xFFFF5C8A
                        )
                    )
                ) {
                    Text("Create")
                }

                _root_ide_package_.androidx.compose.foundation.layout.Spacer(
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.height(
                        24.dp
                    )
                )

                _root_ide_package_.androidx.compose.material3.Text(
                    text = "Ideas to get started",
                    fontWeight = _root_ide_package_.androidx.compose.ui.text.font.FontWeight.Companion.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )

                _root_ide_package_.androidx.compose.foundation.layout.Spacer(
                    modifier = _root_ide_package_.androidx.compose.ui.Modifier.Companion.height(
                        16.dp
                    )
                )

                IdeaItem(
                    text = "My watch list 🍿",
                    bgColor = Color(0xFFFFDFC1)
                ) {
                    name = it
                }

                Spacer(modifier = Modifier.height(10.dp))

                IdeaItem(
                    text = "fav movies of all time 👀",
                    bgColor = Color(0xFFD2EBFF)
                ) {
                    name = it
                }

                Spacer(modifier = Modifier.height(10.dp))

                IdeaItem(
                    text = "my fav books 🎀",
                    bgColor = Color(0xFFFFDBEA)
                ) {
                    name = it
                }
            }
        }
    }

}

@Composable
fun IdeaItem(
    text: String,
    bgColor: Color,
    onCreateClick: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = text,
            style = MaterialTheme.typography.bodyLarge)

        Button(
            onClick = { onCreateClick(text) },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5C8A)
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
        ) {
            Text("Create")
        }
    }
}