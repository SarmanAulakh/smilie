package com.example.smilie.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smilie.model.UserTypes
import com.example.smilie.model.view.UserRegisterViewModel
import com.example.smilie.ui.components.ProfileImage
import com.example.smilie.ui.components.common.LoadingButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val errorMessage by viewModel.errorMessage

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Create Your User") })
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(imageUri = uiState.imageUri, setImageUri = viewModel::onImageChange)
            }
            UsernameInput(uiState.username, viewModel::onUsernameChange)
            UserTypeDropdown(uiState.userType.name, viewModel::onUserTypeChange)

            LoadingButton(
                text = "Finish",
                onClick = { viewModel.onRegisterUserClick(openAndPopUp) },
                isLoading = uiState.loading,
                modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
            )
            Text(text = uiState.message, color = Color.Red)
            Text(text = errorMessage, color = Color.Red)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameInput(
    value: String,
    onChange: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { onChange(it) },
            placeholder = { Text("username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeDropdown(
    value: String,
    onChange: (UserTypes) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Select user type:")
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                UserTypes.values().forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            onChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}