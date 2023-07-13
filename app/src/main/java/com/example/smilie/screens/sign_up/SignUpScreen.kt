package com.example.smilie.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.smilie.model.User
import com.example.smilie.ui.components.common.EmailField
import com.example.smilie.ui.components.common.LoadingButton
import com.example.smilie.ui.components.common.PasswordField
import com.example.smilie.ui.components.common.ext.fieldModifier
import com.example.smilie.ui.navigation.LOGIN_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val errorMessage by viewModel.errorMessage

    TopAppBar(title = { Text("Sign up Page") })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, "password", viewModel::onPasswordChange, Modifier.fieldModifier())
        PasswordField(uiState.repeatPassword, "repeat password", viewModel::onRepeatPasswordChange, Modifier.fieldModifier())

        LoadingButton(
            text = "Sign up",
            onClick = { viewModel.onSignUpClick(openAndPopUp) },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp),
            isLoading = uiState.loading
        )
        Button(
            onClick = { openAndPopUp(LOGIN_SCREEN) }
        ) {
            Text(text = "Go Back")
        }
        Text(text = uiState.message, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
        Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
    }
}