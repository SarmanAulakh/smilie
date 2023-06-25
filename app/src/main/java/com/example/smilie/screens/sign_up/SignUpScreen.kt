package com.example.smilie.screens.sign_up

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
import com.example.smilie.ui.components.navigation.LOGIN_SCREEN
import com.example.smilie.ui.components.navigation.SIGN_UP_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    TopAppBar(title = { Text("Sign Up Page") }, actions = {
        Row() {
            Button(
                onClick = { openAndPopUp(LOGIN_SCREEN) }
            ) {
                Text(text = "Go Back")
            }
        }
    })

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
        PasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, Modifier.fieldModifier())

        Button(
            onClick = { viewModel.onSignInClick(openAndPopUp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "Sign up")
        }
        Text(text = uiState.message, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
    }
}