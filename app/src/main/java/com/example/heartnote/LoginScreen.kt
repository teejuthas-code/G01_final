package com.example.heartnote

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController, viewModel: HeartnoteViewModel) {
    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val loginResult = viewModel.loginResult
    val errorMsg = viewModel.errorMessage

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (!it.error) {

                it.user_id?.let { id ->
                    sharedPref.saveLoginStatus(true, id, username)
                }

                viewModel.resetLoginResult()

                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                navController.navigate(Screen.Note.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }

            } else {
                viewModel.resetLoginResult()
                Toast.makeText(context, it.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        val savedUsername = sharedPref.getSavedUsername()
        savedUsername?.let {
            username = it
        }
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(
                context,
                "Login failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFF95B1),
                        Color(0xFFFFDD95)
                    )
                )
            )
    ) {
        TextButton(
            onClick = { navController.navigate(Screen.Home.route) },
            contentPadding = PaddingValues(30.dp)
        ) {
            Text(
                text = "< Back",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.heartnote_logo_1),
                contentDescription = null,
                modifier = Modifier.size(110.dp)
            )

        }

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Welcome Back!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF95B1)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.login(username, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(255, 187, 116, 255),
                        contentColor = Color.White
                    )
                ) {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 14.sp
                    )

                    TextButton(
                        onClick = { navController.navigate(Screen.Register.route) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Register",
                            color = Color(0xFFFF95B1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}