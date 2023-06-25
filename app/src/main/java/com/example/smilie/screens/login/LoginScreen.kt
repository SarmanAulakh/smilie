package com.example.smilie.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.ui.components.common.EmailField
import com.example.smilie.ui.components.common.PasswordField
import com.example.smilie.ui.components.common.ext.fieldModifier
import com.example.smilie.ui.components.navigation.SIGN_UP_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    TopAppBar(title = { Text("Login In Page") })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

        Button(
            onClick = { viewModel.onSignInClick(openAndPopUp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "Sign in")
        }
        Button(
            onClick = { openAndPopUp(SIGN_UP_SCREEN) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Sign up")
        }
        Text(text = uiState.message, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
    }
}