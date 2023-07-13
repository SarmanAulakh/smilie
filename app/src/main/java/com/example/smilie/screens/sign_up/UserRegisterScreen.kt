package com.example.smilie.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.smilie.model.User
import com.example.smilie.model.UserTypes
import com.example.smilie.ui.components.ProfileImage
import com.example.smilie.ui.components.common.EmailField
import com.example.smilie.ui.components.common.LoadingButton
import com.example.smilie.ui.components.common.PasswordField
import com.example.smilie.ui.components.common.ext.fieldModifier
import com.example.smilie.ui.navigation.LOGIN_SCREEN


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    openAndPopUp: (String) -> Unit,
    setUser: (User) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserRegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val errorMessage by viewModel.errorMessage
    TopAppBar(title = { Text("Create your User") })

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(imageUri = uiState.imageUrl, setImageUri = {})
        UsernameInput(uiState.username, viewModel::onUsernameChange)
        UserTypeDropdown(uiState.userType.name, viewModel::onUserTypeChange)

        Button(
            onClick = { viewModel.onRegisterUserClick(openAndPopUp, setUser) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "Finish")
        }
        Text(text = uiState.message, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
        Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(vertical = 12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameInput(
    value: String,
    onChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fieldModifier(),
        value = value,
        onValueChange = { onChange(it) },
        placeholder =  { Text("username") },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeDropdown(
    value: String,
    onChange: (UserTypes) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded}
        ) {
            TextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                modifier = Modifier.menuAnchor()
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
//                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}